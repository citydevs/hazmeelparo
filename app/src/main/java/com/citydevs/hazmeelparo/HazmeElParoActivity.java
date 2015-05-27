package com.citydevs.hazmeelparo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citydevs.hazmeelparo.config.Configuracion;
import com.citydevs.hazmeelparo.contact.ContactoActivity;
import com.citydevs.hazmeelparo.contact.MySimpleArrayAdapter;
import com.citydevs.hazmeelparo.facebook.FacebookLoginActivity;
import com.citydevs.hazmeelparo.interfaces.OnListenerCambiarTextoEnApp;
import com.citydevs.hazmeelparo.interfaces.OnListenerOpenContact;
import com.citydevs.hazmeelparo.localizacion.ServicioLocalizacion;
import com.citydevs.hazmeelparo.panic.PanicAlert;
import com.citydevs.hazmeelparo.popup.ActionItem;
import com.citydevs.hazmeelparo.popup.QuickAction;
import com.citydevs.hazmeelparo.splash.SplashActivity;
import com.citydevs.hazmeelparo.utils.Utils;

import java.util.ArrayList;

public class HazmeElParoActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnListenerOpenContact {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
    private ArrayList<String> pointsLat;
    private ArrayList<String> pointsLon;
	private static AlertDialog customDialog = null;
	protected static Activity activity;
	private static Point p;
	private static int TIPO_SELECCION_ACCION = 0;
	private static int aviso_a= 0;
    private static int TIPO_DE_ACOSO =0;
	public static TextView tv_problemas_titulo;

	public static ImageView iv_reporte_usuario;

	public static ImageView iv_reporte_chofer;

	public static ImageView alarma_iv_alarma;

	public static AnimationDrawable frameAnimation;

	private static LinearLayout ll_quien;
	private static LinearLayout ll_enviando_mensaje;
	private static LinearLayout ll_reporte_hecho;
	
	
	//Dialogo de emergencia
	private static final int TOCO = 1;
    private static final int MIRO = 2;
    private static final int VERBAL = 3;
    private static final int EXCIVO = 4;
    static QuickAction mQuickAction;
    private static int index=1;

    private static OnListenerCambiarTextoEnApp onListenerCambiarTextoEnApp;
    private ArrayList<String> listaCels;
    static String[] info;
    private static final int ENVIAR_ALARMA_CHOFER=0;
    private static final int ENVIAR_ALARMA_FAMILIAR_CHOFER=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (new Utils(HazmeElParoActivity.this).getPreferenciasGCM()==null) {//si ya registro
			startActivity(new Intent().setClass(HazmeElParoActivity.this, SplashActivity.class));
			this.finish();
		} 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hazme_el_paro);
		activity = HazmeElParoActivity.this;
        getActionBar().setDisplayShowTitleEnabled(false);


		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = "";

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

        ContactoActivity.setOnClickOpenContactListener(this);

        info= new Utils(this).getPreferenciasContacto();
    }

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container,PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
            index = 1;
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
            index = 2;
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
            index = 3;
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setTitle("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setDisplayShowTitleEnabled(false);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
			//getMenuInflater().inflate(R.menu.hazme_el_paro, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = null;
            if(index == 1) {
                 rootView = inflater.inflate(R.layout.fragment_hazme_el_paro, container, false);
                LinearLayout hazme_el_paro_ll_reporta = (LinearLayout) rootView.findViewById(R.id.hazme_el_paro_ll_reporta);
                hazme_el_paro_ll_reporta.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Utils util = new Utils(activity);
                        if(util.getPreferenciasCAS().equals("true")||util.getPreferenciasContactarMando().equals("true")||util.getPreferenciasMensaje().equals("true")) {
                            showDialogQuienTieneProblemas().show();
                        }else{
                            Utils.Toast(activity,"No tenemos a nadie a quien reportar, agrega a un contacto", Toast.LENGTH_LONG);
                        }
                    }
                });

                LinearLayout hazme_el_paro_ll_conecta = (LinearLayout) rootView.findViewById(R.id.hazme_el_paro_ll_conecta);
                hazme_el_paro_ll_conecta.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, FacebookLoginActivity.class));

                    }
                });

            }else if(index == 2) {
                ContactoActivity contacto = new ContactoActivity(getActivity());
                contacto.setOrigen("APP");
                rootView = contacto.getView();
            }
            else if(index == 3) {
                Configuracion configuracion = new Configuracion(getActivity());
                rootView = configuracion.getView();
            }

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((HazmeElParoActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	
	/*Dialogos*/
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 * 
	 * @return Dialog (regresa el dialogo creado)
	 **/

	public  Dialog showDialogQuienTieneProblemas() {
	
		
		ActionItem addItem = new ActionItem(MIRO, "Mirada\nLasciva", getResources().getDrawable(R.drawable.ic_launcher_mirar));
        ActionItem acceptItem   = new ActionItem(VERBAL, "Agresión\nVerbal", getResources().getDrawable(R.drawable.ic_launcher_agredir));
        ActionItem uploadItem   = new ActionItem(TOCO, "Tocamiento\nArrimón", getResources().getDrawable(R.drawable.ic_launcher_tocar));
        ActionItem exhibicionismotItem   = new ActionItem(EXCIVO, "Exhibición\nSexual", getResources().getDrawable(R.drawable.ic_launcher_excibir));
        uploadItem.setSticky(true);

        mQuickAction  = new QuickAction(activity);
       
        mQuickAction.addActionItem(addItem);
        mQuickAction.addActionItem(acceptItem);
        mQuickAction.addActionItem(uploadItem);
        mQuickAction.addActionItem(exhibicionismotItem);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		View view = activity.getLayoutInflater().inflate(R.layout.activity_reporte, null);
		builder.setView(view);
		builder.setCancelable(true);


		
		p = Utils.getTamanoPantalla(activity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 3, p.x / 3);

		Button btn_alguien_mas = (Button) view.findViewById(R.id.safebus_btn_alguien_mas);
		btn_alguien_mas.setLayoutParams(lp);
		btn_alguien_mas.setOnClickListener(this);

		Button btn_yo = (Button) view.findViewById(R.id.safebus_btn_yo);
		btn_yo.setLayoutParams(lp);
		btn_yo.setOnClickListener(this);
		
		Button btn_aceptar = (Button) view.findViewById(R.id.enviar_alarma_btn_aceptar);
		btn_aceptar.setOnClickListener(this);

		 tv_problemas_titulo = (TextView) view.findViewById(R.id.safebus_tv_problemas_titulo);
		 alarma_iv_alarma = (ImageView) view	.findViewById(R.id.enviar_alarma_iv_alarma);
		 alarma_iv_alarma.setLayoutParams(lp);

		alarma_iv_alarma.setBackgroundResource(R.drawable.animation_alarma);
		 frameAnimation = (AnimationDrawable) alarma_iv_alarma.getBackground();
		
		frameAnimation = (AnimationDrawable) alarma_iv_alarma.getBackground();
		
		 ll_quien = (LinearLayout)view.findViewById(R.id.safebus_ll_quien);
		 ll_enviando_mensaje=(LinearLayout)view.findViewById(R.id.safebus_ll_enviando_mensaje);
		 ll_reporte_hecho=(LinearLayout)view.findViewById(R.id.safebus_ll_reporte_hecho);
		
		 iv_reporte_chofer = (ImageView) view	.findViewById(R.id.enviar_alarma_iv_reporte_chofer);
		iv_reporte_chofer.setLayoutParams(lp);
		
		 iv_reporte_usuario = (ImageView) view	.findViewById(R.id.enviar_alarma_iv_reporte_usuario);
		iv_reporte_usuario.setLayoutParams(lp);
		
		 mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
	            @Override
	            public void onItemClick(QuickAction quickAction, int pos, int actionId) {
	                ActionItem actionItem = quickAction.getActionItem(pos);

	                if (actionId == TOCO) {
	                	new MensajeTask(TIPO_SELECCION_ACCION,TOCO).execute();
	                } else if (actionId == MIRO){
	                	new MensajeTask(TIPO_SELECCION_ACCION,MIRO).execute();
	                }else if (actionId == VERBAL){
	                	new MensajeTask(TIPO_SELECCION_ACCION,VERBAL).execute();
	                }else if (actionId == EXCIVO){
	                	new MensajeTask(TIPO_SELECCION_ACCION,EXCIVO).execute();
	                }
	                mQuickAction.dismiss();
	            }
	        });

	        mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
	            @Override
	            public void onDismiss() {
	                //Toast.makeText(activity, "Ups..dismissed", Toast.LENGTH_SHORT).show();
	            }
	        });
			
		return (customDialog = builder.create());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safebus_btn_alguien_mas:
			  TIPO_SELECCION_ACCION= 2;
			  mQuickAction.show(v);//new MensajeTask(TIPO_SELECCION_ACCION).execute();
			break;
		case R.id.safebus_btn_yo:
		     TIPO_SELECCION_ACCION = 1;
		     mQuickAction.show(v);//new MensajeTask(TIPO_SELECCION_ACCION).execute();
			break;
		case R.id.enviar_alarma_btn_aceptar:
			 customDialog.dismiss();
			break;
		default:
			break;
		}
		
	}
	
	}
	
	
	
	/**
	 * Clase que envia al contacto de emergencia 
	 * @author mikesaurio
	 *
	 */
	public static class MensajeTask extends AsyncTask<Integer, Void, Boolean> {
	    private long time;

	    public MensajeTask(int i, int motivo) {
	    	aviso_a=i;
            TIPO_DE_ACOSO = motivo;
		}

		@Override
	    protected void onPreExecute() {

	    	ll_quien.setVisibility(LinearLayout.GONE);
			ll_enviando_mensaje.setVisibility(LinearLayout.VISIBLE);
			tv_problemas_titulo.setText("Enviando alarma...");
			frameAnimation.start();
			if(aviso_a==2){
	    		enviarAlarma(ENVIAR_ALARMA_CHOFER);
	    	}else{
		  		if(info[0]!=null){
		    		enviarAlarma(ENVIAR_ALARMA_FAMILIAR_CHOFER);
		  				
		  		} else {
		    		enviarAlarma(ENVIAR_ALARMA_CHOFER);
		  		}
	    	}
	        super.onPreExecute();
	        time = System.currentTimeMillis();
	    }

	    @Override
	    protected Boolean doInBackground(Integer... params) {	
	        try {

                if(Utils.hasInternet(activity)) {
                    if(Boolean.parseBoolean(new Utils(activity).getPreferenciasContactarMando())){
                        activity.startService(new Intent(activity, ServicioLocalizacion.class));
                    }
                }


	        /*	if(new Utils(activity).getPreferenciasMando()){
	        		Utils.doHttpConnection("https://cryptic-peak-2139.herokuapp.com/api/client_panic?email="
		        			+ UserInfo.getEmail(activity)
		        			+"&placa="+new Utils(activity).getPreferenciasPlaca()[0]);
				}*/
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Boolean result) {
	    	frameAnimation.stop();
	    	ll_enviando_mensaje.setVisibility(LinearLayout.GONE);
	    	ll_reporte_hecho.setVisibility(LinearLayout.VISIBLE);
	    	if(aviso_a==2){
	    		tv_problemas_titulo.setText(activity.getResources().getString(R.string.notificar_chofer));
	    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
	    	}else{
	    		String[] info= new Utils(activity).getPreferenciasContacto();
		  		if(info[0]!=null){
		  			tv_problemas_titulo.setText(activity.getResources().getString(R.string.notificar_chofer_familia));
		    		iv_reporte_usuario.setVisibility(ImageView.VISIBLE);
		    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
		  				
		  		} else {
		  			tv_problemas_titulo.setText(activity.getResources().getString(R.string.notificar_chofer));
		    		iv_reporte_chofer.setVisibility(ImageView.VISIBLE);
		  		}	
	    	}
	        super.onPostExecute(result);  
	    }
	}



    @Override
    public void onListenerOpenContact() {
        open_contact();
    }

    public void open_contact(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            String telefono = getContactInfo(data);
            if (telefono.equals("MULTIPLES")){
                dialogoLista();
            }else{
                set_contact(telefono);
            }
        }

    }

    /**
     * metodo que llena tanto el numero celular como correo de emergencia con los contactos del usuario
     * @param intent
     */
    public  String getContactInfo(Intent intent)
    {
        String telefono = "";
        try{
            @SuppressWarnings("deprecation")
            Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
            if(!cursor.isClosed()&&cursor!=null){
                while (cursor.moveToNext())
                {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if ( hasPhone.equalsIgnoreCase("1")){
                        hasPhone = "true";

                    }else{
                        hasPhone = "false" ;
                    }
                    if (Boolean.parseBoolean(hasPhone))
                    {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                        listaCels= new ArrayList<String>();
                        while (phones.moveToNext())
                        {

                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumber=  phoneNumber.replaceAll(" ", "");

                            final char c = phoneNumber.charAt(0);
                            if(c=='+'){
                                try{
                                    phoneNumber =  phoneNumber.substring(3, 13);
                                }catch(Exception e){

                                }
                            }

                            listaCels.add(phoneNumber);
                        }
                        if(listaCels.size()==1){ //si tiene solo un telefono
                            telefono = listaCels.get(0);
                        }else if(listaCels.size()==0){//si no tiene telefono
                            telefono = "";
                        }else{

                            telefono = "MULTIPLES";
                            //dialogoLista(tag+"");
                        }
                        phones.close();
                    }
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return telefono;
    }


    /**
     * Envia alarma de emergencia
     * @param tipo (int)
     * ENVIAR_ALARMA_FAMILIAR_CHOFER envia la alarma al chofer y un SMS a contacto de emergencia
     * ENVIAR_ALARMA_CHOFER envia alarma a chofer
     */
    public static void enviarAlarma(int tipo) {
        switch (tipo) {
            case ENVIAR_ALARMA_CHOFER:
                if(Boolean.parseBoolean(new Utils(activity).getPreferenciasCAS())){
                    new PanicAlert(activity).contactaAPolicia();
                }

                break;
            case ENVIAR_ALARMA_FAMILIAR_CHOFER:
                if(Boolean.parseBoolean(new Utils(activity).getPreferenciasMensaje())) {
                    PanicAlert.sendSMS(info[0], activity.getString(R.string.mensaje_emergencia));
                }
                if(Boolean.parseBoolean(new Utils(activity).getPreferenciasCAS())){
                    new PanicAlert(activity).contactaAPolicia();
                }


                break;

            default:
                break;
        }
    }

    /**
     * agrega la vista del contacto de emergencia
     */
    public void dialogoLista(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialogo_contactos, null);
        builder.setView(view);
        builder.setCancelable(true);
        final ListView listview = (ListView) view.findViewById(R.id.dialogo_contacto_lv_contactos);
        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listaCels);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                if (item != null){
                    set_contact(item.replaceAll(" ", ""));
                    customDialog.dismiss();
                }
            }
        });
        customDialog=builder.create();
        customDialog.show();
    }

    public void set_contact(String telefono){
        onListenerCambiarTextoEnApp.onListenerCambiarTextoEnApp(telefono);
    }

    /**
     * escucha para poder cambair el texto de la pagina 2
     * @param listener
     */
    public static void setOnClickCambiarTextoEnAppListener( OnListenerCambiarTextoEnApp listener)
    {

        onListenerCambiarTextoEnApp = listener;
    }

    /**
     * manejo de transmiciones
     */

    private BroadcastReceiver onBroadcast = new BroadcastReceiver() {



        @Override
        public void onReceive(Context ctxt, Intent t) {

            pointsLat = t.getStringArrayListExtra("latitud");
            pointsLon = t.getStringArrayListExtra("longitud");

            new PanicAlert(activity).contactaAlMAndo("Mensaje de emergencia en ",TIPO_SELECCION_ACCION+"",pointsLat.get(pointsLat.size()),pointsLon.get(pointsLon.size()));

        }
    };


    @Override
    public void onBackPressed() {
        if(index==3){
            startActivity(new Intent(HazmeElParoActivity.this, HazmeElParoActivity.class));
            finish();
        }else{
            super.onBackPressed();
        }

    }
}

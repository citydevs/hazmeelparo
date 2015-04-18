package com.citydevs.hazmeelparo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.citydevs.hazmeelparo.splash.SplashActivity;
import com.citydevs.hazmeelparo.utils.Utils;

public class HazmeElParoActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

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
	private static AlertDialog customDialog = null;
	protected static Activity activity;
	private static Point p;
	private static int TIPO_SELECCION_ACCION = 0;
	private static int aviso_a= 0;

	public static TextView tv_problemas_titulo;

	public static ImageView iv_reporte_usuario;

	public static ImageView iv_reporte_chofer;

	public static ImageView alarma_iv_alarma;

	public static AnimationDrawable frameAnimation;

	private static LinearLayout ll_quien;
	private static LinearLayout ll_enviando_mensaje;
	private static LinearLayout ll_reporte_hecho;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (new Utils(HazmeElParoActivity.this).getPreferenciasGCM()==null) {//si ya registro
			startActivity(new Intent().setClass(HazmeElParoActivity.this, SplashActivity.class));
			this.finish();
		} 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hazme_el_paro);
		activity = HazmeElParoActivity.this;
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.hazme_el_paro, menu);
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
		if (id == R.id.action_settings) {
			return true;
		}
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
			View rootView = inflater.inflate(R.layout.fragment_hazme_el_paro,container, false);
			LinearLayout hazme_el_paro_ll_reporta = (LinearLayout)rootView.findViewById(R.id.hazme_el_paro_ll_reporta);
			hazme_el_paro_ll_reporta.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 showDialogQuienTieneProblemas().show();
					
				}
			});
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
			
		return (customDialog = builder.create());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safebus_btn_alguien_mas:
			  TIPO_SELECCION_ACCION= 2;
			  new MensajeTask(TIPO_SELECCION_ACCION).execute();
			break;
		case R.id.safebus_btn_yo:
		     TIPO_SELECCION_ACCION = 1;
		     new MensajeTask(TIPO_SELECCION_ACCION).execute();
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

	    public MensajeTask(int i) {
	    	aviso_a=i;
		}

		@Override
	    protected void onPreExecute() {
	    
	    	ll_quien.setVisibility(LinearLayout.GONE);
			ll_enviando_mensaje.setVisibility(LinearLayout.VISIBLE);
			tv_problemas_titulo.setText("Enviando alarma...");
			frameAnimation.start();
			/*if(aviso_a==2){
	    		enviarAlarma(ENVIAR_ALARMA_CHOFER);
	    	}else{
		  		if(info[0]!=null){
		    		enviarAlarma(ENVIAR_ALARMA_FAMILIAR_CHOFER);
		  				
		  		} else {
		    		enviarAlarma(ENVIAR_ALARMA_CHOFER);
		  		}
	    	}
	    	*/
	        super.onPreExecute();
	        time = System.currentTimeMillis();
	    }

	    @Override
	    protected Boolean doInBackground(Integer... params) {	
	        try {
	        	
	        /*	if(new Utils(activity).getPreferenciasMando()||aviso_a==2){
	        		Utils.doHttpConnection("https://cryptic-peak-2139.herokuapp.com/api/client_panic?email="
		        			+UserInfo.getEmail(activity)
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
	

}

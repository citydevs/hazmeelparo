package com.citydevs.hazmeelparo.contact;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.citydevs.hazmeelparo.HazmeElParoActivity;
import com.citydevs.hazmeelparo.InstructionsActivity;
import com.citydevs.hazmeelparo.R;
import com.citydevs.hazmeelparo.gcm.GCM;
import com.citydevs.hazmeelparo.interfaces.OnListenerCambiarTexto;
import com.citydevs.hazmeelparo.interfaces.OnListenerCambiarTextoEnApp;
import com.citydevs.hazmeelparo.interfaces.OnListenerOpenContact;
import com.citydevs.hazmeelparo.utils.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * 
 * @author mikesaurio
 *
 */
public class ContactoActivity extends View implements OnListenerCambiarTexto, OnListenerCambiarTextoEnApp {
	
	private ImageButton btn_contacto;

	private EditTextBackEvent et_telefono,et_mensaje_emergencia;

	private Button btn_guardar,btn_eliminar_contacto;
	private ImageView ImageView_titulo_contacto;
	private Activity context;
	private View view;
	private static OnListenerOpenContact onListenerOpenContact;
    String origen = null;
	
	//GCM
	private GoogleCloudMessaging gcm;
	private GCM mGCM;
	
	public ContactoActivity(Activity context) {
		super(context);
		this.context = context;
		init();
	}

	public ContactoActivity(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ContactoActivity(Activity context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	

	
public void init(){
	InstructionsActivity.setOnClickCambiarTextoListener(ContactoActivity.this);
    HazmeElParoActivity.setOnClickCambiarTextoEnAppListener(ContactoActivity.this);

    //HazmeElParoActivity.setOnClickCambiarTextoListener(ContactoActivity.this);

	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	view = inflater.inflate(R.layout.activity_contacto, null);
	
		et_telefono=(EditTextBackEvent)view.findViewById(R.id.registro_et_telefono);
		et_mensaje_emergencia=(EditTextBackEvent)view.findViewById(R.id.registro_et_mensaje_emergencia);
/*		((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
*/
		
		btn_guardar=(Button)view.findViewById(R.id.registro_btn_guardar);
		btn_guardar.setOnClickListener(new View.OnClickListener() {
			
			
			
			@Override
			public void onClick(View v) {
				if(validaEditText()){
					new Utils(context).setPreferenciasContacto(new String[]{et_telefono.getText().toString(),et_mensaje_emergencia.getText().toString()});
					Utils.Toast(context, "Contacto guardado", Toast.LENGTH_SHORT);
                    new Utils(context).setPreferenciasMensaje("true");
					 mGCM= new GCM(context);

					  if (mGCM.checkPlayServices()) {
				            gcm = GoogleCloudMessaging.getInstance(context);
				            mGCM.registerInBackground(gcm);       
				        }
				}
			}
		});
		
		
		btn_contacto=(ImageButton)view.findViewById(R.id.registro_btn_contacto);
		btn_contacto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                        onListenerOpenContact.onListenerOpenContact();
			}
		});
		
		btn_eliminar_contacto=(Button)view.findViewById(R.id.btn_eliminar_contacto);
		btn_eliminar_contacto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Utils(context).setPreferenciasContacto(new String[]{null,null});
                btn_eliminar_contacto.setVisibility(GONE);
                et_telefono.setText("");
                new Utils(context).setPreferenciasMensaje("false");
			}
		});
		
		
		
		
		String[] info = new Utils(context).getPreferenciasContacto();
		if(info[0]!=null){
			et_telefono.setText(info[0]);
			et_mensaje_emergencia.setText(info[1]);
		}else{
			btn_eliminar_contacto.setVisibility(Button.GONE);
		}
		
		
				 
		InputMethodManager imm = (InputMethodManager)context.getSystemService(
		Context.INPUT_METHOD_SERVICE);
		//txtName is a reference of an EditText Field
		imm.hideSoftInputFromWindow(et_telefono.getWindowToken(), 0);
	
	}
	
	
	/**
	 * valida todos los editText 
	 * @return (true) si el editText esta bie llenado
	 */
	public boolean validaEditText() {
	
      	 //validamos que no esten vacios
      	  if(et_telefono.getText().toString().equals("")){
      		  et_telefono.setError(getResources().getString(R.string.registro_telefono_vacio));
      		//  Mensajes.simpleToast(ContactoActivity.this, getResources().getString(R.string.registro_telefono_vacio), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(et_mensaje_emergencia.getText().toString().equals("")){
    		  et_mensaje_emergencia.setError(getResources().getString(R.string.registro_correo_vacio));
    		 // Mensajes.simpleToast(ContactoActivity.this, getResources().getString(R.string.registro_correo_vacio), Toast.LENGTH_LONG);
    		  return false;
    	  }
    	  //validamos que esten bien escritos
    	  if(et_telefono.getText().toString().length()!=10){
    		  et_telefono.setError(getResources().getString(R.string.registro_telefono_largo));
    		//  Mensajes.simpleToast(ContactoActivity.this,getResources().getString(R.string.registro_telefono_largo), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(EditTextValidator.isNumeric(et_telefono.getText().toString())){
    		  et_telefono.setError(getResources().getString(R.string.registro_telefono_incorrecto));
      		  return false;  
    	  }
    	  
    	/*  if(!EditTextValidator.esCorreo(et_correo)){
    		  et_correo.setError(getResources().getString(R.string.registro_correo_incorrecto));
			  return false;
		  }*/
		return true;
	}




	public View getView(){
		return view;
	}

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
	public void onListenerCambiarTexto(String tipo) {
		et_telefono.setText(tipo);
		
	}

    @Override
    public void onListenerCambiarTextoEnApp(String tipo) {
        et_telefono.setText(tipo);
    }

	/**
	 * escucha para poder cambair el texto de la pagina 2
	 * @param listener
	 */
	public static void setOnClickOpenContactListener( OnListenerOpenContact listener)
	{
		onListenerOpenContact = listener;
	}

}


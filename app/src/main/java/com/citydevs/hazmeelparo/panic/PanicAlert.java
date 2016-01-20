package com.citydevs.hazmeelparo.panic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.citydevs.hazmeelparo.localizacion.ServicioLocalizacion;
import com.citydevs.hazmeelparo.utils.Utils;

/**
 * Clase que envia los SMS y correos al haber un mensaje de panico
 * @author mikesaurio
 *
 */
public class PanicAlert {

	Activity context;
	int levelBattery = 0;
	
	public PanicAlert(Activity context) {
       this.context=context;
    }

	/**
	 * al activarse el selular vibra
	 */
    public void activate() {
    	Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    	v.vibrate(3000);
    }
    
    
     /**
      * envia un SMS
      * @param phoneNumber (String) numero de emergencia
      * @param message (String) mensaje de emergencia
      */ 
	public static  void sendSMS(String phoneNumber, String message)
     { 
   try{
		 SmsManager smsManager = SmsManager.getDefault();
       	 smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }catch(Exception e){
    	e.printStackTrace();
    }
   
      }

	/**
	 * Si es por sms o correo
	 */
	public  void contactaAlMAndo(String mensaje, String type, String lat, String lon) {
        try{
                if(Utils.doHttpPostAvisoAlMando(context,"https://cryptic-peak-2139.herokuapp.com/alerts.json",type, lat, lon)){
                    Utils.Toast(context,"Notificamos al Centro de Mando", Toast.LENGTH_LONG);

                    //Detener servicio
                    Intent service = new Intent(context, ServicioLocalizacion.class);
                    context.stopService(service);
                }
        }catch(Exception e){
            e.printStackTrace();
        }
	}


    /**
     * Si es por sms o correo
     */
    public  void contactaAPolicia() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "060"));
        context.startActivity(intent);
    }



}

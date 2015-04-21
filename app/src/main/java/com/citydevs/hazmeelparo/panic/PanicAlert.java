package com.citydevs.hazmeelparo.panic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsManager;
/**
 * Clase que envia los SMS y correos al haber un mensaje de panico
 * @author mikesaurio
 *
 */
public class PanicAlert {

	Context context;
	int levelBattery = 0;
	
	public PanicAlert(Context context) {
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
	public  void contactaAlMAndo() {
		
		
	}


    /**
     * Si es por sms o correo
     */
    public  void contactaAPolicia() {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "55335533"));
        context.startActivity(intent);
    }



}

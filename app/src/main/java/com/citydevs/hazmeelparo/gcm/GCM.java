package com.citydevs.hazmeelparo.gcm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.citydevs.hazmeelparo.HazmeElParoActivity;
import com.citydevs.hazmeelparo.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GCM {

	private Activity activity;
	    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	    static final String TAG = "GCMDemo";
	    private String regid;
	    String SENDER_ID = "536245866204";
	    String URL = "MY_SERVER_URL";
	 	public static final String EXTRA_MESSAGE = "message";
	    public static final String PROPERTY_REG_ID = "registration_id";


	    AtomicInteger msgId = new AtomicInteger();
	    SharedPreferences prefs;
	    Context context;
	
	public GCM(Activity activity){
		this.activity=activity;
		
	}
	
	
	/********************************************GCM****************************************/
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            activity.finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	public void registerInBackground(final GoogleCloudMessaging gcm) {
		

	    
		  new AsyncTask<String, String, String>() {
			  GoogleCloudMessaging gcm_;
			  ProgressDialog pd;
			  
	    	@Override
			protected void onPreExecute() {
                Log.d("*******************", gcm+"");
	    	pd = Utils.anillo(activity, pd);
	    	pd.show();   
	    		 if (gcm == null) {
                     Log.d("*******************", "gcm _nill");
	    			 gcm_ = GoogleCloudMessaging.getInstance(activity);
	                }else{
                     Log.d("*******************", "gcm _ NO nill");
	                 gcm_ = gcm;
	                }
				super.onPreExecute();
			}
	    	

			@Override
	        protected String doInBackground(String... params) {
	            String msg = "";
	            try {
	                regid = gcm_.register(SENDER_ID);
	                msg = regid;

	            } catch (IOException ex) {
                    ex.printStackTrace();
	                msg = "Error";
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	        	if(!msg.equals("Error")){
	        		 new   Utils(activity).setPreferenciasGCM(msg);
	        		 if(Utils.doHttpPostAltaUsuario(activity,"https://cryptic-peak-2139.herokuapp.com/clients")){
	        			 if(pd!=null)
	     	        	    pd.dismiss();

	        			 activity.startActivity(new Intent(activity,HazmeElParoActivity.class));
				         activity.finish();
	        		 }
			         

	        	}
	        	if(pd!=null)
	        		pd.dismiss();
	         
	        }
	        
	    }.execute(null, null, null);
	    
	}
	

	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	
	
	
	
	
	
	
}

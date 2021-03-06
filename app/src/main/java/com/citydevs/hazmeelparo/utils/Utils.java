package com.citydevs.hazmeelparo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.citydevs.hazmeelparo.HazmeElParoActivity;
import com.citydevs.hazmeelparo.R;
import com.citydevs.hazmeelparo.gcm.UserInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Utils {

	Activity activity;
	AlertDialog customDialog= null;
	String telefono= "";

	public Utils(Activity activity) {
		this.activity = activity;

	}

	public void setPreferenciasContacto(String[] info) {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("telefono", info[0]);
		editor.putString("mensaje", info[1]);
		editor.commit();
	}

	public String[] getPreferenciasContacto() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		String[] info = new String[2];
		info[0]=prefs.getString("telefono", null);
		info[1]=prefs.getString("mensaje", null);
		return info;
	}
	
	public void setPreferenciasGCM(String gcm) {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("gcm", gcm);
		editor.putString("placa", "1");
		editor.commit();
	}

	public String getPreferenciasGCM() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
		return prefs.getString("gcm", null);
	}

    public void setPreferenciasCAS(String CAS) {

        SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cas", CAS);
        editor.commit();
    }

    public String getPreferenciasCAS() {
        SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
        return prefs.getString("cas", "true");
    }


    public void setPreferenciasMensaje(String message) {

        SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mensajeContacto", message);
        editor.commit();
    }

    public String getPreferenciasMensaje() {
        SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
        return prefs.getString("mensajeContacto","true");
    }


    public void setPreferenciasContactarMando(String mando) {
        SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mando", mando);
        editor.commit();
    }

    public String getPreferenciasContactarMando() {
        SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBus", Context.MODE_PRIVATE);
        return prefs.getString("mando","true");
    }

    /**
	 * metodo que vaida que el telefono tenga internet
	 * 
	 * @param a
	 * @return
	 */
	public static boolean hasInternet(Activity a) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
		return hasConnectedWifi || hasConnectedMobile;
	}

	/**
	 * metodo que vaida que el telefono tenga GPS encendido
	 * 
	 * @param a
	 * @return
	 */
	public static boolean hasGPS(Activity a) {
		LocationManager mLocationManager = (LocationManager) a
				.getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}

	/**
	 * metodo que hace la conexion al servidor con una url especifica
	 * 
	 * @param url
	 *            (String) ruta del web service
	 * @return (String) resultado del service
	 */
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
			// Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
			return EntityUtils.toString(httpentiti);
		} catch (ParseException e) {

			e.getStackTrace();

			return null;
		} catch (IOException e) {
			e.getStackTrace();
			return null;
		}
	}

	

	/**
	 * obtienes el tama�o de pantalla
	 * 
	 * @param (activity) Activity
	 * @return (Point) .x = width .y = height
	 */
	public static Point getTamanoPantalla(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		return (new Point(width, height));
	}

	/**
	 * dialogo de espera
	 */
	public static ProgressDialog anillo(Activity activity,
			ProgressDialog pDialog) {
		pDialog = new ProgressDialog(activity);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setMessage(activity.getString(R.string.espere));
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(false);
		return pDialog;

	}

	
	/**
	 * Toast custom 
	 * @param context (Activity) actividad que lo llama
	 * @param text (String) texto a mostrar
	 * @param duration (int) duracion del toast
	 */
		public static void Toast(Activity context, String text, int duration) {
			LayoutInflater inflater = context.getLayoutInflater();
			View layouttoast = inflater.inflate(R.layout.toastcustom, (ViewGroup)context.findViewById(R.id.toastcustom));
			((TextView) layouttoast.findViewById(R.id.texttoast)).setText(text);
			((TextView) layouttoast.findViewById(R.id.texttoast)).setTextColor(context.getResources().getColor(R.color.color_base));
			Toast mytoast = new Toast(context);
	        mytoast.setView(layouttoast);
	        mytoast.setDuration(Toast.LENGTH_LONG);
	        mytoast.show();
		}
		
		/**
		 * Regisro de usuario en el servidor
		 * @param act
		 * @param url
		 * @return
		 */
		public static boolean doHttpPostAltaUsuario(Activity act,String url){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			 HttpParams myParams = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			    HttpConnectionParams.setSoTimeout(myParams, 10000);
			    HttpClient httpclient = new DefaultHttpClient(myParams );
			    try { 
			    	
			    JSONObject json = new JSONObject();
			    JSONObject manJson = new JSONObject();
			    manJson.put("email", UserInfo.getEmail(act));
			    manJson.put("reg_id", new Utils(act).getPreferenciasGCM());
			    manJson.put("device", "android");
			    json.put("client",manJson);
			    
			        HttpPost httppost = new HttpPost(url.toString());
			        httppost.setHeader("Content-type", "application/json");

			        StringEntity se = new StringEntity(json.toString()); 
			        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			        httppost.setEntity(se); 

			        HttpResponse response = httpclient.execute(httppost);
			        String temp = EntityUtils.toString(response.getEntity());

			return true;
			} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
			} catch (IOException e) {
			e.printStackTrace();
			return false;
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			
		}


    /**
     * metodo que hace la conexion al servidor con una url especifica
     *
     * @param url
     *            (String) ruta del web service
     * @return (String) resultado del service
     */
    public static String getPasswordBus(String url) {
        HttpClient Client = new DefaultHttpClient();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpGet httpget = new HttpGet(url);
            HttpResponse hhrpResponse = Client.execute(httpget);
            HttpEntity httpentiti = hhrpResponse.getEntity();
             //Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
            return EntityUtils.toString(httpentiti);
        } catch (ParseException e) {

            e.getStackTrace();

            return null;
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }


    public void pushNotification(String titulo, String contenido) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(titulo)
                        .setContentText(contenido);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        stackBuilder.addParentStack(HazmeElParoActivity.class);
        NotificationManager mNotificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }


    /**
     * Regisro de usuario en el servidor
     * @param act
     * @param url
     * @return
     */
    public static boolean doHttpPostAvisoAlMando(Activity act,String url, String alert_type, String lat, String lon){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        HttpClient httpclient = new DefaultHttpClient(myParams );
        try {

            JSONObject json = new JSONObject();
            JSONObject manJson = new JSONObject();
            manJson.put("email", UserInfo.getEmail(act));
            manJson.put("alert_type", alert_type);
            manJson.put("latitude", lat+"");
            manJson.put("longitude", lon+"");
            json.put("alert",manJson);

            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            String temp = EntityUtils.toString(response.getEntity());

            Log.d("PRUEBAS ED", json.toString());
			Log.d("PRUEBAS ED", temp);
			return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}

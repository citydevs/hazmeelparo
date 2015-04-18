package com.citydevs.hazmeelparo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.view.Display;

import com.citydevs.hazmeelparo.R;

public class Utils {

	Activity activity;

	public Utils(Activity activity) {
		this.activity = activity;

	}

	public void setPreferenciasContacto(String[] info) {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("telefono", info[0]);
		editor.putString("mensaje", info[1]);
		editor.commit();
	}

	public String[] getPreferenciasContacto() {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		String[] info = new String[2];
		info[0] = prefs.getString("telefono", null);
		info[1] = prefs.getString("mensaje", null);
		return info;
	}

	public void setPreferenciasGCM(String gcm) {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("gcm", gcm);
		editor.putString("placa", "1");
		editor.commit();
	}

	public String getPreferenciasGCM() {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		return prefs.getString("gcm", null);
	}

	public void setPreferenciasCAS(boolean CAS) {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("cas", CAS);
		editor.commit();
	}

	public boolean getPreferenciasCAS() {
		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		return prefs.getBoolean("cas", false);
	}

	public void setPreferenciasMando(boolean mando) {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("mando", mando);
		editor.commit();
	}

	public boolean getPreferenciasMando() {
		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		return prefs.getBoolean("mando", false);
	}

	public void setPreferenciasPlaca(String placa, String calificacion) {

		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("placa", placa);
		editor.putString("fecha_bus", getFechaHoy() + "");
		editor.putString("calificacion", calificacion);
		editor.commit();
	}

	public String[] getPreferenciasPlaca() {
		String[] info = new String[3];
		SharedPreferences prefs = activity.getSharedPreferences(
				"PreferenciasSafeBus", Context.MODE_PRIVATE);
		info[0] = prefs.getString("placa", null);
		info[1] = prefs.getString("calificacion", null);
		info[2] = prefs.getString("fecha_bus", "0");
		return info;
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
	 * Envia calificacion de viaje anterior
	 * 
	 * @param act
	 * @param calif
	 * @param comentario
	 * @return
	 */
	public static boolean doHttpPostCalificacionUsuario(Activity act,
			String calif, String comentario) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
		HttpClient httpclient = new DefaultHttpClient(myParams);
		return true;

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
	 * obtener los milisegundos de una fecha
	 * 
	 * @return
	 */
	public static long getFechaHoy() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fechaCel = now.get(Calendar.DAY_OF_MONTH) + "/"
				+ ((now.get(Calendar.MONTH)) + 1) + "/"
				+ now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY)
				+ ":" + now.get(Calendar.MINUTE) + ":"
				+ now.get(Calendar.SECOND);
		try {
			return (formatter.parse(fechaCel)).getTime();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * De assets a String
	 * 
	 * @param name
	 * @return
	 */
	private String loadAssetTextAsString(String name) {
		BufferedReader in = null;
		try {
			StringBuilder buf = new StringBuilder();
			InputStream is = activity.getAssets().open(name);
			in = new BufferedReader(new InputStreamReader(is));

			String str;
			boolean isFirst = true;
			while ((str = in.readLine()) != null) {
				if (isFirst)
					isFirst = false;
				else
					buf.append('\n');
				buf.append(str);
			}
			return buf.toString();
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		return null;
	}

}

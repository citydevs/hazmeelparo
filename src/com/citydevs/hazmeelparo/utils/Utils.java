package com.citydevs.hazmeelparo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citydevs.hazmeelparo.R;
import com.citydevs.hazmeelparo.contact.MySimpleArrayAdapter;

public class Utils {

	Activity activity;
	private ArrayList<String> listaCels;
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
		 * metodo que llena tanto el numero celular como correo de emergencia con los contactos del usuario
		 * @param intent
		 * @param tag
		 */
		public  String getContactInfo(Intent intent, int tag)
		{

			try{
				
				
				@SuppressWarnings("deprecation")
				Cursor   cursor =  activity.managedQuery(intent.getData(), null, null, null, null);      
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
				        Cursor phones = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
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
				        	dialogoLista(tag+"");
				        }
				        phones.close();
				       }
				       break;
				  }  
				  }
			}catch(Exception e){
			
			}
			return telefono;
		}
		
		
		/**
		 * agrega la vista del contacto de emergencia
		 */
		public void dialogoLista(final String tag){
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_contactos, null);
			    builder.setView(view);
			    builder.setCancelable(true);
				final ListView listview = (ListView) view.findViewById(R.id.dialogo_contacto_lv_contactos);
				final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(activity, listaCels);
			    listview.setAdapter(adapter);
			    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
			    final String item = (String) parent.getItemAtPosition(position);
			    	telefono = item.replaceAll(" ","");
			         customDialog.dismiss();
			       }
			     });
		     customDialog=builder.create();
		     customDialog.show();
		}

}

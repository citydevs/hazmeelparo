package com.citydevs.hazmeelparo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.citydevs.hazmeelparo.contact.ContactoActivity;
import com.citydevs.hazmeelparo.contact.MySimpleArrayAdapter;
import com.citydevs.hazmeelparo.interfaces.OnListenerCambiarTexto;
import com.citydevs.hazmeelparo.interfaces.OnListenerOpenContact;
import com.citydevs.hazmeelparo.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class InstructionsActivity extends Activity implements OnListenerOpenContact {
	private static Point p;
	private static int index_view = 0;
	private static OnListenerCambiarTexto onListenerCambiarTexto;
	AlertDialog customDialog= null;
	private ArrayList<String> listaCels;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	static ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_instructions);
		p = Utils.getTamanoPantalla(InstructionsActivity.this);
		
		ContactoActivity.setOnClickOpenContactListener(this); 

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);	
		
	}
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();

			return null;
		}
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;

			if (index_view == 0) {
				rootView = inflater.inflate(R.layout.fragment_instructions,
						container, false);
				index_view += 1;
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 3, p.y / 4);

				ImageView instrucciones_iv_logo = (ImageView) rootView.findViewById(R.id.instrucciones_iv_logo);
				instrucciones_iv_logo.setLayoutParams(lp);
				
				Button instrucciones_btn_siguiente = (Button) rootView.findViewById(R.id.instrucciones_btn_siguiente);
				instrucciones_btn_siguiente.setOnClickListener(this);
				
			} else if (index_view == 1) {
				ContactoActivity contacto = new ContactoActivity(getActivity());
                contacto.setOrigen("REGISTRO");
				rootView = contacto.getView();
				
				index_view = 0;
			}

			return rootView;
		}
		
		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(1);
		}
		 
	}
	
	
	public void set_contact(String telefono){
		onListenerCambiarTexto.onListenerCambiarTexto(telefono);
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
	 * Interface que comunica la pagina con la actividad 
	 * @author mikesaurio
	 *
	 */

	
	 /**
	  * escucha para poder cambair el texto de la pagina 2
	  * @param listener
	  */
	public static void setOnClickCambiarTextoListener( OnListenerCambiarTexto listener)
	{
	
		onListenerCambiarTexto = listener;
	}

	@Override
	public void onListenerOpenContact() {
		open_contact();
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
		    	set_contact(item.replaceAll(" ",""));
		         customDialog.dismiss();
		       }
		     });
	     customDialog=builder.create();
	     customDialog.show();
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
			Cursor   cursor =  managedQuery(intent.getData(), null, null, null, null);      
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
	
}

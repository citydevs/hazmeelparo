package com.citydevs.hazmeelparo;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.citydevs.hazmeelparo.contact.ContactoActivity;
import com.citydevs.hazmeelparo.contact.ContactoActivity.OnListenerOpenContact;
import com.citydevs.hazmeelparo.utils.Utils;

public class InstructionsActivity extends Activity implements OnListenerOpenContact{
	private static Point p;
	private static int index_view = 0;
	private String telefono;
	private static OnListenerCambiarTexto onListenerCambiarTexto;
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
	
	
	public void set_contact(){
		onListenerCambiarTexto.onListenerCambiarTexto(telefono);
	}
	
	public void open_contact(){
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
		startActivityForResult(intent, 0);
	}
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {	
			telefono = new Utils(InstructionsActivity.this).getContactInfo(data,0);
			set_contact();
		}
 		
	}
	
	/**
	 * Interface que comunica la pagina con la actividad 
	 * @author mikesaurio
	 *
	 */
	 public interface OnListenerCambiarTexto
	    {
	        void onListenerCambiarTexto(String telefono);
	    }
	
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

}

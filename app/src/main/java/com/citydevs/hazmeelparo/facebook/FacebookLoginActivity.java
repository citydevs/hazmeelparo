package com.citydevs.hazmeelparo.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.citydevs.hazmeelparo.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.LikeView;

import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;


/**
 * Clase que se encarga de dar like a una fanPage
 * @author mikesaurio
 *
 */
public class FacebookLoginActivity extends Activity {

	private LoginButton loginButton;
    CallbackManager callbackManager;
	private LikeView likeView; //likeButton;

	private static final List<String> PERMISSIONS = Arrays.asList("user_likes", "user_status","public_profile");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create(); // this line doesn't matter
        setContentView(R.layout.activity_facebooklogin);

	     	//uiHelper = new UiLifecycleHelper(this, statusCallback);
     		//uiHelper.onCreate(savedInstanceState);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_likes", "user_status","public_profile", "user_friends");
        // If using in a fragment
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(GONE);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

		/*	loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
			loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
				@Override
				public void onUserInfoFetched(GraphUser user) {
					if (user != null) {
						
						loginBtn.setVisibility(LoginButton.GONE);
                        likeView.setVisibility(LikeView.VISIBLE);
						likePage();
						
					} 
				}
			});

*/
        LikeView likeView = (LikeView) findViewById(R.id.like_view);
        likeView.setObjectIdAndType("https://www.facebook.com/FacebookDevelopers",LikeView.ObjectType.PAGE);

		
	}
	
	/*private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
                    Log.d("************","Open");

			} else if (state.isClosed()) {
                Log.d("************","close");
			}
		}
	};*/
	
	/*public boolean checkPermissions() {
		Session s = Session.getActiveSession();
		if (s != null) {
			return s.getPermissions().contains("publish_actions");
		} else
			return false;
	}*/

	/*public void requestPermissions() {
		Session s = Session.getActiveSession();
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, PERMISSIONS));
	}*/

	@Override
	public void onResume() {
		super.onResume();
		//uiHelper.onResume();
	
	}

	@Override
	public void onPause() {
		super.onPause();
		//uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//uiHelper.onActivityResult(requestCode, resultCode, data);
		//LikeView.handleOnActivityResult(this, requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
       // uiHelper.onActivityResult(requestCode, resultCode, data, null);
	}
	

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		//uiHelper.onSaveInstanceState(savedState);
	}
	
	/**
	 * Comprueba que el usuario haya dado like a una pagina de facebook
	 */
	/*private void likePage() {
		Session session = Session.getActiveSession();
		if (session != null) {
			Request likeRequest = new Request(session, 
					"/me/likes/775396332498712",
					null,
					HttpMethod.GET,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							FacebookRequestError error = response.getError();
							if (error != null) {
								Log.d("error like", error.getErrorMessage().toString());
							} else {
								try{
								 JSONArray albumArr = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
								 if(albumArr.length()>0){
									 Utils.Toast(FacebookLoginActivity.this, getString(R.string.Pagina_liked), Toast.LENGTH_SHORT);
									// GCM.peticionContrasena();
									 finish(); 
								 }else{
                                     Log.d("***************","else");
                                 }
								}catch(JSONException e){
                                    Log.d("***************","FALLLLLLA");
									e.printStackTrace();
								}
							}
						}
					});
			Request.executeBatchAsync(likeRequest);
		}
	}
*/


}

package com.citydevs.hazmeelparo.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.citydevs.hazmeelparo.R;
import com.citydevs.hazmeelparo.gcm.GCM;
import com.citydevs.hazmeelparo.utils.Utils;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LikeView;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;


/**
 * Clase que se encarga de dar like a una fanPage
 * @author mikesaurio
 *
 */
public class FacebookLoginActivity extends Activity {

    private LoginButton loginBtn;
    private LikeView likeButton;
    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("user_likes", "user_status", "public_profile");



    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.activity_facebooklogin);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);


        setContentView(R.layout.activity_facebooklogin);

        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions("user_likes", "user_status","public_profile", "user_friends");
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {

                    loginBtn.setVisibility(LoginButton.GONE);
                    likeButton.setVisibility(LikeView.VISIBLE);
                    likePage();

                }
            }
        });

        likeButton=(LikeView)findViewById(R.id.facebook_btn_post);
        likeButton.setObjectId("775396332498712");
	}

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {

            } else if (state.isClosed()) {

            }
        }
    };

    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        likePage();

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        LikeView.handleOnActivityResult(this, requestCode, resultCode, data);
        likePage();
    }


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    /**
     * Comprueba que el usuario haya dado like a una pagina de facebook
     */
    private void likePage() {
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
                                Log.i("error like", error.getErrorMessage().toString());
                            } else {
                                try{
                                    JSONArray albumArr = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
                                    if(albumArr.length()>0){
                                        Utils.Toast(FacebookLoginActivity.this, getString(R.string.Pagina_liked), Toast.LENGTH_SHORT);

                                       new GCM(FacebookLoginActivity.this).peticionContrasena();

                                        finish();
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            Request.executeBatchAsync(likeRequest);
        }
    }

}

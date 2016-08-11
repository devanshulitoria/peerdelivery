package peerdelivers.peerdelivery;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class FacebookLogin extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;;
    TextView tv;
    String phNumber,auth_code;
    Typeface custom_font;
    AccessTokenTracker accessTokenTracker;
    String fbAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreStart.inst.finish();
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
//        ComponentName receiver = new ComponentName(getApplication(), IncomingSms.class);
//        PackageManager pm = getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                phNumber= null;
                auth_code=null;

            } else {
                phNumber= extras.getString("phNumber");
                auth_code= extras.getString("auth_code");

            }
        } else {
            phNumber= (String) savedInstanceState.getSerializable("phNumber");
            auth_code= (String) savedInstanceState.getSerializable("auth_code");
        }
        //<// TODO: 6/24/2016

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_login);
        CheckConnection.isConnected(getApplicationContext(), FacebookLogin.this);
         accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email", "user_birthday", "user_work_history", "user_friends"));
        tv=(TextView)findViewById(R.id.fbText);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
        tv.setTypeface(custom_font);
        Log.e("start", "facebook devanshu");
        AppEventsLogger.newLogger(this);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("facebook 1","devanshu");
                loginButton.setEnabled(false);
                tv.setText("Please Wait! We are logging you in.");
                fbAccessToken=loginResult.getAccessToken().getToken();
                Log.e("Facebook token", fbAccessToken);
                Intent i = new Intent(FacebookLogin.this, MainActivity.class);
                i.putExtra("fbToken",fbAccessToken);
                //<// TODO: 5/28/2016  start
               // phNumber="+919706783069";
                //auth_code="ascd-55er-5fbg-qwer7-258fr";
                i.putExtra("phNumber",phNumber);
                i.putExtra("auth_code", auth_code);
                //// TODO: 5/28/2016  end
                startActivity(i);
                finish();
//                new GraphRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        "/me",
//                        null,
//                        HttpMethod.GET,
//                        new GraphRequest.Callback() {
//                            public void onCompleted(GraphResponse response) {
//            /* handle the result */
//                                Log.e("facebook",response.toString());
//                                JSONObject obj=response.getJSONObject();
//                                JSONArray jsonArray = obj.optJSONArray("data");
//                                for(int i=0; i < jsonArray.length(); i++){
//                                    JSONObject jsonObject = null;
//                                    try {
//                                        jsonObject = jsonArray.getJSONObject(i);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    String id = jsonObject.optString("id").toString();
//                                    String name = jsonObject.optString("name").toString();
//
//
//                                    tempdata += "Node"+i+" : \n id= "+ id +" \n Name= "+ name +  "\n ";
//                                }
//                                tv.setText(tempdata);
//                            }
//                        }
//                ).executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getBaseContext(),"Huh! Why would you cancel",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getBaseContext(),"Shit Login Failed!"+exception.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.e(data.getComponent().getClassName(),"intent data facebook");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(FacebookLogin.this, MainActivity.class);
                    i.putExtra("fbToken",fbAccessToken);
                    i.putExtra("phNumber",phNumber);
                    i.putExtra("auth_code",auth_code);
                    startActivity(i);

                    finish();
                }
            }, 100000);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(FacebookLogin.this, PreStart.class);
                    startActivity(i);

                    finish();
                }
            }, 100000);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}

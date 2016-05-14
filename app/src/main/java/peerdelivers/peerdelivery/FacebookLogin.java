package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookLogin extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    String tempdata="kela kela";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_login);
        tv=(TextView)findViewById(R.id.fbText);
        //tv.setText(tempdata);
        Log.e("start", "facebook devanshu");
        AppEventsLogger.newLogger(this);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("facebook 1","devanshu");
                String str=loginResult.getAccessToken().getToken();
                tv.setText(str);
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */
                                Log.e("facebook",response.toString());
                                JSONObject obj=response.getJSONObject();
                                JSONArray jsonArray = obj.optJSONArray("data");
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = jsonArray.getJSONObject(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String id = jsonObject.optString("id").toString();
                                    String name = jsonObject.optString("name").toString();


                                    tempdata += "Node"+i+" : \n id= "+ id +" \n Name= "+ name +  "\n ";
                                }
                                tv.setText(tempdata);
                            }
                        }
                ).executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.e("facebook exception","cancel method");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("facebook exception",exception.toString());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.e(data.getComponent().getClassName(),"intent data facebook");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package peerdelivers.peerdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {
    AsyncHttpClient myClient;
    TextView temp;
    WebView view;
    SharedPreferences cookies;
    String fbID,auth_code,phNumber;
    SharedPreferences sharedpreferences;
    String URL;
    RequestParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("SplashActivity"));
        setContentView(R.layout.activity_splash);
        URL=getResources().getString(R.string.URL)+"/splashActivity.php";
        Log.e("Connecting to:", URL);
        cookies = this.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        view = (WebView) findViewById(R.id.myWebView);
        view.loadUrl("file:///android_asset/screen.gif");
        view.setBackgroundColor(Color.TRANSPARENT);
        CheckConnection.isConnected(getApplicationContext(), SplashActivity.this);
        view.setVisibility(View.VISIBLE);
        if(CheckPreferences.hasAlreadySignedUp(getApplicationContext()))
        LoginUser();
        else{
            //<// TODO: 6/25/2016
            Intent goToNextActivity = new Intent(SplashActivity.this, PreStart.class);
            startActivity(goToNextActivity);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }



    }


    private void LoginUser() {
        sharedpreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        fbID=sharedpreferences.getString("facebook_id", null);
        auth_code=sharedpreferences.getString("auth_code", null);
        phNumber=String.valueOf(sharedpreferences.getLong("phNumber", 0));

        if(fbID==null && auth_code==null && phNumber=="0")
        {
            Toast.makeText(getApplicationContext(),
                    "Something went terribly wrong", Toast.LENGTH_LONG)
                    .show();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        PackageManager manager = this.getPackageManager();
        PackageInfo info=null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        params = new RequestParams();
        params.put("fbID",fbID);
        params.put("auth_code",auth_code);
        params.put("phNumber",phNumber);
        params.put("version",String.valueOf(info.versionCode));
        Log.e("version code:", String.valueOf(info.versionCode));
        Log.e("fbID:", String.valueOf(fbID));
        Log.e("phNumber:", String.valueOf(phNumber));
        Log.e("auth_code:", String.valueOf(auth_code));
        myClient = new AsyncHttpClient();



        myClient.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("SplashActivity:","http started");

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String obj) {
                // called when response HTTP status is "200 OK"
                String s = null;
                String version=null;
                String action=null;
                String playStoreURL=null;
                String serverMessage=null;
                JSONObject serverJSON=null;
                try {
                    serverJSON=new JSONObject(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("SplashActivity","http response:"+obj.toString());
                try {
                    s = serverJSON.getString("sessionId").toString();
                    Log.e("sessionID",s);
                    version=serverJSON.getString("change");
                    action=serverJSON.getString("exit");
                    playStoreURL=serverJSON.getString("url");
                    serverMessage=serverJSON.getString("message");
                    Log.e("versionCheck:",version+"~"+serverMessage);
                    if (!s.equalsIgnoreCase("0")) {
                        if(version.equalsIgnoreCase("true")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
                            alertDialogBuilder.setTitle("Newer Version available!");
                            alertDialogBuilder
                                    .setMessage(serverMessage)
                                    .setCancelable(false)
                                    .setPositiveButton("Get it now", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }
                                            finish();
                                        }
                                    }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            });

                            alertDialogBuilder.show();

                        }
                        else {
                            SharedPreferences.Editor editor = cookies.edit();
                            editor.putString("phpID", s);
                            editor.commit();
                            Intent goToNextActivity = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(goToNextActivity);
                            finish();
                        }

                    }

                    else if(s.equalsIgnoreCase("0")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
                        alertDialogBuilder.setTitle("This is Embarrassing!");
                        alertDialogBuilder
                                .setMessage("Well We hate to say it...but something is wrong.We promise to fix this soon")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });

                        alertDialogBuilder.show();
                    }

                    else {
                        //<// TODO: 6/24/2016 change mainactivity to prestart
                        Intent goToNextActivity = new Intent(SplashActivity.this, PreStart.class);
                        startActivity(goToNextActivity);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,  String errorResponse,Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("http failure", e.toString());
                Toast.makeText(getApplicationContext(),
                        "It seems there is some issue with your internet connection", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("retry", String.valueOf(retryNo));
            }
        });
    }
}

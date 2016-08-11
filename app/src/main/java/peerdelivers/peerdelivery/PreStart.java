package peerdelivers.peerdelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PreStart extends AppCompatActivity {
    Button sendBtn,btn_code;
    EditText txtphoneNo,et_code;
    static String phoneNo;
    static String[] message;
    static String uuid,tt;
    static int wrong_attempt=0;
    public static PreStart inst;
    public static final String user_id = "user_id";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    SharedPreferences sharedpreferences;
    List<String> permissionsNeeded = new ArrayList<String>();
    Typeface custom_font;



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PreStart.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pre_start);
        getSupportActionBar().hide();
        CheckConnection.isConnected(getApplicationContext(), PreStart.this);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
        TextView tv=(TextView)findViewById(R.id.textView8);
        tv.setTypeface(custom_font);
        inst=PreStart.this;



        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        btn_code = (Button) findViewById(R.id.bt_code);

        txtphoneNo = (EditText) findViewById(R.id.editText);
        et_code = (EditText) findViewById(R.id.et_code);

        sendBtn.setTypeface(custom_font);
        txtphoneNo.setTypeface(custom_font);
        txtphoneNo.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sharedpreferences = getSharedPreferences(user_id, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (sharedpreferences.getLong("user_id", 0) != 0) {
            Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToNextActivity);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                if(txtphoneNo.getText().toString().trim().length()==10) {
                    sendBtn.setEnabled(false);
                    sendBtn.setText(" Waiting for text message! ");
                    sendBtn.setBackgroundColor(Color.GRAY);
                    sendBtn.setVisibility(View.INVISIBLE);
                    txtphoneNo.setVisibility(View.INVISIBLE);
                    btn_code.setVisibility(View.VISIBLE);
                    et_code.setVisibility(View.VISIBLE);

                    accessPermissions();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Enter a valid phone number", Toast.LENGTH_LONG).show();
                }


            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String temp_code=et_code.getText().toString().trim();
                Log.e("from screen:",temp_code);
                Log.e("generated code:",tt);
                String[] someshit=tt.split("-");
                Log.e("after code:",someshit[0]);
                if(temp_code.equalsIgnoreCase(someshit[0])){
                    Intent inte = new Intent(PreStart.this,FacebookLogin.class);
                    inte.putExtra("phNumber",PreStart.phoneNo);
                    inte.putExtra("auth_code",PreStart.uuid);

                    inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(inte);
                }
                else{
                    if((wrong_attempt++)>3) {
                        btn_code.setVisibility(View.INVISIBLE);
                        et_code.setText("");
                        et_code.setVisibility(View.INVISIBLE);
                        txtphoneNo.setVisibility(View.VISIBLE);
                        sendBtn.setVisibility(View.VISIBLE);
                        sendBtn.setEnabled(true);
                        sendBtn.setText("Verify");

                    }
                    Toast.makeText(PreStart.this, "You have entered the wrong code.You have "+String.valueOf(3-wrong_attempt)+" chances left", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void accessPermissions() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(PreStart.this,
                Manifest.permission.READ_SMS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(PreStart.this,
                    Manifest.permission.READ_SMS)) {
                showMessageOKCancel("Inorder to verify your phone number we need to send a SMS.Could you please let us do that!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PreStart.this,
                                        new String[]{Manifest.permission.READ_SMS},
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(PreStart.this,
                    new String[]{Manifest.permission.READ_SMS},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        sendSMSMessage();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with results

                if (perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    sendSMSMessage();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreStart.this);
                    alertDialogBuilder.setTitle("Denied Permissions");
                    alertDialogBuilder
                            .setMessage("We need these permissions to serve you better")
                            .setCancelable(false)
                            .setPositiveButton("Exit now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    finish();

                                }

                            });


                    // show it
                    alertDialogBuilder.show();
                }
            }
            break;

        }
    }
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
       // <todo>
         phoneNo = "+91"+txtphoneNo.getText().toString();
        uuid = UUID.randomUUID().toString();
         tt=UUID.randomUUID().toString();
            message = tt.split("-");//uuid.split("-");
        Log.e("auth_code",tt);

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message[0], null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            Log.e(e.toString(),"smslogger");
        }

    }

    public static PreStart instance() {
        return inst;
    }



}

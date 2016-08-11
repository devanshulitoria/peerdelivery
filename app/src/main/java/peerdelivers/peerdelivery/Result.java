package peerdelivers.peerdelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class Result extends AppCompatActivity {
    Button accept,back;
    String travel_id=null;
    String source,destination,item_type;
    String URL;
    WebView viewWeb;
    AsyncHttpClient client,SendClient;
    RequestParams params,params2;
    JSONArray travel_details = null;
    JSONObject jso=new JSONObject();
    EditText et_message;
    String facebook_id;
    String fb_id;
    String noti_id="0";
    String messageRequest;
    Typeface custom_font;
    CircleImageView iv_profilepic;
    TextView tv_name,tv_work,probabilty,travel_city,travel_date;
    ImageView iv_postImage,iv_travel_type;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST=2;
    String mCurrentPhotoPath;
    private PopupWindow pgallery;
    private RadioGroup itemSort;
    private RelativeLayout ll;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);
        viewWeb = (WebView) findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Result"));
        ll=(RelativeLayout)findViewById(R.id.rr_relative_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        URL=getResources().getString(R.string.URL)+"/Result.php";
        iv_profilepic=(CircleImageView)findViewById(R.id.iv_profilepic);
        iv_profilepic.setBorderColor(Color.parseColor("#FFFFFF"));
        iv_profilepic.setBorderWidth(2);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
        iv_travel_type=(ImageView)findViewById(R.id.iv_type);
        tv_name=(TextView)findViewById(R.id.textView2);
        tv_work=(TextView)findViewById(R.id.textView3);
        probabilty=(TextView)findViewById(R.id.textView4);
        travel_city=(TextView)findViewById(R.id.textView5);
        travel_date=(TextView)findViewById(R.id.tv_doj);
        et_message=(EditText)findViewById(R.id.request_box);
        tv_name.setTypeface(custom_font);
        tv_work.setTypeface(custom_font);
        probabilty.setTypeface(custom_font);
        travel_city.setTypeface(custom_font);
        travel_date.setTypeface(custom_font);
        et_message.setTypeface(custom_font);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                travel_id= null;
                source=null;
                destination=null;
                item_type=null;


            } else {
                travel_id= extras.getString("travel_id");
                source= extras.getString("source");
                destination=extras.getString("destination");
                item_type=extras.getString("item_type");

            }
        } else {
            travel_id= (String) savedInstanceState.getSerializable("travel_id");
            source= (String) savedInstanceState.getSerializable("source");
            destination= (String) savedInstanceState.getSerializable("destination");
            item_type= (String) savedInstanceState.getSerializable("item_type");

        }
        Log.e("travel id", travel_id);
        Log.e("search_id123", source);
        accept=(Button)findViewById(R.id.bt_accept);
        back=(Button)findViewById(R.id.bt_back);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageRequest=et_message.getText().toString();
                if(messageRequest.length()>10 && mCurrentPhotoPath!=null){
                    accept.setText("Requesting...");
                    accept.setEnabled(false);
                    sendRequestToserver();

                }
                else{
                    Toast.makeText(getBaseContext(), "You cannot leave the message request blank or not upload the item picture",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        accept.setEnabled(false);
        iv_postImage = (ImageView) findViewById(R.id.imVPostPicture);
        iv_postImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPermission();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FetchDataFromTheServer();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initiatePopupWindowSort(){
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) Result.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_gallery,
                    (ViewGroup) findViewById(R.id.popup_gallery_linear));
            pgallery = new PopupWindow(layout, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT, true);
            pgallery.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pgallery.setOutsideTouchable(false);

            itemSort = (RadioGroup) layout.findViewById(R.id.radioSort);
            itemSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String selected="gallery";
                    Log.e("result","pop click|"+String.valueOf(checkedId));
                    switch(checkedId)
                    {
                        case R.id.camera:
                            dispatchTakePictureIntent();
                            break;
                        case R.id.gallery:
                            showFileChooser();
                            break;
                    }

                    pgallery.dismiss();
                }
            });
            pgallery.showAtLocation(ll,Gravity.NO_GRAVITY,500,500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("SearchForPeer",ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Result.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void checkPermission(){
        Log.e("result.java","checkpermission is called");
        int permissioncheck=-1;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            permissioncheck= ContextCompat.checkSelfPermission(Result.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.e("result.java","before permissioncheck:"+String.valueOf(permissioncheck));
            if(permissioncheck== PackageManager.PERMISSION_GRANTED){
                Log.e("result.java","permissioncheck:"+String.valueOf(permissioncheck));
                initiatePopupWindowSort();
            }
            else{
                accessPermissions();
            }
        } else {
            // Pre-Marshmallow
            initiatePopupWindowSort();

        }

        Log.e("permission", String.valueOf(permissioncheck));
    }
    private void accessPermissions() {
        Log.e("Result","inside access permissions");
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(Result.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(Result.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showMessageOKCancel("We need to external storage write permission to store pic you would be uploading",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Result.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(Result.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Result", "inside onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results

                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    initiatePopupWindowSort();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Result.this);
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
    private File createImageFile() throws IOException {
        // Create an image file name
        if(isExternalStorageWritable()){
            Log.e("External storage","Is writable");
        }
        else{
            Log.e("External storage","Is not writable");
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PeerDelivery" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        Log.e("directory",storageDir.getAbsolutePath());
        if (!storageDir.mkdirs()) {
            Log.e("directory", "Directory not created");
            Toast.makeText(getBaseContext(), "Permission to write to external storage is denied. We are sorry",
                    Toast.LENGTH_LONG).show();
            //finish();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        try{
            if(storageDir.mkdirs()) {
                Log.e("directory", "Directory  created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            Log.e("result.java","inside exception"+e.getMessage());
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =  image.getAbsolutePath();
        Log.e("photo path", mCurrentPhotoPath);
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = iv_postImage.getWidth();
        int targetH = iv_postImage.getHeight();
        Log.e("width",String.valueOf(targetW));
        Log.e("height",String.valueOf(targetH));

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Log.e("reading from",mCurrentPhotoPath);
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);



        iv_postImage.setImageBitmap(bitmap);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
            setPic();
        }
        if (requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK) {
            Uri filePath = data.getData();

            if (Build.VERSION.SDK_INT < 11)
                mCurrentPhotoPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                mCurrentPhotoPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                mCurrentPhotoPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            Log.e("gallery path::", mCurrentPhotoPath);
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                iv_postImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

public void FetchDataFromTheServer(){
    String sessionId=GetSessionCookie.getCookie(getApplicationContext());
    client = new AsyncHttpClient();
    params = new RequestParams();
    client.addHeader("Cookie", "PHPSESSID=" + sessionId + "");

    params.put("travel_id", travel_id);


    client.post(URL, params, new TextHttpResponseHandler() {

        @Override
        public void onStart() {
            // called before request is started
            Log.e("HTTP", "STARTED");

        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, String obj) {
            Log.e("SearchforResult", obj.toString());
            viewWeb.setVisibility(View.GONE);
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(String.valueOf(obj));
                travel_details = jsonObj.getJSONArray("travel_details");
                Log.e("length", String.valueOf(travel_details.length()));
                HashMap<String, String> tDetail;
                for (int i = 0; i < travel_details.length(); i++) {
                    JSONObject c = travel_details.getJSONObject(i);
                    String travel_id = c.getString("travel_id");
                    Log.e("travel_id", travel_id);
                    String name = c.getString("name");
                    String work = c.getString("work");
                    String source = c.getString("source");
                    String allow_status=c.getString("allowed_status");
                    if(Integer.parseInt(allow_status)>0){
                        accept.setEnabled(false);
                        accept.setText("Already Requested");
                    }
                    else{
                        accept.setEnabled(true);
                    }
                    facebook_id=c.getString("facebook_id");
                    String profilePicURL = c.getString("profile_pic_url");
                    String destination = c.getString("destination");
                    String type = c.getString("type_transport");
                    String gender = c.getString("gender");
                    String doj = c.getString("doj");
                    fb_id=c.getString("facebook_id");
                    DateFormat format12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date src = format12.parse(doj);
                    DateFormat ft = new SimpleDateFormat("E dd MMM yyyy");
                    doj = ft.format(src);
                    Log.e("doj", doj);
                    tv_name.setText(name);
                    tv_work.setText(work);
                    travel_city.setText(source + " to " + destination);
                    travel_date.setText(doj);
                    if (type.equalsIgnoreCase("B")) {
                        iv_travel_type.setImageResource(R.drawable.bus);
                    } else if (type.equalsIgnoreCase("T")) {
                        iv_travel_type.setImageResource(R.drawable.train);
                    } else
                        iv_travel_type.setImageResource(R.drawable.airplane);
                    if(gender.equalsIgnoreCase("M"))
                        Picasso.with(getApplicationContext()).load(profilePicURL).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.showman)).error(getApplicationContext().getResources().getDrawable(R.drawable.showman)).into(iv_profilepic);
                    else
                        Picasso.with(getApplicationContext()).load(profilePicURL).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.woman)).error(getApplicationContext().getResources().getDrawable(R.drawable.woman)).into(iv_profilepic);


                }
                Log.e("m here", "123");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("exception", e.getMessage().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            Toast.makeText(getApplicationContext(),
                    "It seems there is some issue with your internet connection "+errorResponse, Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onRetry(int retryNo) {
            // called when request is retried
            Log.e("retry", String.valueOf(retryNo));
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.i("onFinish", "OKOKOK");
        }

    });
}

    public void sendRequestToserver(){

        Bitmap photo =BitmapFactory.decodeFile(mCurrentPhotoPath);
        double photoW =Math.ceil(photo.getWidth() / 2);
        double photoH = Math.ceil(photo.getHeight() / 2);
        photo = Bitmap.createScaledBitmap(photo, (int)photoW, (int)photoH, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File myFile = new File(mCurrentPhotoPath);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(myFile);
            fOut.write(bytes.toByteArray());
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Result expection",e.getMessage());
        }

        File myFile2 = new File(mCurrentPhotoPath);
        params2=new RequestParams();
        try {
            params2.put("picture_item", myFile2);
        }catch(FileNotFoundException e) {
            Log.e("Exception","File not found");
        }
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        SendClient=new AsyncHttpClient();
        SendClient.setConnectTimeout(120000);
        SendClient.setMaxConnections(3);
        SendClient.setTimeout(120000);
        SendClient.addHeader("Cookie", "PHPSESSID=" + sessionId + "");

        params2.put("travel_id", travel_id);
        params2.put("message", messageRequest);
        params2.put("request_to", facebook_id);
        params2.put("item_type",item_type);
        params2.put("source",source);
        params2.put("destination",destination);
        //<// TODO: 7/5/2016
//        Log.e("fetchdataResult", item_type);
//        Log.e("rqst", travel_id);
//        Log.e("message", messageRequest);
//        Log.e("request_to", facebook_id);
        SendClient.post(URL, params2, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("HTTP", "STARTED");

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String obj) {
                Log.e("result.java",obj);
                accept.setText("Request Completed");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("http failure", e.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("retry", String.valueOf(retryNo));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i("onFinish", "OKOKOK");
            }

        });
    }

}

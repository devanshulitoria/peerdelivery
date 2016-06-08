package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * Created by iMac on 5/8/2016.
 */
public class SearchForPeer extends AppCompatActivity {
    Button Search;
    private RadioGroup itemRadioGroup;
    private RadioButton itemRadioButton;
    private static final String LOG_TAG = "PostSample";
    static HashMap<String,Integer> hmCities;
    static String[] cities={
            "Andaman" ,
            "Nicobar" ,
            "Adilabad" ,
            "Anantapur" ,
            "Chittoor" ,
            "Guntur" ,
            "Hyderabad" ,
            "Kadapa" ,
            "Karimnagar" ,
            "Khammam" ,
            "Krishna" ,
            "Kurnool" ,
            "Mahbubnagar" ,
            "Medak" ,
            "Nalgonda" ,
            "Nellore" ,
            "Nizamabad" ,
            "Prakasam" ,
            "Rangareddi" ,
            "Srikakulam" ,
            "Vishakhapatnam" ,
            "Vizianagaram" ,
            "Warangal" ,
            "Godavari" ,
            "Anjaw" ,
            "Changlang" ,
            "Lohit" ,
            "Lower Subansiri" ,
            "Papum Pare" ,
            "Tirap" ,
            "Dibang Valley" ,
            "Upper Subansiri" ,
            "Kameng" ,
            
            "Barpeta" ,
            "Bongaigaon" ,
            "Cachar" ,
            "Darrang" ,
            "Dhemaji" ,
            "Dhubri" ,
            "Dibrugarh" ,
            "Goalpara" ,
            "Golaghat" ,
            "Hailakandi" ,
            "Jorhat" ,
            "Karbi Anglong" ,
            "Karimganj" ,
            "Kokrajhar" ,
            "Lakhimpur" ,
            "Marigaon" ,
            "Nagaon" ,
            "Nalbari" ,
            "Cachar Hills" ,
            "Sibsagar" ,
            "Sonitpur" ,
            "Tinsukia" ,
            
            "Araria" ,
            "Aurangabad" ,
            "Banka" ,
            "Begusarai" ,
            "Bhagalpur" ,
            "Bhojpur" ,
            "Buxar" ,
            "Darbhanga" ,
            "Purba Champaran" ,
            "Gaya" ,
            "Gopalganj" ,
            "Jamui" ,
            "Jehanabad" ,
            "Khagaria" ,
            "Kishanganj" ,
            "Kaimur" ,
            "Katihar" ,
            "Lakhisarai" ,
            "Madhubani" ,
            "Munger" ,
            "Madhepura" ,
            "Muzaffarpur" ,
            "Nalanda" ,
            "Nawada" ,
            "Patna" ,
            "Purnia" ,
            "Rohtas" ,
            "Saharsa" ,
            "Samastipur" ,
            "Sheohar" ,
            "Sheikhpura" ,
            "Saran" ,
            "Sitamarhi" ,
            "Supaul" ,
            "Siwan" ,
            "Vaishali" ,
            "Pashchim Champaran" ,
            "Bastar" ,
            "Bilaspur" ,
            "Dantewada" ,
            "Dhamtari" ,
            "Durg" ,
            "Jashpur" ,
            "Janjgir-Champa" ,
            "Korba" ,
            "Koriya" ,
            "Kanker" ,
            "Kawardha" ,
            "Mahasamund" ,
            "Raigarh" ,
            "Rajnandgaon" ,
            "Raipur" ,
            "Surguja" ,
            
            
            "Diu" ,
            "Daman" ,
            "New Delhi" ,

            
            "Goa" ,
            "Ahmedabad" ,
            "Amreli District" ,
            "Anand" ,
            "Banaskantha" ,
            "Bharuch" ,
            "Bhavnagar" ,
            "Dahod" ,
            "The Dangs" ,
            "Gandhinagar" ,
            "Jamnagar" ,
            "Junagadh" ,
            "Kutch" ,
            "Kheda" ,
            "Mehsana" ,
            "Narmada" ,
            "Navsari" ,
            "Patan" ,
            "Panchmahal" ,
            "Porbandar" ,
            "Rajkot" ,
            "Sabarkantha" ,
            "Surendranagar" ,
            "Surat" ,
            "Vadodara" ,
            "Valsad" ,
            
            "Ambala" ,
            "Bhiwani" ,
            "Faridabad" ,
            "Fatehabad" ,
            "Gurgaon" ,
            "Hissar" ,
            "Jhajjar" ,
            "Jind" ,
            "Karnal" ,
            "Kaithal" ,
            "Kurukshetra" ,
            "Mahendragarh" ,
            "Mewat" ,
            "Panchkula" ,
            "Panipat" ,
            "Rewari" ,
            "Rohtak" ,
            "Sirsa" ,
            "Sonepat" ,
            "Yamuna Nagar" ,
            "Palwal" ,
            
            "Bilaspur" ,
            "Chamba" ,
            "Hamirpur" ,
            "Kangra" ,
            "Kinnaur" ,
            "Kulu" ,
            "Lahaul and Spiti" ,
            "Mandi" ,
            "Shimla" ,
            "Sirmaur" ,
            "Solan" ,
            "Una" ,
            "Anantnag" ,
            "Badgam" ,
            "Bandipore" ,
            "Baramula" ,
            "Doda" ,
            "Jammu" ,
            "Kargil" ,
            "Kathua" ,
            "Kupwara" ,
            "Leh" ,
            "Poonch" ,
            "Pulwama" ,
            "Rajauri" ,
            "Srinagar" ,
            "Samba" ,
            "Udhampur" ,
            
            "Bokaro" ,
            "Chatra" ,
            "Deoghar" ,
            "Dhanbad" ,
            "Dumka" ,
            "Purba Singhbhum" ,
            "Garhwa" ,
            "Giridih" ,
            "Godda" ,
            "Gumla" ,
            "Hazaribagh" ,
            "Koderma" ,
            "Lohardaga" ,
            "Pakur" ,
            "Palamu" ,
            "Ranchi" ,
            "Sahibganj" ,
            "Seraikela and Kharsawan" ,
            "Pashchim Singhbhum" ,
            "Ramgarh" ,
            "Bidar" ,
            "Belgaum" ,
            "Bijapur" ,
            "Bagalkot" ,
            "Bellary" ,
            "Bangalore" ,
            "Chamarajnagar" ,
            "Chikmagalur" ,
            "Chitradurga" ,
            "Davanagere" ,
            "Dharwad" ,
            "Dakshina Kannada" ,
            "Gadag" ,
            "Gulbarga" ,
            "Hassan" ,
            "Haveri District" ,
            "Kodagu" ,
            "Kolar" ,
            "Koppal" ,
            "Mandya" ,
            "Mysore" ,
            "Raichur" ,
            "Shimoga" ,
            "Tumkur" ,
            "Udupi" ,
            "Uttara Kannada" ,
            "Ramanagara" ,
            "Chikballapur" ,
            "Yadagiri" ,
            "Alappuzha" ,
            "Ernakulam" ,
            "Idukki" ,
            "Kollam" ,
            "Kannur" ,
            "Kasaragod" ,
            "Kottayam" ,
            "Kozhikode" ,
            "Malappuram" ,
            "Palakkad" ,
            "Pathanamthitta" ,
            "Thrissur" ,
            "Thiruvananthapuram" ,
            "Wayanad" ,
            
            
            "Alirajpur" ,
            "Anuppur" ,
            "Ashok Nagar" ,
            "Balaghat" ,
            "Barwani" ,
            "Betul" ,
            "Bhind" ,
            "Bhopal" ,
            "Burhanpur" ,
            "Chhatarpur" ,
            "Chhindwara" ,
            "Damoh" ,
            "Datia" ,
            "Dewas" ,
            "Dhar" ,
            "Dindori" ,
            "Guna" ,
            "Gwalior" ,
            "Harda" ,
            "Hoshangabad" ,
            "Indore" ,
            "Jabalpur" ,
            "Jhabua" ,
            "Katni" ,
            "Khandwa" ,
            "Khargone" ,
            "Mandla" ,
            "Mandsaur" ,
            "Morena" ,
            "Narsinghpur" ,
            "Neemuch" ,
            "Panna" ,
            "Rewa" ,
            "Rajgarh" ,
            "Ratlam" ,
            "Raisen" ,
            "Sagar" ,
            "Satna" ,
            "Sehore" ,
            "Seoni" ,
            "Shahdol" ,
            "Shajapur" ,
            "Sheopur" ,
            "Shivpuri" ,
            "Sidhi" ,
            "Singrauli" ,
            "Tikamgarh" ,
            "Ujjain" ,
            "Umaria" ,
            "Vidisha" ,
            
            "Ahmednagar" ,
            "Akola" ,
            "Amrawati" ,
            "Aurangabad" ,
            "Bhandara" ,
            "Beed" ,
            "Buldhana" ,
            "Chandrapur" ,
            "Dhule" ,
            "Gadchiroli" ,
            "Gondiya" ,
            "Hingoli" ,
            "Jalgaon" ,
            "Jalna" ,
            "Kolhapur" ,
            "Latur" ,
            "Mumbai City" ,
            "Mumbai suburban" ,
            "Nandurbar" ,
            "Nanded" ,
            "Nagpur" ,
            "Nashik" ,
            "Osmanabad" ,
            "Parbhani" ,
            "Pune" ,
            "Raigad" ,
            "Ratnagiri" ,
            "Sindhudurg" ,
            "Sangli" ,
            "Solapur" ,
            "Satara" ,
            "Thane" ,
            "Wardha" ,
            "Washim" ,
            "Yavatmal" ,
            "Bishnupur" ,
            "Churachandpur" ,
            "Chandel" ,
            "Imphal" ,
            "Senapati" ,
            "Tamenglong" ,
            "Thoubal" ,
            "Ukhrul" ,
            "Garo Hills" ,
            "Khasi Hills" ,
            "Jaintia Hills" ,
            "Ri-Bhoi" ,
            "Garo Hills" ,
            "Aizawl" ,
            "Champhai" ,
            "Kolasib" ,
            "Lawngtlai" ,
            "Lunglei" ,
            "Mamit" ,
            "Saiha" ,
            "Serchhip" ,
            
            "Dimapur" ,
            "Kohima" ,
            "Mokokchung" ,
            "Mon" ,
            "Phek" ,
            "Tuensang" ,
            "Wokha" ,
            "Zunheboto" ,
            
            "Angul" ,
            "Boudh" ,
            "Bhadrak" ,
            "Bolangir" ,
            "Bargarh" ,
            "Baleswar" ,
            "Cuttack" ,
            "Debagarh" ,
            "Dhenkanal" ,
            "Ganjam" ,
            "Gajapati" ,
            "Jharsuguda" ,
            "Jajapur" ,
            "Jagatsinghpur" ,
            "Khordha" ,
            "Kendujhar" ,
            "Kalahandi" ,
            "Kandhamal" ,
            "Koraput" ,
            "Kendrapara" ,
            "Malkangiri" ,
            "Mayurbhanj" ,
            "Nabarangpur" ,
            "Nuapada" ,
            "Nayagarh" ,
            "Puri" ,
            "Rayagada" ,
            "Sambalpur" ,
            "Subarnapur" ,
            "Sundargarh" ,
            "Karaikal" ,
            "Mahe" ,
            "Puducherry" ,
            "Yanam" ,
            "Amritsar" ,
            "Bathinda" ,
            "Firozpur" ,
            "Faridkot" ,
            "Fatehgarh Sahib" ,
            "Gurdaspur" ,
            "Hoshiarpur" ,
            "Jalandhar" ,
            "Kapurthala" ,
            "Ludhiana" ,
            "Mansa" ,
            "Moga" ,
            "Mukatsar" ,
            "Nawan Shehar" ,
            "Patiala" ,
            "Rupnagar" ,
            "Sangrur" ,
            "Ajmer" ,
            "Alwar" ,
            "Bikaner" ,
            "Barmer" ,
            "Banswara" ,
            "Bharatpur" ,
            "Baran" ,
            "Bundi" ,
            "Bhilwara" ,
            "Churu" ,
            "Chittorgarh" ,
            "Dausa" ,
            "Dholpur" ,
            "Dungapur" ,
            "Ganganagar" ,
            "Hanumangarh" ,
            "Juhnjhunun" ,
            "Jalore" ,
            "Jodhpur" ,
            "Jaipur" ,
            "Jaisalmer" ,
            "Jhalawar" ,
            "Karauli" ,
            "Kota" ,
            "Nagaur" ,
            "Pali" ,
            "Pratapgarh" ,
            "Rajsamand" ,
            "Sikar" ,
            "Sawai Madhopur" ,
            "Sirohi" ,
            "Tonk" ,
            "Udaipur" ,
            "Sikkim" ,
            "Ariyalur" ,
            "Chennai" ,
            "Coimbatore" ,
            "Cuddalore" ,
            "Dharmapuri" ,
            "Dindigul" ,
            "Erode" ,
            "Kanchipuram" ,
            "Kanyakumari" ,
            "Karur" ,
            "Madurai" ,
            "Nagapattinam" ,
            "The Nilgiris" ,
            "Namakkal" ,
            "Perambalur" ,
            "Pudukkottai" ,
            "Ramanathapuram" ,
            "Salem" ,
            "Sivagangai" ,
            "Tiruppur" ,
            "Tiruchirappalli" ,
            "Theni" ,
            "Tirunelveli" ,
            "Thanjavur" ,
            "Thoothukudi" ,
            "Thiruvallur" ,
            "Thiruvarur" ,
            "Tiruvannamalai" ,
            "Vellore" ,
            "Villupuram" ,
            "Dhalai" ,
            "Tripura" ,
            "Almora" ,
            "Bageshwar" ,
            "Chamoli" ,
            "Champawat" ,
            "Dehradun" ,
            "Haridwar" ,
            "Nainital" ,
            "Pauri Garhwal" ,
            "Pithoragharh" ,
            "Rudraprayag" ,
            "Tehri Garhwal" ,
            "Udham Singh Nagar" ,
            "Uttarkashi" ,
            "Agra" ,
            "Allahabad" ,
            "Aligarh" ,
            "Ambedkar Nagar" ,
            "Auraiya" ,
            "Azamgarh" ,
            "Barabanki" ,
            "Badaun" ,
            "Bagpat" ,
            "Bahraich" ,
            "Bijnor" ,
            "Ballia" ,
            "Banda" ,
            "Balrampur" ,
            "Bareilly" ,
            "Basti" ,
            "Bulandshahr" ,
            "Chandauli" ,
            "Chitrakoot" ,
            "Deoria" ,
            "Etah" ,
            "Kanshiram Nagar" ,
            "Etawah" ,
            "Firozabad" ,
            "Farrukhabad" ,
            "Fatehpur" ,
            "Faizabad" ,
            "Gautam Buddha Nagar" ,
            "Gonda" ,
            "Ghazipur" ,
            "Gorkakhpur" ,
            "Ghaziabad" ,
            "Hamirpur" ,
            "Hardoi" ,
            "Mahamaya Nagar" ,
            "Jhansi" ,
            "Jalaun" ,
            "Jyotiba Phule Nagar" ,
            "Jaunpur District" ,
            "Kanpur Dehat" ,
            "Kannauj" ,
            "Kanpur Nagar" ,
            "Kaushambi" ,
            "Kushinagar" ,
            "Lalitpur" ,
            "Lakhimpur Kheri" ,
            "Lucknow" ,
            "Mau" ,
            "Meerut" ,
            "Maharajganj" ,
            "Mahoba" ,
            "Mirzapur" ,
            "Moradabad" ,
            "Mainpuri" ,
            "Mathura" ,
            "Muzaffarnagar" ,
            "Pilibhit" ,
            "Pratapgarh" ,
            "Rampur" ,
            "Rae Bareli" ,
            "Saharanpur" ,
            "Sitapur" ,
            "Shahjahanpur" ,
            "Sant Kabir Nagar" ,
            "Siddharthnagar" ,
            "Sonbhadra" ,
            "Sant Ravidas Nagar" ,
            "Sultanpur" ,
            "Shravasti" ,
            "Unnao" ,
            "YASVANTPUR",
            "Varanasi" ,
            "Birbhum" ,
            "Bankura" ,
            "Bardhaman" ,
            "Darjeeling" ,
            "Dakshin Dinajpur" ,
            "Hooghly" ,
            "Howrah" ,
            "Jalpaiguri" ,
            "Cooch Behar" ,
            "Kolkata" ,
            "Malda" ,
            "Midnapore" ,
            "Murshidabad" ,
            "Nadia" ,
            "Parganas" ,
            "Purulia" ,
            "Uttar Dinajpur"};
    AutoCompleteTextView source;
    AutoCompleteTextView destination;
    ArrayAdapter adapter;
    String sourceText,DestText;
    AsyncHttpClient client;
    String prevFilePath="";
    String URL;
    RequestParams params;
        ImageView iv_postImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST=2;
    String mCurrentPhotoPath;
    private PopupWindow pgallery;
    private RadioGroup itemSort;
    private LinearLayout ll;
    JSONObject jso=new JSONObject();
    JSONArray jsa = new JSONArray();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        URL=getResources().getString(R.string.URL)+"/SearchForPeer.php";
        hashmapCities();
        ll=(LinearLayout)findViewById(R.id.ll_search_for_peer);
       itemRadioGroup = (RadioGroup) findViewById(R.id.radioGroupitems);
        source=(AutoCompleteTextView)findViewById(R.id.autoCompleteSource);
        destination=(AutoCompleteTextView)findViewById(R.id.autocompleteDest);
            source.setImeOptions(EditorInfo.IME_ACTION_DONE);
            destination.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Search=(Button)findViewById(R.id.b_searchPeer);
        adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,cities);
        source.setThreshold(1);//will start working from first character
        source.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                sourceText = String.valueOf(parent.getItemAtPosition(pos));

            }

        });






        destination.setThreshold(1);//will start working from first character
        destination.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        destination.setTextColor(Color.rgb(255, 225, 255));
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                DestText = String.valueOf(parent.getItemAtPosition(pos));

            }

        });

        Search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                int selectedId = itemRadioGroup.getCheckedRadioButtonId();
                itemRadioButton = (RadioButton) findViewById(selectedId);
                String errorMsg=null;
                int checkID=0;
                checkID=preCheckBeforeSending();
                if (checkID==0 || checkID==5) {
                    try {
                        jso.put("source", source.getText().toString().trim().toUpperCase());
                        jso.put("destination", destination.getText().toString().trim().toUpperCase());
                        jso.put("item", itemRadioButton.getTag().toString().trim().toUpperCase());
                        jsa.put(0,jso);
                        Log.e("SearchForpeer", jsa.toString());
                        if(checkID!=5)
                        postImageToServer();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent a = new Intent(SearchForPeer.this, SearchResult.class);
                    a.putExtra("source", source.getText().toString().trim().toUpperCase());
                    a.putExtra("destination", destination.getText().toString().trim().toUpperCase());
                    a.putExtra("item", itemRadioButton.getTag().toString().trim().toUpperCase());
                    startActivity(a);
                } else if(checkID==1) {
                    errorMsg="Source and Destination cannot be the same";
                }
                else if(checkID==2) {
                    errorMsg="Source and Destination does not exists";
                }
                else if(checkID==3) {
                    errorMsg="Picture of the item not selected";
                } else if(checkID==4) {
                    errorMsg="Type of item not selected";
                }
               if(errorMsg!=null) {
                   Toast.makeText(getBaseContext(), errorMsg,
                           Toast.LENGTH_LONG).show();

               }
                errorMsg=null;
            }
        });
         iv_postImage = (ImageView) findViewById(R.id.imVPostPicture);
        iv_postImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initiatePopupWindowSort();

            }
        });


    }
    private void initiatePopupWindowSort(){
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) SearchForPeer.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_gallery,
                    (ViewGroup) findViewById(R.id.popup_gallery_linear));
            pgallery = new PopupWindow(layout, 300, 370, true);
            pgallery.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pgallery.setOutsideTouchable(false);

            itemSort = (RadioGroup) layout.findViewById(R.id.radioSort);
            itemSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String selected="gallery";
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
                e.printStackTrace();
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
    public void postImageToServer(){

        File myFile = new File(mCurrentPhotoPath);
        RequestParams params = new RequestParams();
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        params.put("data",jsa.toString());
        try {
            params.put("picture_item", myFile);
        }catch(FileNotFoundException e) {
            Log.e("Exception","File not found");
        }
        AsyncHttpClient myClient1;
        myClient1 = new AsyncHttpClient();

        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("SearchForPeer",sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }

        myClient1.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("FRAGMENT ACTIVITY", "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("FRAGMENT ACTIVITY", "failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                // called when response HTTP status is "200 OK"
                Log.e("http response", str);


//
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
public static void hashmapCities(){
    hmCities=new HashMap<String,Integer>();
    for(int i=0;i<cities.length;i++){
        hmCities.put(cities[i].trim().toUpperCase(),i);
    }

}
    private int preCheckBeforeSending() {
        Log.e("current photo path",mCurrentPhotoPath);
        Log.e("previous photo path",prevFilePath);
        if(source.getText().toString().equalsIgnoreCase(destination.getText().toString())&& (source.getText().toString()==null) && (destination.getText().toString()==null)){
            return 1;
        }
        else if(!(hmCities.containsKey(source.getText().toString().trim().toUpperCase()))&& !(hmCities.containsKey(destination.getText().toString().trim().toUpperCase()))){
            return 2;
        }
        else if(mCurrentPhotoPath==null){
            return 3;
        }
        else if((itemRadioButton.getTag().toString()==null)){
            return 4;
        }

        else if(mCurrentPhotoPath.equalsIgnoreCase(prevFilePath)){

            prevFilePath=mCurrentPhotoPath;
            return 5;
        }
        else
        return 0;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pre_start, menu);
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

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }



}


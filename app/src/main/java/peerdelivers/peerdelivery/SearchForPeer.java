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

    private static final String LOG_TAG = "PostSample";
    static HashMap<String,Integer> hmCities;
    static String[] cities={"Andaman" , "Nicobar" , "Adilabad" , "Anantapur" , "Chittoor" , "Guntur" , "Hyderabad" , "Kadapa" , "Karimnagar" , "Khammam" , "Krishna" , "Kurnool" , "Mahbubnagar" , "Medak" , "Nalgonda" , "Nellore" , "Nizamabad" , "Prakasam" , "Rangareddi" , "Srikakulam" , "Vishakhapatnam" , "Vizianagaram" , "Warangal" , "Godavari" , "Anjaw" , "Changlang" , "Lohit" , "Subansiri" , "Papum Pare" , "Tirap" , "Dibang Valley" , "Kameng" , "Barpeta" , "Bongaigaon" , "Cachar" , "Darrang" , "Dhemaji" , "Dhubri" , "Dibrugarh" , "Goalpara" , "Golaghat" , "Hailakandi" , "Jorhat" , "Karbi Anglong" , "Karimganj" , "Kokrajhar" , "Lakhimpur" , "Marigaon" , "Nagaon" , "Nalbari" , "Cachar Hills" , "Sibsagar" , "Sonitpur" , "Tinsukia" , "Araria" , "Aurangabad" , "Banka" , "Begusarai" , "Bhagalpur" , "Bhojpur" , "Buxar" , "Darbhanga" , "Purba Champaran" , "Gaya" , "Gopalganj" , "Jamui" , "Jehanabad" , "Khagaria" , "Kishanganj" , "Kaimur" , "Katihar" , "Lakhisarai" , "Madhubani" , "Munger" , "Madhepura" , "Muzaffarpur" , "Nalanda" , "Nawada" , "Patna" , "Purnia" , "Rohtas" , "Saharsa" , "Samastipur" , "Sheohar" , "Sheikhpura" , "Saran" , "Sitamarhi" , "Supaul" , "Siwan" , "Vaishali" , "Pashchim Champaran" , "Bastar" , "Bilaspur" , "Dantewada" , "Dhamtari" , "Durg" , "Jashpur" , "Janjgir-Champa" , "Korba" , "Koriya" , "Kanker" , "Kawardha" , "Mahasamund" , "Raigarh" , "Rajnandgaon" , "Raipur" , "Surguja" , "Diu" , "Daman" , "Delhi" , "Goa" , "Ahmedabad" , "Amreli" , "Anand" , "Banaskantha" , "Bharuch" , "Bhavnagar" , "Dahod" , "The Dangs" , "Gandhinagar" , "Jamnagar" , "Junagadh" , "Kutch" , "Kheda" , "Mehsana" , "Narmada" , "Navsari" , "Patan" , "Panchmahal" , "Porbandar" , "Rajkot" , "Sabarkantha" , "Surendranagar" , "Surat" , "Vadodara" , "Valsad" , "Ambala" , "Bhiwandi" , "Faridabad" , "Fatehabad" , "Gurgaon" , "Hissar" , "Jhajjar" , "Jind" , "Karnal" , "Kaithal" , "Kurukshetra" , "Mahendragarh" , "Mewat" , "Panchkula" , "Panipat" , "Rewari" , "Rohtak" , "Sirsa" , "Sonepat" , "Yamuna Nagar" , "Palwal" , "Chamba" , "Hamirpur" , "Kangra" , "Kinnaur" , "Kulu" , "Lahaul and Spiti" , "Mandi" , "Shimla" , "Sirmaur" , "Solan" , "Una" , "Anantnag" , "Badgam" , "Bandipore" , "Baramula" , "Doda" , "Jammu" , "Kargil" , "Kathua" , "Kupwara" , "Leh" , "Poonch" , "Pulwama" , "Rajauri" , "Srinagar" , "Samba" , "Udhampur" , "Bokaro" , "Chatra" , "Deoghar" , "Dhanbad" , "Dumka" , "Singhbhum" , "Garhwa" , "Giridih" , "Godda" , "Gumla" , "Hazaribagh" , "Koderma" , "Lohardaga" , "Pakur" , "Palamu" , "Ranchi" , "Sahibganj" , "Seraikela and Kharsawan" , "Ramgarh" , "Bidar" , "Belgaum" , "Bijapur" , "Bagalkot" , "Bellary" , "Bangalore" , "Chamarajnagar" , "Chikmagalur" , "Chitradurga" , "Davanagere" , "Dharwad" , "Gadag" , "Gulbarga" , "Hassan" , "Haveri" , "Kodagu" , "Kolar" , "Koppal" , "Mandya" , "Mysore" , "Raichur" , "Shimoga" , "Tumkur" , "Udupi" , "Ramanagara" , "Chikballapur" , "Yadagiri" , "Alappuzha" , "Ernakulam" , "Idukki" , "Kollam" , "Kannur" , "Kasaragod" , "Kottayam" , "Kozhikode" , "Malappuram" , "Palakkad" , "Pathanamthitta" , "Thrissur" , "Thiruvananthapuram" , "Wayanad" , "Alirajpur" , "Anuppur" , "Ashok Nagar" , "Balaghat" , "Barwani" , "Betul" , "Bhind" , "Bhopal" , "Burhanpur" , "Chhatarpur" , "Chhindwara" , "Damoh" , "Datia" , "Dewas" , "Dhar" , "Dindori" , "Guna" , "Gwalior" , "Harda" , "Hoshangabad" , "Indore" , "Jabalpur" , "Jhabua" , "Katni" , "Khandwa" , "Khargone" , "Mandla" , "Mandsaur" , "Morena" , "Narsinghpur" , "Neemuch" , "Panna" , "Rewa" , "Rajgarh" , "Ratlam" , "Raisen" , "Sagar" , "Satna" , "Sehore" , "Seoni" , "Shahdol" , "Shajapur" , "Sheopur" , "Shivpuri" , "Sidhi" , "Singrauli" , "Tikamgarh" , "Ujjain" , "Umaria" , "Vidisha" , "Ahmednagar" , "Akola" , "Amrawati" , "Aurangabad" , "Bhandara" , "Beed" , "Buldhana" , "Chandrapur" , "Dhule" , "Gadchiroli" , "Gondiya" , "Hingoli" , "Jalgaon" , "Jalna" , "Kolhapur" , "Latur" , "Mumbai" , "Nandurbar" , "Nanded" , "Nagpur" , "Nashik" , "Osmanabad" , "Parbhani" , "Pune" , "Raigad" , "Ratnagiri" , "Sindhudurg" , "Sangli" , "Solapur" , "Satara" , "Thane" , "Wardha" , "Washim" , "Yavatmal" , "Bishnupur" , "Churachandpur" , "Chandel" , "Imphal" , "Senapati" , "Tamenglong" , "Thoubal" , "Ukhrul" , "Garo Hills" , "Khasi Hills" , "Jaintia Hills" , "Ri-Bhoi" , "Aizawl" , "Champhai" , "Kolasib" , "Lawngtlai" , "Lunglei" , "Mamit" , "Saiha" , "Serchhip" , "Dimapur" , "Kohima" , "Mokokchung" , "Mon" , "Phek" , "Tuensang" , "Wokha" , "Zunheboto" , "Angul" , "Boudh" , "Bhadrak" , "Bolangir" , "Bargarh" , "Baleswar" , "Cuttack" , "Debagarh" , "Dhenkanal" , "Ganjam" , "Gajapati" , "Jharsuguda" , "Jajapur" , "Jagatsinghpur" , "Khordha" , "Kendujhar" , "Kalahandi" , "Kandhamal" , "Koraput" , "Kendrapara" , "Malkangiri" , "Mayurbhanj" , "Nabarangpur" , "Nuapada" , "Nayagarh" , "Puri" , "Rayagada" , "Sambalpur" , "Subarnapur" , "Sundargarh" , "Karaikal" , "Mahe" , "Puducherry" , "Yanam" , "Amritsar" , "Bathinda" , "Firozpur" , "Faridkot" , "FatehgarhSahib" , "Gurdaspur" , "Hoshiarpur" , "Jalandhar" , "Kapurthala" , "Ludhiana" , "Mansa" , "Moga" , "Mukatsar" , "NawanShehar" , "Patiala" , "Rupnagar" , "Sangrur" , "Ajmer" , "Alwar" , "Bikaner" , "Barmer" , "Banswara" , "Bharatpur" , "Baran" , "Bundi" , "Bhilwara" , "Churu" , "Chittorgarh" , "Dausa" , "Dholpur" , "Dungapur" , "Ganganagar" , "Hanumangarh" , "Juhnjhunun" , "Jalore" , "Jodhpur" , "Jaipur" , "Jaisalmer" , "Jhalawar" , "Karauli" , "Kota" , "Nagaur" , "Pali" , "Pratapgarh" , "Rajsamand" , "Sikar" , "Sawai Madhopur" , "Sirohi" , "Tonk" , "Udaipur" , "Sikkim" , "Ariyalur" , "Chennai" , "Coimbatore" , "Cuddalore" , "Dharmapuri" , "Dindigul" , "Erode" , "Kanchipuram" , "Kanyakumari" , "Karur" , "Madurai" , "Nagapattinam" , "Nilgiris" , "Namakkal" , "Perambalur" , "Pudukkottai" , "Ramanathapuram" , "Salem" , "Sivagangai" , "Tiruppur" , "Tiruchirappalli" , "Theni" , "Tirunelveli" , "Thanjavur" , "Thoothukudi" , "Thiruvallur" , "Thiruvarur" , "Tiruvannamalai" , "Vellore" , "Villupuram" , "Dhalai" , "Tripura" , "Almora" , "Bageshwar" , "Chamoli" , "Champawat" , "Dehradun" , "Haridwar" , "Nainital" , "Pauri Garhwal" , "Pithoragharh" , "Rudraprayag" , "Tehri Garhwal" , "Udham Singh Nagar" , "Uttarkashi" , "Agra" , "Allahabad" , "Aligarh" , "Ambedkar" , "Auraiya" , "Azamgarh" , "Barabanki" , "Badaun" , "Bagpat" , "Bahraich" , "Bijnor" , "Ballia" , "Banda" , "Balrampur" , "Bareilly" , "Basti" , "Bulandshahr" , "Chandauli" , "Chitrakoot" , "Deoria" , "Etah" , "Kanshiram" , "Etawah" , "Firozabad" , "Farrukhabad" , "Fatehpur" , "Faizabad" , "Gautam Buddha Nagar" , "Gonda" , "Ghazipur" , "Gorkakhpur" , "Ghaziabad" , "Hamirpur" , "Hardoi" , "Mahamaya Nagar" , "Jhansi" , "Jalaun" , "Jyotiba Phule Nagar" , "Jaunpur District" , "Kannauj" , "Kanpur" , "Kaushambi" , "Kushinagar" , "Lalitpur" , "Lakhimpur" , "Lucknow" , "Mau" , "Meerut" , "Maharajganj" , "Mahoba" , "Mirzapur" , "Moradabad" , "Mainpuri" , "Mathura" , "Muzaffarnagar" , "Pilibhit" , "Pratapgarh" , "Rampur" , "Rae Bareli" , "Saharanpur" , "Sitapur" , "Shahjahanpur" , "Siddharthnagar" , "Sonbhadra" , "Sultanpur" , "Shravasti" , "Unnao" , "Yasvantpur", "Varanasi" , "Birbhum" , "Bankura" , "Bardhaman" , "Darjeeling" , "Dakshin Dinajpur" , "Hooghly" , "Howrah" , "Jalpaiguri" , "Cooch Behar" , "Kolkata" , "Malda" , "Midnapore" , "Murshidabad" , "Nadia" , "Parganas" , "Purulia" , "Dinajpur","Jamshedpur","Chandigarh","Baroda","Guwahati","Gangtok","Gulmarg","Itanagar","Karwar","Nahsik","Rourkela","Tezpur","Tirupati","Vijayawada","Hubli","Noida","Durgapur","Kolapur","Loni","Siliguri","Mangalore","Ambattur","Malegoan","Maheshtala"};
        AutoCompleteTextView source;
    AutoCompleteTextView destination;
    ArrayAdapter adapter;
    String sourceText,DestText;
    AsyncHttpClient client;
    String prevFilePath="";
    String URL;
    RequestParams params;
    private RadioGroup itemRadioGroup;
    private RadioButton itemRadioButton;
    Button change;
    JSONObject jso=new JSONObject();
    JSONArray jsa = new JSONArray();
    String search_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        itemRadioGroup = (RadioGroup) findViewById(R.id.radioGroupitems);
        change=(Button) findViewById(R.id.bt_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                if(source.getText().toString()!="" || destination.getText().toString()!="") {
                    temp = source.getText().toString();
                    source.setText(destination.getText().toString());
                    destination.setText(temp);
                }
            }
        });
        URL=getResources().getString(R.string.URL)+"/SearchForPeer.php";
        hashmapCities();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
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
                    Intent a = new Intent(SearchForPeer.this, SearchResult.class);
                    a.putExtra("source", source.getText().toString().trim().toUpperCase());
                    a.putExtra("destination", destination.getText().toString().trim().toUpperCase());
                    a.putExtra("item", itemRadioButton.getTag().toString().trim().toUpperCase());
                        a.putExtra("noti_id","0");
                    startActivity(a);
                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                } else if(checkID==1) {
                    errorMsg="Source and Destination cannot be the same";
                }
                else if(checkID==2) {
                    errorMsg="Source and Destination does not exists";
                }
                else if(checkID==4) {
                    errorMsg="Type of item not selected";
                }
               if(errorMsg!=null) {
                   Toast.makeText(getBaseContext(), errorMsg,
                           Toast.LENGTH_LONG).show();

               }
                errorMsg=null;
            }
        });



    }

        public static HashMap<String, Integer> hashmapCities(){
            hmCities=new HashMap<String,Integer>();
            for(int i=0;i<cities.length;i++){
                hmCities.put(cities[i].trim().toUpperCase(),i);
            }
                return hmCities;
        }
    private int preCheckBeforeSending() {
        Log.e("previous photo path",prevFilePath);
        if(source.getText().toString().equalsIgnoreCase(destination.getText().toString())&& (source.getText().toString()==null) && (destination.getText().toString()==null)){
            return 1;
        }
        else if(!(hmCities.containsKey(source.getText().toString().trim().toUpperCase()))&& !(hmCities.containsKey(destination.getText().toString().trim().toUpperCase()))){
            return 2;
        }
        else if((itemRadioButton.getTag().toString()==null)){
            return 4;
        }

        else
        return 0;


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
    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }



}


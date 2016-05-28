package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
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
            "North and Middle Andaman" ,
            "South Andaman" ,
            "Nicobar" ,
            "Adilabad" ,
            "Anantapur" ,
            "Chittoor" ,
            "East Godavari" ,
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
            "West Godavari" ,
            
            "Anjaw" ,
            "Changlang" ,
            "East Kameng" ,
            "Lohit" ,
            "Lower Subansiri" ,
            "Papum Pare" ,
            "Tirap" ,
            "Dibang Valley" ,
            "Upper Subansiri" ,
            "West Kameng" ,
            
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
            "North Cachar Hills" ,
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
            
            "Central Delhi" ,
            "East Delhi" ,
            "New Delhi" ,
            "North Delhi" ,
            "North East Delhi" ,
            "North West Delhi" ,
            "South Delhi" ,
            "South West Delhi" ,
            "West Delhi" ,
            
            "North Goa" ,
            "South Goa" ,
            
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
            "Imphal East" ,
            "Senapati" ,
            "Tamenglong" ,
            "Thoubal" ,
            "Ukhrul" ,
            "Imphal West" ,
            
            "East Garo Hills" ,
            "East Khasi Hills" ,
            "Jaintia Hills" ,
            "Ri-Bhoi" ,
            "South Garo Hills" ,
            "West Garo Hills" ,
            "West Khasi Hills" ,
            
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
            
            "East Sikkim" ,
            "North Sikkim" ,
            "South Sikkim" ,
            "West Sikkim" ,
            
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
            "North Tripura" ,
            "South Tripura" ,
            "West Tripura" ,
            
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
    final String URL="http://146.148.22.97/kela.php";
    RequestParams params;

    public void sendPOSTRequest(String data){
        Log.e("inside devanshu","posst request devanshu");
        params = new RequestParams();
        params.put("name", "devanshu litoria kela kela");
        //params.put("more", "data");
        client = new AsyncHttpClient();
        JSONObject jdata = new JSONObject();
        Log.e("inside","posst request devanshu");
        try {
            jdata.put("name", "devanshu HE JEY HEY");
            //jdata.put("key2", val2);
        } catch (Exception ex) {
            // json exception
            Log.e("json","exception");
        }
        StringEntity  entity=null;
        try {
            Log.e("name:::::", (String) jdata.get("name"));
            entity= new StringEntity(jdata.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("exception","entity ");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        client.post( URL,params , new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("HTTP", "STARTED");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String s = new String(response);
                Log.e("http response", s);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("http failure", errorResponse.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("retry", String.valueOf(retryNo));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        hashmapCities();

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
        destination.setTextColor(Color.rgb(255,225,255));
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                DestText=String.valueOf(parent.getItemAtPosition(pos));

            }

        });

        Search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                int selectedId = itemRadioGroup.getCheckedRadioButtonId();
                itemRadioButton = (RadioButton) findViewById(selectedId);
                if (preCheckBeforeSending()) {
                    //sendPOSTRequest("devanshu");
                    Intent a = new Intent(SearchForPeer.this, SearchResult.class);
                    a.putExtra("from", source.getText().toString().trim().toUpperCase());
                    a.putExtra("to", destination.getText().toString().trim().toUpperCase());
                    a.putExtra("item", itemRadioButton.getTag().toString().trim().toUpperCase());
                    startActivity(a);
                }
                else{
                    Toast.makeText(getBaseContext(), "You choose wrong source and Destination",
                            Toast.LENGTH_LONG).show();
                }


            }
        });



    }
public static void hashmapCities(){
    hmCities=new HashMap<String,Integer>();
    for(int i=0;i<cities.length;i++){
        hmCities.put(cities[i].trim().toUpperCase(),i);
    }

}
    private boolean preCheckBeforeSending() {
        if(source.getText().toString().equalsIgnoreCase(destination.getText().toString())&& (source.getText().toString()!=null) && (destination.getText().toString()!=null)){
            return false;
        }
        else if(!(hmCities.containsKey(source.getText().toString().trim().toUpperCase())&& hmCities.containsKey(destination.getText().toString().trim().toUpperCase())) && (itemRadioButton.getTag().toString()!=null)){
            return false;
        }
        else
        return true;
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


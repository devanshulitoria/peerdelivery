package peerdelivers.peerdelivery;
//[0]-> from
//[1]->TO
//[2]->PNR
//[3]->DOJ MM/DD/YYYY only
import android.util.Log;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by iMac on 5/29/2016.
 */
public class TextProcessing {
    static HashMap<String,String> stncode=new HashMap<String,String>();
    static HashMap<String,String> aircode=new HashMap<String,String>();
    public static void airportAndRailwaystnCodes(){
        String str="SILCHAR|SCL,SILGHAT|SHTT,SILIGURI|SGUJ,SILIGURI|SGUT,SIMALUGURI|SLGR,SINGRAULI|SGRL,SIRPUR|SKZR,SIRSA|SSA,SISWA|SBZ,SITAMARHI|SMI,SITAPUR|SPC,SITAPUR|SCC,SIURI|SURI,SIWAN|SV,SOJAT|SOD,SOLAN|SOL,SOLAPUR|SUR,SOMNATH|SMNH,SOMPETA|SPT,SONPUR|SEE,KOLHAPUR|CSMT,SRIDUNGARGARH|SDGH,SRIGANGA|SGNR,SRIKAKULAM|CHE,PUTTAPARTHI|SSPN,SUJANGARH|SUJH,SULTANPUR|SLN,SURAT|ST,SURATGARH|SOG,SURATHKAL|SL,SURENDRA|SUNR,TADEPALLIGUDEM|TDD,TAMBARAM|TBM,TATANAGAR|TATA,THALASSERY|TLY,TENALI|TEL,TENKASI|TS,TEZPUR|TZTB,THANJAVUR|TJ,THIRUVARUR|TVR,TIRUCHCHIRAPPALLI|TPJ,TIRUCHENDUR|TCN,TIRUNELVELI|TEN,TIRUPATI|TPTY,TIRUPPUR|TUP,TIRUR|TIR,TITLAGARH|TIG,THRISUR|TCR,THIRUVANANTHAPURAM|TVC,TIRUVANNAMALAI|TNM,TUMSAR|TMR,TUNDLA|TDL,TUNI|TUNI,TUTICORIN|TN,UDAIPUR|UDZ,UDHAMPUR|UHP,UDHNA|UDN,UDUPI|UD,UJJAIN|UJN,UNA|UNA,UNCHAHAR|UCR,UNNAO|ON,VADAKARA|BDJ,VADODARA|BRC,VALSAD|BL,VANCHI|MEJ,VARANASI|BSB,VARKALA|VAK,VASAI|BSR,VASCO|VSG,VELANKANNI|VLKN,VERAVAL|VRL,VIDISHA|BHS,VIJAYAWADA|BZA,VILLUPURAM|VM,VIRAMGAM|VG,VIRUDUNAGAR|VPT,VISAKHAPATNAM|VSKP,VIZIANAGARAM|VZM,VRIDDHACHALAM|VRI,WADI|WADI,WANKANER|WKR,WARANGAL|WL,WARDHA|WR,BANGALORE|YPR,ZAFARABAD|ZBD,PUDUKOTTAI|PDKT,PULGAON|PLO,PUNE|PUNE,PURANPUR|PP,PURI|PURI,PURNA|PAU,PURNIA|PRNA,PURULIA|PRR,RADHIKAPUR|RDP,RAEBARELI|RBL,RAICHUR|RC,RAIGARH|RIG,RAIPUR|R,RAJAHMUNDRY|RJY,RAJAKASAHASPUR|RJK,AGRA|RKM,RAJENDRANAGAR|RJQ,RAJGIR|RGD,RAJKOT|RJT,RANDGAON|RJN,RAJPURA|RPJ,RAMAGUNDAM|RDM,RAMANATHAPURAM|RMD,RAMESWARAM|RMM,RAMNAGAR|RMR,RAMPUR|RMU,RAMPURHAT|RPH,RANAGHAT|RHA,RANCHI|RNC,RANGAPARA|RPAN,RANGIYA|RNY,RANINAGAR|ROJ,RATANGARH|RTGH,RATLAM|RTM,RATNAGIRI|RN,RAWATGANJ|RJ,RAXAUL|RXL,RAYAGADA|RGDA,RENIGUNTA|RU,REWA|REWA,REWARI|RE,RINGUS|RGS,ROHTAK|ROK,ROORKEE|RK,ROURKELA|ROU,SADULPUR|SDLP,SAGARJAMBAGARU|SRF,SAGAULI|SGL,SAGAR|SGO,SAHARANPUR|SRE,SAHARSA|SHC,SAHIBGANJ|SBG,SHIRDI|SNSI,SALEM|SA,SALEMPUR|SRU,SAMALKOT|SLO,SAMASTIPUR|SPJ,SAMBALPUR|SBP,SAMBALPUR|SBPY,SAMDARI|SMR,SAMUKTALA|AMTA,SANGLI|SLI,SANTRAGACHI|SRC,SARNATH|SRNT,SATARA|STR,SATNA|STA,SATTENAPALLE|SAP,SAWAIMADHOPUR|SWM,SAWANTWADI|SWV,SEALDAH|SDAH,SECUNDERABAD|SC,SENGOTTAI|SCT,SENSOA|SCF,SEWAGRAM|SEGM,SHAHABAD|SDB,SHAHGANJ|SHG,SHAHJAHANPUR|SPN,SHAHPUR|PATOREE|SPP,SHAKTINAGAR|SKTN,SHALIMAR|SHM,SHAMGARH|SGZ,SHAMLI|SMQL,SHIKOHABAD|SKB,SHIMLA|SML,SHIMOGA|SMET,SHIVPURI|SVPI,SHORANUR|SRR,MAHABIRJI|SMBJ,SIHOR|SOJN,SIKAR|SIKR,MORADABAD|MB,MOTIHARI|MKI,MUDKHED|MUE,MUGHALSARAI|MGS,MUMBAI|BCT,MUMBAI|CSTM,MUNIGUDA|MNGD,MURI|MURI,MURKEONGSELEK|MZS,MURTAJAPUR|MZR,MUZAFFAR|MOZ,MUZAFFARPUR|MFP,MYSORE|MYS,NABADWIPDHAM|NDAE,NADIAD|ND,NADIKUDI|NDKD,NAGAPPATT|NGT,KATA|NKB,SOL|NSL,NAGBHIR|NAB,NAGDA|NAD,NAGERCOIL|NCJ,NAGORE|NCR,NAGPUR|NGP,NAGRAKATA|NKB,NAINPUR|NIR,NAJIBABAD|NBD,NALANDA|NLD,NALGONDA|NLDA,NAMAKKAL|NMKL,NANDALUR|NRE,NANDGAON|NGN,NANDURBAR|NDB,NANDYAL|NDL,NANGAL|NLDM,NARKATIAGANJ|NKE,NARASAPUR|NS,NARSINGPUR|NU,NARWANA|NRW,NASIK|NK,NAUGARH|NUH,NELLORE|NLR,GOMOH|GMO,ALIPURDUAR|NOQ,BONGAIGAON|NBQ,COOCHBEHAR|NCB,DELHI|NDLS,FARAKKA|NFK,JALPAIGURI|NJP,NEWMAL|NMZ,TINSUKIA|NTSK,NIDADAVOLU|NDD,NIDAMANGALAM|NMJ,NIDUBROLU|NDO,NIMACH|NMH,NIZAMABAD|NZB,DELHI|NZM,NOLI|NOLI,LAKHIMPUR|NLP,ODLABARI|ODB,OKHA|OKHA,ONGOLE|OGL,ORAI|ORAI,PACHORA|PC,PALANI|PLNI,PALANPUR|PNU,PALASA|PSA,PALAKKAD|PGT,PALAKKAD|PGTN,PALIAKALAN|PLK,PANDHARAPUR|PVR,PANIPAT|PNP,PARADEEP|PRDP,PARASNATH|PNME,PANVEL|PNVL,PARBHANI|PBN,PARVATIPURAM|PVPT,PATHANKOT|PTK,PATIALA|PTA,PATNA|PNBE,PATNA|PNC,PHALODI|PLC,PHAPHAMAU|PFM,PHULERA|FL,PILIBHIT|PBE,PIPARIYA|PPI,PODANUR|PTJ,PORBANDAR|PBR,PRATAPGARH|PBH,KAZIPET|KZJ,KESINGA|KSNG,KENDUJHARGARH|KDJR,KHAGARIA|KGG,KHALILABAD|KLD,KHAMMAM|KMT,KHANDWA|KNW,KHARAGPUR|KGP,KHEKRA|KEX,KHURDA|KUR,KHURJA|KRJ,KISHANGANJ|KNE,KISHANGARH|KSG,KIUL|KIUL,KOCHUVELI|KCVL,KODAIKANAL|KQN,KOLKATA|KOAA,KOLLAM|QLN,KOPERGAON|KPG,KORAPUT|KRPU,KORBA|KRBA,KOTA|KOTA,KOTDWARA|KTW,KOTKAPURA|KKP,KOTTAYAM|KTYM,KOZHIKODE|CLT,KRISHNANAGAR|KNJ,KRISHNARAJAPURAM|KJM,KUMARGHAT|KUGT,KUMBAKONAM|KMU,KUNDAPURA|KUDA,KURDUWADI|KWV,KURNOOL|KRNT,KURUKSHETRA|KKDE,LAKHIMPUR|LMP,LAKSAR|LRJ,LALGARH|LGH,LALGOLA|LGL,LALITPUR|LAR,LALKUAN|LKU,LATUR|LUR,LEDO|LEDO,LOHARU|LHU,LOKMANYATILAK|LTT,LONAVLA|LNL,LONDA|LD,HALFLONG|LFG,LUCKNOW|LKO,LUDHIANA|LDH,LUMDING|LMG,LUNI|LUNI,MACHILIPATNAM|MTM,MADARIHAT|MDT,MADDUR|MAD,GOA|MAO,MADHUBANI|MBI,MADHUPUR|MDP,MADURAI|MDU,MAHASAMUND|MSMD,MAHBUBNAGAR|MBNR,MAHUVA|MHV,MUNIGUDA|MNGD,MAHESANA|MSH,MAHOBA|MBA,MAILANI|MLN,MAKSI|MKC,MALDA|MLDT,MANAMADURAI|MNM,MANDUADIH|MUV,MANGALORE|MAQ,MANGALORE|MAJN,MANIKPUR|MKP,MANKAPUR|MUR,MANMAD|MMR,MANNARGUDI|MQ,MANSI|MNE,MANU|MANU,MARIANI|MXN,MARWAR|MJ,MATHURA|MTJ,MAU|MAU,MAYILADUTURAI|MV,MEERUT|MTC,MERTA|MTD,METTUPALAYAM|MTP,MIDNAPORE|MDN,MIRAJ|MRJ,MIRYALAGUDA|MRGA,MIRZAPUR|MZP,MOGA|MOF,BELGAUM|BGM,BELLARY|BAY,BETTIAH|BTH,BETUL|BZU,BHADOHI|BOY,BHADRAK|BHC,BHAGALPUR|BGP,BHAGAT|BGKI,BHARATPUR|BTE,BHARUCH|BH,BHATAPARA|BYT,BHATKAL|BTJL,BHATNI|BTT,BHAVNAGAR|BVC,BHAWANIPATNA|BWPI,BHILWARA|BHL,BHIMAVARAM|BVRM,BHIMAVARAM|TOWN|BVRT,BHIND|BIX,BHIWANI|BNW,BHOPAL|BPL,BHUBANESWAR|BBS,BHUJ|BHUJ,BHUSAVAL|BSL,BIJAPUR|BJP,BIJNOR|BJO,BIKANER|BKN,BILASPUR|BSP,BINA|BINA,BINNAGURI|BNV,BIRUR|RRB,BITRAGUNTA|BTTR,BIYAVARA|BRRG,BOBBILI|VBL,BOINDA|BONA,BARAPA|BRPL,BAGHBAHAR|BGBR,BOKARO|BKSC,BOLPUR|BHP,BORIVALI|BVI,BOTAD|BTD,BUDAUN|BEM,BUNDI|BUDI,BURHANPUR|BAU,BURHWAL|BUW,BUXAR|BXR,CANACONA|CNO,CHAKIA|CAA,CHAKKI|BANK|CHKB,CHAKRADHARPUR|CKP,CHALISGAON|CSN,CHAMPA|CPH,CHANDAUSI|CH,CHANDERIYA|CNA,CHANDIGARH|CDG,CHANDIL|CNI,CHANDRAPUR|CD,CHANDRAPURA|CRP,CHAPARMUKH|CPK,CHATRAPUR|CAP,CHENGALPATTU|CGL,CHENGANNUR|CNGR,CHENNAI|MAS,CHHAPRA|CPR,CHHINDWARA|CWA,CHIDAMBARAM|CDM,CHIKJAJUR|J|RU,CHIPLUN|CHI,CHIRALA|CLX,CHITRAKOOTDHAM|CKTD,CHITTARANJAN|CRJ,CHITTAURGARH|COR,CHITTOOR|CTO,CHOPAN|CPU,CHUNAR|CAR,CHURU|CUR,COIMBATORE|CBE,COONOOR|ONR,CUDDALORE|CUPJ,CUDDAPAH|HX,CUTTACK|CTC,DADAR|DR,DAHANU|DRD,DAHOD|DHD,DALGAON|DLO,DALTONGANJ|DTO,DALLIRAJHARA|DRZ,DAMOH|DMO,ABUROAD|ABR,ADILABAD|ADB,ADONI|AD,ADRA|ADRA,AGARTALA|AGTL,AGRA|FORT|AF,AGRA|AGC,AHMADNAGAR|ANG,AHMEDABAD|ADI,AJMER|AII,AJNI|AJNI,AKOLA|AK,ALIGARH|ALJN,ALIPURDUAR|APDJ,ALLAHABAD|ALD,ALAPPUZHA|ALLP,ALNAWAR|LWR,ALUVA|AWY,ALWAR|AWR,AMALNER|AN,AMB|ANDAVRA|AADR,AMBALA|UMB,AMBIKAPUR|ABKP,AMLA|AMLA,AMRITSAR|ASR,ANAKAPALLE|AKP,ANAND|ANND,ANAND|ANDN,ANAND|VIHAR|TERMINUS|ANVT,ANANTAPUR|ATP,ANGUL|ANGL,ANNAVARAM|ANV,ANUPPUR|APR,ARA|ARA,ARAKKONAM|AJJ,ARSIKERE|ASK,ASANSOL|ASN,AUNRIHAR|ARJ,AURANGABAD|AWB,AYODHYA|AY,AZAMGARH|AMH,AZIMGANJ|AZ,BADARPUR|BPB,BADNERA|BD,BAGALKOT|BGK,BAGHPAT|BPM,BAIDYANATHDHAM|BDME,BAKTHIYARPUR|BKP,BALASORE|BLS,BALAGHAT|BTC,BALANGIR|BLGR,BALUGAON|BALU,BALURGHAT|BLGT,BALHARSHAH|BPQ,BALLIA|BUI,BANARHAT|BNQ,BANDA|BNDA,BANDEL|BDC,BANDIKUI|BKI,BANDRA|BDTS,BANGALORE|SBC,BANGALORE|BNC,BANGARAPET|BWT,BANGRIPOSI|BGY,BANKURA|BQA,BANMANKHI|BNKI,BAPATLA|BPP,BARABANKI|BBK,BARABIL|BBN,BARAN|BAZ,BARAUNI|BJU,BARAUT|BTU,BARAPALLI|BRPL,BARDDHAMAN|BWN,BAREILLY|BE,BARGARH|BRGA,BARHNI|BNY,BARKAKANA|BRKA,BARMER|BME,BAROG|BOF,BARSOI|BOE,BARWADIH|BRWD,BASAR|BSX,BASTI|BST,BATHINDA|BTI,BAYANA|BXN,BEAS|BEAS,BEAWAR|BER,BELAPUR|BAP,DANAPUR|DNR,DARBHANGA|DBG,DAUND|DD,DAVANGERE|DVG,DEHRADUN|DDN,DEHRI|DOS,DELHI|DLI,DELHI|NDLS,DELHI|DEC,DELHI|DEE,DELHI|DSA,DEORIASADAR|DEOS,DEVLALI|DVL,DHAMANGAON|DMN,DHANBAD|DHN,DHARMABAD|DAB,DHARMANAGAR|DMR,DHARMAPURI|DPJ,DHARMAVARAM|DMM,DHARWAD|DWR,DHASA|DAS,DHAULPUR|DHO,DHENUANAL|DNUL,DHOLA|DLJ,DHONE|DHNE,DHRANGADHRA|DHG,DHUBRI|DBB,DHURI|DUI,DIBRUGARH|DBRG,DIBRUGARH|DBRT,DIGHA|DGHA,DILDARNAGAR|DLN,DIMAPUR|DMV,DINDIGUL|DG,DONGARGARH|DGG,DORNAKAL|DKJ,DUNGARPUR|DNRP,DURG|DURG,DURGAPUR|DGR,DUVVADA|DVD,DWARKA|DWK,ELURU|EE,ERNAKULAM|ERS,ERNAKULAM|ERN,ERODE|ED,ETAWAH|ETW,FAIZABAD|FD,FARIDABAD|FDB,FARRUKHABAD|FBD,FATEHABAD|FTD,FATEHPUR|FTP,FATUHA|FUT,FAZILKA|FKA,FIROZABAD|FZD,FIROZPUR|FZP,FIROZPUR|FZR,FORBESGANJ|FBG,FURKATING|FKG,GADAG|GDG,GAJRAULA|GJL,GANDHIDHAM|GIM,GANDHINAGAR|GADJ,GANGAPUR|GGC,GARWA|GHD,GAYA|GAYA,GEVRA|GAD,GHATSILA|GTS,GHAZIABAD|GZB,GOALPARA|GLPT,GODHRA|GDA,GOLA|GK,GONDA|GD,GONDIA|G,GOOTY|GY,GORAKHPUR|GKP,GOSSAINGAON|GOGH,GUDIVADA|GDV,GUDUR|GDR,GULBARGA|GR,GUNA|GUNA,GUNTAKAL|GTL,GUNTUR|GNT,GURGAON|GGN,GURUVAY|GUV,GUWAHATI|GHY,GWALIOR|GWL,GYANPUR|GYN,HABIBGANJ|HBJ,HAJIPUR|HJP,HALDIA|HLZ,HALDIBARI|HDB,HAMILTONGANJ|HOJ,HANUMANGARH|HMH,HAPA|HAPA,HAPUR|HPU,HARDA|HD,HARIDWAR|HW,HARIHAR|HRR,HARPALPUR|HPP,HASIMARA|HSA,HATIA|HTE,HAZUR|NED,HILSA|HIL,HIMMAT|HMT,HINDUPUR|HUP,HINGOLI|HNL,HISAR|HSR,HOSHANGABAD|HBD,HOSPET|HPT,HOSUR|HSRA,HOWBADH|HBG,HOWRAH|HWH,HUBLI|UBL,HYDERABAD|HYB,IGATPURI|IGP,INDARA|IAA,INDORE|INDB,ISLAMPUR|IPR,ITARSI|ET,JABALPUR|JBP,JAGDALPUR|JDB,JAIPUR|JP,JAYPUR|JYP,JAISALMER|JSM,JAJPUR|JJKR,JAKHAL|JHL,JALAMB|JM,JALANDHAR|JRC,JALGAON|JL,JALNA|J,JALPAIGURI|JPG,JAMALPUR|JMP,JAMMU|JAT,JAMNAGAR|JAM,JANGHAI|JNH,JASIDIH|JSME,JAUNPUR|JNU,JAYNAGAR|JYG,JETALSAR|JLR,JHAJHA|JAJ,JHANSI|JHS,JHARGRAM|JGM,JHARSUGUDA|JSG,JIND|JIND,JODHPUR|JU,JOGBANI|JBN,JOLARPETTAI|JTJ,JORHAT|JT,JUNAGARH|JND,KACHEGUDA|KCG,KAKINADA|COA,KALCHINI|KCF,KALKA|KLK,KALOL|KLL,KALYAN|KYN,KAMAKHYA|KYQ,KANCHIPURAM|CJ,KANDHLA|KQL,KANNIYAKUMARI|CAPE,KANNUR|CAN,KANPUR|CNB,KANPUR|CPA,KAPTANGANJ|CPJ,KARAIKAL|KIK,KARAIKKUDI|KKDI,KARIMGANJ|KXJ,KARNAL|KUN,KARUR|KRR,KARWAR|KAWR,KASARAGOD|KGQ,KASGANJ|KSJ,KATHGODAM|KGM,KATIHAR|KIR,KATNI|KTE,KATPADI|KPD,KATWA|KWAE";
        String[] splittemp=str.split(",");
        String[] tmp;
        for(int i=0;i<splittemp.length;i++){
            String x=new String(splittemp[i].replace("|", ","));
            tmp=x.split(",");
            stncode.put(tmp[1].trim(), tmp[0].trim());
        }


        String airportName="AGARTALA ,AGATTI ISLAND ,AGRA ,AHMEDABAD ,AIZAWL ,AKOLA ,ALLAHABAD ,ALONG ,AMRITSAR ,ANAND ,AURANGABAD ,BAGDOGRA ,BALURGHAT ,BANGALORE ,BELGAUM ,BELLARY ,BHATINDA ,BHAVNAGAR ,BHOPAL ,BHUBANESWAR ,BHUJ ,BIKANER ,BILASPUR ,BIRD ISLAND ,BOMBAY ,CALCUTTA ,CALICUT ,CAR NICOBAR ,CHANDIGARH ,CHENNAI ,COCHIN ,COIMBATORE ,COOCH BEHAR ,CUDDAPAH ,DAMAN ,DAPARIZO ,DARJEELING ,DEHRADUN ,DELHI ,DHANBAD ,DHARAMSALA ,DIBRUGARH ,DIMAPUR ,DIU ,FARIDABAD ,GAUHATI ,GAYA,GOA ,GORAKHPUR ,GUNA ,GUWAHATI ,GWALIOR ,HISSAR ,HUBLI ,HYDERABAD ,IMPHAL ,INDORE ,JABALPUR ,JAGDALPUR ,JAIPUR ,JAISALMER ,JAMMU ,JAMNAGAR ,JAMSHEDPUR ,JEYPORE ,JODHPUR ,JORHAT ,KAILASHAHAR ,KAMALPUR ,KANDLA ,KANPUR ,KESHOD ,KHAJURAHO ,KHOWAI ,KOLHAPUR ,KOTA ,KOZHIKODE ,KULU ,LEH ,LILABARI ,LUCKNOW ,LUDHIANA ,MADRAS ,MADURAI ,MAHE ISLAND ,MALDA ,MANGALORE ,MOHANBARI ,MUZAFFARNAGAR ,MUZAFFARPUR ,MYSORE ,NAGPUR ,NANDED ,NASIK ,NAWANSHAHAR ,NEYVELI ,OSMANABAD ,PANAJI ,PANTNAGAR ,PASSIGHAT ,PATHANKOT ,PATNA ,PONDICHERRY ,PUNE ,PORBANDAR ,PUTTAPARTHI ,RAE BARELI ,RAIPUR ,RAJAHMUNDRY ,RAJKOT ,RAJOURI ,RAMAGUNDAM ,RANCHI ,RATNAGIRI ,REWA ,ROURKELA ,RUPSI ,SATNA ,SHILLONG ,SHOLAPUR ,SILCHAR ,SIMLA ,SRINAGAR ,SURAT ,TEZPUR ,TEZU ,THANJAVUR ,TIRUCHIRAPALLY ,TIRUPATI ,TRIVANDRUM ,TUTICORIN ,UDAIPUR ,VADODARA ,VARANASI ,VIJAYAWADA ,VISHAKHAPATNAM ,VIZAG ,WARRANGAL ,ZERO";
        String airportCode="IXA ,AGX ,AGR ,AMD ,AJL ,AKD ,IXD ,IXV ,ATQ ,QNB ,IXU ,IXB ,RGH ,BLR ,IXG ,BEP ,BUP ,BHU ,BHO ,BBI ,BHJ ,BKB ,PAB ,BDI ,BOM ,CCU ,CCJ ,CBD ,IXC ,MAA ,COK ,CJB ,COH ,CDP ,NMB ,DAE ,DAI ,DED ,DEL ,DBD ,DHM ,DIB ,DMU ,DIU ,QNF ,GAU ,GAY ,GOI ,GOP ,GUX ,GAU ,GWL ,HSS ,HBX ,HYD ,IMF ,IDR ,JLR ,JGB ,JAI ,JSA ,IXJ ,JGA ,IXW ,PYB ,JDH ,JRH ,IXH ,IXQ ,IXY ,KNU ,IXK ,HJR ,IXN ,KLH ,KTU ,CCJ ,KUU ,IXL ,IXI ,LKO ,LUH ,MAA ,IXM ,SEZ ,LDA ,IXE ,MOH ,MZA ,MZU ,MYQ ,NAG ,NDC ,ISK ,QNW ,NVY ,OMN ,DBL ,PGH ,IXT ,IXP ,PAT ,PNY ,PNQ ,PBD ,BEK ,PUT ,RPR ,RJA ,RAJ ,RJI ,RMD ,IXR ,RTC ,REW ,RRK ,RUP ,TNI ,SHL ,SSE ,IXS ,SLV ,SXR ,STV ,TEZ ,TEI ,TJV ,TRZ ,TIR ,TRV ,TCR ,UDR ,BDQ ,VNS ,VGA ,VTZ ,VIZ ,WGC ,ZER";
        String[] splitAirName=airportName.split(",");
        String[] splitAirCode=airportCode.split(",");
        for(int j=0;j<splitAirName.length;j++){
            aircode.put(splitAirCode[j].trim(),splitAirName[j].trim());
        }

    }
    public  static String[] irtcProcessing(String str){
        String[] temp=null;
        String[] info=new String[5];
            System.out.println();
            if(str.contains(",")){
                temp=str.split(",");
                info=checkforTokens(temp,str);
                if(info!=null){
                   return info;
                }

            }

        return null;
    }
    public static String[] checkforTokens(String[] tokens,String str){
        airportAndRailwaystnCodes();
        String[] tempinfo=new String[5];
        String[] travelDetails=new String[5];
        String[] grabage;
        if(str.contains("TID")){
            return null;
        }
        else{
            try {
                for(int i=0;i<tokens.length;i++){
                    // to fetch pnr
                    if(tokens[i].contains("PNR")){
                        grabage=tokens[i].split(":");
                        tempinfo[0]=grabage[1];
                    }


                    else if(tokens[i].contains("TRAIN")){
                        grabage=tokens[i].split(":");
                        tempinfo[4]=grabage[1];
                    }

                    else if(tokens[i].contains("DOJ")){
                        grabage=tokens[i].split(":");
                        tempinfo[3]=grabage[1];
                        tempinfo[3]=dateConversion(tempinfo[3],"dd-MM-yy");
                    }

                    else if(tokens[i].contains("-")){
                        if(tokens[i].startsWith("-", 3) && tokens[i].charAt(2)!='-'){
                            grabage=tokens[i].split("-");
                            tempinfo[1]=grabage[0];
                            tempinfo[2]=grabage[1];
                        }
                    }

                    grabage=null;

                }
                if(tempinfo[0]!=null && tempinfo[1]!=null) {
                    travelDetails[0] = stncode.get(tempinfo[1].toUpperCase().trim());
                    travelDetails[1] = stncode.get(tempinfo[2].toUpperCase().trim());
                    travelDetails[2] = tempinfo[0].toUpperCase().trim();
                    travelDetails[3] = tempinfo[3].toUpperCase().trim();
                    Log.e("Text train",travelDetails[0]+":"+travelDetails[1]+":"+travelDetails[2]);
                    return travelDetails;
                }
                else
                    return null;
            } catch (NullPointerException e) {
                return null;
            }catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }

        }


    }
    public static String dateConversion(String dt,String format){
        Date src=null;
        DateFormat srcDf=new SimpleDateFormat(format);
        try {
            src=srcDf.parse(dt);

        } catch (ParseException e1) {

            return null;
        }
        DateFormat ft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = null;

        d=ft.format(src);
        return d;
    }
    public static String[] redBusProcessing(String[] str,String original){
        Log.e("textprocessing", Arrays.toString(str));
        for(int y=0;y<str.length;y++){
            Log.e("bus loop",str[y]+"-->"+String.valueOf(y));
        }
        String[] travelDetails=new String[5];
        try {
            int start = 0, end = 0;
            travelDetails[0] = str[2];
            travelDetails[1] = str[3];
            travelDetails[2] = str[11];
            Pattern pattern = Pattern.compile("<Dep time>");
            Matcher matcher = pattern.matcher(original);
            if (matcher.find()) {
                start = matcher.start() + "<Dep time>".length() + 1;
            }
            pattern = Pattern.compile("<Seats>");
            matcher = pattern.matcher(original);
            if (matcher.find()) {
                end = matcher.start();
            }
            travelDetails[3] = original.substring(start, end).trim();
            travelDetails[3] = dateConversion(travelDetails[3], "hh:mm a MMM dd,yyyy");

            return travelDetails;
        }catch (Exception e) {
            Log.e("text bus exception",e.getMessage());
            return null;
        }
    }
    public static String[] spicejet(String[] str){
        String[] travelDetails=new String[5];
        for(int i=0;i<str.length;i++){
            Log.e("striping",str[i]+"-->"+String.valueOf(i));
        }
        DateFormat srcDf=new SimpleDateFormat("dd-MMM-yy hh:mm a");
        try {
            travelDetails[3] = str[6] + "-" + str[7] + "-" + str[8] + " " + str[10] + ":" + str[11] + " " + str[12];
            travelDetails[0] = str[13];
            travelDetails[1] = str[14];
            travelDetails[2] = str[16];

            Date src = null;

            src = srcDf.parse(travelDetails[3]);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = null;

            d = format.format(src);

            travelDetails[3] = d;
            return travelDetails;
        }catch (Exception e){
            Log.e("spicejet exception",e.getMessage());
            return null;
        }

    }
    public static String[] makeMyTripProcessing(String[] str){
        String[] travelDetails=new String[5];
        try {
            travelDetails[0] = str[4];
            travelDetails[1] = str[5];

            travelDetails[3] = str[9] + ":" + str[10] + " " + str[7] + " " + str[6] + "," + "20" + str[8];

            travelDetails[2] = str[9];

            travelDetails[3] = dateConversion(travelDetails[3], "hh:mm MMM dd,yyyy");

            return travelDetails;
        }catch (Exception e){
            Log.e("makemytripException",e.getMessage());
            return null;
        }



    }
    public static String[] goIbiboProcessing(String[] str){
        String[] travelDetails=new String[5];
        airportAndRailwaystnCodes();
       String []dayWeek={"","SUN","MON","TUE","WED","THU","FRI","SAT"};
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        DateFormat format12=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date src=null;
        try{
            if(aircode.containsKey(str[8]))
            travelDetails[0]=aircode.get(str[8]);
            if(aircode.containsKey(str[9]))
            travelDetails[1]=aircode.get(str[9]);
            else
            return null;
            travelDetails[3]=str[10].substring(0, 2)+":"+str[10].substring(2, 4)+" "+str[7]+" "+str[6]+","+String.valueOf(year);
            travelDetails[3] = dateConversion(travelDetails[3], "hh:mm MMM dd,yyyy");
            travelDetails[2]=str[2];

            src=format12.parse(travelDetails[3]);
            c.setTime(src);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if(dayWeek[dayOfWeek].equalsIgnoreCase(str[5])) {

                return travelDetails;
            }
            else {
                travelDetails[3] = str[10].substring(0, 2) + ":" + str[10].substring(2, 4) + " " + str[7] + " " + str[6] + "," + String.valueOf(year+1);
                travelDetails[3] = dateConversion(travelDetails[3], "hh:mm MMM dd,yyyy");
                return travelDetails;
            }
        }catch (Exception e){
            Log.e("exception text14",e.getMessage());
            return null;

        }

    }
    public static String[] yatraaProcessing(String[] str){
        try {
            String[] travelDetails = new String[5];
            travelDetails[0] = str[7];
            travelDetails[1] = str[8];
            String day = (Integer.parseInt(str[5]) < 9) ? "0" + str[5] : str[5];
            int year = Calendar.getInstance().get(Calendar.YEAR);
            travelDetails[3] = "11" + ":" + "00" + " " + str[6] + " " + day + "," + String.valueOf(year);
            travelDetails[2] = str[4];
            travelDetails[3] = dateConversion(travelDetails[3], "hh:mm MMMM dd,yyyy");
            return travelDetails;
        }catch (Exception e){
            return null;
        }
    }
    public static String[] striping(String str){
        String []filter={"[-+.^:,@!/><]","\\s+US\\s","\\s+HAVE\\s","\\s+A\\s","\\s+RS\\s","BOOK.","\\s+WE\\s","\\s+CALL\\s","\\s+AND\\s","\\s+ARE\\s","CHECK","TIP\\s","THE\\s","DEAR","THAN.","GUEST","\\s+YOUR\\s","\\s+YOU\\s","\\s+IS\\s","\\s+ON\\s","\\s+TO\\s","\\s+FROM\\s","\\s+AT\\s","\\s+FOR\\s","TICKET","\\s\\s","\\s\\s\\s"};
        str=str.toUpperCase();
        for(int i=0;i<filter.length;i++){
            str=str.replaceAll(filter[i]," ");
            str=str.trim();

        }
        String[] token=str.split(" ");
       return token;
    }
}

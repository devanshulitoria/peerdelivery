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

        String str="AH|ACHHNERA,ADRA|ADRA,ADI|AHMADABAD,AMP|AHMADPUR,AI|AI,A|AOD,AK|AKOLA,AL|ALIGARH,APDJ|ALIPURDUAR,ALD|ALLAHABAD,LWR|ALNAVAR,UMB|AMBALACANT,AMLA|AMLA,ASR|AMRITSAR,ANND|ANAND,UDL|ANDAL,AKV|ANKLESHWAR,APR|ANUPPUR,ASK|ARSIKERE,ASN|ASANSOL,ASV|ASARVA,ARJ|AUNRIHAR,AZ|AZIMGANJ,BCA|BACHWARA,BD|BADNERA,BKP|BAKHTIYARPUR,BTC|BALAGHAT,BLM|BALAMU,BLT|BALOTRA,BNDA|BANDA,BDC|BANDEL,BKI|BANDIKUI,SBC|BANGALORE,BNKI|BANMANKHI,BBK|BARABANKI,BCQ|BARACHAK,BJU|BARAUNI,BHW|BARHARWA,BOE|BARSOI,BRWD|BARWADIH,BAT|BATALA,BVA|BAURIA,BXN|BAYANA,BAY|BELLARY,VAA|BHAGA,BTE|BHARATPUR,BHRL|BHAROLI,BH|BHARUCH,BTI|BHATINDA,BTT|BHATNI,BVRM|BHIMAVARAM,BPR|BHOJIPURA,BJE|BHOJUDIH,BPL|BHOPAL,BSL|BHUSAVAL,BJO|BIOR,BKN|BIKANER,BSP|BILASPUR,BIM|BILIMORA,BINA|BINA,RRB|BIRUR,MM|BMBYMAHIM,BR|BRAJRAAGAR,CSN|CHALISGAON,CLD|CHALSA,CPN|CHAMPANERRD,CH|CHANDAUSI,CNI|CHANDIL,CPK|CHAPARMUKH,CWA|CHHINDWARA,JRU|CHIKJAJUR,CIL|CHILBILA,CBE|COIMBATORE,COT|CUDDALORE,DMW|DALMAU,DBG|DARBHANGA,DD|DAUND,DNA|DEGANA,DSJ|DELHISAFDARG,DHN|DHANBAD,DMM|DHARMAVARAM,DAS|DHASA,DUI|DHURI,DLN|DILDARNAGAR,DG|DINDIGUL,DKJ|DORNAKAL,DNC|DRONACHELLAM,DDA|DURAUNDHA,ERS|ERANAKULAM,ED|ERODE,FD|FAIZABAD,FKM|FAKIRAGRAM,FTD|FATEHABADCH,FKG|FURKATING,GDG|GADAG,GJL|GAJRAULA,GIM|GANDHIDHAM,GAYA|GAYA,GDA|GODHRA,GMO|GOMOH,G|GONDIA,GDV|GUDIVADA,GDR|GUDUR,GTL|GUNTAKAL,GTLJ|GUNTAKAL,GNT|GUNTUR,HJP|HAJIPUR,HMH|HANUMANGARH,HW|HARIDWAR,HTZ|HATHIDAH,HRS|HATHRAS,HPT|HOSPET,HWH|HOWRAH,UBL|HUBLI,IDH|IDGAHAGRA,IAA|INDARA,INDB|INDOREBG,INDM|INDOREMG,ET|ITARSI,JHL|JAKHAL,JM|JALAMB,JL|JALGAON,JMP|JAMALPUR,H|JANGHAI,JSME|JASIDIH,U|JAUNPUR,JHS|JHANSI,JSG|JHARSUGUDA,JIND|JIND,UK|PURKUTCHERRY,JU|JODHPUR,KLNP|KALINARYNPUR,KYN|KALYAN,KNHN|KANTHAN,CPJ|KAPTANGANJ,KKDI|KARAIKKUDI,KRLR|KARAILAROAD,KIR|KATIHAR,KPD|KATPADI,KZJ|KAZIPET,KID|KHAIRAR,KAN|KHANA,KGP|KHARAGPUR,KUX|KHIRSADOH,KUR|KHURDAROAD,KRJ|KHURJA,KIUL|KIUL,KSB|KOSAMBA,KOTA|KOTA,KNJ|KRISHNGRCTY,CLA|KURLA,KKDE|KURUKSHETRA,LRJ|LAKSAR,LGH|LALGARH,LNK|LOHIANKHAS,LD|LONDA,LKR|LUCKEESARAI,L|LUCKNOW,LDH|LUDHIANA,LMG|LUMDING,LUNI|LUNI,MDP|MADHUPUR,MDU|MADURAI,MSH|MAHESANA,MKN|MAKRANA,MALB|MALIYAMIYANA,MNM|MANAMADURAI,MDA|MANDHANA,MKP|MANIKPUR,MEJ|MANIYACHCHI,MUR|MANKAPUR,MMR|MANMAD,MNE|MANSI,MXN|MARIANI,MJ|MARWAR,MTJ|MATHURA,MAU|MAU,MVJ|MAVLI,MTD|MERTAROAD,MRJ|MIRAJ,MYGL|MIYAGAMKNG,MKA|MOKAMEH,MGS|MUGHALSARAI,MFP|MUZAFFARPUR,MYS|MYSORE,ND|NADIAD,NCJ|NAGARCOIL,NAB|NAGBHIR,NAD|NAGDA,NH|NAIHATI,NIR|NAINPUR,NBD|NAJIBABAD,NRO|NAKODAR,NHT|NALHATI,NNP|NANPARA,NKE|NARKATIAGANJ,NRW|NARWANA,NWP|NAUPADA,NFK|NEWFARAKKA,NMZ|NEWMAL,NDD|NIDADAVOLU,PC|PACHORA,PAK|PAKALA,PNU|PALANPUR,PBM|PAMBAN,PNP|PANIPAT,PBN|PARBHANI,PRLI|PARLIVAIATH,PBH|PARTAPGARH,PNBE|PATNA,PEM|PERALAM,PGW|PHAGWARA,PFM|PHAPHAMAU,PEP|PHEPHNA,PHR|PHILLAUR,FL|PHULERA,PPR|PIPARROAD,PPD|PIPLOD,PTJ|PODANUR,POY|POLLACHI,PLO|PULGAON,PUNE|PUNE,PAU|PURNA,PRNA|PURNIA,PRR|PURULIA,QLN|QUILON,RBL|RAEBARELI,R|RAIPUR,RKSN|RAJKHARSAWAN,RJT|RAJKOT,RPJ|RAJPURA,RHA|RANAGHAT,RNY|RANGIYA,RTGH|RATANGARH,RTM|RATLAM,RXL|RAXUAL,RU|RENIGUNTA,RGS|RINGAS,ROK|ROHTAK,ROZA|ROZA,ROP|RUPSA,SBI|SABARMATI,SDLP|SADULPUR,SGL|SAGAULI,SHC|SAHARSA,SBG|SAHIBGANJ,SKJ|SAHIBPURKML,SKI|SAKRI,SLJ|SAKRIGALI,SA|SALEM,SRU|SALEMPUR,SLO|SAMALKOT,SPJ|SAMASTIPUR,SMR|SAMDHARI,SMLA|SAMLAYA,SRC|SANTRAGACHI,SC|SECUNDERABAD,SHG|SHAHGANJ,SKB|SHIKOHABAD,SRR|SHORANUR,SAGR|SHRIRAAGAR,SIKR|SIKAR,SGUJ|SILIGURI,SLGR|SIMALUGURI,SINI|SINI,SIR|SIRHIND,SV|SIWAN,SUR|SOLAPUR,SURM|SOLAPUR,SEE|SONPUR,SBE|SORBHOG,SPLE|SUIPARA,SOG|SURATGARH,TKE|TARIKERE,TATA|TATANAGAR,TEL|TENALI,TSI|TENKASI,THAN|THAN,THB|THANABIHPUR,TVR|THIRUVARUR,TPH|TINPAHAR,TSK|TINSUKIA,TPT|TIRUPATTUR,TTP|TIRUTURAIPDI,TDL|TUNDLA,UDN|UDHNA,U|UJJAIN,UCR|UNCHAHAR,ON|UNNAO,BRC|VADODARA,BSB|VARANASI,VKA|VERKA,BZA|VIJAYAWADA,VKB|VIKARABAD,VM|VILLUPARAM,VG|VIRAMGAM,VPT|VIRUDUNAGAR,VZM|VIZIANAGRAM,VRI|VRIDHACHALAM,WJR|WALAJAHROAD,WKR|WANKANER,WR|WARDHA,YPR|YASVANTPUR,YNK|YELAHANKA,ZBD|ZAFARABAD";
        String[] splittemp=str.split(",");
        String[] tmp;
        for(int i=0;i<splittemp.length;i++){
            String x=new String(splittemp[i].replace("|", ","));
            tmp=x.split(",");
            // System.out.println(tmp[0]+","+tmp[1]);
            //System.out.println(x);
            stncode.put(tmp[0].trim(), tmp[1].trim());
        }


        String airportName="AGARTALA ,AGATTI ISLAND ,AGRA ,AHMEDABAD ,AIZAWL ,AKOLA ,ALLAHABAD ,ALONG ,AMRITSAR ,ANAND ,AURANGABAD ,BAGDOGRA ,BALURGHAT ,BANGALORE ,BELGAUM ,BELLARY ,BHATINDA ,BHAVNAGAR ,BHOPAL ,BHUBANESWAR ,BHUJ ,BIKANER ,BILASPUR ,BIRD ISLAND ,BOMBAY ,CALCUTTA ,CALICUT ,CAR NICOBAR ,CHANDIGARH ,CHENNAI ,COCHIN ,COIMBATORE ,COOCH BEHAR ,CUDDAPAH ,DAMAN ,DAPARIZO ,DARJEELING ,DEHRADUN ,DELHI ,DHANBAD ,DHARAMSALA ,DIBRUGARH ,DIMAPUR ,DIU ,FARIDABAD ,GAUHATI ,GAYA,GOA ,GORAKHPUR ,GUNA ,GUWAHATI ,GWALIOR ,HISSAR ,HUBLI ,HYDERABAD ,IMPHAL ,INDORE ,JABALPUR ,JAGDALPUR ,JAIPUR ,JAISALMER ,JAMMU ,JAMNAGAR ,JAMSHEDPUR ,JEYPORE ,JODHPUR ,JORHAT ,KAILASHAHAR ,KAMALPUR ,KANDLA ,KANPUR ,KESHOD ,KHAJURAHO ,KHOWAI ,KOLHAPUR ,KOTA ,KOZHIKODE ,KULU ,LEH ,LILABARI ,LUCKNOW ,LUDHIANA ,MADRAS ,MADURAI ,MAHE ISLAND ,MALDA ,MANGALORE ,MOHANBARI ,MUZAFFARNAGAR ,MUZAFFARPUR ,MYSORE ,NAGPUR ,NANDED ,NASIK ,NAWANSHAHAR ,NEYVELI ,OSMANABAD ,PANAJI ,PANTNAGAR ,PASSIGHAT ,PATHANKOT ,PATNA ,PONDICHERRY ,POONA ,PORBANDAR ,PUTTAPARTHI ,RAE BARELI ,RAIPUR ,RAJAHMUNDRY ,RAJKOT ,RAJOURI ,RAMAGUNDAM ,RANCHI ,RATNAGIRI ,REWA ,ROURKELA ,RUPSI ,SATNA ,SHILLONG ,SHOLAPUR ,SILCHAR ,SIMLA ,SRINAGAR ,SURAT ,TEZPUR ,TEZU ,THANJAVUR ,TIRUCHIRAPALLY ,TIRUPATI ,TRIVANDRUM ,TUTICORIN ,UDAIPUR ,VADODARA ,VARANASI ,VIJAYAWADA ,VISHAKHAPATNAM ,VIZAG ,WARRANGAL ,ZERO";
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
            // TODO Auto-generated catch block
            return null;
        }
        DateFormat ft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = null;

        d=ft.format(src);
        return d;
    }
    public static String[] redBusProcessing(String[] str,String original){
Log.e("text", Arrays.toString(str));
        String[] travelDetails=new String[5];
        try {
            int start = 0, end = 0;
            travelDetails[0] = str[3];
            travelDetails[1] = str[4];
            travelDetails[3] = str[11];
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
            travelDetails[2] = original.substring(start, end).trim();
            travelDetails[2] = dateConversion(travelDetails[2], "hh:mm a MMM dd,yyyy");
            Log.e("Text bus",travelDetails[0]+":"+travelDetails[1]+":"+travelDetails[2]);
            return travelDetails;
        }catch (Exception e) {
            return null;
        }
    }
    public static String[] makeMyTripProcessing(String[] str){
        String[] travelDetails=new String[4];
        try {
            travelDetails[0] = str[4];
            travelDetails[1] = str[5];
            travelDetails[2] = str[7] + ":" + str[8].substring(0, 2) + " " + str[6].substring(2, 5) + " " + str[6].substring(0, 2) + "," + "20" + str[6].substring(5, 7);
            travelDetails[3] = str[9];
            travelDetails[2] = dateConversion(travelDetails[2], "hh:mm MMM dd,yyyy");
            return travelDetails;
        }catch (Exception e){
            return null;
        }



    }
    public static String[] goIbiboProcessing(String[] str){
        String[] travelDetails=new String[4];
        airportAndRailwaystnCodes();
       String []dayWeek={"","SUN","MON","TUE","WED","THU","FRI","SAT"};
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        DateFormat format12=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date src=null;
        try{
            if(aircode.containsKey(str[8]))
            travelDetails[0]=aircode.get(str[8]);
            else if(aircode.containsKey(str[9]))
            travelDetails[1]=aircode.get(str[9]);
            else
            return null;
            travelDetails[2]=str[10].substring(0, 2)+":"+str[10].substring(2, 4)+" "+str[7]+" "+str[6]+","+String.valueOf(year);
            travelDetails[3]=str[2];
            travelDetails[2] = dateConversion(travelDetails[2], "hh:mm MMM dd,yyyy");
            src=format12.parse(travelDetails[2]);
            c.setTime(src);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if(dayWeek[dayOfWeek].equalsIgnoreCase(str[5]))
            return travelDetails;
            else {
                travelDetails[2] = str[10].substring(0, 2) + ":" + str[10].substring(2, 4) + " " + str[7] + " " + str[6] + "," + String.valueOf(year+1);
                travelDetails[2] = dateConversion(travelDetails[2], "hh:mm MMM dd,yyyy");
                return travelDetails;
            }
        }catch (Exception e){
            return null;
        }

    }
    public static String[] yatraaProcessing(String[] str){
        try {
            String[] travelDetails = new String[4];
            travelDetails[0] = str[7];
            travelDetails[1] = str[8];
            String day = (Integer.parseInt(str[5]) < 9) ? "0" + str[5] : str[5];
            int year = Calendar.getInstance().get(Calendar.YEAR);
            travelDetails[2] = "11" + ":" + "00" + " " + str[6] + " " + day + "," + String.valueOf(year);
            travelDetails[3] = str[4];
            travelDetails[2] = dateConversion(travelDetails[2], "hh:mm MMMM dd,yyyy");
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
        }
        String[] token=str.split(" ");
       return token;
    }
}

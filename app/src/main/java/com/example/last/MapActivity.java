package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MapActivity extends AppCompatActivity {
    HashMap<String,String> code;
    Intent intent;
    String state, city,getData,information="";
    String value;
    ArrayList<String> list = new ArrayList<String>();
    int count = 0,num=0; ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        intent = getIntent();
        state = intent.getStringExtra("state");
        city  = intent.getStringExtra("city");
        code =  new HashMap<String,String>();
        code.put("강남구","110001");code.put("강동구","110002");code.put("강서구","110003");code.put("관악구","110004");code.put("구로구","110005");code.put("도봉구","110006");
        code.put("원주시","320400");// 현재 자신의 GPS값을 통해 얻은 주소를 지역코드로 변환

        Iterator<String> iterator = code.keySet().iterator();
        while(iterator.hasNext()){//hashmap의 key를 파싱
            String key = iterator.next();
            if(city.equals(key))
                value = code.get(key);//해당 지역의 지역코드 값
        }


        Log.d("TAG","value"+value);
        String StrUrl = "http://apis.data.go.kr/B551182/hospInfoService/getHospBasisList";
        String ServiceKey = "ia%2BWZP4Bo7U5pMEpmqmUw3nDb71jG4SgpHULit6rJIz5h3lDkjd6kP9yK08opcY%2Feol5VgQjhmipDXrZC0aT9w%3D%3D";
        String TotalUrl = StrUrl + "?ServiceKey=" + ServiceKey + "&sgguCd="+value+"&dgsbjtCd=06";
        DownLoad1 d1 = new DownLoad1();
        Log.d("TAG","DownLoad1");
        d1.execute(TotalUrl);
        Log.d("TAG","d1.execute");
    }
    public class DownLoad1 extends AsyncTask<String,Void, String> {////////////////////// 사용자가 입력한 dgsbjtCd 진료과목을 갖는 병원 파싱

        @Override
        protected String doInBackground(String... url) {
            try {
                Log.d("TAG","url"+url[0]);
                return (String) DownLoadUrl1((String) url[0]);

            }catch (IOException e) {
                return "다운로드 실패";
            }
        }
        protected void onPostExecute(String result){
            //super.onPostExecute(result);
            String resultCode = "";
            String yadmNm = "";
            String addr = "";
            String telno = "";
            String XPos = "";
            String YPos = "";

            boolean ho_resultCode=false;
            boolean ho_yadmNm = false;
            boolean ho_addr = false;
            boolean ho_telno = false;
            boolean ho_XPos = false;
            boolean ho_YPos = false;
            try{
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParserFactory.setNamespaceAware(true);
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

                xmlPullParser.setInput(new StringReader(result));
                int eventType = xmlPullParser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_DOCUMENT){
                    }else if(eventType == XmlPullParser.START_TAG){
                        String tag_name = xmlPullParser.getName();
                        switch (tag_name) {

                            case "resultCode":
                                ho_resultCode = true;
                                break;
                            case "yadmNm":
                                ho_yadmNm = true;
                                break;
                            case "addr":
                                ho_addr = true;
                                break;
                            case "telno":
                                ho_telno = true;
                                break;
                            case "XPos":
                                ho_XPos = true;
                                break;
                            case "YPos":
                                ho_YPos = true;
                                break;
                        }
                    }else if(eventType ==XmlPullParser.TEXT){
                        if (ho_resultCode) {
                            resultCode = xmlPullParser.getText();
                            ho_resultCode = false;
                        }
                        if(resultCode.equals("00")){
                            if(ho_addr){//병원주소 0
                                addr = xmlPullParser.getText();
                                information+=addr+"/";

                                ho_addr=false;
                            }
                            if(ho_telno){//병원 전화번호 1
                                telno = xmlPullParser.getText();
                                //textView.append(count+"]"+telno+"\n");
                                information = information+telno+"/";

                                ho_telno = false;
                            }
                            if(ho_XPos){//병원 위치 X좌표  3
                                XPos = xmlPullParser.getText();
                                //textView.append(count+"]"+XPos+"\n");
                                information =information+ XPos+"/";

                                ho_XPos = false;
                            }
                            if(ho_YPos){//병원 위치 Y좌표  2
                                YPos = xmlPullParser.getText();
                                //textView.append(count+"]"+YPos+"\n");
                                information =information+ YPos+"/";

                                ho_YPos = false;
                            }
                            if(ho_yadmNm){//병원명  4
                                yadmNm = xmlPullParser.getText();
                                String countname = Integer.toString(count+1);
                                String name = countname + "번" +yadmNm + "\n";
                                //totalyadmNm = totalyadmNm + name;//병원 목록 음성 출력
                                information= information+ yadmNm;//병원 정보 문자열

                                list.add(information);
                                information="";
                                count++;
                                ho_yadmNm = false;
                            }
                        }

                    }else if (eventType == XmlPullParser.END_TAG) {//끝을 의미하는 TAG
                        ;
                    }
                    eventType = xmlPullParser.next();
                }



            }
            catch (Exception e) {
            }
            if(count==10) {
                ArrayList<String> list1 = new ArrayList<String>();
                for (int i = 0; i < count; i++) {
                    StringTokenizer token1 = new StringTokenizer(list.get(i),"/");// 긱 병원의 정보들을 하나의 세트로 토큰 실시
                    String[] buffer1 = new String[token1.countTokens()];


                    int count1 = 0;
                    while (token1.hasMoreTokens()) {//병원 별로 정보 token
                        buffer1[count1] = token1.nextToken();
                        buffer1[count1] = buffer1[count1].replaceAll("\n", "");
                        count1++;
                    }
                    list1.add("병원명:"+buffer1[4]+"\r\n주소:"+buffer1[0]+"\r\n전화번호"+buffer1[1]);
                    count1 = 0;
                    ListView listView = findViewById(R.id.listview);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1,list1);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),HosmapActivity.class);
                            intent.putExtra("list",list.get(position));
                            startActivity(intent);
                        }
                    });
                }
            }

        }

        public String DownLoadUrl1(String myurl) throws IOException {//url
            HttpURLConnection com = null;
            BufferedReader bufferedReader;
            try{
                URL url = new URL(myurl);
                com = (HttpURLConnection) url.openConnection();
                com.setRequestMethod("GET");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(com.getInputStream());
                bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream,"UTF-8"));

                String total = null;
                getData = "";
                while((total=bufferedReader.readLine())!=null){
                    getData += total;
                }
                return getData;
            }finally {
                com.disconnect();
            }
        }
    }
}

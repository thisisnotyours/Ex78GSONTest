package com.suek.ex78gsontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv= findViewById(R.id.tv);
    }

    public void clickBtn(View view) {
        //GSON Library 를 이용하여 json 문자열을 Person 객체로 아예 곧바로 파싱

        String jsonStr= "{'name': 'Robin', 'age': 25}";

        Gson gson= new Gson();
        Person p= gson.fromJson(jsonStr, Person.class);  //json 문자열에서 곧바로 Person 객체로 파싱
        tv.setText(p.name+","+p.age);
    }



    public void clickBtn2(View view) {   //역파싱

        Person p= new Person("Robin", 25);  //객체

        //json 문자열로 바꾸기
        Gson gson= new Gson();
        String jsonStr= gson.toJson(p);
        tv.setText(jsonStr);
    }



    //멤버변수
    ListView listView;
    ArrayAdapter adapter;
    List<String> items= new ArrayList<>();

    public void clickBtn3(View view) {
      //json 배열도 손쉽게 파싱 가능
        String str= "[{'name':'hong','age':20}, {'name':'kim','age':25}]";

        Gson gson= new Gson();
        Person[] personArray= gson.fromJson(str, Person[].class);

        for(Person p : personArray){
            items.add(p.name+", "+p.age);  //문자열로 바뀜
        }

        listView= findViewById(R.id.listView);
        adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }


    
    public void clickBtn4(View view) {
        final String serverUrl="http://suhyun2963.dothome.co.kr/test.json";

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url= new URL(serverUrl);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);

                    InputStream is= url.openStream();
                    InputStreamReader isr= new InputStreamReader(is);

                    //Reader 까지만 있으면 GSON 이 알아서 읽어와서 객체로 파싱해줄것임..
                    Gson gson= new Gson();
                    final Person p= gson.fromJson(isr, Person.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(p.name+","+p.age);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

package com.fsm.meteo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
TextView _compteur,_villeTxt,_tempTxt,_tempminTxt,_tempMaxtxt,_humiditeTxt,_vitessVentTxt;
EditText _ville;
Button _okBtn;
int titre=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _compteur =findViewById(R.id.compteurId);
        _okBtn = findViewById(R.id.okBtnId);
        _ville = findViewById(R.id.cityId);
        _villeTxt = findViewById(R.id.villeId);
        _tempMaxtxt = findViewById(R.id.temMaxId);
        _tempminTxt = findViewById(R.id.temMinId);
        _humiditeTxt = findViewById(R.id.humId);
        _vitessVentTxt= findViewById(R.id.vitessVentId);
        _tempTxt =findViewById(R.id.temp);

        _okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(){
                  public void run(){
                      methodeLong();
                  }
                };
                thread.start();
                Toast.makeText(getApplicationContext(),""+_ville.getText().toString(),Toast.LENGTH_LONG).show();
                Thread thread1 = new Thread(){
                    public void run(){
                        try {
                            getM(_ville.getText().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                thread1.start();



            }
        });

    }


    public void methodeLong(){
        try {
            for (int i =1;i<=5;i++){
                Thread.sleep(2000);
                titre+=1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _compteur.setText(titre+"");
                    }
                });
            }


        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void getM(String v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    getMeteo(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getMeteo(String ville) throws JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

            String strUrl = "https://api.openweathermap.org/data/2.5/weather?q="
                    + ville + "&units=metric&APPID=626440fa1d75aedb3180c6148c1d89ea";
        String text = "";
        try {
            URL url = new URL(strUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                text += line;
            }
            is.close();
            System.out.println("text : " + text);
            Toast.makeText(getApplicationContext(),"succes",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject(text);
        System.out.println("Ville : " + json.getString("name"));
        _villeTxt.setText(json.getString("name"));
        JSONObject mainMeteo = json.getJSONObject("main");
        JSONObject windMeteo = json.getJSONObject("wind");
        _tempMaxtxt.setText(mainMeteo.getString("temp_max"));
        _tempminTxt.setText(mainMeteo.getString("temp_min"));
        _tempTxt.setText(mainMeteo.getString("temp"));
        _humiditeTxt.setText(mainMeteo.getString("humidity"));
        _vitessVentTxt.setText(windMeteo.getString("speed")+"(m/s)");

    }
}
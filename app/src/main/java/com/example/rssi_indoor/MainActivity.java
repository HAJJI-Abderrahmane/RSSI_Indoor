// MainActivity.java
package com.example.rssi_indoor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;






public final class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private ArrayList<ArrayList> AL = new ArrayList<>();
    Handler handler = new Handler();
    Runnable myRunnable;
    long delay= 1000;
    long samplingnumber=0;
    private TextView sampling;
    private String filename="Test11";
    private Button pressme;
    private EditText pos1;
    private boolean running = false;
    private boolean containsit=false;
    private Map<String, String> map = new HashMap<String, String>();

    public int biggest(ArrayList<ArrayList> AL2){
        int max=0;
        for (ArrayList i: AL2) if(i.size()>max) max=i.size();
        return max;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        TextView todaystime = (TextView) this.findViewById(R.id.todaystime);
        LocalDate currentDateTime = java.time.LocalDate.now();
        todaystime.setText(currentDateTime.toString());

        this.pressme = (Button) this.findViewById(R.id.pressme);
        this.pressme.setOnClickListener(this);

        this.pos1 = (EditText) this.findViewById(R.id.Position1);

        this.sampling =(TextView) this.findViewById(R.id.samplingnumber);

        TextView ssid1 = (TextView) this.findViewById(R.id.ssid1);
        TextView ssid2 = (TextView) this.findViewById(R.id.ssid2);
        TextView ssid3 = (TextView) this.findViewById(R.id.ssid3);
        TextView ssid4 = (TextView) this.findViewById(R.id.ssid4);
        TextView ssidlevel1 = (TextView) this.findViewById(R.id.ssidlevel1);
        TextView ssidlevel2 = (TextView) this.findViewById(R.id.ssidlevel2);
        TextView ssidlevel3 = (TextView) this.findViewById(R.id.ssidlevel3);
        TextView ssidlevel4 = (TextView) this.findViewById(R.id.ssidlevel4);
        ssid1.setAlpha(0.0F);
        ssid2.setAlpha(0.0F);
        ssid3.setAlpha(0.0F);
        ssid4.setAlpha(0.0F);
        ssidlevel1.setAlpha(0.0F);
        ssidlevel2.setAlpha(0.0F);
        ssidlevel3.setAlpha(0.0F);
        ssidlevel4.setAlpha(0.0F);
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        if (ContextCompat.checkSelfPermission((Context) this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.ACCESS_FINE_LOCATION")) {
                ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
            } else {
                ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
            }
        }
        if (ContextCompat.checkSelfPermission((Context) this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            } else {
                ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }
        }



        this.myRunnable = new Runnable() {
            public void run() {

                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                ((WifiManager) getSystemService(WIFI_SERVICE)).startScan();
                WifiInfo info2 = wifiManager.getConnectionInfo();
                List<ScanResult> info = wifiManager.getScanResults();
                if (!(0 >= info.size())) {
                    ssid1.setText(String.valueOf(info.get(0).SSID));
                    ssidlevel1.setText(String.valueOf(info.get(0).level));
                } else {
                    ssid1.setText("AP not found");
                    ssidlevel1.setText("N/A");
                }
                if (!(1 >= info.size())) {
                    ssid2.setText(String.valueOf(info.get(1).SSID));
                    ssidlevel2.setText(String.valueOf(info.get(1).level));
                } else {
                    ssid2.setText("AP not found");
                    ssidlevel2.setText("N/A");
                }
                if (!(2 >= info.size())) {
                    ssid3.setText(String.valueOf(info.get(2).SSID));
                    ssidlevel3.setText(String.valueOf(info.get(2).level));
                } else {
                    ssid3.setText("AP not found");
                    ssidlevel3.setText("N/A");
                }
                if (!(3 >= info.size())) {
                    ssid4.setText(String.valueOf(info.get(3).SSID));
                    ssidlevel4.setText(String.valueOf(info.get(3).level));
                } else{
                    ssid4.setText("AP not found");
                    ssidlevel4.setText("N/A");
                }
                ssid1.setAlpha(1.0F);
                ssid2.setAlpha(1.0F);
                ssid3.setAlpha(1.0F);
                ssid4.setAlpha(1.0F);
                ssidlevel1.setAlpha(1.0F);
                ssidlevel2.setAlpha(1.0F);
                ssidlevel3.setAlpha(1.0F);
                ssidlevel4.setAlpha(1.0F);
                System.out.print("APs available : ");
                System.out.println(info.size());
                int theonesize=0;
                int baggast=biggest(AL);
                for(ScanResult i: info){

                    for(ArrayList j : AL){
                        if(j.contains(i.SSID)){
                            containsit=true;
                            theonesize=j.size();
                        }
                    }
                    System.out.print("Baggast size :");
                    System.out.println(baggast);
                    if(!containsit && baggast<=2){
                            ArrayList freshlist = new ArrayList();
                            freshlist.add(i.SSID);
                            freshlist.add(i.level);
                            AL.add(freshlist);

                    }else if(!containsit && baggast>2 ) {
                        ArrayList freshlist = new ArrayList();
                        IntStream.range(0, baggast-2).forEach(
                                n -> {
                                    freshlist.add(-100);
                                }
                        );
                        freshlist.add(0,i.SSID);
                        freshlist.add(i.level);
                        AL.add(freshlist);


                    }else if(containsit && theonesize >= baggast){
                        for(ArrayList j : AL){
                            if(j.contains(i.SSID)){
                                j.add(i.level);
                            }
                        }
                    }else if(containsit && theonesize<baggast){
                        for(ArrayList j : AL){
                            if(j.contains(i.SSID)){
                                IntStream.range(0, baggast-theonesize).forEach(
                                        n -> {
                                            j.add(-100);
                                        }
                                );
                                j.add(i.level);
                            }
                        }

                    }
                    containsit=false;
                }
                samplingnumber++;
                sampling.setText(String.valueOf(samplingnumber));
                MainActivity.this.handler.postDelayed(MainActivity.this.myRunnable, (long) MainActivity.this.delay);
            }
        };



    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pressme :
                if (!this.running) {
                    filename=this.pos1.getText().toString();
                    this.pos1.setEnabled(false);
                    this.pressme.setText("STOP TEST");
                    this.handler.postDelayed(this.myRunnable, (long) this.delay);
                    this.running = true;
                    return;
                }
                samplingnumber=0;
                sampling.setText(String.valueOf(samplingnumber));
                Toast.makeText(this, "Wait a minute Please.", Toast.LENGTH_SHORT).show();
                this.pos1.setEnabled(true);
                this.pressme.setText("START TEST");
                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("script");

                PyObject obj= pyobj.callAttr("main",AL.toString(),filename);
//                writeToFile(obj.toString(), filename);
                System.out.println(AL);
                System.out.println(obj);


                AL.clear();
                this.handler.removeCallbacks(this.myRunnable);
                this.running = false;
                return;

            default:
                return;
        }
    }
}


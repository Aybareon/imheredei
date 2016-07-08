package com.example.danielprimo.imheredei;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Calibrating extends AppCompatActivity {
    WifiManager wifi;
    private TextView remainingTime;
    String positionName;
    int count;
    private List<ResultData> resultsData;
    Timer timer;
    TimerTask myTimerTask;
    int totalCount=15;
    private PositionData positionData;
    private String posX,posY;
    private int cond1,cond2;
    TextView calibratingTitulo;
    EditText positionX, positionY;
    Button aceitaCoordenadas;

    public class ResultData {
        private AcessPoint ap;

        public List<Integer> values;

        public ResultData(AcessPoint router) {
            // TODO Auto-generated constructor stub
            ap = router;
            values = new ArrayList<Integer>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrating);

        positionX=(EditText)findViewById(R.id.positionX);
        positionY=(EditText)findViewById(R.id.positionY);
        aceitaCoordenadas=(Button)findViewById(R.id.havePosition);
        calibratingTitulo=(TextView)findViewById(R.id.tituloCalibracao);
        remainingTime=(TextView)findViewById(R.id.remain_time);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Intent intent = getIntent();

        aceitaCoordenadas.setEnabled(false);
        positionX.setVisibility(View.VISIBLE);
        positionY.setVisibility(View.VISIBLE);
        remainingTime.setVisibility(View.INVISIBLE);
        aceitaCoordenadas.setVisibility(View.VISIBLE);
        calibratingTitulo.setVisibility(View.INVISIBLE);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm...");
        alertDialog.setMessage("Scanning requires WiFi.");
        alertDialog.setPositiveButton("Turn on WiFi",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Activity transfer to wifi settings
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        alertDialog.setCancelable(false);
        if(!wifi.isWifiEnabled()) {
            alertDialog.show();
        }
        positionX.setText("");
        positionY.setText("");
        positionName=intent.getStringExtra("POSITION_NAME");
        //darNaContagem();
        resultsData = new ArrayList<ResultData>();
        count = 0;
        timer = new Timer();
        cond1=0;
        cond2=0;
        positionX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    aceitaCoordenadas.setEnabled(false);
                } else
                    if(cond2==1){
                        aceitaCoordenadas.setEnabled(true);
                    }
                    else{
                        aceitaCoordenadas.setEnabled(false);
                    }
                    cond1 = 1;

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        positionY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    aceitaCoordenadas.setEnabled(false);
                } else
                    if(cond1==1){
                        aceitaCoordenadas.setEnabled(true);
                    }
                    else{
                        aceitaCoordenadas.setEnabled(false);
                    }
                    cond2=1;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        aceitaCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calibratingTitulo.setVisibility(View.VISIBLE);
                positionX.setVisibility(View.INVISIBLE);
                positionY.setVisibility(View.INVISIBLE);
                remainingTime.setVisibility(View.VISIBLE);
                aceitaCoordenadas.setVisibility(View.INVISIBLE);
                posX=positionX.getText().toString();
                posY=positionY.getText().toString();
                myTimerTask = new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        refresh();
                    }
                };
                timer.schedule(myTimerTask, 0, 1000);
            }
        });

    }

    private void refresh() {
        // TODO Auto-generated method stub
        if (count >= totalCount) {
            if (myTimerTask != null)
                myTimerTask.cancel();

        }
        count++;
        wifi.startScan();
        List<ScanResult> results = wifi.getScanResults();
        for (int i = 0; i < results.size(); i++) {
            // System.out.println("test2");
            String ssid0 = results.get(i).SSID;
            String bssid = results.get(i).BSSID;

            int rssi0 = results.get(i).level;
            boolean found = false;
            for (int pos = 0; pos < resultsData.size(); pos++) {
                if (resultsData.get(pos).ap.BSSID.equals(bssid)) {
                    found = true;
                    resultsData.get(pos).values.add(rssi0);
                    break;
                }
            }
            if (!found) {

                ResultData data = new ResultData(new AcessPoint(ssid0, bssid));
                data.values.add(rssi0);
                resultsData.add(data);
            }
            // String rssiString0 = String.valueOf(rssi0);
            // textStatus = textStatus.concat("\n" + ssid0 + "   " +
            // rssiString0);
            // System.out.println("ajsdhks"+textStatus);
        }
        // Log.v("textStatus", textStatus);
        // System.out.println(""+textStatus);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // result.setText("here"+currentCount);
                remainingTime
                        .setText(" " + (totalCount - count) + "s");
                if (count >= totalCount) {
                    returnResults();
                }
            }
        });

    }

    private void returnResults() {
        // TODO Auto-generated method stub

        positionData = new PositionData(positionName);
        for (int length = 0; length < resultsData.size(); length++) {

            int sum = 0;
            for (int l = 0; l < resultsData.get(length).values.size(); l++) {
                sum += resultsData.get(length).values.get(l);

            }
            int average = sum / resultsData.get(length).values.size();

            positionData.addValue(resultsData.get(length).ap, average);
        }

        Intent intent = new Intent(getApplicationContext(), Criacao.class);
        intent.putExtra("PositionData", (Serializable) positionData);
        intent.putExtra("POSITIONX", posX);
        intent.putExtra("POSITIONY", posY);
        setResult(RESULT_OK, intent);
        finish();


    }
    /*
    @Override
    protected void onActivityResult(int request, int result, Intent intent) {
        if (result == RESULT_OK) {
            super.onActivityResult(request, result, intent);
        }
    }*/

}

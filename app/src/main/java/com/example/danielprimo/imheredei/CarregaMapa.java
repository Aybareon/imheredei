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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CarregaMapa extends AppCompatActivity {
    WifiManager wifi;
    BaseDados db;
    //ArrayList<String> choosedAPs;
    String positionName=null;
    int count;
    private List<ResultData> resultsData;
    Timer timer;
    TimerTask myTimerTask;
    int totalCount=3;
    private PositionData positionData;
    ArrayAdapter<String> arrayAdapter;
    String edificio="Polo2";
    ArrayList<PositionData> positionsData;
    float posX, posY;
    String informacaoDef;
    ArrayList<String> descricao= new ArrayList<>();
    //String [] descricao= new String[2];


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
        setContentView(R.layout.activity_carrega_mapa);
        db = new BaseDados(this);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        posX=0;
        posY=0;
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


        resultsData = new ArrayList<ResultData>();
        count = 0;
        timer = new Timer();
        myTimerTask = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                refresh();
            }
        };
        timer.schedule(myTimerTask, 0, 1000);
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

                if (count > totalCount) {
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
            //System.out.println("______________________________________________________________________________");
            if(resultsData.get(length).ap.SSID.equals("DEI")){
                descricao.add(resultsData.get(length).ap.SSID);
                descricao.add(resultsData.get(length).ap.BSSID);
                descricao.add(Integer.toString(average));
            }
            //System.out.println("______________________________________________________________________________");
            positionData.addValue(resultsData.get(length).ap, average);
        }
        /*
        Intent intent = new Intent(getApplicationContext(), Criacao.class);
        intent.putExtra("PositionData", (Serializable) positionData);
        setResult(RESULT_OK,intent);
        finish();*/
        System.out.println("CA ESTA ELE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        fazer_Knearest(positionData);

    }

    public void fazer_Knearest(PositionData positionData){
        float somatorioTotal;
        float somatorio1=0,somatorio2=0;
        String [] point1;
        String [] point2;
        positionsData=db.getReadings(edificio);
        String closestPosition = null;
        ArrayList<AcessPoint> wifis = db.getFriendlyWifis(edificio);

        int min_distance = positionData.uDistance(positionsData.get(0), wifis);
        int j=0;
        closestPosition = positionsData.get(0).apName;
        String res = "";
        res += closestPosition + "\n" + min_distance;
        res += "\n" + positionsData.get(0).toString();
        for (int i = 1; i < positionsData.size(); i++) {
            //System.out.println("--------->"+positionsData.get(i).apName);
            int distance = positionData.uDistance(positionsData.get(i), wifis);
            res += "\n" + positionsData.get(i).apName + "\n" + distance;
            res += "\n" + positionsData.get(i).toString();
            if (distance < min_distance) {
                min_distance = distance;
                somatorio1=min_distance;
                //System.out.println("-->"+positionsData.get(i).apName+"_____"+distance);
                j=i;
                closestPosition = positionsData.get(i).apName;

            }

        }
        System.out.println("-->"+res);

        //////////////////////////////////////////////////
        min_distance = positionData.uDistance(positionsData.get(0), wifis);
        String closestPosition2 = null;
        String closestPosition2final = null;

        closestPosition2 = positionsData.get(0).apName;
        res = "";
        res += closestPosition2 + "\n" + min_distance;
        res += "\n" + positionsData.get(0).toString();
        for (int i = 1; i < positionsData.size(); i++) {
            //System.out.println("oooooooooooo>"+positionsData.get(i).apName);
            if(i!=j) {
                int distance = positionData.uDistance(positionsData.get(i), wifis);
                res += "\n" + positionsData.get(i).apName + "\n" + distance;
                res += "\n" + positionsData.get(i).toString();
                closestPosition2 = positionsData.get(i).apName;//////////////////////////
                if(closestPosition2.equals(closestPosition))
                    continue;
                if (distance < min_distance) {
                    //System.out.println("ooo>"+positionsData.get(i).apName+"_____"+distance);
                    min_distance = distance;
                    somatorio2=min_distance;
                    closestPosition2final = positionsData.get(i).apName;

                }
            }
        }




        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        res += "\nCurrent:\n" + positionData.toString();
        //Log.v("Result", res);
        //System.out.println("-->" + res);
        informacaoDef="";
        if(somatorio1!=0 && somatorio2!=0){
            //System.out.println("------------------------------------------------->ENTROU????");
            point1=db.getPosit(edificio,closestPosition);
            point2=db.getPosit(edificio,closestPosition2final);
            System.out.println("--------zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+point1[0]+"zzzzzzzzzzzzzzzzzzzzz"+point2[1]);
            System.out.println("--------zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+point2[0]+"zzzzzzzzzzzzzzzzzzzzz"+point2[1]);
            somatorio1= (float) (1/Math.sqrt(somatorio1));
            somatorio2= (float) (1/Math.sqrt(somatorio2));
            System.out.println("000000zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+somatorio1+"zzzzzzzzzzzzzzzzzzzzz"+somatorio2);
            somatorioTotal= somatorio1+somatorio2;
            System.out.println("11111111111zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + somatorioTotal + "zzzzzzzzzzzzzzzzzzzzz");
            posX=((Float.parseFloat(point1[0])*somatorio1)/somatorioTotal)+((Float.parseFloat(point2[0])*somatorio2)/somatorioTotal);
            posY=((Float.parseFloat(point1[1])*somatorio1)/somatorioTotal)+((Float.parseFloat(point2[1])*somatorio2)/somatorioTotal);
            System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" + posX + "zzzzzzzzzzzzzzzzzzzzz" + posY);
        }
        //System.out.println("OOOOOOO" + informacao);
        //super.onActivityResult(requestCode, resultCode, intent);
        Intent intent=new Intent(this,Mapa.class);
        intent.putExtra("POSITIONX", Float.toString(posX));
        intent.putExtra("POSITIONY", Float.toString(posY));
        startActivity(intent);
    }


}

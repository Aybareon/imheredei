package com.example.danielprimo.imheredei;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Daniel Primo on 05/05/2016.
 */
public class EstouAqui extends AppCompatActivity {
    WifiManager wifi;
    BaseDados db;
    //ArrayList<String> choosedAPs;
    private TextView remainingTime;
    String positionName=null;
    TextView informacao,local;
    int count;
    private List<ResultData> resultsData;
    Timer timer;
    TimerTask myTimerTask;
    int totalCount=5;
    private PositionData positionData;
    ArrayAdapter<String> arrayAdapter;
    String edificio="Polo2";
    ArrayList<PositionData> positionsData;
    TextView resultado1,resultado2;
    Button acabou,abrirDeficoes;
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
        setContentView(R.layout.activity_estou_aqui);
        db = new BaseDados(this);
        //choosedAPs = db.getBuildings();
        remainingTime=(TextView)findViewById(R.id.tempo_restante);
        resultado1=(TextView)findViewById(R.id.resultado_1);
        resultado2=(TextView)findViewById(R.id.resultado_2);
        acabou=(Button)findViewById(R.id.concluido);
        abrirDeficoes=(Button)findViewById(R.id.defenicoes);
        informacao=(TextView)findViewById(R.id.informacao);
        local=(TextView)findViewById(R.id.local);
        acabou.setVisibility(View.INVISIBLE);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
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
                remainingTime
                        .setText(" " + (totalCount - count) + "s");
                if (count > totalCount) {
                    informacao.setVisibility(View.INVISIBLE);
                    remainingTime.setVisibility(View.INVISIBLE);
                    returnResults();
                }
            }
        });

        acabou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mapa.class);
                intent.putExtra("POSITIONX", Float.toString(posX));
                intent.putExtra("POSITIONY", Float.toString(posY));
                startActivity(intent);
                //setResult(RESULT_OK, intent);
                finish();
            }
        });
        abrirDeficoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Defenicoes.class);
                intent.putExtra("DEFENITION", informacaoDef);
                startActivity(intent);
                //setResult(RESULT_OK, intent);
                //finish();
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
        if (min_distance == PositionData.MAX_DISTANCE){
            resultado1.setText("Ponto 1 fora de range");
            //closestPosition="OUT OF RANGE";
            //Toast.makeText(this,"You are out of range of the selected building",Toast.LENGTH_LONG).show();

        }
        else{
            resultado1.setText("Ponto Proximo:\n  "+ closestPosition+ "->"+min_distance);
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

        if (min_distance == PositionData.MAX_DISTANCE){
            resultado2.setText("Ponto 2 fora de range");
            //closestPosition2="E";
            //Toast.makeText(this,"You are out of range of the selected building",Toast.LENGTH_LONG).show();

        }
        else{
            resultado2.setText("Ponto Proximo:\n " + closestPosition2final+ "->"+min_distance);
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
        for (int pos = 0; pos < descricao.size(); pos++) {
            //System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            informacaoDef=informacaoDef+"Nome: "+descricao.get(pos)+"\nMAC: ";
            pos++;
            informacaoDef=informacaoDef+descricao.get(pos)+"\nLevel :";
            pos++;
            informacaoDef=informacaoDef+descricao.get(pos)+"\n\n";
        }
        if(somatorio1!=0 && somatorio2!=0) {
            informacaoDef = informacaoDef + "\nCoordenada X-> " + posX + "\nCoordenada Y->" + posY;
        }
        //System.out.println("OOOOOOO" + informacao);
        ondeEstou();
        acabou.setVisibility(View.VISIBLE);
        //super.onActivityResult(requestCode, resultCode, intent);
    }

    public void ondeEstou(){
        ArrayList<String> locais;
        int i;
        int cond=0;
        float positionX, positionY,auxX,auxY;
        String nomeLocal="";

        //criaVirtualizacaoMapa();
        System.out.println("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!!!");
        locais=db.getRoom("Polo2");
        System.out.println("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA2222222222222!!!");
        for(i=0;i<locais.size();i++){
            System.out.println("A---------------------"+i+"------------");
            nomeLocal=locais.get(i);
            i++;
            System.out.println("B---------------------"+i+"------------");
            positionX=Float.parseFloat(locais.get(i));
            i++;
            System.out.println("C---------------------"+i+"------------");
            positionY=Float.parseFloat(locais.get(i));
            i++;
            System.out.println("D---------------------"+i+"------------");
            auxX=positionX+Float.parseFloat(locais.get(i));
            i++;
            System.out.println("E---------------------"+i+"------------");
            auxY=positionY+Float.parseFloat(locais.get(i));
            System.out.println("E---------------------"+i+"------------");
            System.out.println("xxxxxxxxxxxxxxx"+nomeLocal+"xxxx"+positionX+"xxxx"+positionY+"xxxx"+auxX+"xxxx"+auxY);
            if(posX>=positionX && posX<=auxX && posY>=positionY && posY<=auxY){
                System.out.println("çççççççççççççççççççççççççççççççççççççççççççççççççççççç");
                cond=1;
                break;
            }
        }
        if(cond==1){
            local.setText(nomeLocal);
        }

    }

    public void criaVirtualizacaoMapa(){
        db.addRoom("Polo2","C41" ,"96" ,"89" ,"13" ,"19.5");
        db.addRoom("Polo2","C42" ,"96" ,"108.5" ,"13" ,"19.5");
        db.addRoom("Polo2","C43" ,"96" ,"128" ,"13" ,"19.5");
        db.addRoom("Polo2","C44" ,"113" ,"128" ,"13" ,"19.5");
        db.addRoom("Polo2","C45" ,"113" ,"108.5" ,"13" ,"19.5");
        db.addRoom("Polo2","C46" ,"113" ,"89" ,"13" ,"19.5");
        db.addRoom("Polo2","E41" ,"156" ,"89" ,"13" ,"29");
        db.addRoom("Polo2", "E42", "156", "118", "13", "14.75");
        db.addRoom("Polo2", "E43", "156", "132.75", "13", "14.75");
        db.addRoom("Polo2", "E44", "173", "132.75", "13", "14.75");
        db.addRoom("Polo2", "E45", "173", "118", "13", "14.75");
        db.addRoom("Polo2", "E46", "173", "103.5", "13", "14.5");
        db.addRoom("Polo2", "E47", "173", "89", "13", "14.5");
        db.addRoom("Polo2", "G41", "216", "89", "13", "19.5");
        db.addRoom("Polo2", "G42", "216", "108.5", "13", "19.5");
        db.addRoom("Polo2", "G41", "216", "128", "13", "19.5");
        db.addRoom("Polo2", "G44", "233", "128", "13", "19.5");
        db.addRoom("Polo2", "G44", "233", "108.5", "13", "19.5");
        db.addRoom("Polo2", "G44", "233", "89", "13", "19.5");
        db.addRoom("Polo2","Restauracao" ,"6" ,"80" ,"24" ,"40");
        db.addRoom("Polo2","Bar" ,"16.5" ,"120" ,"24" ,"16.5");
        db.addRoom("Polo2","WC4" ,"6" ,"120" ,"10" ,"24");
        db.addRoom("Polo2","Corredor4BarC" ,"30" ,"80" ,"79" ,"9");
        db.addRoom("Polo2","Corredor4CE" ,"113" ,"80" ,"55.5" ,"9");
        db.addRoom("Polo2","Corredor4EG" ,"173" ,"80" ,"55.5" ,"9");
        db.addRoom("Polo2","Corredor4C" ,"109" ,"76" ,"4" ,"61");
        db.addRoom("Polo2","Corredor4E" ,"169" ,"76" ,"4" ,"61");
        db.addRoom("Polo2","Corredor4G" ,"229" ,"76" ,"4" ,"61");
    }
}

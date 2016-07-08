package com.example.danielprimo.imheredei;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

public class ChooseAPs extends AppCompatActivity {
    BaseDados db;
    private Button addAPs;
    WifiManager wifi;
    List<ScanResult> connections;
    protected CharSequence[] options_AP;
    protected boolean[] selected_aps;
    Button saveAPs;
    ListView apList;
    ArrayList<AcessPoint> wifis;
    ArrayAdapter<AcessPoint> arrayAdapter;
    String mapaName;
    Button syncronization,apagaBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_aps);
        db = new BaseDados(this);
        addAPs = (Button) findViewById(R.id.more_aps);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        addAPs.setOnClickListener(new ButtonClickHandler());
        saveAPs=(Button) findViewById(R.id.save_aps);
        syncronization=(Button)findViewById(R.id.sync);
        apList = (ListView) findViewById(R.id.lista_aps);
        //apagaBase=(Button)findViewById(R.id.apagar_aps);
        Intent intent=getIntent();

        mapaName = intent.getStringExtra("EXTRA");
        //db.deleteFriendlyWifis(mapaName);
        wifis=db.getFriendlyWifis(mapaName);
        arrayAdapter = new ArrayAdapter<AcessPoint>(this,
                android.R.layout.simple_list_item_1, wifis);


        //guarda a lista de aps escolhidas na base de dados
        saveAPs.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.addFriendlyWifis(mapaName, wifis)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Tudo Ok!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                Intent intent = new Intent(getApplicationContext(), Calibrating.class);
                //intent.putExtra("PositionData", (Serializable) positionData);
                setResult(RESULT_OK, intent);
                finish();
            }
        }));
        //elimina items da lista com cliques de longa duração sobre os items
        apList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                wifis.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
        syncronization.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constroiListaPredefinida();
            }
        }));
        constroiListaPredefinida();
    }

    //função de clicke no botao add "Add AP"
    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            //connections=wifi.getScanResults();
            registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context c, Intent intent)
                {
                    wifi.startScan();
                    connections = wifi.getScanResults();
                    //size = results.size();
                }
            }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            updateOptions();

            onCreateDialog(0).show();
        }

    }

    //faz update as aps detetadas e guarda-as num "array de selecção"
    public void updateOptions() {
        options_AP = new CharSequence[connections.size()];
        for (int i = 0; i < connections.size(); i++)
            options_AP[i] = connections.get(i).SSID+"-"+ connections.get(i).BSSID;
        selected_aps = new boolean[options_AP.length];

    }

    //cria caixa de dialogo com as opções de APs disponiveis
    @Override
    protected Dialog onCreateDialog(int id) {
        return new AlertDialog.Builder(this)
                .setTitle("Choose Friendly Wifis")
                .setMultiChoiceItems(options_AP, selected_aps,
                        new DialogSelectionClickHandler())
                .setPositiveButton("OK", new DialogButtonClickHandler())
                .create();
    }

    //torna a opção clicavel, e guada as opções com um certo
    public class DialogSelectionClickHandler implements
            DialogInterface.OnMultiChoiceClickListener {
        public void onClick(DialogInterface dialog, int clicked,
                            boolean selected) {
            Log.i("ME", options_AP[clicked] + " selected: " + selected);
        }
    }

    //seleciona as que estao selecionas e manda-as para update a lista de aps escolhidas
    public class DialogButtonClickHandler implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int clicked) {
            switch (clicked) {
                case DialogInterface.BUTTON_POSITIVE:
                    updateFriendlyWifis();
                    break;
            }
        }
    }

    //faz update a lista de aps escolhidas
    protected void updateFriendlyWifis() {
        for (int i = 0; i < options_AP.length; i++) {
            if (selected_aps[i]) {
                AcessPoint acessPoint = new AcessPoint(connections.get(i).SSID,
                        connections.get(i).BSSID);
                if (!wifis.contains(acessPoint))
                    wifis.add(acessPoint);

            }
            arrayAdapter = new ArrayAdapter<AcessPoint>(this,
                    android.R.layout.simple_list_item_1, wifis);
            // Set The Adapter
            apList.setAdapter(arrayAdapter);
            Log.i("ME", options_AP[i] + " selected: " + selected_aps[i]);
        }
    }

    protected void constroiListaPredefinida(){
        int i;
        //System.out.println("-------------->AQUILOGO!");
        wifi.startScan();
        connections=wifi.getScanResults();
        options_AP=new CharSequence[connections.size()];
        for (i = 0; i < connections.size(); i++){
            options_AP[i] = connections.get(i).SSID+"-"+ connections.get(i).BSSID;
        }
        //System.out.println("-------------->AQUI1!");
        selected_aps=new boolean[options_AP.length];
        //System.out.println("-------------->AQUI2!");
        for (i = 0; i < options_AP.length; i++) {
            //System.out.println("-------------->AQUI1!"+i);
            if (connections.get(i).SSID.equals("DEI")) {
                AcessPoint acessPoint = new AcessPoint(connections.get(i).SSID,
                        connections.get(i).BSSID);
                if (!wifis.contains(acessPoint))
                    wifis.add(acessPoint);
                selected_aps[i]=true;

            }
            else{
                selected_aps[i]=false;
            }
            arrayAdapter = new ArrayAdapter<AcessPoint>(this,
                    android.R.layout.simple_list_item_1, wifis);
            // Set The Adapter
            apList.setAdapter(arrayAdapter);
            Log.i("ME", options_AP[i] + " selected: " + selected_aps[i]);
        }
        //System.out.println("-------------->SAI!");
    }

}

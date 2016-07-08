package com.example.danielprimo.imheredei;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.google.gson.Gson;

public class Criacao extends AppCompatActivity {
    Button choose_wifiButton;
    BaseDados db;
    Button calibrar;
    Button allFine;
    EditText positionName;
    String mapaName;
    ListView pointList;
    int calibrationTime=15;
    ArrayList<String> allPositions;
    ArrayAdapter arrayAdapter;
    private PositionData positionData;
    //Gson gson;

    @SuppressWarnings("null")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criacao);
        db=new BaseDados(this);
        choose_wifiButton=(Button)findViewById(R.id.choose_aps);
        choose_wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseAPs.class);
                intent.putExtra("EXTRA", mapaName);
                startActivity(intent);
            }
        });
        Intent intent=getIntent();
        mapaName=intent.getStringExtra("EXTRA");
        calibrar=(Button)findViewById(R.id.calibration);
        allFine=(Button)findViewById(R.id.all_fine);
        positionName=(EditText)findViewById(R.id.position_name);
        pointList=(ListView)findViewById(R.id.pontos_referencia);
        allPositions=db.getPositions(mapaName);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, allPositions);
        pointList.setAdapter(arrayAdapter);
        //criaVirtualizacaoMapa();
        if(db.getFriendlyWifis(mapaName).isEmpty()) {
            calibrar.setVisibility(View.INVISIBLE);
            allFine.setVisibility(View.INVISIBLE);
            positionName.setVisibility(View.INVISIBLE);
        }
        else{
            calibrar.setVisibility(View.VISIBLE);
            allFine.setVisibility(View.VISIBLE);
            allFine.setEnabled(false);
            positionName.setVisibility(View.VISIBLE);
        }
        positionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    calibrar.setEnabled(false);
                } else
                    calibrar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        positionName.setText("");
        calibrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Calibrating.class);
                intent.putExtra("POSITION_NAME", positionName.getText().toString());
                //intent.putExtra("isLearning", true);
                //intent.putExtra("NUMBER_OF_SECONDS", calibrationTime);
                startActivityForResult(intent, 0);
                allFine.setEnabled(true);
            }
        });
        if(allPositions.size()!=0) {
            allFine.setEnabled(true);
        }
        pointList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Calibrating.class);
                String selecposition = (String) parent.getItemAtPosition(position);
                intent.putExtra("POSITION_NAME", selecposition);
                startActivityForResult(intent, 0);
            }
        });
        pointList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteReading(mapaName, allPositions.get(position));
                allPositions.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
        /*gson = new Gson();
        allFine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        Criacao.class);
                setResult(2, intent);
                ArrayList<PositionData> buildingReadings = db.getReadings(mapaName);
                ArrayList<AcessPoint> friendlyWifis = db.getFriendlyWifis(mapaName);
                String buildingReadingsJson = gson.toJson(buildingReadings);
                String friendlyWifisJson = gson.toJson(friendlyWifis);
                JSONObject json = new JSONObject();
                try {
                    json.accumulate("building_id", mapaName);
                    json.accumulate("readings", new JSONArray(buildingReadingsJson));
                    json.accumulate("friendly_wifis", new JSONArray(friendlyWifisJson));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Submit(getApplicationContext()).execute(json.toString());
                finish();

            }
        });*/
        //pointList.setOnTouchListener(touchListener);




    }

    @Override
    protected void onActivityResult(int request, int result, Intent intent) {
        String posX, posY;
        String [] auxpos;

        posX=intent.getStringExtra("POSITIONX");
        posY=intent.getStringExtra("POSITIONY");
        System.out.println("--------------------------------------ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
        if(result==RESULT_OK) {
            positionData = (PositionData) intent
                    .getSerializableExtra("PositionData");
            Log.v("Before db : ", positionData.toString());
            db.addReadings(mapaName, positionData);
            System.out.println("--------------------------------------BLAAAAAAAA11111111111111111");
            //db.criaCenas();
            db.addPositions(mapaName, positionData, posX, posY);
            System.out.println("--------------------------------------BLAAAAAAAA222222222222222222222222");
            auxpos=db.getPosit(mapaName,positionData.apName);
            System.out.println("............................................................." + auxpos[0]+"......"+auxpos[1]);
            allPositions = db.getPositions(mapaName);
            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, allPositions);
            pointList.setAdapter(arrayAdapter);
            super.onActivityResult(request, result, intent);
        }
    }
    @Override
    protected void onResume() {
        allPositions = db.getPositions(mapaName);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, allPositions);
        pointList.setAdapter(arrayAdapter);
        positionName.setText("");
        calibrar.setEnabled(false);
        super.onResume();
    }



}

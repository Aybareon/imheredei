package com.example.danielprimo.imheredei;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Defenicoes extends AppCompatActivity {

    TextView defenition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defenicoes);

        defenition=(TextView)findViewById(R.id.definitionDescrition);
        Intent intent=getIntent();
        defenition.setText(intent.getStringExtra("DEFENITION"));
        //System.out.println("OOOOOOO" + intent.getStringExtra("DEFENITION"));

    }

}

package com.gamesp.gamesp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class unit2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }


    public void editActions(View view) {

        String num = view.getContentDescription().toString();
        if (num.equals("cinco")) {
            Intent intent = new Intent(this, unit8_1.class);
            startActivity(intent);
        }
        if (num.equals("seis")) {
            Intent intent = new Intent(this, unit8_2.class);
            startActivity(intent);
        }
        if (num.equals("siete")) {
            Intent intent = new Intent(this, unit8_3.class);
            startActivity(intent);
        }
    }

}

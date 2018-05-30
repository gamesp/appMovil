package com.gamesp.gamesp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class unit1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit1);

    }


    public void editActions(View view) {

        String num = view.getContentDescription().toString();
        if (num.equals("uno")) {
            Intent intent = new Intent(this, unit1_1.class);
            startActivity(intent);
        }

    }

}

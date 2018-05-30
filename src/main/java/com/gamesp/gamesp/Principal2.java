package com.gamesp.gamesp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public class Principal2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal2);
        ((Globals) getApplication()).currentContext=this;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Globals) getApplication()).currentContext=this;
    }

    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(listener);// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu1, popup.getMenu());
        popup.show();
    }

    public PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (menuItem.toString().equals("Info")) {
                Log.i("----->", menuItem.toString());
                abrirInfo();
            }
            return false;
        }
    };

    public void libre(View view){
        Intent intent;
        String num = view.getContentDescription().toString();
        int number = Integer.parseInt(num);


        intent = new Intent(this, unit_libre.class);
        intent.putExtra("map", number);
        startActivity(intent);
    }

    public void abrirInfo() {
        Intent intent;
        intent = new Intent(this, info.class);
        startActivity(intent);
    }


    public void order(View view) {
        Intent intent;
        intent = new Intent(this, Principal.class);
        finish();
        startActivity(intent);
    }

    public void editActions(View view) {
        Intent intent;
        String num = view.getContentDescription().toString();
        int number = Integer.parseInt(num);


        intent = new Intent(this, unit1_1.class);
        intent.putExtra("map", number);
        startActivity(intent);

    }
}

package com.gamesp.gamesp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class Principal extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ((Globals) getApplication()).currentContext=this;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Globals) getApplication()).currentContext=this;
    }

    public void libre(View view){
        Intent intent;
        String num = view.getContentDescription().toString();
        int number = Integer.parseInt(num);

        intent = new Intent(this, unit_libre.class);
        intent.putExtra("map", number);
        startActivity(intent);
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
            if (menuItem.toString().equals("Connect wifi")) {
                Log.i("----->", menuItem.toString());
                abrirWifi();
            }
            if (menuItem.toString().equals("Update")) {
                Log.i("----->", menuItem.toString());
                abrirUpdate();
            }

            return false;
        }
    };

    public void abrirInfo() {
        Intent intent;
        intent = new Intent(this, info.class);
        startActivity(intent);
    }
    public void abrirWifi() {
        Intent intent;
        intent = new Intent(this, wifi.class);
        startActivity(intent);
    }
    public void abrirUpdate() {
        Intent intent;
        intent = new Intent(this, update.class);
        startActivity(intent);
    }

    public void order(View view) {
        Intent intent;
        intent = new Intent(this, Principal2.class);
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

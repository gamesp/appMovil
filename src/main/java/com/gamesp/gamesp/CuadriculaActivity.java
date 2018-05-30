package com.gamesp.gamesp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class CuadriculaActivity extends AppCompatActivity {

    private Context context;
    private int mapa;
    private int filas;
    private int columnas;
    private int cocheSize;
    private int cuadriculaWidth;
    private int cuadriculaHeight;
    private int screenWidth;
    private int screenHeight;
    private ImageView imgRobot;
    private ImageView imgCuadricula;
    private String posicionActual = "";
    Handler handler;
    Thread thread;
    Boolean comprobando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadricula);
        ((Globals) getApplication()).currentContext=this;
        context = getApplicationContext();

        Intent intent = getIntent();
        mapa = intent.getIntExtra("mapa", 0);
        filas = getFilas(mapa);
        columnas = getColumnas(mapa);
        try {
            setSizes(filas, columnas);
        }
        catch (Exception e) {
            finish();
        }

        imgRobot = findViewById(R.id.imgRobot);
        imgRobot.getLayoutParams().width = cocheSize;
        imgRobot.getLayoutParams().height = cocheSize;
        imgRobot.setVisibility(View.INVISIBLE);

        imgCuadricula = findViewById(R.id.imgCuadricula);
        imgCuadricula.setImageDrawable(getImage(mapa));
        imgCuadricula.getLayoutParams().width = cuadriculaWidth;
        imgCuadricula.getLayoutParams().height = cuadriculaHeight;

        handler = new Handler();
        thread = new Thread() {
            @Override
            public void run() {
                while (comprobando) {
                    try {
                        Thread.sleep(100);
                        String position = ((Globals) getApplication()).getPosition();
                        if (position != null && position.length() > 1 && !position.equals(posicionActual)) {
                            Log.i("----->VariablesGlobales", "Recibo: " + position);
                            posicionActual = position;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    actualizarCoche();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((Globals) getApplication()).currentContext=this;
        comprobando = true;
        thread.start();
    }

    public void volver(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        comprobando = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void actualizarCoche() {
        int stringStart = posicionActual.indexOf('[') + 1;
        int stringLength = posicionActual.indexOf(']') - stringStart + 1;
        posicionActual = posicionActual.substring(stringStart, stringLength);
        String[] vars = posicionActual.split(",");

        int x = Integer.parseInt(vars[1].trim());
        int y = Integer.parseInt(vars[0].trim());
        String orientation = vars[2].trim();

        setPosition(x, y, orientation);
        imgRobot.setVisibility(View.VISIBLE);
    }

    private void setPosition(int x, int y, String orientation) {
        switch (orientation) {
            case "N":
                imgRobot.setRotation(0);
                break;
            case "S":
                imgRobot.setRotation(180);
                break;
            case "E":
                imgRobot.setRotation(90);
                break;
            case "W":
                imgRobot.setRotation(270);
                break;
        }
        imgRobot.setX(x * cocheSize);
        imgRobot.setY(y * cocheSize);
    }

    private void setSizes(int filas, int columnas) {
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;
        int sizeW = screenWidth / columnas;
        int sizeH = screenHeight / filas;
        cocheSize = sizeW < sizeH ? sizeW : sizeH;
        cuadriculaWidth = cocheSize * columnas;
        cuadriculaHeight = cocheSize * filas;
    }


    public int getFilas(int mapa) {
        int filas = 0;
        switch (mapa) {
            case 11:
                filas = context.getResources().getInteger(R.integer.f1_1);
                break;
            case 12:
                filas = context.getResources().getInteger(R.integer.f1_2);
                break;
            case 13:
                filas = context.getResources().getInteger(R.integer.f1_3);
                break;
            case 21:
                filas = context.getResources().getInteger(R.integer.f2_1);
                break;
            case 22:
                filas = context.getResources().getInteger(R.integer.f2_2);
                break;
            case 23:
                filas = context.getResources().getInteger(R.integer.f2_3);
                break;
            case 31:
                filas = context.getResources().getInteger(R.integer.f3_1);
                break;
            case 32:
                filas = context.getResources().getInteger(R.integer.f3_2);
                break;
            case 33:
                filas = context.getResources().getInteger(R.integer.f3_3);
                break;
            case 41:
                filas = context.getResources().getInteger(R.integer.f4_1);
                break;
            case 42:
                filas = context.getResources().getInteger(R.integer.f4_2);
                break;
            case 43:
                filas = context.getResources().getInteger(R.integer.f4_3);
                break;
            case 51:
                filas = context.getResources().getInteger(R.integer.f5_1);
                break;
            case 52:
                filas = context.getResources().getInteger(R.integer.f5_2);
                break;
            case 53:
                filas = context.getResources().getInteger(R.integer.f5_3);
                break;
            case 61:
                filas = context.getResources().getInteger(R.integer.f6_1);
                break;
            case 62:
                filas = context.getResources().getInteger(R.integer.f6_2);
                break;
            case 63:
                filas = context.getResources().getInteger(R.integer.f6_3);
                break;
            case 71:
                filas = context.getResources().getInteger(R.integer.f7_1);
                break;
            case 72:
                filas = context.getResources().getInteger(R.integer.f7_2);
                break;
            case 73:
                filas = context.getResources().getInteger(R.integer.f7_3);
                break;
            case 81:
                filas = context.getResources().getInteger(R.integer.f8_1);
                break;
            case 82:
                filas = context.getResources().getInteger(R.integer.f8_2);
                break;
            case 83:
                filas = context.getResources().getInteger(R.integer.f8_3);
                break;
            case 91:
                filas = context.getResources().getInteger(R.integer.f9_1);
                break;
            case 92:
                filas = context.getResources().getInteger(R.integer.f9_2);
                break;
            case 93:
                filas = context.getResources().getInteger(R.integer.f9_3);
                break;
            case 101:
                filas = context.getResources().getInteger(R.integer.f10_1);
                break;
            case 102:
                filas = context.getResources().getInteger(R.integer.f10_2);
                break;
            case 103:
                filas = context.getResources().getInteger(R.integer.f10_3);
                break;
        }
        return filas;
    }

    public int getColumnas(int mapa) {
        int columnas = 0;
        switch (mapa) {
            case 11:
                columnas = context.getResources().getInteger(R.integer.c1_1);
                break;
            case 12:
                columnas = context.getResources().getInteger(R.integer.c1_2);
                break;
            case 13:
                columnas = context.getResources().getInteger(R.integer.c1_3);
                break;
            case 21:
                columnas = context.getResources().getInteger(R.integer.c2_1);
                break;
            case 22:
                columnas = context.getResources().getInteger(R.integer.c2_2);
                break;
            case 23:
                columnas = context.getResources().getInteger(R.integer.c2_3);
                break;
            case 31:
                columnas = context.getResources().getInteger(R.integer.c3_1);
                break;
            case 32:
                columnas = context.getResources().getInteger(R.integer.c3_2);
                break;
            case 33:
                columnas = context.getResources().getInteger(R.integer.c3_3);
                break;
            case 41:
                columnas = context.getResources().getInteger(R.integer.c4_1);
                break;
            case 42:
                columnas = context.getResources().getInteger(R.integer.c4_2);
                break;
            case 43:
                columnas = context.getResources().getInteger(R.integer.c4_3);
                break;
            case 51:
                columnas = context.getResources().getInteger(R.integer.c5_1);
                break;
            case 52:
                columnas = context.getResources().getInteger(R.integer.c5_2);
                break;
            case 53:
                columnas = context.getResources().getInteger(R.integer.c5_3);
                break;
            case 61:
                columnas = context.getResources().getInteger(R.integer.c6_1);
                break;
            case 62:
                columnas = context.getResources().getInteger(R.integer.c6_2);
                break;
            case 63:
                columnas = context.getResources().getInteger(R.integer.c6_3);
                break;
            case 71:
                columnas = context.getResources().getInteger(R.integer.c7_1);
                break;
            case 72:
                columnas = context.getResources().getInteger(R.integer.c7_2);
                break;
            case 73:
                columnas = context.getResources().getInteger(R.integer.c7_3);
                break;
            case 81:
                columnas = context.getResources().getInteger(R.integer.c8_1);
                break;
            case 82:
                columnas = context.getResources().getInteger(R.integer.c8_2);
                break;
            case 83:
                columnas = context.getResources().getInteger(R.integer.c8_3);
                break;
            case 91:
                columnas = context.getResources().getInteger(R.integer.c9_1);
                break;
            case 92:
                columnas = context.getResources().getInteger(R.integer.c9_2);
                break;
            case 93:
                columnas = context.getResources().getInteger(R.integer.c9_3);
                break;
            case 101:
                columnas = context.getResources().getInteger(R.integer.c10_1);
                break;
            case 102:
                columnas = context.getResources().getInteger(R.integer.c10_2);
                break;
            case 103:
                columnas = context.getResources().getInteger(R.integer.c10_3);
        }
        return columnas;
    }

    private Drawable getImage(int mapa) {
        Drawable image = null;
        switch (mapa) {
            case 11:
                image = context.getResources().getDrawable(R.drawable.i1_1);
                break;
            case 12:
                image = context.getResources().getDrawable(R.drawable.i1_2);
                break;
            case 13:
                image = context.getResources().getDrawable(R.drawable.i1_3);
                break;
            case 21:
                image = context.getResources().getDrawable(R.drawable.i2_1);
                break;
            case 22:
                image = context.getResources().getDrawable(R.drawable.i2_2);
                break;
            case 23:
                image = context.getResources().getDrawable(R.drawable.i2_3);
                break;
            case 31:
                image = context.getResources().getDrawable(R.drawable.i3_1);
                break;
            case 32:
                image = context.getResources().getDrawable(R.drawable.i3_2);
                break;
            case 33:
                image = context.getResources().getDrawable(R.drawable.i3_3);
                break;
            case 41:
                image = context.getResources().getDrawable(R.drawable.i4_1);
                break;
            case 42:
                image = context.getResources().getDrawable(R.drawable.i4_2);
                break;
            case 43:
                image = context.getResources().getDrawable(R.drawable.i4_3);
                break;
            case 51:
                image = context.getResources().getDrawable(R.drawable.i5_1);
                break;
            case 52:
                image = context.getResources().getDrawable(R.drawable.i5_2);
                break;
            case 53:
                image = context.getResources().getDrawable(R.drawable.i5_3);
                break;
            case 61:
                image = context.getResources().getDrawable(R.drawable.i6_1);
                break;
            case 62:
                image = context.getResources().getDrawable(R.drawable.i6_2);
                break;
            case 63:
                image = context.getResources().getDrawable(R.drawable.i6_3);
                break;
            case 71:
                image = context.getResources().getDrawable(R.drawable.i7_1);
                break;
            case 72:
                image = context.getResources().getDrawable(R.drawable.i7_2);
                break;
            case 73:
                image = context.getResources().getDrawable(R.drawable.i7_3);
                break;
            case 81:
                image = context.getResources().getDrawable(R.drawable.i8_1);
                break;
            case 82:
                image = context.getResources().getDrawable(R.drawable.i8_2);
                break;
            case 83:
                image = context.getResources().getDrawable(R.drawable.i8_3);
                break;
            case 91:
                image = context.getResources().getDrawable(R.drawable.i9_1);
                break;
            case 92:
                image = context.getResources().getDrawable(R.drawable.i9_2);
                break;
            case 93:
                image = context.getResources().getDrawable(R.drawable.i9_3);
                break;
            case 101:
                image = context.getResources().getDrawable(R.drawable.i10_1);
                break;
            case 102:
                image = context.getResources().getDrawable(R.drawable.i10_2);
                break;
            case 103:
                image = context.getResources().getDrawable(R.drawable.i10_3);
        }
        return image;
    }
}

           

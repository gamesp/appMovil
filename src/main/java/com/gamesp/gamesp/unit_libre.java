package com.gamesp.gamesp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class unit_libre extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;
    private String commandText = "";
    private String commandShow = "";
    public String bot_name = "";
    public String nivel;
    public String dificultad;
    public int orientation = 2;
    public int orientationAnt = 2;
    public String posCompleta;
    public int posX = 0, posY = 0;
    public int posAntX = 0, posAntY = 0;
    public int columnas = 6, filas = 6;
    public int tiempo = 0;
    public int map;
    public String orientacion = "E";
    LinearLayout horizontal;
    HorizontalScrollView scroll;


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Globals) getApplication()).currentContext=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_libre);
        ((Globals) getApplication()).currentContext=this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mWebSocketClient = SocketHandler.getSocket();
        sendMessage("Reset");
        horizontal = (LinearLayout) findViewById(R.id.horizontalCommands);
        scroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);

        nivel = "0";
        dificultad = "0";


        String pos = "p" + nivel + "_" + dificultad;
        int pos_aux = getStringIdentifier(this, pos);
        posCompleta = getResources().getString(pos_aux);
        Log.i("----->", posCompleta);

        ((Globals) getApplication()).setPosition(posCompleta);

        int stringStart = posCompleta.indexOf('[') + 1;
        int stringLength = posCompleta.indexOf(']') - stringStart + 1;
        posCompleta = posCompleta.substring(stringStart, stringLength);
        String[] vars = posCompleta.split(",");

        for (String v : vars)
            Log.i("VariablesGlobales", v);

        posX = Integer.parseInt(vars[1].trim());
        posY = Integer.parseInt(vars[0].trim());
        orientacion = vars[2].trim();
        if (orientacion.equals("N"))
            orientation = 1;
        if (orientacion.equals("E"))
            orientation = 2;
        if (orientacion.equals("S"))
            orientation = 3;
        if (orientacion.equals("W"))
            orientation = 4;


        String variable = "unit_" + nivel + "_title";
        int titulo_aux = getStringIdentifier(this, variable);
        TextView txtTitulo = (TextView) findViewById(R.id.text5);
        txtTitulo.setText(titulo_aux);


        start();
    }


    public void start() {


        URI uri = null;

        try {
            uri = new URI("ws://192.168.4.1:81");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        mWebSocketClient = new WebSocketClient(uri, new Draft_17(), headers, 0) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.w("-----> robota", "Websocket Opened");
                // send message 'STOP/SLEEP' when open socket
                sendMessage("start");
                Button buttonConnect = (Button) findViewById(R.id.connect);
                //buttonConnect.setText("Connected");
                //buttonConnect.setHeight(0);
            }

            @Override
            public void onMessage(final String onMsg) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("-----> robota", "Message ON:" + onMsg);
                        //JSON onMsg
                        try {
                            JSONObject client = new JSONObject(onMsg);
                            if (client.has("idRobota")) {
                                //TextView textView = (TextView) findViewById(R.id.id_content);
                                //textView.setText(client.getString("idRobota"));
                            }
                            if (client.has("state")) {
                                //TextView textView = (TextView) findViewById(R.id.state_content);
                                //textView.setText(client.getString("state"));
                                if (client.getString("state").equals("connected")) {
                                    sendMessage("start");
                                }
                            }
                            if (client.has("mov")) {
                                Log.i("-----> MOVIMIENTO", "[" + client.getString("X") + "," + client.getString("Y") + "," + client.getString("compass") + "]");
                                posCompleta = "[" + client.getString("X") + "," + client.getString("Y") + "," + client.getString("compass") + "]";
                                ((Globals) getApplication()).setPosition(posCompleta);
                            }
                        } catch (JSONException e) {
                            Log.e("-----> robota", e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.w("-----> robota", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.w("-----> robota", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
        SocketHandler.setSocket(mWebSocketClient);
    }


    public void volver(View view) {
        finish();
    }

    public int getStringIdentifier(Context context, String name) {
        return this.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    public void borrar(View view) {
        commandText = "";
        commandShow = "";
        //textView.setText(commandShow);
        horizontal.removeAllViews();
        posX = posAntX;
        posY = posAntY;
        orientation = orientationAnt;
    }


    public void mapa(View view) {
        Intent intent = new Intent(this, CuadriculaActivity.class);
        intent.putExtra("mapa", map);
        startActivity(intent);
    }

    private void connectWebSocket() {


    }

    public void connectws(View view) {
        connectWebSocket();
    }

    public void moveScroll() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100);
    }

    public void addCommand(View view) {
        orientationAnt = orientation;
        posAntX = posX;
        posAntY = posY;


        if (view.getContentDescription().equals("F")) {
            boolean posible = true;
            Log.i("----->", "Orientacion: " + orientation);
            switch (orientation) {
                case 1:

                    posX--;
                    break;
                case 2:
                    posY++;
                    break;
                case 3:
                    posX++;
                    break;
                case 4:
                    posY--;
                    break;
            }
            if (posible) {
                tiempo += 2700;
                ImageView iv = new ImageView(getApplicationContext());
                iv.setImageDrawable(getDrawable(R.drawable.appgamesp_controles_delante));
                horizontal.addView(iv);
                commandText += view.getContentDescription();
            }
        }
        if (view.getContentDescription().equals("L")) {
            ImageView iv = new ImageView(getApplicationContext());
            iv.setImageDrawable(getDrawable(R.drawable.appgamesp_controles_izquierda));
            horizontal.addView(iv);
            orientation--;
            if (orientation < 1)
                orientation = 4;
            tiempo += 1000;
            commandText += view.getContentDescription();
        }
        if (view.getContentDescription().equals("R")) {
            ImageView iv = new ImageView(getApplicationContext());
            iv.setImageDrawable(getDrawable(R.drawable.appgamesp_controles_derecha));
            horizontal.addView(iv);
            orientation++;
            if (orientation > 4)
                orientation = 1;
            tiempo += 1000;
            commandText += view.getContentDescription();
        }
        if (view.getContentDescription().equals("B")) {

            boolean posible = true;
            switch (orientation) {
                case 1:
                    posX++;
                    break;
                case 2:
                    posY--;
                    break;
                case 3:
                    posX--;
                    break;
                case 4:
                    posY++;
                    break;
            }
            if (posible) {
                tiempo += 2700;
                ImageView iv = new ImageView(getApplicationContext());
                iv.setImageDrawable(getDrawable(R.drawable.appgamesp_controles_atras));
                horizontal.addView(iv);
                commandText += view.getContentDescription();
            }
        }


        //textView.setText(commandShow);
        moveScroll();
    }

    public boolean sendMessage(String sendMsg) {
        JSONObject client = new JSONObject();
        try {

            if (sendMsg.equals("Reset")) {
                client.put("compass", "E");
                //client.put("X", 1);
                Log.i("-----> Codigo especial:", " YES");
            } else {
                if (sendMsg.equals("start")) {
                    client.put("X", posX);
                    client.put("Y", posY);
                    client.put("compass", orientacion);
                    client.put("UD","Free");
                } else {
                    client.put("commands", sendMsg);
                    client.put("cells", "A");
                }
            }
        } catch (JSONException e) {
            Log.e("-----> robota", e.getMessage());
            return false;
        }
        // only send if object was created
        if (null == mWebSocketClient)
            return false;
        else {
            Log.w("-----> robota", client.toString());
            mWebSocketClient.send(client.toString());
            return true;
        }
    }

    public void sendCommand(View view) {

        Log.i("-----> Comando total: ", commandText);


        // Send
        sendMessage(commandText);


        commandText = "";
        commandShow = "";
        //textView.setText(commandShow);
        horizontal.removeAllViews();

    }
}


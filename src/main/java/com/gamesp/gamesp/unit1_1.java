package com.gamesp.gamesp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

public class unit1_1 extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;
    private String commandText = "";
    private String commandShow = "";
    public String bot_name="";
    public String nombre[][]={{" ","VAN","JUNIOR","MY","SUPER","ALIEN"},
            {"ROM","GAME","HI!","BUTTON","PORTU","LIP"},
            {"ICE","EURO","SKATE", "ITA", "FLOWER","STAR"},
            {"FLY","POL","CAT","BAN","EYE","ESP"},
            {"TUC","CHERRY","OK","BOT","ARROW","RAT"},
            {"MICRO","HAPPY","MONEY","UMBRELLA","MONKEY","DON"}};
    public int orientation=2;
    public int posX =0, posY =0;
    public int tiempo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit1_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mWebSocketClient = SocketHandler.getSocket();
        sendMessage("Reset");
    }


    private void connectWebSocket() {



    }

    public void connectws(View view) {
        connectWebSocket();
    }

    public void addCommand(View view) {
        TextView textView = findViewById(R.id.commands_edit);

        commandText += view.getContentDescription();

        if (view.getContentDescription().equals("F")) {
            boolean posible=true;
            switch (orientation){
                case 1:
                    if (posY-1>=0)
                        posY--;
                    else{
                        posible=false;
                    }
                    break;
                case 2:
                    if (posX+1<6)
                        posX++;
                    else{
                        posible=false;
                    }
                    break;
                case 3:
                    if (posY+1<6)
                        posY++;
                    else{
                        posible=false;
                    }
                    break;
                case 4:
                    if (posX-1>=0)
                        posX--;
                    else{
                        posible=false;
                    }
                    break;
            }
            if(posible) {
                tiempo += 2700;
                commandShow += "\u25B2";
            }
        }
        if (view.getContentDescription().equals("L")) {
            commandShow += "\u25C0";
            orientation--;
            if(orientation<1)
                orientation=4;
            tiempo+=1000;
        }
        if (view.getContentDescription().equals("R")) {
            commandShow += "\u25B6";
            orientation++;
            if(orientation>4)
                orientation=1;
            tiempo+=1000;
        }
        if (view.getContentDescription().equals("B")) {

            boolean posible=true;
            switch (orientation){
                case 1:
                    if (posY+1<6)
                        posY++;
                    else{
                        posible=false;
                    }
                    break;
                case 2:
                    if (posX-1>=0)
                        posX--;
                    else{
                        posible=false;
                    }
                    break;
                case 3:
                    if (posY-1>=0)
                        posY--;
                    else{
                        posible=false;
                    }
                    break;
                case 4:
                    if (posX+1<6)
                        posX++;
                    else{
                        posible=false;
                    }
                    break;
            }
            if(posible) {
                tiempo += 2700;
                commandShow += "\u25BC";
                String addedText=nombre[posY][posX];
                Log.i("Texto a añadir: ", addedText);
            }
        }


        textView.setText(commandShow);
    }

    public boolean sendMessage(String sendMsg) {
        JSONObject client = new JSONObject();
        try {

            if (sendMsg.equals("Reset")) {
              client.put("compass", "E");
            //client.put("X", 1);
             Log.i("Codigo especial:"," YES");
            } else {
            client.put("commands", sendMsg);
            }
        } catch (JSONException e) {
            Log.e("robota", e.getMessage());
            return false;
        }
        // only send if object was created
        if (null == mWebSocketClient)
            return false;
        else {
            Log.w("robota", client.toString());
            mWebSocketClient.send(client.toString());
            return true;
        }
    }

    public void sendCommand(View view) {

        TextView textView = findViewById(R.id.commands_edit);
        String addedText=nombre[posY][posX];
        Log.i("Texto a añadir: ", addedText + "en "+ posY+posY);
        Log.i("Comando total: ", commandText);


            // Send
        sendMessage(commandText);


        commandText = "";
        commandShow = "";
        textView.setText(commandShow);

    }
}


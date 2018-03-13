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

public class unit8_3 extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;
    private String commandText = "";
    private String commandShow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit8_3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mWebSocketClient = SocketHandler.getSocket();
        sendMessage("Reset");
    }




    public void addCommand(View view) {
        TextView textView = findViewById(R.id.commands_edit);

        commandText += view.getContentDescription();

        if (view.getContentDescription().equals("F"))
            commandShow += "\u25B2";
        if (view.getContentDescription().equals("L"))
            commandShow += "\u25C0";
        if (view.getContentDescription().equals("R"))
            commandShow += "\u25B6";
        if (view.getContentDescription().equals("B"))
            commandShow += "\u25BC";


        textView.setText(commandShow);
    }

    public boolean sendMessage(String sendMsg) {
        JSONObject client = new JSONObject();
        try {

            if (sendMsg.equals("Reset")) {
              client.put("compass", "E");
              client.put("X",1);
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

        Log.i("Comando total: ", commandText);
        // Send
        sendMessage(commandText);
        commandText = "";
        commandShow = "";
        textView.setText(commandShow);
    }
}


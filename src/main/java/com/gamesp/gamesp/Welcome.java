package com.gamesp.gamesp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    public void abrirSelect(){
        Intent intent = new Intent(this, Select.class);
        startActivity(intent);
    }

    public void start(View view) {

        abrirSelect();

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
                Log.w("robota", "Websocket Opened");
                // send message 'STOP/SLEEP' when open socket
                sendMessage("S");
                Button buttonConnect = (Button) findViewById(R.id.connect);
                //buttonConnect.setText("Connected");
                //buttonConnect.setHeight(0);
                abrirSelect();
            }

            @Override
            public void onMessage(final String onMsg) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("robota", "Message ON:" + onMsg);
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
                            }
                            if (client.has("mov")) {

                            }
                        } catch (JSONException e) {
                            Log.e("robota", e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.w("robota", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.w("robota", "Error " + e.getMessage());

                alert();
            }
        };
        mWebSocketClient.connect();
SocketHandler.setSocket(mWebSocketClient);

    }

    public void alert(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    public boolean sendMessage(String sendMsg) {
        JSONObject client = new JSONObject();
        try {
            client.put("commands", sendMsg);
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

}

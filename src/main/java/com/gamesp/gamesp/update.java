package com.gamesp.gamesp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class update extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;
    public String ssid = "";
    public String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ((Globals) getApplication()).currentContext=this;
        start();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Globals) getApplication()).currentContext=this;
    }

    public void volver(View view) {
        finish();
    }

    public void conectar(View view) {
    sendMessage("wifi");
    }


    public boolean sendMessage(String sendMsg) {
        JSONObject client = new JSONObject();
        try {

             client.put("update", "yes");

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
            Toast toast = Toast.makeText(this, "Robot is updating, wait until GamesP logo come back to the robot screen", Toast.LENGTH_LONG);
            toast.show();
            finish();
            return true;
        }
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
                //sendMessage("update");
                //Button buttonConnect = (Button) findViewById(R.id.connect);
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
                            }
                            if (client.has("mov")) {

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

}

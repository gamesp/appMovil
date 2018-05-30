package com.gamesp.gamesp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class wifi extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;
    public String ssid = "";
    public String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
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
        EditText editssid= findViewById(R.id.editSSID);
        ssid=editssid.getText().toString();
        EditText editpass= findViewById(R.id.editPass);
        pass=editpass.getText().toString();
sendMessage("wifi");
    }


    public boolean sendMessage(String sendMsg) {
        JSONObject client = new JSONObject();
        try {

            if (sendMsg.equals("Reset")) {
                client.put("compass", "E");
                //client.put("X", 1);
                Log.i("-----> Codigo especial:", " YES");
            } else {
                if (sendMsg.equals("wifi")) {
                    client.put("wifi", "yes");
                    client.put("ssid", ssid);
                    client.put("pass", pass);
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
            Toast toast = Toast.makeText(this, "Robot is Connecting to wifi, wait until happy face come back to the robot screen", Toast.LENGTH_LONG);
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
                sendMessage("S");
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

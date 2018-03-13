package com.gamesp.gamesp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    private WebSocketClient mWebSocketClient = null;
    private String commandText = "";
    private String commandShow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private void connectWebSocket() {
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
                buttonConnect.setText("Connected");
                buttonConnect.setHeight(0);
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
                                //clear position
                                //ImageView cellInActive = (ImageView) findViewById(_myposition);
                                //cellInActive.setImageResource(R.drawable.cell);
                                // actualize position
                                //TextView textView = (TextView) findViewById(R.id.position_content);
                                //String executing = client.getString("mov") + ":" + client.getInt("X") + ":" + client.getInt("Y") + ":" + client.getString("compass");
                                //textView.setText(executing);
                                //_myposition = client.getInt("X") * _cell_number + client.getInt("Y");
                                //ImageView cellActive = (ImageView) findViewById(_myposition);
                                //int compass = 0;
                                //switch (client.getString("compass")) {
                                //case "N":
                                // compass = R.drawable.robota_n;
                                // break;
                                // case "S":
                                //     compass = R.drawable.robota_s;
                                //    break;
                                // case "W":
                                //    compass = R.drawable.robota_w;
                                //    break;
                                // case "E":
                                //     compass = R.drawable.robota_e;
                                //    break;
                                //default:
                                //    compass = R.drawable.robota_n;
                                //    break;
                                //}
                                //cellActive.setImageResource(compass);
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
            }
        };
        mWebSocketClient.connect();

    }

    public void connectws(View view) {
        connectWebSocket();
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

            if (sendMsg.equals("RRR")) {
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

        Log.i("Comando total: ", commandText);
        // Send
        sendMessage(commandText);
        commandText = "";
        commandShow = "";
        textView.setText(commandShow);
    }
}


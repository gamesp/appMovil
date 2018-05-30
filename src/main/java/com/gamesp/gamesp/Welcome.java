package com.gamesp.gamesp;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Welcome extends AppCompatActivity {

    List<String> names;
    final Wifis wifis = new Wifis();
    boolean seguir = true;
    boolean buscando = false;
    public String networkName = "";
    public boolean wifiOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome);
        //abrirSelect();
        ((Globals) getApplication()).currentContext=this;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("----->", "No tiene permisos");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            wifis.start(this);
            getWifis();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Globals) getApplication()).currentContext=this;
        if (!seguir) {
            salir();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    wifis.start(this);
                    getWifis();
                } else {
                    Toast toast = Toast.makeText(this, "Permission is mandatory to play", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                return;
            }
        }
    }


    public void abrirSelect() {

        checkNetwork();
        unregisterReceiver(wifis.receiverWifi);
        seguir = false;
        buscando = false;

        final Intent intent = new Intent(this, Principal.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //finish();
                startActivity(intent);
            }
        }, 3000);
    }

    public void showAlert(int numAlert) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Welcome.this);
        builderSingle.setIcon(R.drawable.robot);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Welcome.this, android.R.layout.select_dialog_singlechoice);
        if (numAlert == 0) {
            builderSingle.setTitle("Select your Bot:");
            for (int i = 0; i < names.size(); i++) {
                //Log.i("----->", "Red " + i + ": " + names.get(i));
                arrayAdapter.add(names.get(i));
            }
        }
        if (numAlert == 1) {
            builderSingle.setTitle("No wifi available");
            builderSingle.setMessage("Make sure the robot is ON and try again");
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                salir();
            }
        });

        if (numAlert == 0) {
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(Welcome.this);
                    builderInner.setMessage(strName);
                    builderInner.setTitle("Connect to");
                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderInner.show();

                    networkName = strName;
                    conectWifi();
                }
            });
        }

        if (numAlert == 1) {
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    salir();
                }
            });
        }

        builderSingle.show();
    }

    public void salir() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 200);
    }

    public void getWifis() {
        buscando = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (wifis.enviar) {
                    names = wifis.getNetworks();


                    Set<String> hs = new HashSet<>();
                    hs.addAll(names);
                    names.clear();
                    names.addAll(hs);

                    if (names.size() == 0) {
                        Log.i("----->", "No networks available");
                        showAlert(1);
                        seguir = false;
                        //finish();


                    } else {
                        if (names.size() == 1) {
                            networkName = names.get(0);
                            conectWifi();

                            Toast toast = Toast.makeText(Welcome.this, "Connected to " + networkName, Toast.LENGTH_SHORT);
                            toast.show();

                        } else {
                            showAlert(0);
                            seguir = false;

                        }

                    }

                }
                if (seguir) {
                    getWifis();
                }
            }
        }, 500);
    }


    public void conectWifi() {
        String networkSSID = networkName;

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
        abrirSelect();

    }


    public void checkNetwork() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Context context = ((Globals) getApplication()).currentContext;


                if (wifis.isCurrentNetworkOK( context, networkName)) {
                    checkNetwork();
                    Log.i("----->","La red es correcta");
                }
                else{
                    Log.i("----->","La red no es correcta");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle("Wifi Disconected");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Restart GamesP when the robot can be played")
                            .setCancelable(false)
                            .setPositiveButton("Exit",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    finishAndRemoveTask();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();


                }
            }
        }, 4000);
    }

}

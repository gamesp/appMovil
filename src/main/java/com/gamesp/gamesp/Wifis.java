package com.gamesp.gamesp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Wifis {

    WifiManager mainWifi;
    Wifis.WifiReceiver receiverWifi;
    public Boolean enviar = false;

    StringBuilder sb = new StringBuilder();

    private final Handler handler = new Handler();

    List<String> names = new ArrayList<>();


    public void start(Context context) {


        mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new Wifis.WifiReceiver();
        context.registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (mainWifi.isWifiEnabled() == false) {
            mainWifi.setWifiEnabled(true);
        }



        scan(context);
    }

    public void scan(final Context context) {


        mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new Wifis.WifiReceiver();
        context.registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();

    }


    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();
            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            //Log.i("----->","tamList: "+ wifiList.size() + " "+ enviar);
            enviar = true;
            for (int i = 0; i < wifiList.size(); i++) {
                //Log.i("----->","tamList: "+ wifiList.size() + " i: "+ i);
                //Log.i("----->", wifiList.get(i).SSID);
                if (wifiList.get(i).SSID.length() > 7) {
                    if (wifiList.get(i).SSID.substring(0, 7).equals("DOMOTTA")) {
                        names.add(wifiList.get(i).SSID);
                    }
                }
            }
        }
    }

    public List getNetworks() {

        return names;
    }

    public boolean isCurrentNetworkOK(Context context, String currentSSID){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        currentSSID="\""+currentSSID+"\"";
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            //Log.i("----->","La red es "+wifiInfo.getSSID()+", la red en la que deberia estar es: "+currentSSID);
            if ( currentSSID.equals(wifiInfo.getSSID())){
                return true;
            }
        }

        return false;
    }

}

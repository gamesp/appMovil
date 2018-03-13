package com.gamesp.gamesp;



import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SocketHandler {

    private static WebSocketClient socket;

    public static synchronized WebSocketClient getSocket(){
        return socket;
    }

    public static synchronized void setSocket(WebSocketClient socket){
        SocketHandler.socket = socket;
    }

}

package com.tfg.david.appconversacional;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

/**
 * Created by david on 22/04/2018.
 */

public class Conexion {
    private static WebSocket ws;
    private static OkHttpClient client;

    public static WebSocket getWs() {
        return ws;
    }

    public static void setWs(WebSocket ws) {
        Conexion.ws = ws;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void setClient(OkHttpClient client) {
        Conexion.client = client;
    }
}

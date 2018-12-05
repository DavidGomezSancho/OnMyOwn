package com.tfg.david.appconversacional;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 102;
    //private Button btnSpeak;
    //private TextView textView;

    private Button start;
    private TextView output;
    private OkHttpClient client;
    private WebSocket ws;
    private ScrollView scroller;
    private LinearLayout layoutContenedor;
    private static int intentosReconexion=0;
    private Mensaje credenciales;
    String user, userDestino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
//        textView = (TextView) findViewById(R.id.textView);
//        btnSpeak = (Button) findViewById(R.id.button);
//
//        // hide the action bar
//        //getActionBar().hide();
//
//        btnSpeak.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                reconocerHabla();
//            }
//        });
        start = (Button) findViewById(R.id.button);
        output = (TextView) findViewById(R.id.output);
        scroller = (ScrollView) findViewById(R.id.scroller);
        client = new OkHttpClient();
        layoutContenedor = (LinearLayout)findViewById(R.id.layoutContenedor) ;

        Intent intent = getIntent();
        user=intent.getStringExtra("user");
        userDestino=intent.getStringExtra("userDestino");
        credenciales=new Mensaje(userDestino , user);

        iniciarConexion();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reconocerHabla();
            }
        });

    }


    public void rsetIntentosReconexion(){
        intentosReconexion=0;
    }

    private void iniciarConexion(){
        switch(intentosReconexion){
            case 0:
                intentosReconexion++;
                break;

            case 1:
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intentosReconexion++;
                break;

            case 2:
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intentosReconexion++;
                break;

            case 3:
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
        Request request = new Request.Builder().url("ws://192.168.1.38:8080/server/conectar").build();//echo.websocket.org
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);

    }

    private void reconocerHabla() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Diga algo");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "No soporta entrada por audio",
                    Toast.LENGTH_SHORT).show();
        }
        //client.dispatcher().executorService().shutdown();//Aquí da un error, gestionarlo
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //textView.setText(result.get(0));
                    //Se envia texto
                    Mensaje msg = new Mensaje();
                    msg.setContenido(result.get(0));
                    msg.setUsuario("Usuario de prueba");
                    Gson g = new Gson();
                    //Mensaje p = g.fromJson(jsonString, Mensaje.class)
                    ws.send(g.toJson(msg));
                }
                break;
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        client.dispatcher().executorService().shutdown();
    }

    //--------------------------------------------------
    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        private boolean validado=false;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            rsetIntentosReconexion();
            output("¡Conectado!");
            //Autenticacion
            Mensaje msg = new Mensaje();

            msg.setUsuario(credenciales.getUsuario());
            msg.setContenido(credenciales.getContenido());
            Gson g = new Gson();
            ws.send(g.toJson(msg));
            //webSocket.send("Hello, it's SSaurel !");
            //webSocket.send("What's up ?");
            //webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
//                JSONObject obj = new JSONObject(text);
//                String nombre = obj.getString("nombre");
//                String mensaje = obj.getString("mensaje");
//                output(nombre+": " + mensaje);
                //output("Receive : " + text);
                if(validado){
                    outputMsg(text);
                }else{
                    Gson g = new Gson();
                    Mensaje msg = g.fromJson(text, Mensaje.class);
                    if(msg.getUsuario().equals("Validado") && msg.getContenido().equals("Validado"))
                        validado=true;
                }
            }catch(Exception e){}
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.cancel();
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage()+ "\nReconectando...");
            webSocket.cancel();
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            iniciarConexion();
        }
    }

//    private void start(String message) {
//        Request request = new Request.Builder().url("ws://192.168.1.41:8080/server/prueba").build();//echo.websocket.org
//        EchoWebSocketListener listener = new EchoWebSocketListener();
//        WebSocket ws = client.newWebSocket(request, listener);
//        //client.dispatcher().executorService().shutdown();//Aquí da un error, gestionarlo
//    }
    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
                output.post(new Runnable(){
                    public void run(){
                        scrollDown();
                    }
                });

            }
        });
    }

    private void outputMsg(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson g = new Gson();
                Mensaje msg = g.fromJson(txt, Mensaje.class);
                //ws.send(g.toJson(msg));

                output.setText(output.getText().toString() + "\n\n" + msg.getUsuario()+": "+msg.getContenido());
                output.post(new Runnable(){
                    public void run(){
                        scrollDown();
                    }
                });

            }
        });
    }

    private void scrollDown(){
        scroller.scrollTo(0, layoutContenedor.getHeight());
        //scroller.fullScroll(output.FOCUS_DOWN);
    }

}

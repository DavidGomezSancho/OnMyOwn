package com.tfg.david.appconversacional;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.util.Locale;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class PrincipalActivity extends AppCompatActivity implements TextToSpeech.OnInitListener  {

    private final int REQUEST_CODE = 102;
    protected static final int CAMBIAR_MODO_CODE = 231;
    private final int MY_DATA_CHECK_CODE = 2321;

    private Button start, botonEnviar, cambiar_modo;
    private TextView output;
    private OkHttpClient client;
    private WebSocket ws;
    private ScrollView scroller;
    private LinearLayout layoutContenedor;
    private static int intentosReconexion = 0;
    private Mensaje credenciales;
    String user, userDestino, modo;
    private EditText textEnviar;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        modo = intent.getStringExtra("modo");
        user = intent.getStringExtra("user");
        userDestino = intent.getStringExtra("userDestino");
        tts = new TextToSpeech(this, this);

        switch (modo){
            case "auditiva":
                init_auditiva();
                break;

            case "visual_leve":
                init_visual_leve();
                break;

            case "visual_grave":
                init_visual_grave();
                break;
        }

        credenciales = new Mensaje(userDestino, user);
        client = new OkHttpClient();
        iniciarConexion();

        if(cambiar_modo!=null){
            cambiar_modo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (view.getContext(), SelecModoActivity.class);
                    startActivityForResult(intent, CAMBIAR_MODO_CODE);
                }
            });
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reconocerHabla();
            }
        });
    }

    private void cadenaAVoz(String cadena){
        tts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int i) {
        tts.setLanguage(new Locale("es_ES"));
        Intent ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent, MY_DATA_CHECK_CODE);
        switch (modo){
            case "visual_grave":
                cadenaAVoz("Pulse la pantalla y al escuchar un sonido hable. Si quiere cambiar de modo " +
                        "mantenga pulsado.");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_ayuda);
        builder.setMessage(R.string.mensaje_ayuda);
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public void init_auditiva(){
        setContentView(R.layout.activity_main_auditiva);
        start = (Button) findViewById(R.id.button5);
        output = (TextView) findViewById(R.id.output);
        scroller = (ScrollView) findViewById(R.id.scroller);
        layoutContenedor = (LinearLayout) findViewById(R.id.layoutContenedor);
        textEnviar = (EditText) findViewById(R.id.editText);
        botonEnviar = (Button) findViewById(R.id.button4);
        cambiar_modo = (Button) findViewById(R.id.button3);

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarTexto();
            }
        });
    }

    public void init_visual_leve(){
        setContentView(R.layout.activity_main_visual_leve);
        start = (Button) findViewById(R.id.button5);
        output = (TextView) findViewById(R.id.output);
        scroller = (ScrollView) findViewById(R.id.scroller);
        layoutContenedor = (LinearLayout) findViewById(R.id.layoutContenedor);
        textEnviar = (EditText) findViewById(R.id.editText);
        botonEnviar = (Button) findViewById(R.id.button4);
        cambiar_modo = (Button) findViewById(R.id.button3);

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarTexto();
            }
        });
    }

    public void init_visual_grave(){
        botonEnviar = null;
        output = null;
        scroller = null;
        layoutContenedor = null;
        textEnviar = null;
        cambiar_modo = null;
        setContentView(R.layout.activity_main_visual_grave);
        start = (Button) findViewById(R.id.button2);
        start.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                init_visual_leve();//modoVisualLeve();
                if(cambiar_modo!=null){
                    cambiar_modo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent (view.getContext(), SelecModoActivity.class);
                            startActivityForResult(intent, CAMBIAR_MODO_CODE);
                        }
                    });
                }

                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reconocerHabla();
                    }
                });
                return true;
            }
        });
//        output = (TextView) findViewById(R.id.output);
//        scroller = (ScrollView) findViewById(R.id.scroller);
//        layoutContenedor = (LinearLayout) findViewById(R.id.layoutContenedor);
    }

    public void rsetIntentosReconexion() {
        intentosReconexion = 0;
    }

    private void iniciarConexion() {
        switch (intentosReconexion) {
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
        Request request = new Request.Builder().url("ws://192.168.1.33:8080/server/conectar").build();//echo.websocket.org
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);

    }

    private void enviarTexto(){
        Mensaje msg = new Mensaje();
        String contenido=textEnviar.getText().toString();
        msg.setUsuario(user);
        msg.setContenido(contenido);
        Gson g = new Gson();
        ws.send(g.toJson(msg));
        borraTextoCaja();
    }

    private void reconocerHabla() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable ahora");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "No soporta entrada por audio",
                    Toast.LENGTH_SHORT).show();
        }
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

            case CAMBIAR_MODO_CODE: {
                String resultado = data.getExtras().getString("modo");
                modo = resultado;
                switch (modo){
                    case "auditiva":
                        init_auditiva();
                        break;

                    case "visual_leve":
                        init_visual_leve();
                        break;

                    case "visual_grave":
                        init_visual_grave();
                        break;
                }
                if(cambiar_modo!=null){
                    cambiar_modo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent (view.getContext(), SelecModoActivity.class);
                            startActivityForResult(intent, CAMBIAR_MODO_CODE);
                        }
                    });
                }

                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reconocerHabla();
                    }
                });
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        client.dispatcher().executorService().shutdown();
    }

    private void borraTextoCaja(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textEnviar.setText("");

            }
        });
    }

    //--------------------------------------------------
    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        private boolean validado = false;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            rsetIntentosReconexion();
            if(!modo.equals("visual_grave")) {
                output("¡Conectado!");
                cadenaAVoz("Conectado");
            }
            //Autenticacion
            Mensaje msg = new Mensaje();

            msg.setUsuario(credenciales.getUsuario());
            msg.setContenido(credenciales.getContenido());
            Gson g = new Gson();
            ws.send(g.toJson(msg));
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                if (validado) {
                    Gson g = new Gson();
                    Mensaje msg = g.fromJson(text, Mensaje.class);
                    switch (modo){
                        case "auditiva":
                            outputMsg(text);
                            break;

                        case "visual_leve":
                            outputMsg(text);
                            cadenaAVoz("El usuario "+msg.getUsuario()+" dice. "+msg.getContenido());
                            break;

                        case "visual_grave":
                            cadenaAVoz("El usuario "+msg.getUsuario()+" dice. "+msg.getContenido());
                            break;
                    }
                } else {
                    Gson g = new Gson();
                    Mensaje msg = g.fromJson(text, Mensaje.class);
                    if (msg.getUsuario().equals("Validado") && msg.getContenido().equals("Validado"))
                        validado = true;
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            //output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.cancel();
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            if(!modo.equals("visual_grave")) {
                output("Closing : " + code + " / " + reason);
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            if(!modo.equals("visual_grave")) {
                output("Error : " + t.getMessage() + "\nReconectando...");
            }
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
                output.post(new Runnable() {
                    public void run() {
                        scrollDown();
                    }
                });

            }
        });
    }

    private void outputMsg(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson g = new Gson();
                Mensaje msg = g.fromJson(txt, Mensaje.class);
                //ws.send(g.toJson(msg));

                output.setText(output.getText().toString() + "\n\n" + msg.getUsuario() + ": " + msg.getContenido());
                output.post(new Runnable() {
                    public void run() {
                        scrollDown();
                    }
                });

            }
        });
    }

    private void scrollDown() {
        scroller.scrollTo(0, layoutContenedor.getHeight());
        //scroller.fullScroll(output.FOCUS_DOWN);
    }
}
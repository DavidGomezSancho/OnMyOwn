package com.tfg.david.appconversacional;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ValidarActivity extends AppCompatActivity implements Serializable, TextToSpeech.OnInitListener {

    private final int REQUEST_CODE = 105;
    private final int REQUEST_CODE2 = 111;
    private final int MY_DATA_CHECK_CODE = 120;
    private Button validar;
    private EditText usuario;
    private TextView text;
    private WebSocket ws;
    private OkHttpClient client;
    Mensaje msg;
    int fase;
    String modo, user, userDestino;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        fase=1;
//        tts = new TextToSpeech(this, this);
//        Intent ttsIntent = new Intent();
//        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(ttsIntent, MY_DATA_CHECK_CODE);
        modo =intent.getStringExtra("modo");
        tts = new TextToSpeech(this, this);


        switch (modo){
            case "auditiva":
                vista_auditiva();
                break;

            case "visual_leve":
                vista_visual_leve();
                break;

            case "visual_grave":
                vista_visual_grave();
                break;
        }



//        setContentView(R.layout.activity_validar);
//
//        validar = (Button) findViewById(R.id.validar);
//        usuario = (EditText) findViewById(R.id.usuario);
//        //pass = (EditText) findViewById(R.id.password);
//        text = (TextView) findViewById(R.id.textView3);
//



        Request request = new Request.Builder().url("ws://192.168.1.33:8080/server/validar").build();//echo.websocket.org
        EchoWebSocketListener listener = new EchoWebSocketListener();
        client = new OkHttpClient();
        ws = client.newWebSocket(request, listener);

        Conexion.setClient(client);
        Conexion.setWs(ws);



//
//        validar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                enviarUserPass();
//            }
//        });
//        validar.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                pulsacion_larga();
//                return true;
//            }
//        });

        //Modo cambiar=Modo.AUDITIVA();
        //cambiar.setTam_elementos(4);

        //text.setText(text.getText().toString() + "\n\n" +Integer.toString(cambiar.getTam_elementos()) );
        //setContentView(R.layout.activity_main_auditiva);

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

    private void cadenaAVoz(String cadena){
        tts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
    }


    private void vista_auditiva(){
        setContentView(R.layout.activity_validar);
        validar = (Button) findViewById(R.id.validar);
        text = (TextView) findViewById(R.id.textView3);
        usuario = (EditText) findViewById(R.id.usuario);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarUser();
            }
        });
    }

    private void vista_visual_leve(){
        setContentView(R.layout.activity_validar_visual_leve);
        validar = (Button) findViewById(R.id.validar);
        text = (TextView) findViewById(R.id.textView3);
        usuario = (EditText) findViewById(R.id.usuario);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarUser();
            }
        });
    }

    private void vista_visual_grave(){
        setContentView(R.layout.activity_validar_visual_grave);
        validar = (Button) findViewById(R.id.button6);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hablarParaEnviarUser();
            }
        });
    }


//    private void enviarUserPass(){
//        msg = new Mensaje();
//        msg.setUsuario(usuario.getText().toString());
//        //msg.setContenido(pass.getText().toString());//"pumheadshot"
//        Gson g = new Gson();
//        ws.send(g.toJson(msg));
//    }

    private void enviarUser(){
        msg = new Mensaje();
        user=usuario.getText().toString().toLowerCase();
        msg.setUsuario(user);
        msg.setContenido("disponibilidad");
        Gson g = new Gson();
        //cadenaAVoz("hola que tal");
        ws.send(g.toJson(msg));
    }

    private void enviarUserDestino(){
        msg = new Mensaje();
        userDestino=usuario.getText().toString().toLowerCase();
        msg.setUsuario(userDestino);
        msg.setContenido("conectar");
        Gson g = new Gson();
        ws.send(g.toJson(msg));
    }


    private void hablarParaEnviarUser(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Diga usuario que desea");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "No soporta entrada por audio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void hablarParaEnviarUserDestino(){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Diga usuario que desea");
        try {
            startActivityForResult(intent, REQUEST_CODE2);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "No soporta entrada por audio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Se envia texto
                    msg = new Mensaje();
                    msg.setContenido("disponibilidad");
                    user=result.get(0).toLowerCase();
                    msg.setUsuario(user);
                    Gson g = new Gson();
                    ws.send(g.toJson(msg));
                }
                break;
            }

            case REQUEST_CODE2: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Se envia texto
                    msg = new Mensaje();
                    msg.setContenido("conectar");
                    userDestino=result.get(0).toLowerCase();
                    msg.setUsuario(userDestino);
                    Gson g = new Gson();
                    ws.send(g.toJson(msg));
                }
                break;
            }

            case MY_DATA_CHECK_CODE: {
                    //cadenaAVoz("Hola que tal");
            }
        }
    }

    private void pulsacion_larga(){
        text.setText(text.getText().toString() + "\n\n" + "Se ha pulsado largamente");
    }

    @Override
    public void onInit(int i) {
        tts.setLanguage(new Locale("es_ES"));
        Intent ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent, MY_DATA_CHECK_CODE);
        switch (modo){
            case "visual_leve":
                cadenaAVoz("Introduzca el nombre de usuario a utilizar. Al hacer click acepta el tratamiento de sus datos.");
                break;

            case "visual_grave":
                cadenaAVoz("Pulse para comunicar su nombre de usuario. Al hacer click acepta el tratamiento de sus datos. Pulse la " +
                        "pantalla y al escuchar el sonido diga cómo desea llamarse en la aplicación.");
                break;
        }

    }


    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            //rsetIntentosReconexion();
            //output("¡Conectado!");
            //Autenticacion

            //webSocket.send("Hello, it's SSaurel !");
            //webSocket.send("What's up ?");
            //webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                //output("Receive : " + text);
                comprobarValidacion(text);
            }catch(Exception e){}
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            //output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.cancel();
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            webSocket.cancel();
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            //iniciarConexion();
        }
    }

    private void output(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Gson g = new Gson();
                //Mensaje msg = g.fromJson(txt, Mensaje.class);
                //ws.send(g.toJson(msg));

                //text.setText(text.getText().toString() + "\n\n" + msg.getUsuario()+": "+msg.getContenido());
                text.setText(txt);

            }
        });
    }

    private void borraTextoCaja(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Gson g = new Gson();
                //Mensaje msg = g.fromJson(txt, Mensaje.class);
                //ws.send(g.toJson(msg));

                //text.setText(text.getText().toString() + "\n\n" + msg.getUsuario()+": "+msg.getContenido());
                usuario.setText("");

            }
        });
    }

    private void outputButton(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Gson g = new Gson();
                //Mensaje msg = g.fromJson(txt, Mensaje.class);
                //ws.send(g.toJson(msg));

                //text.setText(text.getText().toString() + "\n\n" + msg.getUsuario()+": "+msg.getContenido());
                validar.setText("Pulse para comunicar el usuario con el que desea hablar");

            }
        });
    }

    private boolean comprobarValidacion(String text){
        Gson g = new Gson();
        Mensaje msg = g.fromJson(text, Mensaje.class);

        //intent.putExtra("ObjetoSocket", (Serializable) ws);
        //intent.putExtra("ObjetoSocketCliente", (Serializable) client);

        if(msg.getUsuario().equals("Validado") && msg.getContenido().equals("Validado")){
            if(fase==1){
                switch (modo){
                    case "auditiva":
                        output("Introduzca usuario con el que se desea comunicar");
                        borraTextoCaja();
                        //validar.setOnClickListener(null);
                        validar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                enviarUserDestino();
                            }
                        });
                        break;

                    case "visual_leve":
                        output("Introduzca usuario con el que se desea comunicar");
                        borraTextoCaja();
                        cadenaAVoz("Introduzca usuario con el que se desea comunicar");
                        validar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                enviarUserDestino();
                            }
                        });
                        break;

                    case "visual_grave":
                        outputButton();
                        cadenaAVoz("Pulse para comunicar el usuario con el que desea hablar");
                        validar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hablarParaEnviarUserDestino();
                            }
                        });
                        break;
                }
                fase=2;
            }else{
                ws.close(1000, null);
                client.dispatcher().executorService().shutdown();
                Intent intent = new Intent(this, PrincipalActivity.class);
                intent.putExtra("userDestino", userDestino);
                intent.putExtra("user", user);
                intent.putExtra("modo", modo);
                startActivity(intent);
            }
        }else{
            user="";
            output("Error, vuelva a introducir un nombre válido");
        }
        return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        tts.shutdown();
    }
}

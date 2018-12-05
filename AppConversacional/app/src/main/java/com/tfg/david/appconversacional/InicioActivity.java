package com.tfg.david.appconversacional;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class InicioActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private Button cambiar_modo;
    TextToSpeech tts;
    private final int MY_DATA_CHECK_CODE = 1201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        tts = new TextToSpeech(this, this);

        cambiar_modo = (Button) findViewById(R.id.button7);

        cambiar_modo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                modo_visual_grave();
                return true;
            }
        });
        cambiar_modo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                siguiente_pregunta();
            }
        });

    }

    @Override
    public void onInit(int i) {
        tts.setLanguage(new Locale("es_ES"));
        Intent ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent, MY_DATA_CHECK_CODE);
        cadenaAVoz("Pulse rápidamente si ve este texto. En caso de no ver el texto mantenga pulsada la " +
                "pantalla hasta que se señale lo contrario");
    }

    private void cadenaAVoz(String cadena){
        tts.speak(cadena, TextToSpeech.QUEUE_FLUSH, null);
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

    public void siguiente_pregunta(){
        cambiar_modo.setTextSize(14);
        cambiar_modo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modo_auditiva();
            }
        });
        cambiar_modo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                modo_visual_leve();
                return true;
            }
        });
        cadenaAVoz("Pulse rápidamente si ve este texto. En caso de no ver el texto mantenga pulsada la " +
                "pantalla hasta que se señale lo contrario");
    }

    public void modo_visual_grave(){
        Intent intent = new Intent(this, ValidarActivity.class);
        intent.putExtra("modo","visual_grave");
        startActivity(intent);
    }

    public void modo_auditiva(){
        Intent intent = new Intent(this, ValidarActivity.class);
        intent.putExtra("modo","auditiva");
        startActivity(intent);
    }

    public void modo_visual_leve(){
        Intent intent = new Intent(this, ValidarActivity.class);
        intent.putExtra("modo","visual_leve");
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

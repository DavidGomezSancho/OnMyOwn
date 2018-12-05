package com.tfg.david.appconversacional;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SelecModoActivity extends AppCompatActivity {

    private Button visual_grave, visual_leve, auditiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecmodo);
        visual_grave = (Button) findViewById(R.id.button8);
        visual_leve = (Button) findViewById(R.id.button9);
        auditiva = (Button) findViewById(R.id.button10);

        visual_grave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("modo","visual_grave");
                setResult(PrincipalActivity.CAMBIAR_MODO_CODE , returnIntent);
                finish();
            }
        });

        visual_leve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("modo","visual_leve");
                setResult(PrincipalActivity.CAMBIAR_MODO_CODE , returnIntent);
                finish();
            }
        });

        auditiva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("modo","auditiva");
                setResult(PrincipalActivity.CAMBIAR_MODO_CODE , returnIntent);
                finish();
            }
        });
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
}

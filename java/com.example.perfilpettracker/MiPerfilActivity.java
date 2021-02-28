package com.example.perfilpettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MiPerfilActivity extends AppCompatActivity {

    private static final int EDITAR_PERFIL_REQUEST_CODE = 1;
    public static final String MENSAJE = "com.example.perfilpettracker.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);
    }

    public void editarPerfil(View view){
        Intent intent = new Intent(this, EditarPerfilActivity.class);
        startActivityForResult(intent, EDITAR_PERFIL_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == EDITAR_PERFIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getStringExtra(MENSAJE).equals("guardar"))
                    Toast.makeText(getBaseContext(), "Su información se guardó", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(), "Edición cancelada", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getBaseContext(), "Edición cancelada", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getBaseContext(), "Edición cancelada", Toast.LENGTH_LONG).show();
        }

    }
}

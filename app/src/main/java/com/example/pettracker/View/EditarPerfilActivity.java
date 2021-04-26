package com.example.pettracker.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pettracker.R;

public class EditarPerfilActivity extends AppCompatActivity {
    public static final String MENSAJE = "com.example.perfilpettracker.MESSAGE";
    private Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarperfil);
        intent = getIntent();
        Spinner spin_mes, spin_dia;
        spin_mes = findViewById(R.id.spin_mes);
        spin_mes.setSelection(0);
        spin_mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mes = (String)spin_mes.getSelectedItem();
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_dia = findViewById(R.id.spin_dias);
        spin_dia.setSelection(0);
        spin_dia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dia = (String)spin_dia.getSelectedItem();
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cancelarPerfil(View view){
        intent = new Intent();
        String message = "cancelar";
        intent.putExtra(MENSAJE,message);
        setResult(RESULT_OK,intent);
        finish();
    }
    public void guardarPerfil(View view) {
        intent = new Intent();
        String message = "guardar";
        intent.putExtra(MENSAJE, message);
        setResult(RESULT_OK, intent);
        finish();
    }
}

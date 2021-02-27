package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    EditText nombre;
    EditText apellido;
    EditText correo;
    EditText contrasena;
    EditText telefono;
    EditText direccion;
    boolean camposVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nombre = findViewById(R.id.campoNombre);
        apellido = findViewById(R.id.campoApellido);
        correo = findViewById(R.id.campocorreoInicio);
        contrasena = findViewById(R.id.campocontrasena);
        telefono = findViewById(R.id.campoTelefono);
        direccion = findViewById(R.id.campoDireccion);


    }
    public void homepage (View view){
        startActivity(new Intent(this, HomePageActivity.class));
        /*String nom = nombre.getText().toString();
        String ape = apellido.getText().toString();
        String mail = correo.getText().toString();
        String contra = contrasena.getText().toString();
        String tele = telefono.getText().toString();
        String dir = direccion.getText().toString();
        if (android.text.TextUtils.isEmpty(nom) && android.text.TextUtils.isEmpty(ape) && android.text.TextUtils.isEmpty(mail) && android.text.TextUtils.isEmpty(contra) && android.text.TextUtils.isEmpty(tele) && android.text.TextUtils.isEmpty(dir)) {
            Toast.makeText(this,
                    "Todos los campos son obligatorios",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(new Intent(this, HomePageActivity.class));
        }*/

    }



}
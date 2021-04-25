package com.example.pettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    EditText nombre;
    EditText apellido;
    EditText correo;
    EditText contrasena;
    EditText telefono;
    EditText direccion;
    Button register;
    boolean camposVer;

    private FirebaseAuth mAuth;
    public static final String TAG = "FB_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.campoNombre);
        apellido = findViewById(R.id.campoApellido);
        correo = findViewById(R.id.campocorreoInicio);
        contrasena = findViewById(R.id.campocontrasena);
        telefono = findViewById(R.id.campoTelefono);
        direccion = findViewById(R.id.campoDireccion);
        register = findViewById(R.id.botonRegistro);


    }
    public void homepage (View view){
        startActivity(new Intent(this, HomePageActivity.class));

    }



}
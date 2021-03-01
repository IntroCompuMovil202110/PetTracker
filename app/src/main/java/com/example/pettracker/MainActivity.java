package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText correo;
    EditText contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = findViewById(R.id.campocorreoInicio);
        contrasena = findViewById(R.id.campocontrasenaInicio);
    }

    public void registro (View view){
        startActivity(new Intent(this, RegistrationActivity.class));

    }

    public void homepage (View view){
        startActivity(new Intent(this, HomePageActivity.class));

    }
}

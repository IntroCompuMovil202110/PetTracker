package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;

public class MisPublicacionesActivity extends AppCompatActivity {
    String[] nombresProductos;
    TypedArray fotosProductos;
    String[] precioProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);
    }
}
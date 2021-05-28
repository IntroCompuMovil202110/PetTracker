package com.example.pettracker.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pettracker.Model.Veterinaria;
import com.example.pettracker.R;

public class VeterinariaActivity extends AppCompatActivity {
    private Veterinaria veterinaria;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        veterinaria = (Veterinaria) intent.getSerializableExtra("veterinaria");
        setContentView(R.layout.activity_veterinaria);
        TextView nomVeterinaria = findViewById(R.id.nomVeterinaria);
        nomVeterinaria.setText(veterinaria.getNombre());
        TextView dirVeterinaria = findViewById(R.id.dirVeterinaria);
        dirVeterinaria.setText("Dirección: " + veterinaria.getDireccion());
        TextView horaVeterinaria = findViewById(R.id.horaVeterinaria);
        horaVeterinaria.setText(veterinaria.getHoras());
        TextView telVeterinaria = findViewById(R.id.telVeterinaria);
        telVeterinaria.setText("Teléfono: " + veterinaria.getTelefono());
    }
}

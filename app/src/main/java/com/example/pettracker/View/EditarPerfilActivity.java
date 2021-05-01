package com.example.pettracker.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pettracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellidos;
    private EditText correo;
    private EditText telefono;
    private EditText direccion;
    private EditText contrasena;
    private EditText confirmContra;
    private TextView tagCostoE;
    private EditText costoE;

    private DatabaseReference reference;
    private String key;
    private Boolean type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarperfil);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            key = bundle.getString("userID");
        } else {
            finish();
        }

        nombre = findViewById(R.id.nombreE);
        apellidos = findViewById(R.id.apellidos);
        correo = findViewById(R.id.correo);
        telefono = findViewById(R.id.telefono);
        direccion = findViewById(R.id.direccion);
        contrasena = findViewById(R.id.contrasena);
        confirmContra = findViewById(R.id.confirmacion_contrasena);
        tagCostoE = findViewById(R.id.tagCostoE);
        costoE = findViewById(R.id.costoE);

        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    type = true;
                    nombre.setText(snapshot.child("nombre").getValue().toString());
                    apellidos.setText(snapshot.child("apellido").getValue().toString());
                    correo.setText(snapshot.child("correo").getValue().toString());
                    telefono.setText(snapshot.child("telefono").getValue().toString());
                    direccion.setText(snapshot.child("direccion").getValue().toString());
                    contrasena.setText(snapshot.child("contrasena").getValue().toString());
                    confirmContra.setText("");
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("walkers");
                    reference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            type = false;
                            tagCostoE.setVisibility(View.VISIBLE);
                            costoE.setVisibility(View.VISIBLE);
                            nombre.setText(dataSnapshot.child("nombre").getValue().toString());
                            apellidos.setText(dataSnapshot.child("apellido").getValue().toString());
                            correo.setText(dataSnapshot.child("correo").getValue().toString());
                            telefono.setText(dataSnapshot.child("telefono").getValue().toString());
                            direccion.setText(dataSnapshot.child("direccion").getValue().toString());
                            costoE.setText(dataSnapshot.child("costoServicio").getValue().toString());
                            contrasena.setText(dataSnapshot.child("contrasena").getValue().toString());
                            confirmContra.setText("");
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        /*Spinner spin_mes, spin_dia;
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
        });*/
    }

    public void cancelarPerfil(View view){
        Toast.makeText(EditarPerfilActivity.this, "Edición Cancelada", Toast.LENGTH_SHORT).show();
    }

    public void guardarPerfil(View view) {
        reference.child(key).child("nombre").setValue(nombre.getText().toString());
        reference.child(key).child("apellido").setValue(apellidos.getText().toString());
        reference.child(key).child("correo").setValue(correo.getText().toString());
        reference.child(key).child("telefono").setValue(telefono.getText().toString());
        reference.child(key).child("direccion").setValue(direccion.getText().toString());
        if(!type) {
            reference.child(key).child("costoServicio").setValue(costoE.getText().toString());
        }
        if ( contrasena.getText().toString().equals(confirmContra.getText().toString()) )
        {
            reference.child(key).child("contrasena").setValue(contrasena.getText().toString());
            Toast.makeText(EditarPerfilActivity.this, "Edición guardada", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(EditarPerfilActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }
}

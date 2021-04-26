package com.example.pettracker.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pettracker.ImageActivity;
import com.example.pettracker.R;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class MiPerfilActivity extends AppCompatActivity {

    private static final int EDITAR_PERFIL_REQUEST_CODE = 1;
    public static final String MENSAJE = "com.example.perfilpettracker.MESSAGE";
    com.google.android.material.imageview.ShapeableImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);
        imageView = findViewById(R.id.foto_perfil);

    }

    public void editarPerfil(View view){
        Intent intent = new Intent(this, EditarPerfilActivity.class);
        startActivityForResult(intent, EDITAR_PERFIL_REQUEST_CODE);
    }


    public void misPublicaciones(View view){
        startActivity(new Intent(this, MisPublicacionesActivity.class));
    }

    public void image(View view){
        Intent intent= new Intent (getBaseContext() , ImageActivity.class);
        intent.putExtra("resId", R.drawable.chocobo);
        startActivity(intent);
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

package com.example.pettracker.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pettracker.ImageActivity;
import com.example.pettracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import okhttp3.internal.cache.DiskLruCache;

public class MiPerfilActivity extends AppCompatActivity {

    private static final int EDITAR_PERFIL_REQUEST_CODE = 1;
    private static final int IMAGEN_REQUEST_CODE = 0;
    public static final String MENSAJE = "com.example.perfilpettracker.MESSAGE";
    public static final String PERFIL_EXTRA = "com.example.pettracker.MESSAGE";
    com.google.android.material.imageview.ShapeableImageView imageView;
    private TextView correo;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);
        imageView = findViewById(R.id.foto_perfil);
        correo = findViewById(R.id.correo);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            correo.setText(user.getEmail());
            if(user.getPhotoUrl() != null){
                Glide.with(this)
                    .load(user.getPhotoUrl())
                        .into(imageView);
                int newHeight = 500; // New height in pixels
                int newWidth = 500; // New width in pixels
                imageView.requestLayout();
                imageView.getLayoutParams().height = newHeight;
                imageView.getLayoutParams().width = newWidth;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    protected void onResume(Bundle savedInstanceState){
        ajustarImagenPerfil();
    }

    protected void onStart(Bundle savedInstanceState){
        ajustarImagenPerfil();
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
        startActivityForResult(intent, IMAGEN_REQUEST_CODE);
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
        if(requestCode == IMAGEN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(MiPerfilActivity.this, "IMAGEN", Toast.LENGTH_SHORT);

                ajustarImagenPerfil();
            }
        }
        else{
            Toast.makeText(getBaseContext(), "Edición cancelada", Toast.LENGTH_LONG).show();
        }

    }
    private void ajustarImagenPerfil(){
        if(user != null){
            if(user.getPhotoUrl() != null){
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageView);
                int newHeight = 500; // New height in pixels
                int newWidth = 500; // New width in pixels
                imageView.requestLayout();
                imageView.getLayoutParams().height = newHeight;
                imageView.getLayoutParams().width = newWidth;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Toast.makeText(MiPerfilActivity.this, "Hay imagen", Toast.LENGTH_SHORT);
            }
            else
                Toast.makeText(MiPerfilActivity.this, "No hay imagen", Toast.LENGTH_SHORT);

        }
    }
}

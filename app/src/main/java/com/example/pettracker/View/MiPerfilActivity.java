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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pettracker.ImageActivity;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import okhttp3.internal.cache.DiskLruCache;

public class MiPerfilActivity extends AppCompatActivity {

    private static final int EDITAR_PERFIL_REQUEST_CODE = 1;
    private static final int IMAGEN_REQUEST_CODE = 0;
    public static final String MENSAJE = "com.example.perfilpettracker.MESSAGE";
    public static final String PERFIL_EXTRA = "com.example.pettracker.MESSAGE";
    com.google.android.material.imageview.ShapeableImageView imageView;
    private TextView tagCorreo;
    private TextView correo;
    private TextView tagNombreCompleto;
    private TextView nombreCarino;
    private TextView nombreCompleto;
    private TextView tagTelefono;
    private TextView telefono;
    private TextView tagDireccion;
    private TextView direccion;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);
        imageView = findViewById(R.id.foto_perfil);
        tagCorreo = findViewById(R.id.tag_correo);
        correo = findViewById(R.id.correo);
        nombreCarino = findViewById(R.id.nombre_carino);
        tagNombreCompleto = findViewById(R.id.tag_nombre_completo);
        nombreCompleto = findViewById(R.id.nombre_completo);
        tagTelefono = findViewById(R.id.tag_telefono);
        telefono = findViewById(R.id.telefono);
        tagDireccion = findViewById(R.id.tag_direccion);
        direccion = findViewById(R.id.direccion);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
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
            }
            String nombre = user.getDisplayName();
            String cel = user.getPhoneNumber();
            String mail = user.getEmail();
            if(nombre != null){
                if(!nombre.equals("")){
                    String[] carino = nombre.split(" ");
                    nombreCarino.setText(carino[0]);
                    tagNombreCompleto.setText("Nombre: ");
                    nombreCompleto.setText(nombre);
                }
            }
            if(cel != null){
                if(!cel.equals("")){
                    tagTelefono.setText("Teléfono: ");
                    telefono.setText(cel);
                }
            }
            if(mail != null){
                if(!mail.equals("")){
                    tagCorreo.setText("Correo: ");
                    correo.setText(mail);
                }
            }

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if(usuario != null){
                        nombreCarino.setText(usuario.nombre);
                        tagNombreCompleto.setText("Nombre: ");
                        nombreCompleto.setText(usuario.nombre + " " + usuario.apellido);
                        tagCorreo.setText("Correo: ");
                        correo.setText(usuario.correo);
                        tagTelefono.setText("Teléfono: ");
                        telefono.setText(usuario.telefono);
                        tagDireccion.setText("Dirección: ");
                        direccion.setText(usuario.direccion);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
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

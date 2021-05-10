package com.example.pettracker.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.ImageActivity;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

public class MiPerfilActivity extends AppCompatActivity {

    private static final int EDITAR_PERFIL_REQUEST_CODE = 1;
    private static final int STORAGE_REQUEST = 33;
    private static final int IMAGE_PICKER_REQUEST = 44;
    private static final int REQUEST_IMAGE_CAPTURE = 55;
    private static final int CAMERA_PERMISSION = 66;

    private TextView nombreCarino;
    private TextView nombreCompleto;
    private TextView correo;
    private TextView telefono;
    private TextView direccion;
    private ImageView imagen_fondo;
    private CircleImageView profilePicture;
    private FloatingActionButton fondo;
    private FloatingActionButton foto_perfil;
    private TextView tagCosto;
    private TextView costo;
    private Boolean type;

    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);

        nombreCarino = findViewById(R.id.nombre_carino);
        nombreCompleto = findViewById(R.id.nombre_completo);
        correo = findViewById(R.id.correo);
        telefono = findViewById(R.id.telefono);
        direccion = findViewById(R.id.direccion);
        imagen_fondo = findViewById(R.id.imagen_fondo);
        profilePicture = findViewById(R.id.profilePicture);
        fondo = findViewById(R.id.fondo);
        foto_perfil = findViewById(R.id.foto_perfil);
        tagCosto = findViewById(R.id.tagCosto);
        costo = findViewById(R.id.costo);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        storage = FirebaseStorage.getInstance();

        if (user != null) {

            reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        nombreCarino.setText(snapshot.child("nombre").getValue().toString());
                        nombreCompleto.setText(snapshot.child("apellido").getValue().toString());
                        correo.setText(snapshot.child("correo").getValue().toString());
                        telefono.setText(snapshot.child("telefono").getValue().toString());
                        direccion.setText(snapshot.child("direccion").getValue().toString());
                        Glide.with(MiPerfilActivity.this)
                                .load(snapshot.child("fotoPerfilURL").getValue().toString())
                                .into(profilePicture);
                        Glide.with(MiPerfilActivity.this)
                                .load(snapshot.child("wallpaper").getValue().toString())
                                .into(imagen_fondo);

                    } else {
                        reference = FirebaseDatabase.getInstance().getReference("walkers");
                        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                tagCosto.setVisibility(View.VISIBLE);
                                costo.setVisibility(View.VISIBLE);
                                nombreCarino.setText(dataSnapshot.child("nombre").getValue().toString());
                                nombreCompleto.setText(dataSnapshot.child("apellido").getValue().toString());
                                correo.setText(dataSnapshot.child("correo").getValue().toString());
                                telefono.setText(dataSnapshot.child("telefono").getValue().toString());
                                direccion.setText(dataSnapshot.child("direccion").getValue().toString());
                                Glide.with(MiPerfilActivity.this)
                                        .load(dataSnapshot.child("fotoPerfilURL").getValue().toString())
                                        .into(profilePicture);
                                Glide.with(MiPerfilActivity.this)
                                        .load(dataSnapshot.child("wallpaper").getValue().toString())
                                        .into(imagen_fondo);
                                costo.setText(dataSnapshot.child("costoServicio").getValue().toString());
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
        }

        fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = true;
                showDialog();
            }
        });

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = false;
                showDialog();
            }
        });

    }

    void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MiPerfilActivity.this);
        dialog.setTitle("Foto de Perfil");
        String[] items = {"Galeria", "Cámara"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        PermissionsManagerPT.requestPermission(MiPerfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, "", STORAGE_REQUEST);
                        break;
                    case 1:
                        PermissionsManagerPT.requestPermission(MiPerfilActivity.this, Manifest.permission.CAMERA, "", CAMERA_PERMISSION);
                        break;
                }
            }
        });
        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    private void takePicture() {
        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void askForImage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICKER_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST:
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    askForImage();
                }
                return;
            case CAMERA_PERMISSION:
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            storageReference = storage.getReference("profileImages");
            final StorageReference imageReference = storageReference.child(imageUri.getLastPathSegment());
            imageReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (type) { // FONDO
                        reference.child(user.getUid()).child("wallpaper").setValue(task.getResult().toString());
                        imagen_fondo.setImageBitmap(selectedImage);
                    }else if(!type) { //PERFIL
                        reference.child(user.getUid()).child("fotoPerfilURL").setValue(task.getResult().toString());
                        profilePicture.setImageBitmap(selectedImage);
                    }
                }
            });
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Context c = this;
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(c.getContentResolver(),bitmap, "Title", null);

            Uri photoURI = Uri.parse(path);
            storageReference = storage.getReference("profileImages");
            final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + ".jpeg");
            imageReference.putFile(photoURI).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(type){ //FONDO
                        reference.child(user.getUid()).child("wallpaper").setValue(task.getResult().toString());
                        imagen_fondo.setImageBitmap(bitmap);
                    }
                    else if(!type) { //PERFIL
                        reference.child(user.getUid()).child("fotoPerfilURL").setValue(task.getResult().toString());
                        profilePicture.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    public void misPublicaciones(View view){
        Intent intent = new Intent(MiPerfilActivity.this, MisPublicacionesActivity.class);
        intent.putExtra("userID", user.getUid());
           startActivity(intent);
    }

    public void editarPerfil(View view){
        Intent intent = new Intent(MiPerfilActivity.this, EditarPerfilActivity.class);
        intent.putExtra("userID", user.getUid());
        startActivity(intent);
    }
}

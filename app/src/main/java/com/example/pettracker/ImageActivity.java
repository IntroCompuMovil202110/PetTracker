package com.example.pettracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {

    private static final int IMAGE_GALLERY_PERMISSION = 0;
    private static final int CAMERA_PERMISSION = 1;
    public static final String PERFIL_EXTRA = "com.example.pettracker.MESSAGE";
    private ImageView imageSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageSelected = findViewById(R.id.imageSelected);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int resId = bundle.getInt("resId");
            imageSelected.setImageResource(resId);
        };

    }

    public void openCamera(View view){
        String message = "Es necesario activar el permiso para acceder a la cámara.";
        String permission = Manifest.permission.CAMERA;
        if(PermissionsManagerPT.askForPermission(this, permission, message, CAMERA_PERMISSION)){
            takePhoto();
        }
    }

    public void openGallery(View view){
        String message = "Es necesario activar el permiso para acceder a la galeria.";
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if(PermissionsManagerPT.askForPermission(this, permission, message, IMAGE_GALLERY_PERMISSION)){
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case IMAGE_GALLERY_PERMISSION:
                if (PermissionsManagerPT.onRequestPermissionsResult(grantResults, this, "Es necesario activar el permiso para acceder a la galeria.")) {
                    selectImage();
                }
                break;
            case CAMERA_PERMISSION:
                if (PermissionsManagerPT.onRequestPermissionsResult(grantResults, this, "Es necesario activar el permiso para acceder a la cámara.")){
                    takePhoto();
                }
        }
    }

    public void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_GALLERY_PERMISSION);
    }

    public void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IMAGE_GALLERY_PERMISSION:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        Bundle extras = data.getExtras();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                        imageSelected.setImageBitmap(imageBitmap);

                        salvarImagen(imageBitmap);

                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }

                }
                break;
            case CAMERA_PERMISSION:
                if (resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageSelected.setImageBitmap(imageBitmap);

                    salvarImagen(imageBitmap);
                }
        }
    }

    private void salvarImagen(Bitmap imageBitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");
        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        descargarURL(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e("UPLOADTASK", "on Failure", e.getCause());
                    }
                });
    }

    private void descargarURL(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Cargar Imagen", "onSuccess" + uri);
                        setImagenPerfil(uri);
                    }
                });
    }

    private void setImagenPerfil(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ImageActivity.this, "Se cambió la imagen de perfil", Toast.LENGTH_LONG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(ImageActivity.this, "Error al cambiar imagen", Toast.LENGTH_LONG);
                    }
                });
        Intent datos = new Intent();
        datos.putExtra(PERFIL_EXTRA,"Bitmap");
        setResult(RESULT_OK,datos);

    }

}
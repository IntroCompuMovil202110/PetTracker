package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PublishProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int EDITAR_PERFIL_REQUEST_CODE = 1;
    private static final int STORAGE_REQUEST = 33;
    private static final int IMAGE_PICKER_REQUEST = 44;
    private static final int REQUEST_IMAGE_CAPTURE = 55;
    private static final int CAMERA_PERMISSION = 66;

    private ImageView addProductImage;
    private EditText txtProductName;
    private EditText txtProductPrice;
    private Spinner txtProductType;
    private Spinner txtProductSpecie;
    private EditText txtProductDescription;
    private Button btnPublish;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    String image;
    String name;
    String price;
    String type;
    String specie;
    String description;
    Boolean typeSelect;
    Boolean specieSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_product);

        addProductImage = findViewById(R.id.addProductImage);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtProductType = findViewById(R.id.txtProductType);
        txtProductSpecie = findViewById(R.id.txtProductSpecie);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        btnPublish = findViewById(R.id.btnPublish);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtProductType.setAdapter(typeAdapter);
        txtProductType.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> specieAdapter = ArrayAdapter.createFromResource(this, R.array.species, android.R.layout.simple_spinner_item);
        specieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtProductSpecie.setAdapter(specieAdapter);
        txtProductSpecie.setOnItemSelectedListener(this);

        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtProductName.getText().toString();
                price = txtProductPrice.getText().toString();
                description = txtProductDescription.getText().toString();

                if(validateForm(name, price, description)) {
                    registerProduct(name, price, description);
                }
            }
        });

    }

    void registerProduct(String name, String price, String description) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombre(snapshot.child("nombre").getValue().toString());
                    usuario.setApellido(snapshot.child("apellido").getValue().toString());
                    usuario.setFotoPerfilURL(snapshot.child("fotoPerfilURL").getValue().toString());
                    LUsuario lUsuario = new LUsuario(user.getUid(), usuario);

                    publish(name, price, description, lUsuario);

                } else  {
                    databaseReference = FirebaseDatabase.getInstance().getReference("walkers");
                    databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Paseador paseador = new Paseador();
                            paseador.setNombre(snapshot.child("nombre").getValue().toString());
                            paseador.setApellido(snapshot.child("apellido").getValue().toString());
                            paseador.setFotoPerfilURL(snapshot.child("fotoPerfilURL").getValue().toString());
                            LUsuario lUsuario = new LUsuario(user.getUid(), paseador);

                            publish(name, price, description, lUsuario);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    void publish(String name, String price, String description, LUsuario lUsuario) {
        Product product = new Product(name, image, description, price, type, specie, lUsuario);
        FirebaseDatabase.getInstance().getReference("products")
                .child(user.getUid())
                .push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(PublishProductActivity.this, "Su producto ha sido publicado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PublishProductActivity.this, MisPublicacionesActivity.class);
                    intent.putExtra("userID", lUsuario.getKey());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Log.w("FB_APP", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(PublishProductActivity.this, "Su Registro Fallo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean validateForm(String name, String price, String description) {
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(price)
                && TextUtils.isEmpty(description) && typeSelect == false && specieSelect == false) {
            txtProductName.setError("Debe ingresar un nombre de producto");
            txtProductPrice.setError("Debe ingresar un precio de producto");
            txtProductDescription.setError("Debe ingresar una descripción de producto");
            ((TextView)txtProductType.getSelectedView()).setError("Escoja un tipo de producto");
            ((TextView)txtProductSpecie.getSelectedView()).setError("Escoja una clasificación por especie");

            return false;
        }
        return true;
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(PublishProductActivity.this);
        dialog.setTitle("Foto del Producto");
        String[] items = {"Galeria", "Cámara"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        PermissionsManagerPT.requestPermission(PublishProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, "", STORAGE_REQUEST);
                        break;
                    case 1:
                        PermissionsManagerPT.requestPermission(PublishProductActivity.this, Manifest.permission.CAMERA, "", CAMERA_PERMISSION);
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
            storageReference = storage.getReference("productImages/" + user.getUid());
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
                    addProductImage.setImageBitmap(selectedImage);
                    image = task.getResult().toString();
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
            storageReference = storage.getReference("productImages/" + user.getUid());
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
                    addProductImage.setImageBitmap(bitmap);
                    image = task.getResult().toString();
                }
            });
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.txtProductType) {
            if(parent.getItemAtPosition(position).equals("Tipo de Producto")){
                typeSelect = false;
            }
            else {
                type = parent.getItemAtPosition(position).toString();
                typeSelect = true;
            }
        } else if(spinner.getId() == R.id.txtProductSpecie) {
            if(parent.getItemAtPosition(position).equals("Clasificación de Especie")){
                specieSelect = false;
            }
            else {
                specie = parent.getItemAtPosition(position).toString();
                specieSelect = true;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
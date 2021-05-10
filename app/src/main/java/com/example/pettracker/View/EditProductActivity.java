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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int STORAGE_REQUEST = 33;
    private static final int IMAGE_PICKER_REQUEST = 44;
    private static final int REQUEST_IMAGE_CAPTURE = 55;
    private static final int CAMERA_PERMISSION = 66;

    private ImageView imageEditProduct;
    private EditText txtEditProductName;
    private EditText txtEditProductPrice;
    private Spinner txtEditProductType;
    private Spinner txtEditProductSpecie;
    private EditText txtEditProductDescription;
    private Button btnEdit;
    private Button btnDelete;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    String product;
    String user;
    String image;
    String type;
    String specie;
    Boolean typeSelect;
    Boolean specieSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getString("key");
            product = bundle.getString("product");
        } else {
            finish();
        }

        imageEditProduct = findViewById(R.id.imageEditProduct);
        txtEditProductName = findViewById(R.id.txtEditProductName);
        txtEditProductPrice = findViewById(R.id.txtEditProductPrice);
        txtEditProductType = findViewById(R.id.txtEditProductType);
        txtEditProductSpecie = findViewById(R.id.txtEditProductSpecie);
        txtEditProductDescription = findViewById(R.id.txtEditProductDescription);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtEditProductType.setAdapter(typeAdapter);
        txtEditProductType.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> specieAdapter = ArrayAdapter.createFromResource(this, R.array.species, android.R.layout.simple_spinner_item);
        specieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtEditProductSpecie.setAdapter(specieAdapter);
        txtEditProductSpecie.setOnItemSelectedListener(this);

        storageReference = FirebaseStorage.getInstance().getReference("productImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.child(user).child(product).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    image = snapshot.child("image").getValue().toString();
                    Glide.with(EditProductActivity.this)
                            .load(image)
                            .into(imageEditProduct);
                    txtEditProductName.setText(snapshot.child("title").getValue().toString());
                    txtEditProductPrice.setText(snapshot.child("price").getValue().toString());
                    txtEditProductType.setSelection(typeAdapter.getPosition(snapshot.child("type").getValue().toString()));
                    txtEditProductSpecie.setSelection(specieAdapter.getPosition(snapshot.child("speciesClassification").getValue().toString()));
                    txtEditProductDescription.setText(snapshot.child("details").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        imageEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(user).child(product).child("image").setValue(image);
                databaseReference.child(user).child(product).child("title").setValue(txtEditProductName.getText().toString());
                databaseReference.child(user).child(product).child("price").setValue(txtEditProductPrice.getText().toString());
                databaseReference.child(user).child(product).child("type").setValue(type);
                databaseReference.child(user).child(product).child("speciesClassification").setValue(specie);
                databaseReference.child(user).child(product).child("details").setValue(txtEditProductDescription.getText().toString());

                Toast.makeText(EditProductActivity.this, "Edición guardada", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(user).child(product).removeValue();
                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProductActivity.this, "Su producto ha sido eliminado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProductActivity.this, MisPublicacionesActivity.class);
                        intent.putExtra("userID", user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
        dialog.setTitle("Foto del Producto");
        String[] items = {"Galeria", "Cámara"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        PermissionsManagerPT.requestPermission(EditProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, "", STORAGE_REQUEST);
                        break;
                    case 1:
                        PermissionsManagerPT.requestPermission(EditProductActivity.this, Manifest.permission.CAMERA, "", CAMERA_PERMISSION);
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
                    imageEditProduct.setImageBitmap(selectedImage);
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
                    imageEditProduct.setImageBitmap(bitmap);
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
        if(spinner.getId() == R.id.txtEditProductType) {
            if(parent.getItemAtPosition(position).equals("Tipo de Producto")){
                typeSelect = false;
            }
            else {
                type = parent.getItemAtPosition(position).toString();
                typeSelect = true;
            }
        } else if(spinner.getId() == R.id.txtEditProductSpecie) {
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
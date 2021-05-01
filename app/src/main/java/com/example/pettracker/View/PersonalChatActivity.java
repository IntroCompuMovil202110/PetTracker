package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.Adapters.AdapterMessage;
import com.example.pettracker.Controller.ChatDAO;
import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.Controller.UsuarioDAO;
import com.example.pettracker.Model.Firebase.LMessage;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Message;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChatActivity extends AppCompatActivity {

    private CircleImageView profilePicture;
    private TextView name;
    private RecyclerView rvMessages;
    private EditText textMsg;
    private FloatingActionButton buttonMsg;
    private FloatingActionButton cameraIcon;
    private FloatingActionButton galeryIcon;
    private AdapterMessage adapter;

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private String KEY_RECEPTOR;
    private String KEY_EMISSOR;

    private String receptorName;
    private String profURL;
    private String url;
    private String currentPhotoPath;
    private File photoFile;

    private static final int STORAGE_REQUEST = 33;
    private static final int SEND_IMAGE = 1;
    private static final int SEND_IMAGE_CAPTURE = 2;
    private static final int CAMERA_PERMISSION = 66;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            KEY_RECEPTOR = bundle.getString("keyReceptor");
            receptorName = bundle.getString("receptorName");
            url = bundle.getString("profileURL");
        }else{
            finish();
        }
        KEY_EMISSOR = UsuarioDAO.getInstancia().getKeyUsuario();

        profilePicture = (CircleImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        textMsg = (EditText) findViewById(R.id.textMsg);
        buttonMsg = (FloatingActionButton) findViewById(R.id.buttonMsg);
        cameraIcon = (FloatingActionButton) findViewById(R.id.cameraIcon);
        galeryIcon = (FloatingActionButton) findViewById(R.id.galeryIcon);

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new AdapterMessage(this);
        LinearLayoutManager l = new LinearLayoutManager(this);

        rvMessages.setLayoutManager(l);
        rvMessages.setAdapter(adapter);

        name.setText(receptorName);
        Glide.with(PersonalChatActivity.this)
                .load(url)
                .into(profilePicture);

        buttonMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMessage = textMsg.getText().toString();
                if(!sendMessage.isEmpty()){
                    Message message = new Message();
                    message.setMessage(sendMessage);
                    message.setHasPicture(false);
                    message.setEmisorKey(UsuarioDAO.getInstancia().getKeyUsuario());
                    ChatDAO.getInstance().newMessage(KEY_EMISSOR,KEY_RECEPTOR,message);
                    textMsg.setText("");
                }
            }
        });

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsManagerPT.requestPermission(PersonalChatActivity.this, Manifest.permission.CAMERA, "", CAMERA_PERMISSION);
            }
        });

        galeryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsManagerPT.requestPermission(PersonalChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, "", STORAGE_REQUEST);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("messages").child(KEY_EMISSOR).child(KEY_RECEPTOR);
        databaseReference.addChildEventListener(new ChildEventListener() {

            Map<String, LUsuario> mapUsersTemp = new HashMap<>();

            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                final Message message = snapshot.getValue(Message.class);
                final LMessage lMessage = new LMessage(snapshot.getKey(), message);
                final int position = adapter.addMessage(lMessage);

                if(mapUsersTemp.get(message.getEmisorKey())!=null){
                    lMessage.setlUser(mapUsersTemp.get(message.getEmisorKey()));
                    adapter.updateMessage(position,lMessage);
                }else{
                    UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLLave(message.getEmisorKey(), new UsuarioDAO.IDevolverUsuario(){
                        @Override
                        public void devolverUsuario(LUsuario lUsuario){
                            mapUsersTemp.put(message.getEmisorKey(),lUsuario);
                            /*lUsuario.setUser(new Usuario());
                            getProfUrl(message.getEmisorKey());
                            lUsuario.getUser().setFotoPerfilURL(profURL);*/
                            lMessage.setlUser(lUsuario);
                            adapter.updateMessage(position,lMessage);
                        }

                        @Override
                        public void devolverError(String error){
                            Toast.makeText(PersonalChatActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void getProfUrl(String key){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println("//////////////////////////////////////");
                    profURL = snapshot.child("fotoPerfilURL").getValue().toString();
                    System.out.println(profURL);
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("walkers");
                    ref.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("-------------------------------------------------");
                            profURL = dataSnapshot.child("fotoPerfilURL").getValue().toString();
                            System.out.println(profURL);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void takePicture() {
        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takepicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepicture, SEND_IMAGE_CAPTURE);
        }

    }
    private void askForImage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), SEND_IMAGE);
    }

    private void setScrollbar() {
        rvMessages.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SEND_IMAGE && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("chat_images");
            final StorageReference imageReference = storageReference.child(u.getLastPathSegment());
            imageReference.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
                public void onComplete(@NonNull Task<Uri> task){
                   if(task.isSuccessful()){
                       Uri uri = task.getResult();
                       Message message = new Message();
                       message.setUrlPicture(uri.toString());
                       message.setHasPicture(true);
                       message.setEmisorKey(UsuarioDAO.getInstancia().getKeyUsuario());
                       ChatDAO.getInstance().newMessage(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR,message);
                   }
               }
           });
        }
        else if (requestCode == SEND_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Context c = this;
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(c.getContentResolver(),bitmap, "Title", null);

            Uri photoURI = Uri.parse(path);
            storageReference = storage.getReference("chat_images");
            final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + ".jpeg");
            imageReference
                    .putFile(photoURI)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task){
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        Message message = new Message();
                        message.setUrlPicture(uri.toString());
                        message.setHasPicture(true);
                        message.setEmisorKey(UsuarioDAO.getInstancia().getKeyUsuario());
                        ChatDAO.getInstance().newMessage(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR,message);
                    }
                }
            });
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
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
}
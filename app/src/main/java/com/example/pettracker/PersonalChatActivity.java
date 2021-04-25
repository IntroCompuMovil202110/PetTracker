package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pettracker.Controller.AdapterMessage;
import com.example.pettracker.Controller.ChatDAO;
import com.example.pettracker.Controller.UsuarioDAO;
import com.example.pettracker.Model.LMessage;
import com.example.pettracker.Model.LUsuario;
import com.example.pettracker.Model.Message;
import com.example.pettracker.Model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChatActivity extends AppCompatActivity {

    private CircleImageView profilePicture;
    private TextView name;
    private RecyclerView rvMessages;
    private EditText textMsg;
    private FloatingActionButton buttonMsg;
    private ImageButton imageMsg;

    private AdapterMessage adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseAuth mAuth;
    private String username;

    private String KEY_RECEPTOR;
    private String KEY_EMISSOR;

    private static final int SEND_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            KEY_RECEPTOR = bundle.getString("keyReceptor");
        }else{
            finish();
        }
        KEY_EMISSOR = UsuarioDAO.getInstancia().getKeyUsuario();

        profilePicture = (CircleImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        textMsg = (EditText) findViewById(R.id.textMsg);
        buttonMsg = (FloatingActionButton) findViewById(R.id.buttonMsg);
        imageMsg = (ImageButton) findViewById(R.id.imageMsg);

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new AdapterMessage(this);
        LinearLayoutManager l = new LinearLayoutManager(this);

        rvMessages.setLayoutManager(l);
        rvMessages.setAdapter(adapter);

        buttonMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMessage = textMsg.getText().toString();
                if(!sendMessage.isEmpty()){
                    Message message = new Message();
                    message.setMessage(sendMessage);
                    message.setEmisorKey(UsuarioDAO.getInstancia().getKeyUsuario());
                    ChatDAO.getInstance().newMessage(KEY_EMISSOR,KEY_RECEPTOR,message);
                    textMsg.setText("");
                }
            }
        });

        imageMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Seleeciona una foto"), SEND_IMAGE);
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

        verifyStoragePermissions(this);
    }

    private void setScrollbar() {
        rvMessages.scrollToPosition(adapter.getItemCount()-1);
    }

    public static boolean verifyStoragePermissions(Activity activity){
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SEND_IMAGE && requestCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("chat_images");
            final StorageReference imageReference = storageReference.child(u.getLastPathSegment());
            imageReference.putFile(u).continueWithTask((task) -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return imageReference.getDownloadUrl();
           }).addOnCompleteListener((task) -> {
               if(task.isSuccessful()){
                   Uri uri = task.getResult();
                   Message message = new Message();
                   message.setMessage("Ha envíado una foto");
                   message.setUrlPicture(uri.toString());
                   message.setHasPicture(true);
                   message.setEmisorKey(UsuarioDAO.getInstancia().getKeyUsuario());
                   ChatDAO.getInstance().newMessage(KEY_EMISSOR,KEY_RECEPTOR,message);
               }
           });
        }
    }
}
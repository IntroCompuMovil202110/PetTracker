package com.example.pettracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pettracker.Controller.AdapterMessage;
import com.example.pettracker.Controller.ReceiveMessage;
import com.example.pettracker.Controller.SendMessage;
import com.example.pettracker.Model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    private static final int SEND_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        profilePicture = (CircleImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        textMsg = (EditText) findViewById(R.id.textMsg);
        buttonMsg = (FloatingActionButton) findViewById(R.id.buttonMsg);
        imageMsg = (ImageButton) findViewById(R.id.imageMsg);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");
        storage = FirebaseStorage.getInstance();

        adapter = new AdapterMessage(this);
        LinearLayoutManager l = new LinearLayoutManager(this);

        rvMessages.setLayoutManager(l);
        rvMessages.setAdapter(adapter);

        buttonMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.push().setValue(new SendMessage(textMsg.getText().toString(),name.getText().toString(),"","1", ServerValue.TIMESTAMP));
                textMsg.setText("");
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

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                ReceiveMessage m = snapshot.getValue(ReceiveMessage.class);
                adapter.addMessage(m);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setScrollbar() {
        rvMessages.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SEND_IMAGE && requestCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("chat_images");
            final StorageReference imageReference = storageReference.child(u.getLastPathSegment());
            imageReference.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
                    SendMessage m = new SendMessage("Imagen", name.getText().toString(), "", u.toString(), "2", ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                }
            });
        }
    }
}
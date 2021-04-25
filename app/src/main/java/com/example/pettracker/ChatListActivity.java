package com.example.pettracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pettracker.Controller.AdapterMessage;
import com.example.pettracker.Controller.ChatViewHolder;
import com.example.pettracker.Controller.UserViewHolder;
import com.example.pettracker.Model.LMessage;
import com.example.pettracker.Model.LUsuario;
import com.example.pettracker.Model.Message;
import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView chatList;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseUser currentUser;
    private DatabaseReference databaseRefence;

    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        chatList = findViewById(R.id.chatList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(linearLayoutManager);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("messages")
                .child(currentUser.getUid());

        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                    .setQuery(query, Usuario.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Usuario, ChatViewHolder>(options) {
            @Override
            public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_chatlist, parent, false);

                return new ChatViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ChatViewHolder holder, int position, Usuario model) {

                holder.getNameMessage().setText("Paseador" + position);
                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);;

                holder.getCardLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatListActivity.this, PersonalChatActivity.class);
                        intent.putExtra("keyReceptor", lUsuario.getKey());
                        intent.putExtra("receptorName", lUsuario.getUser().getNombre() + " " + lUsuario.getUser().getApellido());
                        startActivity(intent);
                    }
                });
            }
        };

        chatList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
package com.example.pettracker.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pettracker.Controller.ChatViewHolder;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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

                holder.getNameMessage().setText(model.getNombre());
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
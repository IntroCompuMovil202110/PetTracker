package com.example.pettracker.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.Holders.ChatViewHolder;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView chatList;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseUser currentUser;
    private DatabaseReference databaseRefence;

    private Usuario userSearch;


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
                DatabaseReference walkReference = FirebaseDatabase.getInstance().getReference("walkers");
                walkReference.child(getSnapshots().getSnapshot(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                        Usuario walker = snapshot.getValue(Usuario.class);
                        if(walker == null){
                            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
                            userReference.child(getSnapshots().getSnapshot(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot snapshot) {
                                    Usuario user = snapshot.getValue(Usuario.class);
                                    holder.getNameMessage().setText(user.getNombre() + " " + user.getApellido());
                                    model.setNombre(user.getNombre());
                                    model.setApellido(user.getApellido());
                                    model.setFotoPerfilURL(user.getFotoPerfilURL());
                                    Glide.with(ChatListActivity.this)
                                            .load(model.getFotoPerfilURL())
                                            .into(holder.getProfilePictureMessage());
                                }

                                @Override
                                public void onCancelled(@NotNull DatabaseError error) {

                                }
                            });
                        }
                        else if(walker != null) {
                            holder.getNameMessage().setText(walker.getNombre() + " " + walker.getApellido());
                            model.setNombre(walker.getNombre());
                            model.setApellido(walker.getApellido());
                            model.setFotoPerfilURL(walker.getFotoPerfilURL());
                            Glide.with(ChatListActivity.this)
                                    .load(model.getFotoPerfilURL())
                                    .into(holder.getProfilePictureMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {

                    }
                });

                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getCardLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatListActivity.this, PersonalChatActivity.class);
                        intent.putExtra("keyReceptor", lUsuario.getKey());
                        intent.putExtra("receptorName", lUsuario.getUser().getNombre() + " " + lUsuario.getUser().getApellido());
                        intent.putExtra("profileURL", lUsuario.getUser().getFotoPerfilURL());
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
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
import com.example.pettracker.Controller.Holders.UserViewHolder;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class WalkersListActivity extends AppCompatActivity {
    ArrayList<Usuario> walkersList;

    private RecyclerView walkers;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkers_list);
        walkers = findViewById(R.id.walkersList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        walkers.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").orderByChild("rol").equalTo("Paseador");

        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                    .setQuery(query, Usuario.class)
                    .build();

        adapter = new FirebaseRecyclerAdapter<Usuario, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_users, parent, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, Usuario model) {
                Glide.with(WalkersListActivity.this)
                        .load(model.getFotoPerfilURL())
                        .into(holder.getProfilePicture());
                holder.getName().setText(model.getNombre() + " " + model.getApellido());

                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getPriLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WalkersListActivity.this, PersonalChatActivity.class);
                        intent.putExtra("keyReceptor", lUsuario.getKey());
                        intent.putExtra("receptorName", lUsuario.getUser().getNombre() + " " + lUsuario.getUser().getApellido());
                        intent.putExtra("profileURL", lUsuario.getUser().getFotoPerfilURL());
                        startActivity(intent);
                    }
                });
            }
        };

        walkers.setAdapter(adapter);
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
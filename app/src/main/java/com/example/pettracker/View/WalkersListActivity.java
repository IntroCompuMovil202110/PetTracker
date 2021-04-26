package com.example.pettracker.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pettracker.Controller.UserViewHolder;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Paseador;
import com.example.pettracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class WalkersListActivity extends AppCompatActivity {
    ArrayList<Paseador> walkersList;

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
                .child("walkers")
                .orderByValue();

        FirebaseRecyclerOptions<Paseador> options =
                new FirebaseRecyclerOptions.Builder<Paseador>()
                    .setQuery(query, Paseador.class)
                    .build();

        adapter = new FirebaseRecyclerAdapter<Paseador, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_users, parent, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, Paseador model) {
                //Glide.with(WalkersListActivity.this).load()
                holder.getName().setText(model.getNombre() + " " + model.getApellido());

                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getPriLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WalkersListActivity.this, PersonalChatActivity.class);
                        intent.putExtra("keyReceptor", lUsuario.getKey());
                        intent.putExtra("receptorName", lUsuario.getUser().getNombre() + " " + lUsuario.getUser().getApellido());
                        startActivity(intent);
                    }
                });
            }
        };

        walkers.setAdapter(adapter);
       /* walkersList = new ArrayList<>();
        loadWalkers();
        CustomWalkerAdapter adapter = new CustomWalkerAdazpter(this, R.layout.walker_item, walkersList);
        walkers = (ListView) findViewById(R.id.walkersList);
        walkers.setAdapter(adapter);
        walkers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), PersonalChatActivity.class);
                Paseador p = walkersList.get(position);
                intent.putExtra("nombre", p.getNombre());
                startActivity(intent);
            }
        });*/
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

    /*private void loadWalkers() {
        walkersList.add(new Paseador("Paseador 1", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 2", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 3", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 4", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 5", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 6", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 7", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 8", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 9", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
    }*/
}
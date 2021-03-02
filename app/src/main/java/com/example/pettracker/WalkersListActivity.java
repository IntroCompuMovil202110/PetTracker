package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class WalkersListActivity extends AppCompatActivity {
    ArrayList<Paseador> walkersList;
    ListView walkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkers_list);
        walkersList = new ArrayList<>();
        loadWalkers();
        CustomWalkerAdapter adapter = new CustomWalkerAdapter(this, R.layout.walker_item, walkersList);
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
        });
    }

    private void loadWalkers() {
        walkersList.add(new Paseador("Paseador 1", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 2", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 3", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 4", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 5", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 6", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 7", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 8", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
        walkersList.add(new Paseador("Paseador 9", "Apellido", "email@email.com", "pass", "123456", "abc 123", new ArrayList<Product>(), "12000"));
    }
}
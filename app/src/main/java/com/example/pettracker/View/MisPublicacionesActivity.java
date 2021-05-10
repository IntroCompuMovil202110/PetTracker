package com.example.pettracker.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.Adapters.CustomPublicationsAdapter;
import com.example.pettracker.Model.Product;
import com.example.pettracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MisPublicacionesActivity extends AppCompatActivity {

    private RecyclerView products;
    private FirebaseRecyclerAdapter adapter;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);
        products = findViewById(R.id.productList);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentUser = bundle.getString("userID");
        } else {
            finish();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        products.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("products")
                .child(currentUser);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Product, CustomPublicationsAdapter>(options) {
            @Override
            public CustomPublicationsAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.publicacion_item, parent, false);

                return new CustomPublicationsAdapter(view);
            }

            @Override
            protected void onBindViewHolder(CustomPublicationsAdapter holder, int position, Product model) {
                Glide.with(MisPublicacionesActivity.this)
                        .load(model.getImage())
                        .into(holder.getImagen());
                holder.getNombre().setText(model.getTitle());
                holder.getPrecio().setText(model.getPrice());

                holder.getGridLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MisPublicacionesActivity.this, EditProductActivity.class);
                        intent.putExtra("product", adapter.getRef(position).getKey().toString());
                        intent.putExtra("key", currentUser);
                        startActivity(intent);
                    }
                });
            }

        };

        products.setAdapter(adapter);
        /*CustomPublicationsAdapter adapter = new CustomPublicationsAdapter(this, R.layout.publicacion_item, products);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                Product p = products.get(position);
                intent.putExtra("nombre", p.getTitle());
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

  /*  private void loadPublications() {
        products.add(new Product("Agility Gold Razas Grandes 3kg", "@drawable/producto1", "Comida para perros de 3kg para razas grandes, tambien se le puede dar a razas medianas.", "$38 500"));
        products.add(new Product("Saco Snoppy Talla S", "@drawable/producto2", "Saco de la tira animada snoppy talla S. Le sirve a razas medianas como french poodles, pomeranis o schnauzer.",  "$34 900"));
        products.add(new Product("Plato para Gatos", "@drawable/producto3", "Plato para comida o agua. Uselo a su preferencia. De aproximadamente 500 ml.",  "$14 600"));
        products.add(new Product("Correa para perros", "@drawable/producto4", "Correa para perros de cuero sintetico de alta duracion. Muy Buena.", "$6 300"));
        products.add(new Product("Juguete para Gatos", "@drawable/producto5", "Juguete para gatos muy entretenido y duradero. Por experiencia personal se que le fascina a los gatos y hasta tambien a perro juguetones. ",  "$45 200"));
    }*/


    public void crear(View view){
        startActivity(new Intent(this, PublishProductActivity.class));
    }

}

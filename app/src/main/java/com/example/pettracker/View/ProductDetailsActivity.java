package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Product;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ProductDetailsActivity extends AppCompatActivity {

    CollapsingToolbarLayout title;
    TextView details;
    ImageView image;
    Product product;
    TextView price;
    TextView type;
    TextView species;
    String productkey;
    String typ;
    String keyUser;

    DatabaseReference productsDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        title = findViewById(R.id.product_title_about);
        details = findViewById(R.id.product_about);
        image = findViewById(R.id.product_image_about);
        price = findViewById(R.id.price_about);
        type = findViewById(R.id.type_about);
        species = findViewById(R.id.species_about);

        productsDbRef = FirebaseDatabase.getInstance().getReference("products");

        Bundle bs = getIntent().getExtras();
        productkey =  bs.getString("product");
        keyUser = bs.getString("user");
        typ = bs.getString("tipo");
        System.out.println(productkey);

        productsDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot productsData: snapshot.getChildren()){
                    if(productsData.getKey().equals(keyUser)){
                        for(DataSnapshot prod: productsData.getChildren()){
                            if(prod.getKey().equals(productkey)){
                                String detalle = prod.child("details").getValue().toString();
                                String titulo = prod.child("title").getValue().toString();
                                String imagen = prod.child("image").getValue().toString();
                                String precio = prod.child("price").getValue().toString();
                                String tipo = prod.child("type").getValue().toString();
                                String classificacion = prod.child("speciesClassification").getValue().toString();
                                String key = prod.child("publisher").child("key").getValue().toString();
                                String apellido = prod.child("publisher").child("user").child("apellido").getValue().toString();
                                String nombre = prod.child("publisher").child("user").child("nombre").getValue().toString();

                                Usuario user = new Usuario("imagen", apellido, nombre);
                                LUsuario usuario = new LUsuario(key, user);
                                product = new Product(titulo, imagen, detalle, precio, tipo, classificacion, usuario, prod.getKey());
                            }
                        }
                    }

                }
                init();
        }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });

    }

    public void back(View v) {
        Intent intent = new Intent(this, SearchResultsMainActivity.class);
        intent.putExtra("tipo", typ);
        System.out.println(typ);
        startActivity(intent);
    }

    public void init(){
        title.setTitle(product.getTitle());
        details.setText(product.getDetails());
        Glide.with(ProductDetailsActivity.this).load(product.getImage()).into(image);
        price.setText(product.getPrice());
        type.setText(product.getType());
        species.setText(product.getSpeciesClassification());
    }

    public void chat(View view){
        Intent intent = new Intent(ProductDetailsActivity.this, PersonalChatActivity.class);
        intent.putExtra("keyReceptor", product.getPublisher().getKey());
        intent.putExtra("receptorName", product.getPublisher().getUser().getNombre() + " " + product.getPublisher().getUser().getApellido());

        startActivity(intent);
    }
}
package com.example.pettracker.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pettracker.Model.Product;
import com.example.pettracker.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ProductDetailsActivity extends AppCompatActivity {

    CollapsingToolbarLayout title;
    TextView details;
    ImageView image;
    Product product;
    TextView price;
    TextView type;
    TextView species;

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

        Bundle bundle = getIntent().getBundleExtra("data");
        product = (Product) bundle.getSerializable("product");
        init();
    }

    public void back(View v) {
        startActivity(new Intent(this, SearchResultsMainActivity.class));
    }

    public void init(){
        title.setTitle(product.getTitle());
        details.setText(product.getDetails());
        int imageId = getResources().getIdentifier(product.getImage() , "drawable", getPackageName());
        image.setImageResource(imageId);
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
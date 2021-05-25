package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Product;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<Product> products;

    DatabaseReference studentDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_main);
        toolBar = findViewById(R.id.toolbar2);
        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView = findViewById(R.id.nav_view2);
        setSupportActionBar(toolBar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        list = findViewById(R.id.results);

        studentDbRef = FirebaseDatabase.getInstance().getReference("products");

        products = new ArrayList<Product>();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String tip = extras.getString("tipo");

        CustomAdapter customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);


        studentDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                products.clear();

                for(DataSnapshot productsData: snapshot.getChildren()){
                    for(DataSnapshot prod: productsData.getChildren()){
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
                        Product product = new Product(titulo, imagen, detalle, precio, tipo, classificacion, usuario);

                        if(tip.equals("comida")){
                            if(product.getType() == "Comida"){
                                products.add(product);
                            }
                        } else if(tip.equals("juguetes")){
                            if(product.getType().equals("Juguetes")){
                                products.add(product);
                            }
                        } else if(tip.equals("accesorios")){
                            if(product.getType().equals("Accesorios")){
                                products.add(product);
                            }
                        } else if(tip.equals("limpieza")){
                            if(product.getType().equals("Limpieza")){
                                products.add(product);
                            }
                        } else if(tip.equals("medicamentos")){
                            if(product.getType().equals("Medicamentos")){
                                products.add(product);
                            }
                        }else {
                            products.add(product);
                        }
                    }

                }
                CustomAdapter customAdapter = new CustomAdapter();
                list.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent (getBaseContext() , ProductDetailsActivity.class);
                Bundle bs = new Bundle();
                Product p = products.get(position);
                bs.putSerializable("product", p);
                intent.putExtra("data",bs);
                startActivity(intent);
            }
        });

    }
    
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.search_bar){
            Toast.makeText(getBaseContext(), "Oprimio buscar", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(this, HomePageActivity.class));
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_pet:
                String message = "Es necesario activar el permiso para acceder al GPS.";
                String permission = PermissionsManagerPT.FINE_LOCATION_PERMISSION_NAME;
                if(PermissionsManagerPT.askForPermission(this, permission, message, PermissionsManagerPT.LOCATION_PERMISSION_ID)){
                    Intent intent = new Intent(SearchResultsMainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_walkers:
                startActivity(new Intent(this, WalkersListActivity.class));
                break;
            case R.id.nav_chat:
                Toast.makeText(getApplicationContext(), "Selecciono mis chats", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PermissionsManagerPT.LOCATION_PERMISSION_ID:
                if (PermissionsManagerPT.onRequestPermissionsResult(grantResults, this, "Es necesario activar el permiso para acceder al mapa.")) {
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return products.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.static_rv_item,null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.Product_image);
            TextView text_title = (TextView) convertView.findViewById(R.id.Product_title);
            TextView text_details = (TextView) convertView.findViewById(R.id.Product_details);

            text_title.setText(products.get(position).getTitle());
            text_details.setText(products.get(position).getPrice());
            Glide.with(SearchResultsMainActivity.this).load(products.get(position).getImage()).into(imageView);

            return convertView;
        }
    }

}
package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.Model.Product;
import com.example.pettracker.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_main);
        toolBar = findViewById(R.id.toolbar2);
        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView = findViewById(R.id.nav_view2);
        setSupportActionBar(toolBar);

        try {
            initArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        list = findViewById(R.id.results);
        CustomAdapter customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);
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

    private void initArray() throws JSONException {
        products = new ArrayList<Product>();
        JSONObject json = new JSONObject(loadJSONFromAsset());
        JSONArray paisesJson = json.getJSONArray("products");
        for (int i =0 ; i < paisesJson.length();i++){
            JSONObject objeJson = paisesJson.getJSONObject(i);
            Product p = new Product(objeJson.getString("title"), objeJson.getString("image"),objeJson.getString("about"), objeJson.getString("price"), "accesorios","perros", null);
            products.add(p);
        }
    }

    public String loadJSONFromAsset() {
        String json =null;
        try{
            InputStream is = this.getAssets().open("products.json");
            int size = is.available();
            byte [] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json=new String (buffer, "UTF-8");
        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
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
            int imageId = getResources().getIdentifier(products.get(position).getImage() , "drawable", getPackageName());
            imageView.setImageResource(imageId);
            return convertView;
        }
    }
}
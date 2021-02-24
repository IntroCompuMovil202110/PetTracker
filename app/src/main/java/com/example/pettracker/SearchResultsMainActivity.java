package com.example.pettracker;

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

import com.google.android.material.navigation.NavigationView;

public class SearchResultsMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    int[] IMANENES = {R.drawable.bone, R.drawable.products, R.drawable.dog_collar, R.drawable.search_results,R.drawable.bone, R.drawable.products, R.drawable.dog_collar, R.drawable.search_results,R.drawable.bone, R.drawable.products, R.drawable.dog_collar, R.drawable.search_results};

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
        CustomAdapter customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(),ProductDetailsActivity.class));
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
                startActivity(new Intent(this,HomePageActivity.class));
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_pet:
                Toast.makeText(getApplicationContext(), "Selecciono mis mascotas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_shops:
                Toast.makeText(getApplicationContext(), "Selecciono mis compras", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_chat:
                Toast.makeText(getApplicationContext(), "Selecciono mis chats", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return IMANENES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
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

            imageView.setImageResource(IMANENES[position]);
            text_title.setText("Hermoso collar");
            text_details.setText("$58.000");
            return convertView;
        }
    }
}
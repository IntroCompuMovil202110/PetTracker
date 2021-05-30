package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.Model.Information;
import com.example.pettracker.Model.Product;
import com.example.pettracker.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterTextureView;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolBar;
    CardView comida;
    CardView juguetes;
    CardView accesorios;
    CardView limpieza;
    CardView medicamentos;
    CardView todos;
    CardView perros;
    CardView gatos;
    CardView veterinarias;
    CardView informacion;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolBar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolBar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        comida = findViewById(R.id.comida);
        juguetes = findViewById(R.id.juguetes);
        accesorios = findViewById(R.id.accesorios);
        limpieza = findViewById(R.id.limpieza);
        medicamentos = findViewById(R.id.meds);
        todos = findViewById(R.id.todos);

        perros = findViewById(R.id.perros);
        gatos = findViewById(R.id.gatos);
        veterinarias = findViewById(R.id.veterinarias);
        informacion = findViewById(R.id.informacion);

        comida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultsMainActivity.class);
                intent.putExtra("tipo", "comida");
                startActivity(intent);
            }
        });

        juguetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultsMainActivity.class);
                intent.putExtra("tipo", "juguetes");
                startActivity(intent);
            }
        });

        accesorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultsMainActivity.class);
                intent.putExtra("tipo", "accesorios");
                startActivity(intent);
            }
        });

        limpieza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultsMainActivity.class);
                intent.putExtra("tipo", "limpieza");
                startActivity(intent);
            }
        });

        medicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultsMainActivity.class);
                intent.putExtra("tipo", "medicamentos");
                startActivity(intent);
            }
        });

        todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultsMainActivity.class);
                intent.putExtra("tipo", "todos");
                startActivity(intent);
            }
        });

        perros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultAnimal.class);
                intent.putExtra("tipo", "perros");
                startActivity(intent);
            }
        });

        gatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getBaseContext() , SearchResultAnimal.class);
                intent.putExtra("tipo", "gatos");
                startActivity(intent);
            }
        });

        veterinarias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), BuscarVeterinariaActivity.class));
            }
        });

        informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FlutterActivity.createDefaultIntent(getBaseContext()));
            }
        });

    }

    public void ingresarInformacion(){
        String titulo = "Tips para pasear a tu perro en cuarentena";
        String contenido = "1. Lávate las manos antes de salir.\n" +
                "Es importante que laves tus manos de forma adecuada con agua y jabón antibacterial durante al menos 20 segundos.\n" +
                "2. Sólo una persona debe salir.\n" +
                "Salir con la mascota debe hacerse en el menor tiempo posible para que ésta haga sus necesidades fisiológicas. \n" +
                "3. Evita socializar con otras personas y/o mascotas.\n" +
                "Mantén una distancia responsable de al menos 1.5 metros\n" +
                "4. Aseo al regresar a casa.\n" +
                "Limpia las patas de tu perro con agua o toallitas desinfectantes sin alcohol. Después lávate bien las manos y cámbiate de ropa.\n" +
                "5. Sal a pasear temprano en la mañana y/o en la noche.\n" +
                "Se sugiere salir a la calle en horarios donde sea más probable que haya menos gente transitando.";

        Information information = new Information(titulo, contenido);

        databaseReference.push().setValue(information);

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
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, MiPerfilActivity.class));
                break;
            case R.id.nav_pet:
                String message = "Es necesario activar el permiso para acceder al GPS.";
                String permission = PermissionsManagerPT.FINE_LOCATION_PERMISSION_NAME;
                if(PermissionsManagerPT.askForPermission(this, permission, message, PermissionsManagerPT.LOCATION_PERMISSION_ID)){
                    Intent intent = new Intent(HomePageActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_walkers:
                startActivity(new Intent(this, WalkersListActivity.class));
                break;
            case R.id.nav_chat:
                startActivity(new Intent(this, ChatListActivity.class));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class ));
                finish();
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

    public void SearchKeyWord(View v) {
        startActivity(new Intent(this, SearchResultsMainActivity.class));
    }
}
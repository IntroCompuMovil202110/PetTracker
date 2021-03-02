package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.security.auth.Subject;

public class MisPublicacionesActivity extends AppCompatActivity {
    /*String[] nombresProductos = {"hp,dsa,das,das"};
    String[] fotosProductos =  {"hp,dsa,das,das"};
    String[] precioProductos = {"hp,dsa,das,das"};
    String [] detalleProductos = {"hp,dsa,das,das"};*/
    ArrayList<Product> products;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        products = new ArrayList<>();
        loadPublications();
        CustomPublicationsAdapter adapter = new CustomPublicationsAdapter(this, R.layout.publicacion_item, products);
        list = (ListView) findViewById(R.id.listMisPublicaciones);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), PersonalChatActivity.class);
                Product p = products.get(position);
                intent.putExtra("nombre", p.getTitle());
                startActivity(intent);
            }
        });
    }

    private void loadPublications() {
        products.add(new Product("Agility Gold Razas Grandes 3kg", "@drawable/producto1", "Comida para perros de 3kg para razas grandes, tambien se le puede dar a razas medianas.", "$38 500"));
        products.add(new Product("Saco Snoppy Talla S", "@drawable/producto2", "Saco de la tira animada snoppy talla S. Le sirve a razas medianas como french poodles, pomeranis o schnauzer.",  "$34 900"));
        products.add(new Product("Plato para Gatos", "@drawable/producto3", "Plato para comida o agua. Uselo a su preferencia. De aproximadamente 500 ml.",  "$14 600"));
        products.add(new Product("Correa para perros", "@drawable/producto4", "Correa para perros de cuero sintetico de alta duracion. Muy Buena.", "$6 300"));
        products.add(new Product("Juguete para Gatos", "@drawable/producto5", "Juguete para gatos muy entretenido y duradero. Por experiencia personal se que le fascina a los gatos y hasta tambien a perro juguetones. ",  "$45 200"));
    }

    public void crear(View view){
        startActivity(new Intent(this,PublishProductActivity.class));
    }

}

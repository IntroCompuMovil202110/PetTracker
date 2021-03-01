package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class MisPublicacionesActivity extends AppCompatActivity {
    String[] nombresProductos;
    String[] fotosProductos;
    String[] precioProductos;
    String [] detalleProductos;
    ArrayList<Product> productList;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        list  = findViewById(R.id.listMisPublicaciones);
        productList = new ArrayList<Product>();
        productList.add(new Product(nombresProductos[0],fotosProductos[0],detalleProductos[0], precioProductos[0]));
        productList.add(new Product(nombresProductos[1],fotosProductos[1],detalleProductos[1], precioProductos[1]));
        productList.add(new Product(nombresProductos[2],fotosProductos[2],detalleProductos[2], precioProductos[2]));
        productList.add(new Product(nombresProductos[3],fotosProductos[3],detalleProductos[3], precioProductos[3]));

        CustomPublicationsAdapter customAdapter = new CustomPublicationsAdapter(this,R.layout.publicacion_item,productList);
        list.setAdapter(customAdapter);
        list = (ListView) findViewById(R.id.listMisPublicaciones);
        list.setAdapter(customAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent = new Intent(getBaseContext(), walkerChat.class);
                Paseador p = walkersList.get(position);
                intent.putExtra("nombre", p.getNombre());
                startActivity(intent);*/
            }
        });
    }
}
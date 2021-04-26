package com.example.pettracker.Controller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;
import com.example.pettracker.R;

import java.util.ArrayList;

public class CustomPublicationsAdapter extends ArrayAdapter<Product> {
    ArrayList<Product> products;
    Context context;
    int resource;
    public CustomPublicationsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> products){
        super(context, resource, products);
        this.context = context;
        this.resource = resource;
        this.products = products;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.publicacion_item,null,true);
        }
        Product p = getItem(position);
        ImageView pubImg = (ImageView) convertView.findViewById(R.id.imagenPublicacion);
        TextView pubName = (TextView) convertView.findViewById(R.id.nombrePublicacion);
        TextView pubPrecio = (TextView) convertView.findViewById(R.id.precioPublicacion);
        pubName.setText(products.get(position).getTitle());
        return convertView;
    }
}

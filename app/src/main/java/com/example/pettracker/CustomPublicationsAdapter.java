package com.example.pettracker;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;

import java.util.ArrayList;

public class CustomPublicationsAdapter implements ListAdapter {
    ArrayList<Product> publications;
    Context context;
    int resource;

    public CustomPublicationsAdapter(@NonNull Context context, int resource, ArrayList<Product> publications){
        this.context = context;
        this.resource = resource;
        this.publications = publications;
    }

    @Override
    public boolean areAllItemsEnabled(){
        return false;
    }

    @Override
    public boolean isEnabled(int position){
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer){

    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer){

    }
    @Override
    public int getCount(){
        return publications.size();
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public boolean hasStableIds(){
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.publicacion_item,null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            Product p = publications.get(position);
            ImageView PubImg = (ImageView) convertView.findViewById(R.id.imagenPublicacion);
            TextView titulo = (TextView) convertView.findViewById(R.id.nombrePublicacion);
            TextView precio = (TextView) convertView.findViewById(R.id.precioPublicacion);
            titulo.setText(publications.get(position).getTitle());
            return convertView;
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }
    @Override
    public int getViewTypeCount(){
        return publications.size();
    }
    @Override
    public boolean isEmpty(){
        return false;
    }
}


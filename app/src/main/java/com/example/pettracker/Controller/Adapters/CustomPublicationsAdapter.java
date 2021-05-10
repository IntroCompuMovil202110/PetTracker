package com.example.pettracker.Controller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Product;
import com.example.pettracker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomPublicationsAdapter extends RecyclerView.ViewHolder {

    private ImageView imagen;
    private TextView nombre;
    private TextView precio;
    private CardView gridLayout;

    public CustomPublicationsAdapter(View itemView) {
        super(itemView);

        imagen = itemView.findViewById(R.id.imagenPublicacion);
        nombre = itemView.findViewById(R.id.nombrePublicacion);
        precio = itemView.findViewById(R.id.precioPublicacion);
        gridLayout = itemView.findViewById(R.id.gridLayout);
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getPrecio() {
        return precio;
    }

    public void setPrecio(TextView precio) {
        this.precio = precio;
    }

    public CardView getGridLayout() {
        return gridLayout;
    }

    public void setGridLayout(CardView gridLayout) {
        this.gridLayout = gridLayout;
    }

    /*public CustomPublicationsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> products){
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
    }*/
}

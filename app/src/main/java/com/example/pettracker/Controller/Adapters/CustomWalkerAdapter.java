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

import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;

import java.util.ArrayList;

public class CustomWalkerAdapter extends ArrayAdapter<Usuario> {
    ArrayList<Usuario> walkers;
    Context context;
    int resource;
    public CustomWalkerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Usuario> walkers){
        super(context, resource, walkers);
        this.context = context;
        this.resource = resource;
        this.walkers = walkers;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.walker_item,null,true);
        }
        Usuario p = getItem(position);
        ImageView walkerImg = (ImageView) convertView.findViewById(R.id.walkerImg);
        TextView walkerName = (TextView) convertView.findViewById(R.id.txtName);
        walkerName.setText(walkers.get(position).getNombre());
        return convertView;
    }
}

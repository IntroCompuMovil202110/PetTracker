package com.example.pettracker.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pettracker.Model.Message;
import com.example.pettracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<HolderMessage> {

    private List<ReceiveMessage> listMessages = new ArrayList<>();
    private Context c;

    public AdapterMessage(Context c) {
        this.c = c;
    }

    public void addMessage(ReceiveMessage m){
        listMessages.add(m);
        notifyItemInserted(listMessages.size());
    }

    @Override
    public HolderMessage onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_msg, parent, false);

        return new HolderMessage(v);
    }

    @Override
    public void onBindViewHolder(HolderMessage holder, int position) {
        holder.getMessage().setText(listMessages.get(position).getMessage());

        if(listMessages.get(position).getType_message().equals("2")){
            holder.getImageSend().setVisibility(View.VISIBLE);
            holder.getMessage().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMessages.get(position).getUrlImage()).into(holder.getImageSend());
        }
        else if(listMessages.get(position).getType_message().equals("1")){
            holder.getImageSend().setVisibility(View.GONE);
            holder.getMessage().setVisibility(View.VISIBLE);
        }

        Long hourCode = listMessages.get(position).getHour();
        Date d = new Date(hourCode);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        holder.getHour().setText(sdf.format(d));
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }
}

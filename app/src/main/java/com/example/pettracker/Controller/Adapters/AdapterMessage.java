package com.example.pettracker.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.HolderMessage;
import com.example.pettracker.Controller.UsuarioDAO;
import com.example.pettracker.Model.Firebase.LMessage;
import com.example.pettracker.Model.Message;
import com.example.pettracker.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<HolderMessage> {

    private List<LMessage> listMessages = new ArrayList<>();
    private Context c;

    public AdapterMessage(Context c) {
        this.c = c;
    }

    public int addMessage(LMessage m){
        listMessages.add(m);
        int position = listMessages.size()-1;
        notifyItemInserted(listMessages.size());
        return position;
    }

    public void updateMessage(int position, LMessage lmessage){
        listMessages.set(position,lmessage);
        notifyItemChanged(position);
    }

    @Override
    public HolderMessage onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if(viewType==1){
            v = LayoutInflater.from(c).inflate(R.layout.card_view_msg, parent, false);
        }else{
            v = LayoutInflater.from(c).inflate(R.layout.card_view_msg, parent, false);
        }

        return new HolderMessage(v);
    }

    @Override
    public void onBindViewHolder(HolderMessage holder, int position) {

        LMessage lMessage = listMessages.get(position);
       // LUsuario lUsuario = lMessage.getlUser();

        holder.getMessage().setText(lMessage.getMessage().getMessage());

        if(lMessage.getMessage().isHasPicture()){
            holder.getImageSend().setVisibility(View.VISIBLE);
            holder.getMessage().setVisibility(View.VISIBLE);
            Glide.with(c).load(lMessage.getMessage().getUrlPicture()).into(holder.getImageSend());
        }
        else {
            holder.getImageSend().setVisibility(View.GONE);
            holder.getMessage().setVisibility(View.VISIBLE);
        }

        holder.getHour().setText(lMessage.dateCreationMessage());
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    @Override
    public int getItemViewType(int position){
        if(listMessages.get(position).getlUser()!= null){
            if(listMessages.get(position).getlUser().getKey().equals(UsuarioDAO.getInstancia().getKeyUsuario())){
                return -1;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    public Message getLastMessage(){
        return listMessages.get(listMessages.size()).getMessage();
    }
}

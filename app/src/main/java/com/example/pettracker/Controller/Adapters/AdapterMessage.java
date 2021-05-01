package com.example.pettracker.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.Holders.HolderMessage;
import com.example.pettracker.Controller.UsuarioDAO;
import com.example.pettracker.Model.Firebase.LMessage;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Message;
import com.example.pettracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<HolderMessage> {

    private List<LMessage> listMessages = new ArrayList<>();
    private Context c;
    private String url;

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
            v = LayoutInflater.from(c).inflate(R.layout.card_view_msg_emisor, parent, false);
        }else{
            v = LayoutInflater.from(c).inflate(R.layout.card_view_msg_receptor, parent, false);
        }

        return new HolderMessage(v);
    }

    @Override
    public void onBindViewHolder(HolderMessage holder, int position) {

        LMessage lMessage = listMessages.get(position);
        LUsuario lUsuario = lMessage.getlUser();

        if(lUsuario!=null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(lMessage.getMessage().getEmisorKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Glide.with(c).load(snapshot.child("fotoPerfilURL").getValue().toString()).into(holder.getPicture());
                    } else {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("walkers");
                        ref.child(lMessage.getMessage().getEmisorKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Glide.with(c).load(dataSnapshot.child("fotoPerfilURL").getValue().toString()).into(holder.getPicture());
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

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

    private void getProfUrl(String key){
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    @Override
    public int getItemViewType(int position){
        if(listMessages.get(position).getlUser()!= null){
            if(listMessages.get(position).getlUser().getKey().equals(UsuarioDAO.getInstancia().getKeyUsuario())){
                return 1;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }
}

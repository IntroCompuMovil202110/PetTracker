package com.example.pettracker.Controller;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView profilePictureMessage;
    private TextView nameMessage;
    private CardView cardLayout;

    public ChatViewHolder(View itemView){
        super(itemView);

        profilePictureMessage = itemView.findViewById(R.id.profilePictureMessage);
        nameMessage = itemView.findViewById(R.id.nameMessage);
        cardLayout = itemView.findViewById(R.id.cardLayout);
    }

    public CircleImageView getProfilePictureMessage() {
        return profilePictureMessage;
    }

    public void setProfilePictureMessage(CircleImageView profilePictureMessage) {
        this.profilePictureMessage = profilePictureMessage;
    }

    public TextView getNameMessage() {
        return nameMessage;
    }

    public void setNameMessage(TextView nameMessage) {
        this.nameMessage = nameMessage;
    }

    public CardView getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardView cardLayout) {
        this.cardLayout = cardLayout;
    }
}

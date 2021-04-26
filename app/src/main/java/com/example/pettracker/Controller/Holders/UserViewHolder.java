package com.example.pettracker.Controller.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView profilePicture;
    private TextView name;
    private CardView priCard;

    public UserViewHolder(View itemView) {
        super(itemView);

        profilePicture = itemView.findViewById(R.id.profileCardUser);
        name = itemView.findViewById(R.id.nameCardUser);
        priCard = itemView.findViewById(R.id.priLayout);
    }

    public CircleImageView getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(CircleImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public CardView getPriLayout() {
        return priCard;
    }

    public void setPriCard(CardView priCard) {
        this.priCard = priCard;
    }
}

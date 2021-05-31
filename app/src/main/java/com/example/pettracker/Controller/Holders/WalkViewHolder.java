package com.example.pettracker.Controller.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalkViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView profilePicture;
    private TextView name;
    private TextView status;
    private CardView priCard;
    private Button btnWalkAccept;

    public WalkViewHolder(View itemView) {
        super(itemView);

        profilePicture = itemView.findViewById(R.id.profileCardUserWalk);
        name = itemView.findViewById(R.id.nameCardUserWalk);
        priCard = itemView.findViewById(R.id.priLayoutWalk);
        status = itemView.findViewById(R.id.walkStatus);
        btnWalkAccept = itemView.findViewById(R.id.btnAceptarPaseo);
    }

    public CircleImageView getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(CircleImageView profilePicture) { this.profilePicture = profilePicture; }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public CardView getPriCard() {
        return priCard;
    }

    public void setPriCard(CardView priCard) {
        this.priCard = priCard;
    }

    public TextView getStatus() {
        return status;
    }

    public void setStatus(TextView status) {
        this.status = status;
    }

    public Button getBtnWalkAccept() {
        return btnWalkAccept;
    }

    public void setBtnWalkAccept(Button btnWalkAccept) {
        this.btnWalkAccept = btnWalkAccept;
    }
}

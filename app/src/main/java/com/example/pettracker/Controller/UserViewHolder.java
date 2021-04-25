package com.example.pettracker.Controller;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pettracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView profilePicture;
    private TextView name;
    private LinearLayout priLayout;

    public UserViewHolder(View itemView) {
        super(itemView);

        profilePicture = itemView.findViewById(R.id.profileCardUser);
        name = itemView.findViewById(R.id.nameCardUser);
        priLayout = itemView.findViewById(R.id.priLayout);
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

    public LinearLayout getPriLayout() {
        return priLayout;
    }

    public void setPriLayout(LinearLayout priLayout) {
        this.priLayout = priLayout;
    }
}

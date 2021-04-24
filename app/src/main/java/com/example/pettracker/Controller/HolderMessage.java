package com.example.pettracker.Controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pettracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderMessage extends RecyclerView.ViewHolder {

    private TextView message;
    private TextView hour;
    private CircleImageView picture;
    private ImageView imageSend;

    public HolderMessage(View itemView) {
        super(itemView);

        message = (TextView) itemView.findViewById(R.id.contentMessageCard);
        hour = (TextView) itemView.findViewById(R.id.hourMessageCard);
        picture = (CircleImageView) itemView.findViewById(R.id.profilePictureCard);
        imageSend = (ImageView) itemView.findViewById(R.id.imageSendCard);
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public TextView getHour() {
        return hour;
    }

    public void setHour(TextView hour) {
        this.hour = hour;
    }

    public CircleImageView getPicture() {
        return picture;
    }

    public void setPicture(CircleImageView picture) {
        this.picture = picture;
    }

    public ImageView getImageSend() {
        return imageSend;
    }

    public void setImageSend(ImageView imageSend) {
        this.imageSend = imageSend;
    }
}

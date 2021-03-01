package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListActivity extends AppCompatActivity {

    private CircleImageView profilePictureMessage;
    private TextView nameMessage;
    private TextView contentMessage;
    private CardView chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        profilePictureMessage = (CircleImageView) findViewById(R.id.profilePictureMessage);
        nameMessage = (TextView) findViewById(R.id.nameMessage);
        contentMessage = (TextView) findViewById(R.id.contentMessage);
        chatList = (CardView) findViewById(R.id.chatList);

        chatList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), PersonalChatActivity.class));
            }
        });
    }
}
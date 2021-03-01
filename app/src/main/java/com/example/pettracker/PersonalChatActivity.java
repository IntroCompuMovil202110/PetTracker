package com.example.pettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChatActivity extends AppCompatActivity {

    private CircleImageView profilePicture;
    private TextView name;
    private RecyclerView rvMessages;
    private EditText textMsg;
    private FloatingActionButton buttonMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        profilePicture = (CircleImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        textMsg = (EditText) findViewById(R.id.textMsg);
        buttonMsg = (FloatingActionButton) findViewById(R.id.buttonMsg);
    }
}
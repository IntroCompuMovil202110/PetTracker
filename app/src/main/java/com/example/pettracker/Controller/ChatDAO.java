package com.example.pettracker.Controller;

import com.example.pettracker.Model.Message;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatDAO {

    private static ChatDAO mensajeriaDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenceMensajeria;

    public static ChatDAO getInstance(){
        if(mensajeriaDAO==null) mensajeriaDAO = new ChatDAO();
        return mensajeriaDAO;
    }

    private ChatDAO(){
        database = FirebaseDatabase.getInstance();
        referenceMensajeria = database.getReference("messages");
    }

    public void newMessage(String keyEmissor, String keyReceptor, Message message){
        DatabaseReference referenceEmissor = referenceMensajeria.child(keyEmissor).child(keyReceptor);
        DatabaseReference referenceReceptor = referenceMensajeria.child(keyReceptor).child(keyEmissor);
        referenceEmissor.push().setValue(message);
        referenceReceptor.push().setValue(message);
    }
}

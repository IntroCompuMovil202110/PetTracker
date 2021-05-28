package com.example.pettracker.Controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pettracker.Model.Message;
import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.example.pettracker.View.ChatListActivity;
import com.example.pettracker.View.PersonalChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NotificationJobIntentService extends JobIntentService {
    private static final int JOB_ID = 12;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference availables, myRef;
    public static String CHANNEL_ID = "Notificaciones";
    public static final String PATH_AVAILABLE="messages/";
    private Map<String, String> mensajes;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationJobIntentService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        availables = firebaseDatabase.getReference(PATH_AVAILABLE+firebaseAuth.getUid());
        mensajes = new HashMap<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent){
        availables.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren())
                {
                    for(DataSnapshot de: d.getChildren())
                    {
                        Message mensaje = de.getValue(Message.class);
                        mensajes.put(de.getKey(), mensaje.getEmisorKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        availables.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    for(DataSnapshot d: dataSnapshot.getChildren())
                    {
                        for(DataSnapshot de: d.getChildren())
                        {
                            Message mensaje = de.getValue(Message.class);
                            if(!mensajes.containsKey(de.getKey())){
                                if(!mensaje.getEmisorKey().equalsIgnoreCase(firebaseAuth.getUid())){
                                    mensajes.put(de.getKey(), mensaje.getEmisorKey());
                                    BuildNotification(d.getKey());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void BuildNotification(String id)
    {

        myRef=firebaseDatabase.getReference("users/"+id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                if( user == null){
                    BuildNotificationPaseador(id);
                    return;
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationJobIntentService.this, CHANNEL_ID);
                mBuilder.setSmallIcon(R.drawable.bell);
                mBuilder.setContentTitle("Nuevo mensaje");
                mBuilder.setContentText(user.getNombre() +" te acaba de enviar un mensaje. Haz click verlo");
                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                Intent intent = new Intent(NotificationJobIntentService.this, PersonalChatActivity.class);
                intent.putExtra("keyReceptor", id);
                intent.putExtra("receptorName", user.getNombre()+" "+user.getApellido());
                intent.putExtra("profileURL", user.getFotoPerfilURL());



                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationJobIntentService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true); //Remueve la notificación cuando se toca*/

                int notificationId = 001;
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationJobIntentService.this);
                notificationManager.notify(notificationId, mBuilder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void BuildNotificationPaseador(String id)
    {

        myRef=firebaseDatabase.getReference("walkers/"+id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Paseador user = dataSnapshot.getValue(Paseador.class);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationJobIntentService.this, CHANNEL_ID);
                mBuilder.setSmallIcon(R.drawable.bell);
                mBuilder.setContentTitle("Nuevo mensaje");
                mBuilder.setContentText(user.getNombre() +" te acaba de enviar un mensaje. Haz click verlo");
                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                Intent intent = new Intent(NotificationJobIntentService.this, PersonalChatActivity.class);
                intent.putExtra("keyReceptor", id);
                intent.putExtra("receptorName", user.getNombre()+" "+user.getApellido());
                intent.putExtra("profileURL", user.getFotoPerfilURL());



                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationJobIntentService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true); //Remueve la notificación cuando se toca*/

                int notificationId = 001;
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationJobIntentService.this);
                notificationManager.notify(notificationId, mBuilder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
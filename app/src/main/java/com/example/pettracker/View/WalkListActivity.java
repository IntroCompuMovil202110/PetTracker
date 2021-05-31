package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pettracker.Controller.Holders.UserViewHolder;
import com.example.pettracker.Controller.Holders.WalkViewHolder;
import com.example.pettracker.Controller.PermissionsManagerPT;
import com.example.pettracker.Model.Firebase.LUsuario;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.Model.Walk;
import com.example.pettracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WalkListActivity extends AppCompatActivity {

    private RecyclerView walk;
    private FirebaseRecyclerAdapter adapter;
    private ProgressDialog loadingScreen;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceL;
    private DatabaseReference databaseReferenceE;
    private DatabaseReference databaseReferenceU;
    private DatabaseReference databaseReferenceA;
    private DatabaseReference databaseReferenceW;
    private DatabaseReference databaseReferenceM;
    private FirebaseDatabase database;
    private Usuario currentUser;

    private FusedLocationProviderClient locationProvider;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private boolean approved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        approved = false;
        setContentView(R.layout.activity_walk_list);
        walk = findViewById(R.id.walkList);


        currentUser = new Usuario();
        loadingScreen = new ProgressDialog(WalkListActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        walk.setLayoutManager(linearLayoutManager);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        setUserLocation();
        getUserType();
    }

    public void getUserType(){
        databaseReference = database.getReference("users/" + mAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingScreen.setTitle("Cargando.");
                loadingScreen.setMessage("Por favor espere.");
                loadingScreen.setCancelable(false);
                loadingScreen.show();
                // Load the user data
                Usuario user = dataSnapshot.getValue(Usuario.class);
                if (user.getRol().equalsIgnoreCase("Cliente")){
                    saveWalk();
                    getWalkHistory();
                } else {
                    getPendingWalks();
                }
                loadingScreen.dismiss();
                approved = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Query error " + databaseError.toException());
            }
        });
    }

    public void getWalkHistory(){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("walk").orderByChild("clientID").equalTo(mAuth.getCurrentUser().getUid());

        FirebaseRecyclerOptions<Walk> options =
                new FirebaseRecyclerOptions.Builder<Walk>()
                        .setQuery(query, Walk.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Walk, WalkViewHolder>(options) {
            @Override
            public WalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_walks, parent, false);
                view.findViewById(R.id.btnAceptarPaseo).setVisibility(View.INVISIBLE);
                return new WalkViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(WalkViewHolder holder, int position, Walk model) {
                getUserInfo(model.getClientID());
                try {
                    Thread.sleep(650);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Glide.with(WalkListActivity.this)
                        .load(currentUser.getFotoPerfilURL())
                        .into(holder.getProfilePicture());
                holder.getStatus().setText(model.getStatus());
                holder.getName().setText(currentUser.getNombre() + " " + currentUser.getApellido());
                holder.getPriCard().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReferenceM = database.getReference("walk/" + model.getWalkID());
                        databaseReferenceM.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                loadingScreen.setTitle("Cargando.");
                                loadingScreen.setMessage("Por favor espere.");
                                loadingScreen.setCancelable(false);
                                loadingScreen.show();
                                Walk auxWalk = dataSnapshot.getValue(Walk.class);
                                if(auxWalk.getStatus().equalsIgnoreCase("Aceptado")){
                                    Intent intent = new Intent(WalkListActivity.this, MapsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("clientID", auxWalk.getClientID());
                                    bundle.putString("walkID", auxWalk.getWalkID());
                                    bundle.putString("walkerID", auxWalk.getWalkerID());
                                    intent.putExtra("bundle", bundle);
                                    startActivity(intent);
                                }
                                loadingScreen.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.err.println("Query error " + databaseError.toException());
                            }
                        });
                    }
                });
            }
        };

        walk.setAdapter(adapter);
        adapter.startListening();
    }

    public void getPendingWalks(){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("walk").orderByChild("status").equalTo("Pendiente");

        FirebaseRecyclerOptions<Walk> options =
                new FirebaseRecyclerOptions.Builder<Walk>()
                        .setQuery(query, Walk.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Walk, WalkViewHolder>(options) {
            @Override
            public WalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_walks, parent, false);
                view.findViewById(R.id.btnAceptarPaseo).setVisibility(View.VISIBLE);
                return new WalkViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(WalkViewHolder holder, int position, Walk model) {
                getUserInfo(model.getClientID());
                try {
                    Thread.sleep(650);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Glide.with(WalkListActivity.this)
                        .load(currentUser.getFotoPerfilURL())
                        .into(holder.getProfilePicture());
                holder.getStatus().setText(model.getStatus());
                holder.getName().setText(currentUser.getNombre() + " " + currentUser.getApellido());

                holder.getBtnWalkAccept().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReferenceE = database.getReference("walk/" + model.getWalkID());
                        databaseReferenceE.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                loadingScreen.setTitle("Cargando.");
                                loadingScreen.setMessage("Por favor espere.");
                                loadingScreen.setCancelable(false);
                                loadingScreen.show();
                                // Load the user data
                                Walk myWalk = dataSnapshot.getValue(Walk.class);
                                myWalk.setWalkerID(mAuth.getCurrentUser().getUid());
                                myWalk.setStatus("Aceptado");
                                databaseReferenceE.setValue(myWalk);
                                loadingScreen.dismiss();
                                Intent intent = new Intent(WalkListActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("clientID", myWalk.getClientID());
                                bundle.putString("walkID", myWalk.getWalkID());
                                bundle.putString("walkerID", myWalk.getWalkerID());
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.err.println("Query error " + databaseError.toException());
                            }
                        });
                    }
                });

                final LUsuario lUsuario = new LUsuario(model.getClientID(),currentUser);

                holder.getPriCard().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WalkListActivity.this, PersonalChatActivity.class);
                        intent.putExtra("keyReceptor", lUsuario.getKey());
                        intent.putExtra("receptorName", lUsuario.getUser().getNombre() + " " + lUsuario.getUser().getApellido());
                        intent.putExtra("profileURL", lUsuario.getUser().getFotoPerfilURL());
                        startActivity(intent);
                    }
                });
            }
        };

        walk.setAdapter(adapter);
        adapter.startListening();
    }

    public void getUserInfo(String id){
        databaseReferenceU = database.getReference("users/" + id);
        databaseReferenceU.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingScreen.setTitle("Cargando.");
                loadingScreen.setMessage("Por favor espere.");
                loadingScreen.setCancelable(false);
                loadingScreen.show();
                // Load the user data
                Usuario user = dataSnapshot.getValue(Usuario.class);
                currentUser = user;
                loadingScreen.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Query error " + databaseError.toException());
            }
        });
    }

    public void saveWalk(){
        databaseReferenceL = database.getReference("walk/");
        databaseReferenceL.orderByChild("clientID").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean save = true;
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Walk myWalk = ds.getValue(Walk.class);
                    if(!myWalk.getStatus().equalsIgnoreCase("Terminado")){
                        save = false;
                    }
                }
                if (save){
                    databaseReferenceW = database.getReference("walk/");
                    String key = databaseReferenceW.push().getKey();
                    databaseReferenceW = database.getReference("walk/" + key);
                    Walk walk = new Walk();
                    walk.setClientID(mAuth.getCurrentUser().getUid());
                    walk.setWalkID(key);
                    walk.setStatus("Pendiente");
                    databaseReferenceW.setValue(walk);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Query error " + databaseError.toException());
            }
        });
    }

    public void setUserLocation(){
        locationProvider = LocationServices.getFusedLocationProviderClient(WalkListActivity.this);
        locationRequest = createLocationRequest();
        checkSettings();
        if(ContextCompat.checkSelfPermission(WalkListActivity.this, PermissionsManagerPT.FINE_LOCATION_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED){
            locationProvider = LocationServices.getFusedLocationProviderClient(WalkListActivity.this);
            locationRequest = createLocationRequest();
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        if(mAuth.getCurrentUser() != null){
                            LatLng actualLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            databaseReferenceA = database.getReference("users/" + mAuth.getCurrentUser().getUid());
                            databaseReferenceA.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario myUser = dataSnapshot.getValue(Usuario.class);
                                    myUser.setLatitude(actualLoc.latitude);
                                    myUser.setLongitude(actualLoc.longitude);
                                    databaseReferenceA.setValue(myUser);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("Mapa", "error en la consulta", databaseError.toException());
                                }
                            });
                        }
                    }
                }
            };
        }
    }

    private void checkSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(WalkListActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(WalkListActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(WalkListActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED: {
                        try{
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(WalkListActivity.this, PermissionsManagerPT.LOCATION_PERMISSION_ID);
                        }
                        catch (IntentSender.SendIntentException sendEx) {
                            System.err.println("LocationAPP" + sendEx.getMessage());
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(WalkListActivity.this, PermissionsManagerPT.FINE_LOCATION_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            Toast.makeText(WalkListActivity.this,"You should enable location.", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PermissionsManagerPT.LOCATION_PERMISSION_ID: {
                if (resultCode == Activity.RESULT_OK) {
                    startLocationUpdates();
                } else {
                    System.err.println("LocationAPP. " + "Location unable");
                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(approved){
            getUserType();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
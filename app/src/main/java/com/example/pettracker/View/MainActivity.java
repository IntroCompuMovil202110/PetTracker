package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pettracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText correo;
    EditText contrasena;
    private FirebaseAuth mAuth;
    Button login;


    public static final String TAG = "FB_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Firebase
        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.campocorreoInicio);
        contrasena = findViewById(R.id.campocontrasenaInicio);
        login = findViewById(R.id.botonIniciarSesion);

        contrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correo.getText().toString();
                String password = contrasena.getText().toString();

                if(validateForm(email, password)){
                    signIn(email, password);
                }
            }
        });

    }

    private boolean validateForm(String email, String password) {
        if(email != null && password != null){
            if(!email.isEmpty() && !password.isEmpty()){
                if (email.contains("@") && email.contains(".com") && password.length() > 5){
                    return true;
                } else{
                    this.correo.setError("Su correo debe estar en formato: correo@correo.com");
                    this.contrasena.setError( "Su contraseña debe tener 6 caracteres");
                }
            }
        }
        return false;
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void registro (View view){
        startActivity(new Intent(this, RegistrationActivity.class));

    }

    private void updateUI (FirebaseUser user){
        if(user != null){
            startActivity(new Intent(this, HomePageActivity.class));

        }else{
            correo.setText("");
            contrasena.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}

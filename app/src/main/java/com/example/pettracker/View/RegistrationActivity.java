package com.example.pettracker.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pettracker.Model.Paseador;
import com.example.pettracker.Model.Usuario;
import com.example.pettracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText nombre;
    EditText apellido;
    EditText correo;
    EditText contrasena;
    EditText telefono;
    EditText direccion;
    Button register;
    Spinner roles;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    String email;
    String password;
    String name;
    String surname;
    String telephone;
    String adress;
    String rol;
    String profileURL;
    String wallpaper;
    String costo;
    Boolean select;


    public static final String TAG = "FB_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        nombre = findViewById(R.id.campoNombre);
        apellido = findViewById(R.id.campoApellido);
        correo = findViewById(R.id.campocorreo);
        contrasena = findViewById(R.id.campocontrasena);
        telefono = findViewById(R.id.campoTelefono);
        direccion = findViewById(R.id.campoDireccion);
        register = findViewById(R.id.botonRegistro);
        roles = findViewById(R.id.rol);

        contrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roles.setAdapter(adapter);
        roles.setOnItemSelectedListener(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = correo.getText().toString();
                password = contrasena.getText().toString();
                name = nombre.getText().toString();
                surname = apellido.getText().toString();
                telephone = telefono.getText().toString();
                adress = direccion.getText().toString();
                profileURL = "https://firebasestorage.googleapis.com/v0/b/pettracker-b3317.appspot.com/o/profileImages%2Fprofile.png?alt=media&token=7dda3a15-a2a1-4864-b7a2-198ba2285741";
                wallpaper = "https://firebasestorage.googleapis.com/v0/b/pettracker-b3317.appspot.com/o/profileImages%2Fwallpaper.jpg?alt=media&token=f6d25017-704c-4d89-899e-33484d38af54";

                if(validateForm(email, password, name, surname, telephone, adress, rol)){
                    register(email, password);
                }
            }
        });
    }

   private boolean validateForm(String email, String password, String name, String surname, String telephone, String address, String rol ) {
        if (email != null && password != null) {
            if (!email.isEmpty() && !password.isEmpty()) {
                if (email.contains("@") && email.contains(".com") ) {
                    return true;
                } else {
                    correo.setError("Su correo debe estar en el formato correo@correo.com");
                }
                if(password.length() > 5){
                    return true;
                } else{
                    contrasena.setError("Su contraseña debe tener 6 caracteres");
                }
            }
        }
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)
                && TextUtils.isEmpty(name) && TextUtils.isEmpty(surname) &&  TextUtils.isEmpty(telephone) && select==false ) {
            correo.setError("Debe ingresar un correo de registro");
            contrasena.setError("Debe ingresar una contraseña");
            nombre.setError("Debe ingresar su nombre");
            apellido.setError("Debe ingresar su apellido");
            telefono.setError("Debe ingresar su teléfono celular ");
            ((TextView)roles.getSelectedView()).setError("Escoga un Rol");

            return false;
        }
        return false;
    }

    private void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            String folder = "users";

                            if(rol.equals("Paseador")){
                                costo = "0";
                                Paseador regis = new Paseador(profileURL, wallpaper, name, surname, email, password, telephone, adress, rol, costo);
                                folder = "walkers";

                                FirebaseDatabase.getInstance().getReference(folder)
                                        .child(user.getUid())
                                        .setValue(regis).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull  Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, "Su Usuario ha sido Creado",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegistrationActivity.this, "Su Registro Fallo",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                    }
                                });
                            } else if (rol.equals("Cliente")){
                                Usuario regis = new Usuario(profileURL, wallpaper, name, surname, email, password, telephone, adress, rol);
                                folder = "users";

                                FirebaseDatabase.getInstance().getReference(folder)
                                        .child(user.getUid())
                                        .setValue(regis).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull  Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, "Su Usuario ha sido Creado",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegistrationActivity.this, "Su Registro Fallo",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       if(parent.getItemAtPosition(position).equals("Escoga un Rol")){
           select = false;
       }
       else {
           rol = parent.getItemAtPosition(position).toString();
           select = true;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
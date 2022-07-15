package com.xtremecorp.socialfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Registrar extends AppCompatActivity {

    EditText correo,nombres,password,apellidos,edad,telefono;
    Button SignUp;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //ActionBar
        //Se agigna titulo y boton de regreso
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirma que el titulo no es null
        //Asigna un titulo
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Conectar las views
        correo = findViewById(R.id.correoR);
        nombres = findViewById(R.id.nombresR);
        password = findViewById(R.id.passwordR);
        apellidos = findViewById(R.id.apellidosR);
        edad = findViewById(R.id.edadR);
        telefono = findViewById(R.id.telefonoR);
        SignUp =  findViewById(R.id.ButtonR);

        //Objeto de FireBase
        firebaseAuth = FirebaseAuth.getInstance();

        SignUp.setOnClickListener(view -> {
            String correo_s = correo.getText().toString();
            String password_s = password.getText().toString();

            //Valida patterns de correo
            if (!Patterns.EMAIL_ADDRESS.matcher(correo_s).matches()){
                correo.setError("Correo no válido");
                correo.setFocusable(true); //hace enfoque en el campo de correo
            } else if (password_s.length()<6){
                password.setError("Contraseña no válida");
                password.setFocusable(true);
            }else
                registar(correo_s, password_s);
        });
    }

    //Crear metodo registrar para fire base usuario
    private void registar(String correo_s, String password_s) {
        firebaseAuth.createUserWithEmailAndPassword(correo_s,password_s)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Valida si el registro fue exitoso
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Datos a registrar en String
                            String correo_s = correo.getText().toString();
                            String password_s = password.getText().toString();
                            String nombres_s = nombres.getText().toString();
                            String apellidos_s = apellidos.getText().toString();
                            String edad_s = edad.getText().toString();
                            String telefono_s = telefono.getText().toString();

                            //Se obtiene uid de fare base
                            String uid = user.getUid();

                            //Creamos hashmap para mandar datos a firebase
                            HashMap<Object, String> DatosUsuario = new HashMap<>();

                            //insertamos registros en el hashmap
                            DatosUsuario.put("uid", uid);
                            DatosUsuario.put("correo", correo_s);
                            DatosUsuario.put("password", password_s);
                            DatosUsuario.put("nombres", nombres_s);
                            DatosUsuario.put("apellidos", apellidos_s);
                            DatosUsuario.put("edad", edad_s);
                            DatosUsuario.put("telefono", telefono_s);

                            DatosUsuario.put("imagen", "");

                            //Instancia de base de datos
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //Crear base de datos
                            DatabaseReference reference = database.getReference("USUARIOS_DE_APP");
                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(Registrar.this, "Se registró correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registrar.this, Inicio.class));
                        }else {
                            Toast.makeText(Registrar.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registrar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
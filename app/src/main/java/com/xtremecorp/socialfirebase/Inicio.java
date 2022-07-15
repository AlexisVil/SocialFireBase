package com.xtremecorp.socialfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Inicio extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference BaseDeDatos;

    ImageView FotoDePerfil;
    TextView UidPerfil,CorreoPerfil, NombresPerfil;


    Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //ActionBar
        //Se agigna titulo y boton de regreso
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirma que el titulo no es null
        //Aseigna un titulo
        actionBar.setTitle("Inicio");
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        BaseDeDatos = firebaseDatabase.getReference("USUARIOS_DE_APP");

        FotoDePerfil = findViewById(R.id.fotoPerfil);
        UidPerfil = findViewById(R.id.uidPerfil);
        CorreoPerfil = findViewById(R.id.correoPerfil);
        NombresPerfil = findViewById(R.id.nombresPerfil);

        LogOut = findViewById(R.id.logout);

        LogOut.setOnClickListener(view -> {
            //Metodo para cerrar sesion
            cerrarSesion();
        });
    }


    //OnStart metodo
    @Override
    protected void onStart() {
        signedin();
        super.onStart();
    }

    //Método para recuperar los datos del usuario actual
    private void loadDataUser(){
        Query query = BaseDeDatos.orderByChild("correo").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Se recorren los usuarios registrados en la base de datos hasta encontrar el usuario actual
                for (DataSnapshot ds: snapshot.getChildren()){

                    //Se obtienen valores de datos
                    String uid = "" + ds.child("uid").getValue();
                    String correo = "" + ds.child("correo").getValue();
                    String nombres = "" + ds.child("nombres").getValue();
                    String imagen = "" + ds.child("imagen").getValue();

                    //Seteamos valores en las vistas
                    UidPerfil.setText(uid);
                    CorreoPerfil.setText(correo);
                    NombresPerfil.setText(nombres);

                    /* FOTO DE PERFIL*/
                    /*Declaramos Try Catch para gestionar la foto de perfil*/

                    try {
                        //Si existe una imagen en la base de datos del usuario actual
                        Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(FotoDePerfil);
                    }catch (Exception e){
                        //Si usuario no cuenta con una imagen en la base de datos actual
                        Picasso.get().load(R.drawable.img_perfil).into(FotoDePerfil);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Metodo para validar si el usuario inicio sesion
    public void signedin() {
        if (firebaseUser != null) {
            loadDataUser();
            Handler handler = new Handler();
            handler.postDelayed(() -> Toast.makeText(Inicio.this, "Se ha iniciado sesión", Toast.LENGTH_SHORT).show(),2000);

        } else {

            //ir de la pantalla inicio a la main
            startActivity(new Intent(Inicio.this, MainActivity.class));
            finish();
        }
    }

    //Metodo para cerrar sesion
    public void cerrarSesion() {
        firebaseAuth.signOut(); //Desconectar sesión de usuario activo
        Toast.makeText(Inicio.this, "Ha cerrado sesion", Toast.LENGTH_SHORT).show();
        //Una vez cerrada la sesion nos dirigimos a main activity
        startActivity(new Intent(Inicio.this, MainActivity.class));
    }
}
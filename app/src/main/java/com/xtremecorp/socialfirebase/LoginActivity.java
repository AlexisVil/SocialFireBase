package com.xtremecorp.socialfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText correoL, passL;
    Button ingresar;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ActionBar
        //Se agigna titulo y boton de regreso
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirma que el titulo no es null
        //Asigna un titulo
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Se conectan las vistas y se obtienen instancias
        correoL = findViewById(R.id.correoL);
        passL = findViewById(R.id.passwordL);
        ingresar = findViewById(R.id.btnLoginL);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        dialog = new Dialog(LoginActivity.this);

        //Validacion de datos para login

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = correoL.getText().toString();
                String pass = passL.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    correoL.setError("Correo inválido");
                    correoL.setFocusable(true);
                } else if( pass.length() < 6 ){
                    passL.setError("Debe contener 6 caracteres como mínimo");
                    passL.setFocusable(true);
                }else {
                    loginUsuario(correo, pass);
                }
            }
        });
    }

    /**Metodo para logear usuario**/
    private void loginUsuario(String correo, String pass) {
        /*
        progressDialog.setTitle("Ingresando");
        progressDialog.setMessage("Espere por favor");
        progressDialog.setCancelable(false);
        progressDialog.show();
        */
        //AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.iniciando_sesion, null));
        alertDialog = builder.create();
        alertDialog.show();



        firebaseAuth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si se inicia correctamente
                        if(task.isSuccessful()){
                            progressDialog.dismiss(); //Progress Dialog se cierra
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //Cambiar a la actividad de inicio

                            startActivity(new Intent(LoginActivity.this, Inicio.class));
                            assert user != null;
                            Toast.makeText(LoginActivity.this, "Bienvenido" + " " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            progressDialog.dismiss(); //Se cierra ventana de progress
                            dialogNoInicio();
                            Toast.makeText(LoginActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**Progress Dialog personalizado metodo**/

    private void dialogNoInicio(){

        dialog.setContentView(R.layout.no_sesion); //Pasamos al objeto la vista creada

        Button retry = dialog.findViewById(R.id.retryBtn);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }


    /**Metodo para habilitar regreso del action bar**/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
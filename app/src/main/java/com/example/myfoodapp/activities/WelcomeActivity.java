package com.example.myfoodapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myfoodapp.R;
import com.google.firebase.auth.FirebaseAuth;

//C'est la premier page à afficher dans l'application dont laquelle on choisi se connecter ou de créer un compte
public class WelcomeActivity extends AppCompatActivity {
   ProgressBar progressBar;
FirebaseAuth auth ;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            }
        });
    }

    public void register(View view) {
     startActivity(new Intent(WelcomeActivity.this, RegistrationActivity.class)) ;
    }

}
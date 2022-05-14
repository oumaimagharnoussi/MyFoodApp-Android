package com.example.myfoodapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText mail,pass;
    Button signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        mail=findViewById(R.id.edittext2);
        pass=findViewById(R.id.edittext3);
        signin=findViewById(R.id.button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on récupére le mail et le password pour l'authentification
                String usermail=mail.getText().toString();
                String userpass=pass.getText().toString();

                //on s'authentifier avec le mail et le password
                auth.signInWithEmailAndPassword(usermail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if(auth.getCurrentUser()!=null){
                                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                                        FirebaseUser user= auth.getCurrentUser();
                                        i.putExtra("mail",usermail);
                                        i.putExtra("pass",userpass);

                                        startActivity(i) ;
                                    }else{
                                        Toast.makeText(LoginActivity.this, "auth error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(LoginActivity.this, "error"+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)) ;
    }

    public void mainActivity(View view) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class)) ;
    }
}
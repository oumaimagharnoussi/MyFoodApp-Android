package com.example.myfoodapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;
import com.example.myfoodapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {

    Button register;
    EditText name,mail,pass;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView login;
    FirebaseFirestore store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        name=(EditText)findViewById(R.id.edittext1);
        mail=(EditText)findViewById(R.id.edittext2);
        pass=(EditText)findViewById(R.id.edittext3);
        register=(Button)findViewById(R.id.button);

        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)) ;
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String username=name.getText().toString();
                String usermail=mail.getText().toString();
                String userpass=pass.getText().toString();

                //vérification du champ name de l'utilisateur
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegistrationActivity.this, "name is empty!",Toast.LENGTH_SHORT).show();
                }else{
                    //création de l'utilisateurs
                    auth.createUserWithEmailAndPassword(usermail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                UserModel userModel=new UserModel(username,usermail,userpass,"user");
                                //récupération du l'id de l'utilisateur afin de le sauvagarder avec les autre informations de l'utilisateur
                                String id=task.getResult().getUser().getUid();
                                //database.getReference().child("Users").child(id).setValue(userModel);
                                store.collection("users").document(id)
                                        .set(userModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)) ;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegistrationActivity.this, "registration failed"+e.toString(),Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "error"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void login(View view){
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)) ;
    }

}
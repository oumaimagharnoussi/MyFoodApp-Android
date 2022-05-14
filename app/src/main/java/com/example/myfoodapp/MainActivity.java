package com.example.myfoodapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.activities.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

// cette activity est Navigation Drawer Activity
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Button logout;
    private FirebaseAuth auth;
    private TextView username;
    ImageView image;
    FirebaseFirestore database;
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(this, "main created!", Toast.LENGTH_SHORT).show();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                //tous les item du menu
                R.id.nav_home, R.id.nav_daily_meal, R.id.nav_favourite,R.id.nav_my_cart,R.id.nav_category,R.id.nav_profile
        ,R.id.nav_Order_admin,R.id.nav_users_admin,R.id.nav_product_admin,R.id.nav_category_admin)
                .setOpenableLayout(drawer)
                .build();





        auth= FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        if(user.getEmail()==null){
            Toast.makeText(MainActivity.this, "email not found", Toast.LENGTH_SHORT).show();
        }

        //Menu nav_Menu = navigationView.getMenu();

        //View adminInterface=findViewById(R.id.admin_navigation);
        //View userInterface=findViewById(R.id.user_navigation);

        //on vérifie que l'utilisateur connecté est un admin ou non pour afficher pour chacun son menu de navigation
        DocumentReference doc=database.collection("users").document(user.getUid());
        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                Toast.makeText(MainActivity.this, role, Toast.LENGTH_SHORT).show();
                if (role.equals("admin")){
                    //navigationView.findViewById(R.id.admin_navigation).setVisibility(View.VISIBLE);
                    //navigationView.findViewById(R.id.user_navigation).setVisibility(View.GONE);
                    //navigationView.getMenu().findItem(R.id.admin_navigation).setVisible(true);
                    //navigationView.getMenu().findItem(R.id.user_navigation).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_category).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_favourite).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_my_cart).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_Order).setVisible(false);
                    //adminInterface.setVisibility(View.VISIBLE);
                    //userInterface.setVisibility(View.GONE);
                }else {
                    //navigationView.getMenu().findItem(R.id.admin_navigation).setVisible(false);
                    //navigationView.getMenu().findItem(R.id.user_navigation).setVisible(true);
                    //navigationView.findViewById(R.id.admin_navigation).setVisibility(View.GONE);
                    //navigationView.findViewById(R.id.user_navigation).setVisibility(View.VISIBLE);
                    //adminInterface.setVisibility(View.GONE);
                    //userInterface.setVisibility(View.VISIBLE);
                    navigationView.getMenu().findItem(R.id.nav_Order_admin).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_product_admin).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_category_admin).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_users_admin).setVisible(false);
                }

            }
        });





        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View herderView = navigationView.getHeaderView(0);


        //on affiche le nom et le photo de l'utilisateur en haut de menu de navigation


        //TextView headername= herderView.findViewById(R.id.username);
        ImageView headerImg = herderView.findViewById(R.id.imageView);
        //auth=FirebaseAuth.getInstance();
        //username=findViewById(R.id.username);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        //database=FirebaseFirestore.getInstance();
        //FirebaseUser user= auth.getCurrentUser();
        if(user.getEmail()==null){
            Toast.makeText(MainActivity.this, "email not found", Toast.LENGTH_SHORT).show();
        }else{
            navUsername.setText(user.getEmail());
            //Toast.makeText(MainActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
        }
        storage = FirebaseStorage.getInstance().getReference().child("users/"+user.getUid()+"/profile.jpg");
       // DocumentReference doc=database.collection("users").document(user.getUid());

        doc.addSnapshotListener((documentSnapshot,e)-> {
                    if (documentSnapshot.exists()) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            public void onSuccess(Uri uri) {
                                Picasso.with(MainActivity.this).load(uri).into(headerImg);
                            }
                        });
                        navUsername.setText(documentSnapshot.getString("name"));

                    } else {
                        Toast.makeText(MainActivity.this, "document introuvable", Toast.LENGTH_SHORT).show();
                    }
                });




        //on configure le button du deconnecter
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se déconnect
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class)) ;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
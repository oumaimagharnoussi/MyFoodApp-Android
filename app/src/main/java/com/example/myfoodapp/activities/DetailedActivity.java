package com.example.myfoodapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.CommentaireAdapter;
import com.example.myfoodapp.models.CommentaireModel;
import com.example.myfoodapp.models.ViewAllModel;
import com.example.myfoodapp.models.ratingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

//Cette activité est celle responsable d'affichier et gérer les données du produit(afiichge d'un seul produit)!
//les données du produitn les commentaires,l'évaluation du produit, l'ajout au panier ...
public class DetailedActivity extends AppCompatActivity {
  TextView quantity;
  RatingBar ratingBar;
  int totalQuantity =1;
  int totalPrice = 0;
  int promotion;
  RecyclerView commentaireView;
ImageView detailedImg;
ImageView send;
TextView price,rating,description;
Button addToCart;
ImageView addItem,removeItem;
Toolbar toolbar;
FirebaseFirestore firestore;
FirebaseAuth auth;
ViewAllModel viewModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        //affichage du toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //configuration du fleche à retourner à la page home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
          auth = FirebaseAuth.getInstance();

          //récuperation des données du produit (ViewAllModel)
        viewModel = (ViewAllModel) getIntent().getSerializableExtra("detail");
        /*if(object==null){
            Toast.makeText(DetailedActivity.this, "null reference", Toast.LENGTH_SHORT).show();
        }
        if (object instanceof ViewModel){
        viewModel = (ViewAllModel) object;
            Toast.makeText(DetailedActivity.this, "coversion success", Toast.LENGTH_SHORT).show();
        }*/
        quantity = findViewById(R.id.quantity);
        detailedImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem= findViewById(R.id.remove_item);
        price=findViewById(R.id.detailed_price);
        rating=findViewById(R.id.detailed_rating);
        description=findViewById(R.id.detaild_dec);
        ratingBar=findViewById(R.id.fill_rating);
        //on verifie que les données recuperes existe
        if (viewModel != null){
            //on recupere l'image et on l'insére dans l'imageView detailed Img
            Glide.with(getApplicationContext()).load(viewModel.getImg_url()).into(detailedImg);
            rating.setText(viewModel.getRating());
            description.setText(viewModel.getDescription());
            price.setText("Price : $"+viewModel.getPrice());
            totalPrice = viewModel.getPrice()*totalQuantity;
            promotion=viewModel.getPromotion();
        }
        addToCart=findViewById(R.id.add_to_cart);
        //on imlemente l'evenement onClickListener pour ajouter le produit dans la panier
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

        //on augmente la quantité du produit
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if (totalQuantity < 10){
    totalQuantity++;
    quantity.setText(String.valueOf(totalQuantity));
    //on calcul de nouveau le prix total
    totalPrice = (viewModel.getPrice()-promotion*viewModel.getPrice()/100)*totalQuantity;
}
            }
        });

        //on diminue la quantité du produit
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    //on calcul de nouveau le prix total
                    totalPrice = (viewModel.getPrice()-promotion*viewModel.getPrice()/100)*totalQuantity;
                }
            }
        });


        //send commentaire
        send=findViewById(R.id.send);

       send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView commentaireTextView=findViewById(R.id.commentaire);
                String commentaire=commentaireTextView.getText().toString();

                //on ajoute la date du commentaire
                String saveCurrenDate,saveCurrenTime;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentData = new SimpleDateFormat("MM dd, yyyy");
                saveCurrenDate = currentData.format(calForDate.getTime());

                //on ajoute le temps du commentaire
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrenTime = currentTime.format(calForDate.getTime());

                //on récupere le photo de l'utilisateur pour le commentaire
                FirebaseUser user=auth.getCurrentUser();
                DocumentReference doc=firestore.collection("users").document(user.getUid());

                //chemin du photo de l'utilisateur
                String image_url="users/"+user.getUid()+"/profile.jpg";
                doc.addSnapshotListener((documentSnapshot,e)-> {
                    if (documentSnapshot.exists()) {
                        final String username=documentSnapshot.getString("name");
                        CommentaireModel message=new CommentaireModel(username,image_url,saveCurrenDate,saveCurrenTime,commentaire);
                        //on ajout le commentaire (CommentaireModel) dans la base de données
                        firestore.collection("commentaire").document(viewModel.getName())
                                .collection("message").add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(DetailedActivity.this, "message send success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(DetailedActivity.this, "document introuvable", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

       //on affiche la liste des commentaires dans la recyler view commentaireView
        commentaireView=findViewById(R.id.commentaireRecycler);
        commentaireView.setLayoutManager(new LinearLayoutManager(DetailedActivity.this,RecyclerView.VERTICAL,false));
        ArrayList commentaireList = new ArrayList<>();
        CommentaireAdapter commentaireAdapter = new CommentaireAdapter(DetailedActivity.this, commentaireList);
        //on associe l'adaptateur au recyclerView
        commentaireView.setAdapter(commentaireAdapter);
        //on récupére les commentaires dans la base de données
        firestore.collection("commentaire").document(viewModel.getName())
                .collection("message")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CommentaireModel commentaireModel = document.toObject(CommentaireModel.class);
                                commentaireList.add(commentaireModel);
                                commentaireAdapter.notifyDataSetChange();
                            }
                            //on ajoute les données récupéres du base situé dans la liste commmentaireList qui est
                            //associé à l'adaptateur commentaireAdapter
                            commentaireView.setAdapter(commentaireAdapter);
                        } else {
                            Toast.makeText(DetailedActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        //on récupére les évaluations des produits
        firestore.collection("rating").document(viewModel.getName())
                .collection("product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String userId=auth.getCurrentUser().getUid();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ratingModel model=document.toObject(ratingModel.class);
                    //on vérife que l'évaluation récupéré est celle de l'utlilsateur actuelle
                    if (model.getUserId()==userId){
                        //on associe l'évaluation à ratingbar
                        ratingBar.setRating(model.getRate());
                    }
                }
            }
        });

        //on récupére l'évaluation du l'utilisateur(5 étoile par exemple)
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rate=(int)v;
                ratingModel rateModel=new ratingModel(auth.getCurrentUser().getUid(),rate);
                //on sauvgarde l'évaluation dans la base de données
                firestore.collection("rating").document(viewModel.getName())
                        .collection("product").add(rateModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this,"rate saved",Toast.LENGTH_SHORT);
                        ratingBar.setRating(rate);
                    }
                });
            }
        });

    }

    //la fonction permettant l'ajout du produit dans la panier
    private void addToCart() {

        String saveCurrenDate,saveCurrenTime;
        //on récupére la date de l'ajout
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("MM dd, yyyy");
        saveCurrenDate = currentData.format(calForDate.getTime());

        //on récupére le temps de l'ajout
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrenTime = currentTime.format(calForDate.getTime());

        //on associe lles données au panier model afin de les sauvgarder dans la base de données
        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("product_name",viewModel.getName());
        cartMap.put("Product_price",viewModel.getPrice());
        cartMap.put("current_date",saveCurrenDate);
        cartMap.put("current_time",saveCurrenTime);
        cartMap.put("total_price",totalPrice);
        cartMap.put("total_quantity",quantity.getText().toString());

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
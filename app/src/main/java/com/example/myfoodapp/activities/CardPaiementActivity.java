package com.example.myfoodapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodapp.BuildConfig;
import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardPaiementActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    String username = "";
    String listCart = "";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_paiement);

        btn=(Button) findViewById(R.id.confirm_order_button);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //récupérer les items,method et le total du panier
        List<CartModel> list = (ArrayList<CartModel>) getIntent().getSerializableExtra("itemList");
        String method = (String) getIntent().getStringExtra("paymentMethod");
        Float total = (float) getIntent().getFloatExtra("total", 0);

        if (list != null && list.size() > 0) {
            for (CartModel model : list) {
                //String utilisé pour l'affichage des ordres
                listCart = listCart + model.getTotal_quantity() + " " + model.getProduct_name() + " " + model.getProduct_price() + "\n";
            }


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //récupérer les informations de l'utilisateur connecté pour les stocker dans la order model
                    DocumentReference doc = firestore.collection("users").document(auth.getCurrentUser().getUid());

                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            username = documentSnapshot.get("name").toString();

                            //création du data lié au document firebase
                            HashMap<String, Object> order = new HashMap<>();

                            order.put("listCart", listCart);
                            order.put("payment", method);
                            order.put("total", total);
                            order.put("user", username);
                            order.put("userId", auth.getCurrentUser().getUid());

                            //ajout du document Order model(les commandes)
                            firestore.collection("Order").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    //aprés la sauvgarde des order(les commandes) on doit vider la panier(AddToCart) de l'utilisateur correspondant
                                    firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                            .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            //suppession des documents du panier de l'utilisateur correspondant un par un
                                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                                doc.getReference().delete();
                                            }
                                        }
                                    });
                                }
                            });
                            //on affiche la page du succes du paiement
                            setContentView(R.layout.activity_placed_order);
                }
            });

            /*doc.addSnapshotListener((documentSnapshot,e)-> {
                        if (documentSnapshot.exists()){
                            username =documentSnapshot.get("name").toString();
                            Toast.makeText(PlacedOrderActivity.this,"name:"+documentSnapshot.get("name"),Toast.LENGTH_SHORT).show();
                        }
                    });
*/

                       /*
                       firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                               .collection("CurrentUser")
                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                       if (task.isSuccessful()) {

                                               for (QueryDocumentSnapshot document : task.getResult()) {

                                                   document.getReference().delete()
                                                           ++deleted
                                               }
                                               if (deleted >= batchSize) {
                                                   // retrieve and delete another batch
                                                   deleteCollection(collection, batchSize)
                                               }
                                       } else {
                                           Toast.makeText(PlacedOrderActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                       }
                                   }

                               });/*
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(PlacedOrderActivity.this, "Your order has been placed", Toast.LENGTH_SHORT).show();
                                       }else {
                                           Toast.makeText(PlacedOrderActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               });*/


                }
            });
            //}
        }
    }
}
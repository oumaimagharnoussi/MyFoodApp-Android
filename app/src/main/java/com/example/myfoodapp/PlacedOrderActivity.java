package com.example.myfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myfoodapp.activities.DetailedActivity;
import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//cette activity implément le paiement à la livraison
public class PlacedOrderActivity extends AppCompatActivity {
FirebaseAuth auth;
FirebaseFirestore firestore;

    String username= "";
    String listCart = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        //on récupe les données nécessaire pour les sauvgarder dans la base de donné
        List<CartModel> list = (ArrayList<CartModel>) getIntent().getSerializableExtra("itemList");
        String method = (String) getIntent().getStringExtra("paymentMethod");
        Float total = (float) getIntent().getFloatExtra("total",0);

        //on insére tous les élements de la liste dans une chaine pour l'afficher dans Order fragment(commandes)
        if (list!=null && list.size()>0){
           for(CartModel model : list){
               listCart=listCart+model.getTotal_quantity()+" "+model.getProduct_name()+" "+model.getProduct_price()+"\n";
            }



           //on associe le document de l'tulisisateur afin de récupérer son nom
            DocumentReference doc=firestore.collection("users").document(auth.getCurrentUser().getUid());

           doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                   username =documentSnapshot.get("name").toString();
                   HashMap<String,Object> order = new HashMap<>();

                   order.put("listCart",listCart);
                   order.put("payment",method);
                   order.put("total",total);
                   order.put("user", username);
                   order.put("userId",auth.getCurrentUser().getUid());

                   //on sauvgarde la commande dans la base de donné
                   firestore.collection("Order").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentReference> task) {

                           //on vide le panier de l'utilisateur courant
                           firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                   .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   for (QueryDocumentSnapshot doc : task.getResult()) {
                                       doc.getReference().delete();
                                   }
                               }
                           });
               }
           });
                       Toast.makeText(PlacedOrderActivity.this,"succes",Toast.LENGTH_SHORT).show();


                   }
               });
           //}
        }
    }
}
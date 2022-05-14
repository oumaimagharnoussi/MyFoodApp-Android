package com.example.myfoodapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.PlacedOrderActivity;
import com.example.myfoodapp.R;
import com.example.myfoodapp.activities.PaiementActivity;
import com.example.myfoodapp.adapters.CartAdapter;
import com.example.myfoodapp.adapters.NavCategoryAdapter;
import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.models.NavCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView total;
    RecyclerView recyclerView;
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;
    Button buyNow;
    Float totalPayment=0f;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_cart,container,false);
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.cartRecycler);
        total=root.findViewById(R.id.Total);
        // on configure le broadcast afin de récupérer le montant total du panier
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));
        buyNow= root.findViewById(R.id.buyNow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);
        //Toast.makeText(getContext(), "start loading from database", Toast.LENGTH_SHORT).show();

        //on récupére tous les élements du panier lié au utilisateur courant
        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String documentId = document.getId();



                                CartModel cartModel = document.toObject(CartModel.class);

                                cartModel.setDocumentId(documentId);

                                cartModelList.add(cartModel);

                                cartAdapter.notifyDataSetChange();
                            }
                            recyclerView.setAdapter(cartAdapter);
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

 buyNow.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         //on envoi les donnés nécessaire pour le paiement
         Intent intent = new Intent(getContext(), PaiementActivity.class);
         intent.putExtra("itemList", (Serializable) cartModelList);
         intent.putExtra("total", totalPayment);
         startActivity(intent);
     }
 });
        return root;


    }

    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //on récupére le montant total du panier et on l'affiche
            int totalCart=intent.getIntExtra("TotalAmount",0);
            totalPayment=(float)totalCart;
            total.setText(String.valueOf("Total: "+totalCart+"$"));
        }
    };

}
package com.example.myfoodapp.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.OrderAdapter;
import com.example.myfoodapp.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<OrderModel> orderModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order,container,false);
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.order_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        orderModel = new ArrayList<>();

        OrderAdapter adapter = new OrderAdapter(getContext(), orderModel);
        recyclerView.setAdapter(adapter);
        DocumentReference doc=db.collection("users").document(auth.getCurrentUser().getUid());
        //on verifie que l'utilisateur connecté est un admin ou non
        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                if (role.equals("admin")){
                    //get all orders
                    //get all order for current user
                    /*db.collection("Order").document(auth.getCurrentUser().getUid())
                            .collection("MyOrder")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            OrderModel model = document.toObject(OrderModel.class);
                                            orderModel.add(model); adapter.notifyDataSetChange();
                                        }
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/

                    //on récupére tous les commandes
                    db.collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    OrderModel model = document.toObject(OrderModel.class);
                                    orderModel.add(model); adapter.notifyDataSetChange();
                                }
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });

                }else {
                    //on récupére tous les commandes de l'utilisateur courant
                    db.collection("Order")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            OrderModel model = document.toObject(OrderModel.class);
                                            if(model.getUserId().equals(auth.getCurrentUser().getUid()))
                                            orderModel.add(model); adapter.notifyDataSetChange();
                                        }
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        return root;
    }
}
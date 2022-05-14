package com.example.myfoodapp.ui.slideshow;

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
import com.example.myfoodapp.activities.ProductActivity;
import com.example.myfoodapp.adapters.HomeVerAdapter;
import com.example.myfoodapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<ViewAllModel> productModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite,container,false);

        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.product_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        productModel = new ArrayList<>();

        HomeVerAdapter adapter = new HomeVerAdapter(getContext(), productModel);
        recyclerView.setAdapter(adapter);
        // on récupére tous les produits situé dans la liste favoris du l'utilisateur actuelle
        db.collection("favoris").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ViewAllModel ProductModel = document.toObject(ViewAllModel.class);
                                productModel.add(ProductModel); adapter.notifyDataSetChange();
                            }
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return root;
    }
}
package com.example.myfoodapp.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.HomeVerAdapter;
import com.example.myfoodapp.models.ViewAllModel;
import com.example.myfoodapp.ui.category.CategoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    Button add;
    HomeVerAdapter adapter;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<ViewAllModel> productModel;
    List<ViewAllModel> allproduct=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);


        db = FirebaseFirestore.getInstance();
        //on configure l'adaptateur et le recycle view
        recyclerView = root.findViewById(R.id.product_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        productModel = new ArrayList<>();


        adapter = new HomeVerAdapter(getContext(), productModel,getFragmentManager(), ProductFragment.this.getId());
        recyclerView.setAdapter(adapter);
        //on récupére tous les produits
        db.collection("Product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ViewAllModel ProductModel = document.toObject(ViewAllModel.class);

                                productModel.add(ProductModel);
                                adapter.notifyDataSetChange();

                            }
                            allproduct = productModel;
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // si on click sur le button add on ajoute un nouveau produit
        add=(Button)root.findViewById(R.id.add_product);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductFragment nextFrag= new AddProductFragment();
                getFragmentManager().beginTransaction()
                        .replace(ProductFragment.this.getId(), nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return root;
    }
}
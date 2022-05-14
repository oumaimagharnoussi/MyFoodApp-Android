package com.example.myfoodapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.HomeHorAdapter;
import com.example.myfoodapp.adapters.HomeVerAdapter;

import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;
import com.example.myfoodapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

RecyclerView homeHorizontalRec,homeVerticalRec,homeCatRec;

ArrayList<HomeHorModel> homeHorModelList;
HomeHorAdapter homeHorAdapter;
ArrayList<HomeVerModel> homeVerModelList;
HomeVerAdapter homeVerAdapter;

//search
   EditText search_box;
   private List<ViewAllModel> viewAllModelList;
private RecyclerView recyclerViewSearch;




    HomeVerAdapter adapter;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<ViewAllModel> productModel;
    List<ViewAllModel> allproduct=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        db = FirebaseFirestore.getInstance();
        //on configure la l'adaptateur et le recycle view
        recyclerView = root.findViewById(R.id.product_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        productModel = new ArrayList<>();


        adapter = new HomeVerAdapter(getContext(), productModel,getFragmentManager(),R.layout.fragment_home);
        recyclerView.setAdapter(adapter);
        //on récuêre tous les produits
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
                            allproduct=productModel;
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });










       //recyclerViewSearch = root.findViewById(R.id.product_rec);
        search_box= root.findViewById(R.id.search_rec);
        //on implément le fonctionnalié du recherche
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            // on excute cette foction aprés le changement du text
            @Override
            public void afterTextChanged(Editable s) {
                //si la recherceh view a été changé et est devenu vide alors on affiche de nouveau tous les produits
                if (s.toString().isEmpty()){
                    Toast.makeText(getContext(),"text search empty",Toast.LENGTH_SHORT).show();

                    //productModel=allproduct;
                    //recyclerView.setAdapter(adapter);


                    recyclerView.setAdapter(adapter);
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
                                        allproduct=productModel;
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    searchProduct(s.toString());
                }

            }
        });




        return root;
    }

   private void searchProduct(String name) {
        if (!name.isEmpty()){
            //on récupére les parduits par name
            db.collection("Product").whereEqualTo("name",name).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult()!= null){
                                //on vide la liste des produits
                                productModel.clear();
                                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                    ViewAllModel viewAllModel = doc.toObject(ViewAllModel.class);
                                    productModel.add(viewAllModel);


                                }
                                recyclerView.setAdapter(adapter);
                            }

                        }
                    });
        }
    }
}
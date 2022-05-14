package com.example.myfoodapp.ui.category;

import android.app.DownloadManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.NavCategoryAdapter;
import com.example.myfoodapp.models.NavCategoryModel;
import com.example.myfoodapp.ui.admin.AddCategoryFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
FirebaseFirestore db;
Button add;


RecyclerView recyclerView;
List<NavCategoryModel> categoryModelList;
//NavCategoryModel navCategoryModel;
NavCategoryAdapter navCategoryAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category,container,false);
db = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.cat_rec);

        //on configure l'adaptateur et le recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        categoryModelList = new ArrayList<>();
        NavCategoryAdapter navCategoryAdapter = new NavCategoryAdapter(getActivity(), categoryModelList,getFragmentManager(),CategoryFragment.this.getId());
        recyclerView.setAdapter(navCategoryAdapter);

        //on récupére tous les catégorie
        db.collection("NavCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);

                                categoryModelList.add(navCategoryModel);

                                navCategoryAdapter.notifyDataSetChange();
                            }
                            recyclerView.setAdapter(navCategoryAdapter);
                            Toast.makeText(getContext(), "action success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        //si l'utilisateur connecté est un admin on afiiche le button add
        add=(Button)root.findViewById(R.id.add_categoty);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        DocumentReference doc=db.collection("users").document(user.getUid());
        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                if (role.equals("admin")){
                    add.setVisibility(View.VISIBLE);
                }
            }
        });

        // si on click sur le button add on ajoute un nouveau catégorie
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryFragment nextFrag= new AddCategoryFragment();
                getFragmentManager().beginTransaction()
                        .replace(CategoryFragment.this.getId(), nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;


    }

}

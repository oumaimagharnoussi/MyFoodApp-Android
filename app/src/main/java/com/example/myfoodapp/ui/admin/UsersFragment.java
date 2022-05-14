package com.example.myfoodapp.ui.admin;


import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.OrderAdapter;
import com.example.myfoodapp.adapters.UserAdapter;
import com.example.myfoodapp.models.OrderModel;
import com.example.myfoodapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<UserModel> userModel;
    Button add;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_users,container,false);
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        //on configure l'adaptateur et le recycle view
        recyclerView = root.findViewById(R.id.user_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        userModel = new ArrayList<>();

        List usersId = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(getContext(), userModel,usersId,getFragmentManager(),UsersFragment.this.getId());
        recyclerView.setAdapter(adapter);
        //on récupére tous les utilisateurs
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserModel model = document.toObject(UserModel.class);
                                usersId.add(document.getId());
                                userModel.add(model); adapter.notifyDataSetChange();
                            }
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // si on click su r le button add on ajoute un nouveau utilisateur
        add=(Button)root.findViewById(R.id.add_user);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUserFragment nextFrag= new AddUserFragment();
                getFragmentManager().beginTransaction()
                        .replace(UsersFragment.this.getId(), nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }
}
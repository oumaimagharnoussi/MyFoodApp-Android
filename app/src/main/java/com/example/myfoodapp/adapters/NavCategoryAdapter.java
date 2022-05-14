package com.example.myfoodapp.adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;




import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;



import com.bumptech.glide.Glide;
import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;
import com.example.myfoodapp.activities.ProductActivity;
import com.example.myfoodapp.models.NavCategoryModel;
import com.example.myfoodapp.ui.admin.AddCategoryFragment;
import com.example.myfoodapp.ui.category.CategoryFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;



public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {
    Context context;
    List<NavCategoryModel> list;
    Boolean isAdmin=false;

    FragmentManager manager;
    int fragmentId;


    public NavCategoryAdapter(Context context, List<NavCategoryModel> list) {
        this.context = context;
        this.list = list;

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        DocumentReference doc=database.collection("users").document(user.getUid());
        //on vérifie l'utilisateur connecté est il un admin ou non
        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                if (role.equals("admin")){
                    isAdmin=true;
                }
            }
        });
    }

    public NavCategoryAdapter(Context context, List<NavCategoryModel> list,FragmentManager manager,int fragmentId) {
        this.context = context;
        this.list = list;
        this.manager=manager;
        this.fragmentId=fragmentId;

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        DocumentReference doc=database.collection("users").document(user.getUid());
        //on vérifie l'utilisateur connecté est il un admin ou non
        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                if (role.equals("admin")){
                    isAdmin=true;
                }
            }
        });
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cat_item,parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavCategoryModel model=list.get(position);
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        String name=list.get(position).getName();
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.discount.setText(list.get(position).getDiscount());
        if (isAdmin==false){
            holder.ViewProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //lors du click sur le fleche lié au  l'affichage du categorie on affiche les produits
                    //inclus dans cette catégorie
                    Intent intent=new Intent(context, ProductActivity.class);
                    intent.putExtra("category",name);
                    context.startActivity(intent);
                }
            });
        }else if(isAdmin==true){
            holder.ViewProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //l'admin modifie les informations lié au catégorie
                    AddCategoryFragment nextFrag= new AddCategoryFragment(model);
                    manager.beginTransaction()
                            .replace(fragmentId, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }



    public void notifyDataSetChange() {
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ViewProducts;
        ImageView imageView;
        //ImageView viewProducts;
        TextView name,description,discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_nav_img);
            name= itemView.findViewById(R.id.nav_cat_name);
            description = itemView.findViewById(R.id.nav_cat_description);
            discount = itemView.findViewById(R.id.nav_cat_discount);
            ViewProducts =itemView.findViewById(R.id.viewproductofcategory);



        }
    }
}


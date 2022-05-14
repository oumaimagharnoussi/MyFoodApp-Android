package com.example.myfoodapp.adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;



import com.bumptech.glide.Glide;
import com.example.myfoodapp.R;
import com.example.myfoodapp.activities.DetailedActivity;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.ViewAllModel;
import com.example.myfoodapp.models.ratingModel;
import com.example.myfoodapp.ui.admin.AddProductFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.util.List;


//adaptator des produits
public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {
    Context context;
    List<ViewAllModel> list;
    Boolean isAdmin=false;
    FragmentManager manager;
    int fragmentId;

    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();

    public HomeVerAdapter(Context context, List<ViewAllModel> list) {
        this.context = context;
        this.list = list;

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        //on vérifie si l'utilisateur connecté est un admin ou non
        DocumentReference doc=database.collection("users").document(user.getUid());
        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                if (role.equals("admin")){
                    isAdmin=true;
                }
            }
        });
    }

    //cet constructeur est utilisé dans l'interface admin pour se connecter aux autres fragments
    public HomeVerAdapter(Context context, List<ViewAllModel> list, FragmentManager manager,int fragmentId) {
        this.context = context;
        this.list = list;
        this.manager=manager;
        this.fragmentId=fragmentId;

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        //on vérifie si l'utilisateur connecté est un admin ou non
        DocumentReference doc=database.collection("users").document(user.getUid());
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item,parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        Picasso.with(context).load(list.get(position).getImg_url()).into(holder.imageView);

ViewAllModel model=list.get(position);
//holder.imageView.setImageResource(list.get(position).getImg_url());
        holder.name.setText(list.get(position).getName());
        holder.type.setText(list.get(position).getType());
        holder.rating.setText(list.get(position).getRating());
        holder.promotion.setText("discount: "+list.get(position).getPromotion());
        holder.fidelite.setText("Pt fidelité: "+list.get(position).getFidelite());
        holder.price.setText("price: "+list.get(position).getPrice());
        //si l'utilisateur n'est pas un administrateur
        if(!isAdmin) {
            //permet l'affichage (detail Activity) des données du produit selectionné
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailedActivity.class);
                    intent.putExtra("detail", model);
                    context.startActivity(intent);
                }
            });
            //permet l'ajout du produit à la liste des favoris de l'utilisateur
            holder.favori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firestore.collection("favoris").document(auth.getCurrentUser().getUid())
                            .collection("CurrentUser").add(model).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(context, "Added To favoris", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }else if (isAdmin){
            //l'admin n'a pas de liste des favoris
            holder.favori.setVisibility(View.GONE);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //on start le nouveau fragment AddProductFragment pour updater les informations du produit
                    AddProductFragment nextFrag= new AddProductFragment(model);
                    manager.beginTransaction()
                            .replace(fragmentId, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        //on récupére les évaluations lié au chaque produit
        firestore.collection("rating").document(model.getName())
                .collection("product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int somme=0,count=0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ratingModel model=document.toObject(ratingModel.class);
                    count+= model.getRate();
                    somme++;
                }
                //on calcule la moyenne de l'évaluation et on ajoute au rating bar
                if (somme==0){
                    holder.ratingbar.setRating(0);
                }else {
                    holder.ratingbar.setRating(somme / count);
                }
                holder.ratingbar.setIsIndicator(true);
            }
        });
    }





    @Override
    public int getItemCount() {
        return list.size();
    }



    public void notifyDataSetChange() {
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,favori;
        RatingBar ratingbar;
        TextView name,type,rating,price,fidelite,promotion;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            favori=itemView.findViewById(R.id.favori);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            promotion=itemView.findViewById(R.id.promotion);
            fidelite=itemView.findViewById(R.id.fidelie);
            ratingbar=itemView.findViewById(R.id.ratingbar);
        }
    }
}


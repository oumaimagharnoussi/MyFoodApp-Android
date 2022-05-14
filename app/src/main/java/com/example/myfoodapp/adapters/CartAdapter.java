package com.example.myfoodapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    Context context;
    List<CartModel> cartModelList;
    int total=0;
FirebaseFirestore firestore;
FirebaseAuth auth ;
    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel model=cartModelList.get(position);
        holder.product_name.setText(model.getProduct_name());
        holder.product_price.setText(String.valueOf(model.getProduct_price()));
        holder.current_date.setText(model.getCurrent_date());
        holder.current_time.setText(model.getCurrent_time());
        holder.total_price.setText(String.valueOf(model.getTotal_price()));
        holder.total_quantity.setText(model.getTotal_quantity());

       holder.deleteItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //on supprime le produit sitiué dans la panier(cart)
               firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                       .collection("CurrentUser")
                       .document(model.getDocumentId())
                       .delete()
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   //on supprime le produit de la liste récupérer lors de création du l'adaptateur
                                  cartModelList.remove(model) ;
                                  //calcul du nouveau du prix total du panier
                                   total=total-model.getTotal_price();
                                  notifyDataSetChanged();
                               }else {
                                   Toast.makeText(context, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }

                           }
                       });

           }
       });


        total=total+model.getTotal_price();
        //Toast.makeText(context, "adapter success", Toast.LENGTH_SHORT).show();

        Intent intent=new Intent("MyTotalAmount");
        intent.putExtra("TotalAmount",total);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void notifyDataSetChange() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name,product_price,current_date,current_time,total_price,total_quantity;

        ImageView deleteItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name=itemView.findViewById(R.id.product_name);
            product_price=itemView.findViewById(R.id.product_price);
            current_date=itemView.findViewById(R.id.Current_date);
            current_time=itemView.findViewById(R.id.Current_time);
            total_price=itemView.findViewById(R.id.total_price);
            total_quantity=itemView.findViewById(R.id.total_quantity);

            deleteItem = itemView.findViewById(R.id.delete);

        }
    }
}

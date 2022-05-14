package com.example.myfoodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.models.CommentaireModel;
import com.example.myfoodapp.models.OrderModel;
import com.google.firebase.storage.StorageReference;

import java.sql.Array;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    Context context;
    List<OrderModel> orderModelList;
    StorageReference storage;


    public OrderAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel model=orderModelList.get(position);
        //Array list=model.getListCart();
        holder.total.setText("Total Price: "+model.getTotal());
        holder.payment.setText("payment: "+model.getPayment());
        /*String productDetails="";
        for (Array c:list.) {
            //productDetails=productDetails+c.getTotal_quantity()+" "+c.getProduct_name()+" "+c.getProduct_price()+"\n";
            productDetails=productDetails+ c.toString()+"\n";
        }*/
        holder.product.setText(model.getListCart());
        holder.user.setText(model.getUser());

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public void notifyDataSetChange() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView total,payment,user;
        TextView product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            total=itemView.findViewById(R.id.total_price);
            payment=itemView.findViewById(R.id.payment);
            product=itemView.findViewById(R.id.product_purchase);
            user=itemView.findViewById(R.id.username);
        }
    }
}

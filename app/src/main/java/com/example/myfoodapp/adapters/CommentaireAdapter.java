package com.example.myfoodapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CommentaireModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentaireAdapter extends RecyclerView.Adapter<CommentaireAdapter.ViewHolder> {

    Context context;
    List<CommentaireModel> commentaireModelList;
    StorageReference storage;

    public CommentaireAdapter(Context context, List<CommentaireModel> commentaireModelList) {
        this.context = context;
        this.commentaireModelList = commentaireModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.commentaire_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentaireModel model=commentaireModelList.get(position);
        //Picasso.with(context).load(model.getUser_img()).into(holder.img_profile);
        storage = FirebaseStorage.getInstance().getReference().child(model.getUser_img());
        //on récupére l'image de l'utilisateur responsable du commentaire
        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            public void onSuccess(Uri uri){
                //Toast.makeText(context, "downloaded", Toast.LENGTH_SHORT).show();

                Picasso.with(context).load(uri).into(holder.img_profile);
            }
        });
        holder.details.setText(model.getUsername()+" - "+model.getCurent_time()+" "+model.getCurrent_date());
        holder.message.setText(model.getCommentaire());



    }

    @Override
    public int getItemCount() {
        return commentaireModelList.size();
    }

    public void notifyDataSetChange() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView details,message;

        ImageView img_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_profile=itemView.findViewById(R.id.commentaire_img);
            details=itemView.findViewById(R.id.details);
            message=itemView.findViewById(R.id.message);

        }
    }
}

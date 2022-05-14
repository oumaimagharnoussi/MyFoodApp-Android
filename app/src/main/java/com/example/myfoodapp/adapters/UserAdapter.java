package com.example.myfoodapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;
import com.example.myfoodapp.models.UserModel;
import com.example.myfoodapp.ui.admin.AddUserFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<UserModel> list;
    List usersId;

    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    StorageReference storage;
    FragmentManager manager;
    int fragmentId;

    public UserAdapter(Context context, List<UserModel> list,List usersId,FragmentManager manager,int fragmentId) {
        this.context = context;
        this.list = list;
        this.manager=manager;
        this.fragmentId=fragmentId;
        this.usersId=usersId;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel model=list.get(position);
        String Id=(String) usersId.get(position);

        //on récupere l'image de l'utilisateur situé dans la basa de donnée(Firebase storage
            storage = FirebaseStorage.getInstance().getReference().child("users/"+usersId.get(position)+"/profile.jpg");
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).into(holder.imageView);
                }
            });
        //}

//holder.imageView.setImageResource(list.get(position).getImg_url());
        holder.name.setText("name: "+list.get(position).getName());
        holder.mail.setText("mail: "+list.get(position).getMail());
        if(model.getNumber()!=null) holder.number.setText("number: "+list.get(position).getNumber());
        if(model.getAddress()!=null) holder.address.setText("address: "+list.get(position).getAddress());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on transfére les données du l'utilisateur pour les modifier
                AddUserFragment nextFrag= new AddUserFragment(model,Id);
                manager.beginTransaction()
                        .replace(fragmentId, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
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
        ImageView imageView;
        TextView name,mail,number,address;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mail=(TextView)itemView.findViewById(R.id.mail);
            number=(TextView)itemView.findViewById(R.id.number);
            name=(TextView)itemView.findViewById(R.id.username);
            address=(TextView)itemView.findViewById(R.id.address);
            imageView=(ImageView) itemView.findViewById(R.id.user_img);
        }
    }
}


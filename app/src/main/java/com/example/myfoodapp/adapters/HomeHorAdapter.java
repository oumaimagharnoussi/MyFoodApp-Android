package com.example.myfoodapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.List;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {
    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> list;
    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(Activity activity, ArrayList<HomeHorModel> list) {
        this.activity = activity;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     holder.imageView.setImageResource(list.get(position).getImage());
     holder.name.setText(list.get(position).getName());


     if (check){
        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
        homeVerModels.add(new HomeVerModel(R.drawable.pizza1,"Pizza","10:00 - 23:00","4.9","Min - $35"));
         homeVerModels.add(new HomeVerModel(R.drawable.pizza2,"Pizza","10:00 - 23:00","4.9","Min - $35"));
         homeVerModels.add(new HomeVerModel(R.drawable.pizza3,"Pizza","10:00 - 23:00","4.9","Min - $35"));
         homeVerModels.add(new HomeVerModel(R.drawable.pizza4,"Pizza","10:00 - 23:00","4.9","Min - $35"));
         updateVerticalRec.callBack(position, homeVerModels );
         check = false;
         holder.cardView.setOnClickListener((new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 row_index= holder.getAdapterPosition();
                 notifyDataSetChanged();
                 if (row_index==0){
                     ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                     homeVerModels.add(new HomeVerModel(R.drawable.pizza1,"Pizza","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.pizza2,"Pizza","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.pizza3,"Pizza","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.pizza4,"Pizza","10:00 - 23:00","4.9","Min - $35"));

                     updateVerticalRec.callBack(row_index,homeVerModels);
                 }
                 else if (row_index==1){
                     ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                     homeVerModels.add(new HomeVerModel(R.drawable.burger1,"burger","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.burger2,"burger","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.burger4,"burger","10:00 - 23:00","4.9","Min - $35"));
                     updateVerticalRec.callBack(row_index,homeVerModels);
                 }
                 else if (row_index==2){
                     ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                     homeVerModels.add(new HomeVerModel(R.drawable.fries1,"fries","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.fries2,"fries","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.fries3,"fries","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.fries4,"fries","10:00 - 23:00","4.9","Min - $35"));
                     updateVerticalRec.callBack(row_index,homeVerModels);
                 }
                 else if (row_index==3){
                     ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                     homeVerModels.add(new HomeVerModel(R.drawable.icecream1,"icecream","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.icecream2,"icecream","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.icecream3,"icecream","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.icecream4,"icecream","10:00 - 23:00","4.9","Min - $35"));
                     updateVerticalRec.callBack(row_index,homeVerModels);
                 }
                 else if (row_index==4){
                     ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                     homeVerModels.add(new HomeVerModel(R.drawable.sandwich1,"sandwich","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.sandwich2,"sandwich","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.sandwich3,"sandwich","10:00 - 23:00","4.9","Min - $35"));
                     homeVerModels.add(new HomeVerModel(R.drawable.sandwich4,"sandwich","10:00 - 23:00","4.9","Min - $35"));
                     updateVerticalRec.callBack(row_index,homeVerModels);
                 }
             }
         }));

         if(select){
             if(position==0){
                 holder.cardView.setBackgroundResource(R.drawable.change_bg);
            select= false;
             }
         }

         else{
             if(row_index==0){
                 holder.cardView.setBackgroundResource(R.drawable.change_bg);
             }else{
                 holder.cardView.setBackgroundResource(R.drawable.default_bg);
             }
         }
     }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  imageView;
        TextView name;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cartView);
        }
    }
}

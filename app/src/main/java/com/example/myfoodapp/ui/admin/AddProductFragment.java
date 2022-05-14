package com.example.myfoodapp.ui.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.NavCategoryModel;
import com.example.myfoodapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddProductFragment extends Fragment {
    ViewAllModel model=null;
    TextView name,description,discount,productcategory,price,productFidelite,type;
    ImageView imageView;
    Button btn;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    String id;
    String image_url;

    public AddProductFragment() {
    }

    public AddProductFragment(ViewAllModel model){
        this.model=model;image_url=model.getImg_url();
    }
    /*
    public static AddCategoryFragment newInstance(NavCategoryModel categgory) {
        AddCategoryFragment fragment = new AddCategoryFragment();
        Bundle args = new Bundle();
        args.putString(model, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_add_product, container, false);
        name=(EditText)root.findViewById(R.id.product_name);
        description=(EditText)root.findViewById(R.id.product_description);
        discount=(EditText)root.findViewById(R.id.product_promotion);
        price=(EditText)root.findViewById(R.id.product_price);
        productcategory=(EditText)root.findViewById(R.id.product_categorie);
        productFidelite=(EditText)root.findViewById(R.id.product_fidelite);
        type=(EditText)root.findViewById(R.id.product_type);
        imageView=(ImageView) root.findViewById(R.id.product_img);
        btn=(Button) root.findViewById(R.id.btn_product);
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        if (model!=null){
            name.setText(model.getName());
            description.setText(model.getDescription());
            discount.setText(Integer.toString(model.getPromotion()));
            price.setText(Integer.toString(model.getPrice()));
            productcategory.setText(model.getCategorie());
            productFidelite.setText(Integer.toString(model.getFidelite()));
            type.setText(model.getType());
            Picasso.with(root.getContext()).load(model.getImg_url()).into(imageView);
            btn.setText("update");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAllModel productModel=new ViewAllModel(
                        name.getText().toString(),
                        description.getText().toString(),
                        image_url,
                        type.getText().toString(),
                        productcategory.getText().toString(),
                        Integer.parseInt(price.getText().toString()),
                        Integer.parseInt(discount.getText().toString()),
                        Integer.parseInt(productFidelite.getText().toString())

                );


                //si ona récupére le model du produit alors on le modifie sinon on crée un nouveau produit
                if(model!=null){
                    //on récupére les informations du produit pour extraire l'id
                    firestore.collection("Product")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot d : task.getResult()) {
                                    if (d.get("name").equals( name.getText().toString()))
                                    {
                                        //on récupére l'id du porduit
                                        id =d.getId();

                                        Map<String,Object> produit=new HashMap<>();
                                        produit.put("name",name.getText().toString());
                                        produit.put("description",description.getText().toString());
                                        produit.put("discount",Integer.parseInt(discount.getText().toString()));
                                        produit.put("price",Integer.parseInt(price.getText().toString()));
                                        produit.put("category",productcategory.getText().toString());
                                        produit.put("fidelite",Integer.parseInt(productFidelite.getText().toString()));
                                        produit.put("img_url",image_url);
                                        //on upadte le produit dans notre base de données
                                        firestore.collection("Product").document(id).update(produit).addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(root.getContext(),"document updated!",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                        );
                                    }
                                }
                            }
                        }
                    });

                }else{
                    //on ajoute un nouveau produit
                    firestore.collection("Product").add(productModel).addOnSuccessListener(
                            new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(root.getContext(),"document created!",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on créer l'intent pour ouvrir la galorie dans notre téléphone
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(openGalleryIntent,1000);
                //on start l'activité pour récuoérer une image
                activityResultLauncher.launch(openGalleryIntent);

                Picasso.with(root.getContext()).load(image_url).into(imageView);
            }
        });

        return root;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //on récupere l'image dans notre telephone et on l'insérer dans la base donnée
                        Intent data = result.getData();
                        Uri imageUri= data.getData();
                        uploadImageToFirebase(imageUri);
                    }
                }
            });

    private void uploadImageToFirebase(Uri imageUri){
        StorageReference fileRef=FirebaseStorage.getInstance().getReference().child("produit/");
        //on upload l'image dans la base de donnée
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Toast.makeText(getContext(),task.getResult().toString(),Toast.LENGTH_SHORT).show();
                        image_url=task.getResult().toString();
                    }
                });

            }
        });
    }
}
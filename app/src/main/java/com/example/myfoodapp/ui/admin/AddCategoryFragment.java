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

import com.bumptech.glide.Glide;
import com.example.myfoodapp.R;
import com.example.myfoodapp.models.NavCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class AddCategoryFragment extends Fragment {
NavCategoryModel model=null;
TextView name,description,discount;
ImageView imageView;
Button btn;
FirebaseFirestore firestore;
FirebaseStorage storage;
String image_url;
String id;

    public AddCategoryFragment() {
    }

    public AddCategoryFragment(NavCategoryModel model){
        this.model=model;
        image_url=model.getImg_url();
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
        View root= inflater.inflate(R.layout.fragment_add_category, container, false);
        name=(EditText)root.findViewById(R.id.category_name);
        description=(EditText)root.findViewById(R.id.category_description);
        discount=(EditText)root.findViewById(R.id.category_discount);
        imageView=(ImageView) root.findViewById(R.id.category_img);
        btn=(Button) root.findViewById(R.id.btn_category);
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        //on insérer les données recupéres lors du création du fragment dans leur champs adéquat
        if (model!=null){
            name.setText(model.getName());
            description.setText(model.getDescription());
            discount.setText(model.getDiscount());
            //on affichage l'image du catégorie
            Picasso.with(root.getContext()).load(model.getImg_url()).into(imageView);
            btn.setText("update");
        }

        // on click sur le button update
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavCategoryModel categoryModel=new NavCategoryModel(
                        name.getText().toString(),
                        description.getText().toString(),
                        discount.getText().toString(),
                        image_url
                );

                //si on a récupérer les informations lié au catégorie alors on modifie ce dernier sion on créer
                //un nouveau catégorie
                if(model!=null){
                    firestore.collection("NavCategory").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                                //on récupére l'id du catégorie
                                if (d.getString("name").equals(model.getName())) {
                                    id =d.getId();
                                    // on récupére les informations nécessaire au catégorie
                                    Map<String,Object> cat=new HashMap<>();
                                    cat.put("name",name.getText().toString());
                                    cat.put("description",description.getText().toString());
                                    cat.put("discount",discount.getText().toString());
                                    cat.put("img_url",image_url);
                                    //on modifie la catégorie dans la base de donnés
                                    firestore.collection("NavCategory").document(id).update(cat).addOnSuccessListener(
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
                    });
                }else{
                    //on créer un nouvaue catégorie
                    firestore.collection("NavCategory").add(categoryModel).addOnSuccessListener(
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

                StorageReference storageReference;
                storageReference = FirebaseStorage.getInstance().getReference();

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
        StorageReference fileRef=FirebaseStorage.getInstance().getReference().child("categorie/");
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
                Toast.makeText(getContext(),"image uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"error"+e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
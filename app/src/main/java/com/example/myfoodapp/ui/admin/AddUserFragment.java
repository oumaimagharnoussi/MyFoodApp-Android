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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.concurrent.atomic.AtomicMarkableReference;


public class AddUserFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storage;
    FirebaseFirestore store;
    UserModel userModel=null;

    EditText mail,number,name,address,password;
    Button updateBtn;
    ImageView image;
    RadioButton isUser,isAdmin;
    String Id;


    public AddUserFragment() {
        // Required empty public constructor
    }

    public AddUserFragment(UserModel userModel,String Id){
        this.userModel=userModel;
        this.Id=Id;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_add_user, container, false);

        mail=(EditText)root.findViewById(R.id.profile_email);
        password=(EditText)root.findViewById(R.id.profile_password);
        number=(EditText)root.findViewById(R.id.profile_phone);
        name=(EditText)root.findViewById(R.id.profile_name);
        address=(EditText)root.findViewById(R.id.profile_address);
        updateBtn=(Button)root.findViewById(R.id.update);
        image=(ImageView) root.findViewById(R.id.profile_img);
        isUser=root.findViewById(R.id.role_user);
        isAdmin=root.findViewById(R.id.role_admin);

        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store=FirebaseFirestore.getInstance();
        //DocumentReference doc=store.collection("users").document(user.getUid());


        if (userModel!=null){
            storage = FirebaseStorage.getInstance().getReference().child("users/"+Id+"/profile.jpg");
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri){
                        Picasso.with(root.getContext()).load(uri).into(image);
                    }
                });
            if (userModel.getMail()!=null)  mail.setText(userModel.getMail());
            if (userModel.getName()!=null)    name.setText(userModel.getName());
            if (userModel.getNumber()!=null)    number.setText(userModel.getNumber());
            if (userModel.getAddress()!=null)    address.setText(userModel.getAddress());
            if (userModel.getRole().equals("admin")){
                isAdmin.isChecked();
            }else {
                isUser.isChecked();
            }

            }




        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailText=mail.getText().toString();
                String passText=password.getText().toString();
                String nameText=name.getText().toString();
                String numberText=number.getText().toString();
                String addressText=address.getText().toString();

                String roleUpdated="user";
                if(isAdmin.isChecked()){
                    roleUpdated="admin";
                }


                if(userModel==null){
                    //on ajoute un nouveau utilisateur
                    String finalRoleUpdated = roleUpdated;
                    auth.createUserWithEmailAndPassword(mailText,passText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String id = task.getResult().getUser().getUid();
                                String img_url="users/"+id+"/profile.jpg";
                                UserModel userModel = new UserModel(img_url,mailText,passText,nameText,numberText,addressText, finalRoleUpdated);
                                //database.getReference().child("Users").child(id).setValue(userModel);
                                store.collection("users").document(id)
                                        .set(userModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "user added", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "error" + e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
                }else {
                    store.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot d:queryDocumentSnapshots) {
                                if (d.get("mail")==mailText&&d.get("pass")==passText){
                                    //on récupére l'id du l'utilisateur
                                    Id =d.getId();
                                }
                            }
                        }
                    });
                    String img_url="users/"+ Id +"/profile.jpg";
                    Map<String,Object> userUpdated=new HashMap<>();
                    userUpdated.put("name",name.getText().toString());
                    userUpdated.put("pass",password.getText().toString());
                    userUpdated.put("number",number.getText().toString());
                    userUpdated.put("mail",mail.getText().toString());
                    userUpdated.put("address",address.getText().toString());
                    userUpdated.put("role",roleUpdated);
                    store.collection("users").document(Id)
                            .update(userUpdated).addOnSuccessListener(new OnSuccessListener<Void>() {
                        public void onSuccess(Void aVoid){

                            Toast.makeText(root.getContext(), "success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(openGalleryIntent,1000);
                //on start l'activité pour récuoérer une image
                activityResultLauncher.launch(openGalleryIntent);
                //on associe le lien de l'image dans la base donné
                storage = FirebaseStorage.getInstance().getReference().child("users/"+user.getUid()+"/profile.jpg");
                //on récupére l'image dans notre base de donné
                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri){
                        Picasso.with(root.getContext()).load(uri).into(image);
                    }
                });

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
        StorageReference fileRef=FirebaseStorage.getInstance().getReference().child(userModel.getImg_url());
        //on upload l'image dans la base de donnée
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Tag","update image sucess");
            }
        });
    }

}
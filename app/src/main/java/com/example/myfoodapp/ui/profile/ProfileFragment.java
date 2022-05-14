package com.example.myfoodapp.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storage;
    FirebaseFirestore store;

    EditText mail,number,name,address;
    Button updateBtn;
    ImageView image;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile,container,false);
        mail=(EditText)root.findViewById(R.id.profile_email);
        number=(EditText)root.findViewById(R.id.profile_phone);
        name=(EditText)root.findViewById(R.id.profile_name);
        address=(EditText)root.findViewById(R.id.profile_address);
        updateBtn=(Button)root.findViewById(R.id.update);
        image=(ImageView) root.findViewById(R.id.profile_img);

        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store=FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference().child("users/"+user.getUid()+"/profile.jpg");
        DocumentReference doc=store.collection("users").document(user.getUid());

        // on récupére l'image et les informations du l'utilisateur pour les modifier
        doc.addSnapshotListener((documentSnapshot,e)->{
            if (documentSnapshot.exists()){
                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri){
                        Picasso.with(root.getContext()).load(uri).into(image);
                    }
                });
                mail.setText(documentSnapshot.getString("mail"));
                name.setText(documentSnapshot.getString("name"));
                number.setText(documentSnapshot.getString("number"));
                address.setText(documentSnapshot.getString("address"));

            }else{
                Toast.makeText(root.getContext(), "document introuvable", Toast.LENGTH_SHORT).show();
            }

        });


        //on modifie les données de l'utilisateur
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailText=mail.getText().toString();
                Map<String,Object> userUpdated=new HashMap<>();
                userUpdated.put("name",name.getText().toString());
                userUpdated.put("number",number.getText().toString());
                userUpdated.put("mail",mail.getText().toString());
                userUpdated.put("address",address.getText().toString());


                if(user!=null){
                    user.updateEmail(mailText);
                    doc.update(userUpdated).addOnSuccessListener(new OnSuccessListener<Void>() {
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

   /*
    public void onActivityResult(int requestCode,int resultCode,@andoidx.anotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){

            }
        }
    }*/

    private void uploadImageToFirebase(Uri imageUri){
        StorageReference fileRef=FirebaseStorage.getInstance().getReference().child("users/"+user.getUid()+"/profile.jpg");
        //on upload l'image dans la base de donnée
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Tag","update image sucess");
            }
        });
    }

}
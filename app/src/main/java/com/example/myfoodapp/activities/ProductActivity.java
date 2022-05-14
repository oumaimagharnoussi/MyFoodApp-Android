package com.example.myfoodapp.activities;import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;import android.os.Bundle;
import android.widget.Toast;import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.HomeVerAdapter;
import com.example.myfoodapp.models.HomeVerModel;
import com.example.myfoodapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;import java.util.ArrayList;
import java.util.List;
public class ProductActivity extends AppCompatActivity { RecyclerView recyclerView;
    FirebaseFirestore db;
    List<ViewAllModel> productModel; @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //get la catégorie pour l'affichages de ses produits
        String category=getIntent().getStringExtra("category");
        db = FirebaseFirestore.getInstance();

        //configuration du recyler view du produits
        recyclerView = findViewById(R.id.product_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this,RecyclerView.VERTICAL,false));
        productModel = new ArrayList<>();


        //HomeverAdapter est l'adaptateur du produit
        HomeVerAdapter adapter = new HomeVerAdapter(ProductActivity.this, productModel);
        recyclerView.setAdapter(adapter);

        //récupération de tous les produits
        db.collection("Product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ViewAllModel ProductModel = document.toObject(ViewAllModel.class);
                                //vérification du produit appartient au catégorie spécifié auparavant ou non
                                if (ProductModel.getCategorie().equals(category)){

                                    productModel.add(ProductModel); adapter.notifyDataSetChange();
                                }
                            }
                            //affichage des items du recyler view contenant dans la liste productModel à l'aide
                            //du l'adaptateur
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(ProductActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


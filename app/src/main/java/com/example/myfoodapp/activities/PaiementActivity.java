package com.example.myfoodapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myfoodapp.PlacedOrderActivity;
import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CartModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaiementActivity extends AppCompatActivity {
    Button confirm;
    RadioButton paymentDelivery;
    RadioButton paymentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);
        confirm=findViewById(R.id.confirm_order_button);
        paymentCard=findViewById(R.id.cardPayment);
        paymentDelivery=findViewById(R.id.deliveryPayment);

        Float total = (float) getIntent().getFloatExtra("total",0);

        List<CartModel> list = (ArrayList<CartModel>) getIntent().getSerializableExtra("itemList");



        //on choisie la methode de paiement à la livraison ou paiement avec card et on transfére les données
        //lié au order model(commande) pour la sauvgarder aprés la fin de processus
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paymentCard.isChecked()){
                    Intent intent = new Intent(PaiementActivity.this, CardPaiementActivity.class);
                    intent.putExtra("itemList", (Serializable) list);
                    intent.putExtra("total", total);
                    intent.putExtra("paymentMethod", "card");
                    startActivity(intent);
                }else if (paymentDelivery.isChecked()){
                    Intent intent = new Intent(PaiementActivity.this, PlacedOrderActivity.class);
                    intent.putExtra("itemList", (Serializable) list);
                    intent.putExtra("total", total);
                    intent.putExtra("paymentMethod", "delivery");
                    startActivity(intent);

                }else{
                    Toast.makeText(PaiementActivity.this, "Please choose a payment method!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
package com.aqeel.johnwick.pakforcestestupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aqeel.johnwick.pakforcestestupdate.Models.Subject;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForcesnameActivity extends AppCompatActivity {
    Button airBtn, armyBtn, navyBtn;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcesname);


        airBtn = findViewById(R.id.forcesname_btn_pakairforce);
        armyBtn = findViewById(R.id.forcesname_btn_pakarmy);
        navyBtn = findViewById(R.id.forcesname_btn_paknavy);
        airBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    clicked("pak air force");

            }
        });

        armyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    clicked("pak army");


            }
        });

        navyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked("pak navy");


            }
        });


    }



    void clicked(String buttonName){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("forceName", buttonName);
        startActivity(intent);



    }
}


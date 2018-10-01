package com.aqeel.johnwick.pakforcestestupdate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aqeel.johnwick.pakforcestestupdate.Adapters.ChapterAdapter;
import com.aqeel.johnwick.pakforcestestupdate.Models.Chapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChaptersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String subjectId ;
    TextView subjectNameText ;
    FirebaseFirestore firestore;
    List<Chapter> chapterList = new ArrayList<>();
    CardView loadingCard ;

    AdView adView, loadingAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        //loadAd();




        recyclerView = findViewById(R.id.chapters_recycler_chapters);
        subjectNameText = findViewById(R.id.chapters_text_subjectname);
        loadingCard = findViewById(R.id.chapters_card_loading);

        isLoading(true);

        if (getIntent().hasExtra("subjectId")) {
            subjectId = getIntent().getStringExtra("subjectId");

        }
        else{

        }

        if( getIntent().hasExtra("subjectName")){
            subjectNameText.setText(getIntent().getStringExtra("subjectName"));

        }else{

        }

        firestore = FirebaseFirestore.getInstance();




        firestore.collection("pakforcestestupdate").document(subjectId).collection("chapters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("name").toString();
                                chapterList.add(new Chapter(name, subjectId, document.getId()));




                            }
                            printsubjects() ;
                        } else {
                            Toast.makeText(ChaptersActivity.this, "Failed to connect Internet\nChapters Loading Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    void printsubjects(){
        recyclerView.setAdapter(new ChapterAdapter(chapterList, ChaptersActivity.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        isLoading(false);

    }

    void isLoading(boolean isShow){
        if(isShow){
            recyclerView.setVisibility(View.GONE);
            subjectNameText.setVisibility(View.GONE);
            loadingCard.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            subjectNameText.setVisibility(View.VISIBLE);
            loadingCard.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mShare:

                shareCall();

                break;

            case R.id.mInfo:
                infoCall();
                break;


        }

        return super.onOptionsItemSelected(item);

    }


    void infoCall(){
        Intent intent = new Intent(ChaptersActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    void shareCall(){
        Intent  i = new Intent(

                Intent.ACTION_SEND);

        i.setType("text/plain");

        i.putExtra(

                Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.aqeel.johnwick.pakforcestestupdate");

        startActivity(Intent.createChooser(

                i,

                "Share Using"));
    }


//    void loadAd(){
//
//        MobileAds.initialize(this, getString(R.string.App_ID));
//        adView = findViewById(R.id.chapters_adView);
//        loadingAdview = findViewById(R.id.chapter_loading_adView);
//        AdRequest request = new AdRequest.Builder().build();
//        AdRequest requestLoading = new AdRequest.Builder().build();
//
//        loadingAdview.loadAd(requestLoading);
//        adView.loadAd(request);
//    }
}

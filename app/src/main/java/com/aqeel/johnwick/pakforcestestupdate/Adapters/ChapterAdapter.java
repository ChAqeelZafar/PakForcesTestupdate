package com.aqeel.johnwick.pakforcestestupdate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aqeel.johnwick.pakforcestestupdate.ChaptersActivity;
import com.aqeel.johnwick.pakforcestestupdate.Models.Chapter;
import com.aqeel.johnwick.pakforcestestupdate.PreprationActivity;
import com.aqeel.johnwick.pakforcestestupdate.R;
import com.aqeel.johnwick.pakforcestestupdate.SubjectsActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.Holder> {
    List<Chapter> chaptersList = new ArrayList<>();
    Context ctx ;
    InterstitialAd interstitialAd;

    public ChapterAdapter(List<Chapter> chaptersList, ChaptersActivity ctx) {
        this.chaptersList = chaptersList;
        this.ctx = ctx ;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MobileAds.initialize(ctx,
                ctx.getString(R.string.App_ID));

        interstitialAd = new InterstitialAd(ctx);
        interstitialAd.setAdUnitId(ctx.getString(R.string.Chapter_item_interstialAd));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapterviewholder, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final Chapter chapter = chaptersList.get(position);
        holder.chapternameText.setText(chapter.getChapterName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(haveNetworkConnection()) {


                    if(interstitialAd.isLoaded()) {
                        // Step 1: Display the interstitial
                        interstitialAd.show();
                        // Step 2: Attach an AdListener
                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                // Step 2.1: Load another ad
                                AdRequest adRequest = new AdRequest.Builder().build();
                                interstitialAd.loadAd(adRequest);

                                // Step 2.2: Start the new activity
                                Intent intent = new Intent(ctx, PreprationActivity.class);
                                intent.putExtra("chapterName", chapter.getChapterName());
                                intent.putExtra("subjectId", chapter.getSubjectId());
                                intent.putExtra("chapterId", chapter.getChapterId());
                                ctx.startActivity(intent);
                            }
                        });
                    }else {

                        Intent intent = new Intent(ctx, PreprationActivity.class);
                        intent.putExtra("chapterName", chapter.getChapterName());
                        intent.putExtra("subjectId", chapter.getSubjectId());
                        intent.putExtra("chapterId", chapter.getChapterId());
                        ctx.startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(ctx, "Internet is Unavailble\nConnect to the Internet", Toast.LENGTH_SHORT).show();
                    ctx.startActivity(new Intent(ctx, SubjectsActivity.class));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return chaptersList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView chapternameText ;

        public Holder(@NonNull View itemView) {
            super(itemView);
            chapternameText = itemView.findViewById(R.id.chapterviewholder_text_chaptername);
            cardView = itemView.findViewById(R.id.chapterviewholder_card);
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

package com.aqeel.johnwick.pakforcestestupdate;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aqeel.johnwick.pakforcestestupdate.Models.Question;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    Spinner spinner;
    int totalQuestions = 0 ;
    int liveQuestion = 0 ;
    TextView statementText, totalText, correctText ;

    RadioGroup radioGroup;
    RadioButton o1Radio, o2Radio, o3Radio, o4Radio;
    String subjectName ;

    Button nextBtn, skipBtn, checkBtn ;


    int correctScore = 0 ;

    AdView adViewUp, adViewDown;




    ArrayList<Question>  questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //loadAd();


        statementText = findViewById(R.id.quiz_questionStatement);
        totalText = findViewById(R.id.quiz_text_total);
        radioGroup = findViewById(R.id.quiz_radiogroup);
        nextBtn = findViewById(R.id.quiz_btn_next);
        skipBtn = findViewById(R.id.quiz_btn_skip);
        checkBtn = findViewById(R.id.quiz_btn_check);
        correctText = findViewById(R.id.quiz_text_correct);

        o1Radio = findViewById(R.id.quiz_radio_o1);
        o2Radio = findViewById(R.id.quiz_radio_o2);
        o3Radio = findViewById(R.id.quiz_radio_o3);
        o4Radio = findViewById(R.id.quiz_radio_o4);







        loadFirstQuestion();



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtnClicked();
            }
        });


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipBtnClicked();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBtn.getText().toString().equals("SHOW")){
                    correctText.setVisibility(View.VISIBLE);
                    checkBtn.setText("HIDE");
                }
                else{
                    correctText.setVisibility(View.GONE);
                    checkBtn.setText("SHOW");
                }

            }
        });

    }

    void startQuiz(int liveQuestion){

        totalText.setText(liveQuestion + 1 + "/" + totalQuestions);
        Question question = questionList.get(liveQuestion);
        statementText.setText(question.getStatement());
        correctText.setVisibility(View.GONE);
        checkBtn.setText("SHOW");


        radioGroup.clearCheck();
        if(question.getOption1().equals("")){
            o1Radio.setVisibility(View.GONE);
        }
        else{
            o1Radio.setVisibility(View.VISIBLE);
            o1Radio.setText(question.getOption1());

        }

        if(question.getOption2().equals("")){
            o2Radio.setVisibility(View.GONE);
        }
        else{
            o2Radio.setVisibility(View.VISIBLE);
            o2Radio.setText(question.getOption2());


        }

        if(question.getOption3().equals("")){
            o3Radio.setVisibility(View.GONE);
        }
        else{
            o3Radio.setVisibility(View.VISIBLE);
            o3Radio.setText(question.getOption3());

        }

        if(question.getOption4().equals("")){
            o4Radio.setVisibility(View.GONE);
        }
        else{
            o4Radio.setVisibility(View.VISIBLE);
            o4Radio.setText(question.getOption4());

        }

        if(question.getCorrect().equals("")){
            correctText.setText("Not Available");

        }
        else{
            correctText.setText(question.getCorrect());

        }

    }

    void loadFirstQuestion(){

        Intent i = getIntent();
        questionList = (ArrayList<Question>) i.getSerializableExtra("questionsList");
        totalQuestions = questionList.size();

        Collections.shuffle(questionList);
        startQuiz(liveQuestion);
    }

    void nextBtnClicked(){
        if(liveQuestion  < totalQuestions -1) {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(QuizActivity.this, "Select any option", Toast.LENGTH_SHORT).show();
            } else {

                if (o1Radio.isChecked() && o1Radio.getText().toString().equals(correctText.getText()) ||  o2Radio.isChecked() && o2Radio.getText().toString().equals(correctText.getText())  ||  o3Radio.isChecked() && o3Radio.getText().toString().equals(correctText.getText()) || o4Radio.isChecked() && o4Radio.getText().toString().equals(correctText.getText())) {
                    correctScore++;

                } else {

                }

                startQuiz(++liveQuestion);

            }
        }
        else{
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(QuizActivity.this, "Select any option", Toast.LENGTH_SHORT).show();
            } else {
                startResultActivity();
            }

        }
    }

    void skipBtnClicked(){
        //Toast.makeText(QuizActivity.this, "live : " + liveQuestion + "  Totak : "+ totalQuestions, Toast.LENGTH_SHORT).show();

        if(liveQuestion    < (totalQuestions -1 )) {
            startQuiz(++liveQuestion);

        }
        else{
            startResultActivity();

        }
    }

    void startResultActivity(){

        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("subjectName", subjectName);
        intent.putExtra("correctQuestions", correctScore + "");
        intent.putExtra("totalQuestions", totalQuestions + "");
        startActivity(intent);
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
        Intent intent = new Intent(QuizActivity.this, AboutActivity.class);
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
//        adViewUp = findViewById(R.id.quiz_up_adView);
//        adViewDown = findViewById(R.id.quiz_down_adView);
//        AdRequest requestUp = new AdRequest.Builder().build();
//        AdRequest requestDown = new AdRequest.Builder().build();
//
//
//        adViewUp.loadAd(requestUp);
//        adViewDown.loadAd(requestDown);
//    }


}

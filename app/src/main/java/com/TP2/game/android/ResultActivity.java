package com.TP2.game.android;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerViewAdapter adapter;
    ArrayList<Trial> TrialList;
    Button export;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        export = findViewById(R.id.Export);
        RecyclerView recyclerView = findViewById(R.id.Trials);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        TrialList = (ArrayList<Trial>) intent.getSerializableExtra("key");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, TrialList);
        recyclerView.setAdapter(adapter);
        displayResults();
        export.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fitts' law test");
        intent2.putExtra(Intent.EXTRA_TEXT, listToString(TrialList) );
        startActivity(Intent.createChooser(intent2, "Share via"));
    }

    protected void displayResults(){

        double meanDifficulty = getDifficultyAvg();
        double meanTime = getTimeAvg();
        double difficultyStd = getDifficultyStd(  meanDifficulty  );
        double timeStd = getTimeStd(  meanTime  );
        double r = getR();

//        double b = r * timeStd / difficultyStd;
//        double  a = meanTime - b * meanDifficulty;
//        double r2 = pow(  r, 2  );

        double b = Math.round(  (  r * timeStd / difficultyStd  ) * 1000000d  ) / 1000000d;
        double  a = Math.round(  (  meanTime - b * meanDifficulty  ) * 1000000d  ) / 1000000d;
        double r2 = Math.round(  (  pow(  r, 2  )  ) * 1000000d  ) / 1000000d;

        TextView text = findViewById(R.id.thirdTextView);
        text.setText(getString(R.string.linearRegression, a, b, r2));
    }

    protected double getTimeAvg(){
        double meanTime = 0;

        for(  int i = 0; i < 20; i++  ){
            meanTime = meanTime + TrialList.get(i).time;
        }
        return meanTime / 20;
    }

    protected double getDifficultyAvg(){
        double meanDifficulty = 0;

        for(  int i = 0; i < 20; i++  ){
            meanDifficulty = meanDifficulty + TrialList.get(i).difficulty;
        }

        return meanDifficulty / 20;
    }

    protected double getTimeStd(double meanTime){
        double timeStd = 0;

        for(  int i = 0; i < 20; i++  ){
            timeStd = timeStd + Math.pow(  TrialList.get(i).time - meanTime, 2  );
        }

        return sqrt(  timeStd / 20  );
    }

    protected double getDifficultyStd(double meanDifficulty){
        double difficultyStd = 0;

        for(  int i = 0; i < 20; i++  ){
            difficultyStd = difficultyStd + Math.pow(  TrialList.get(i).difficulty - meanDifficulty, 2  );
        }

        return sqrt(  difficultyStd / 20  );
    }

    protected double getR(){

        double num = 0;
        double denum1 = 0;
        double denum2 = 0;

        for(  int i = 0; i < 20; i++  ){

            num = num + TrialList.get(i).time * TrialList.get(i).difficulty;
            denum1 = denum1 + pow(  TrialList.get(i).time, 2  );
            denum2 = denum2 + pow(  TrialList.get(i).difficulty, 2  );
        }

        return num / sqrt(  denum1 * denum2  );

    }


    public String listToString(  ArrayList<Trial> arr  ){
        StringBuilder builder = new StringBuilder();
        for (  int i = 0; i < 20; i++  ) {
            builder.append( "Trial = " + arr.get(i).trial + " " +
                            "Difficulty = " + arr.get(i).difficulty + " " +
                            "Time = " + arr.get(i).time + "\n"  );
        }
        return builder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this, MainActivity.class);
        startActivity(intent3);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}

package com.TP2.game.android;

//Mohamed Elayat, Pierre Luc Munger, Arnaud L'heureux

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

    //This activity displays the result page
    //of the app and offers the exporting
    //feature. It also allows to restart the
    //game by clicking the back button.

    RecyclerViewAdapter adapter;
    ArrayList<Trial> TrialList;
    RecyclerView recyclerView;
    Button export;
    ActionBar actionBar;
    int numberOfTrials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        initializeVariables();
    }

    protected void initializeVariables(){
        numberOfTrials = getResources().getInteger(R.integer.numberOfTrials);
        export = findViewById(R.id.Export);
        recyclerView = findViewById(R.id.Trials);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);      //displays the back button
        export.setOnClickListener(this);

        TrialList = (ArrayList<Trial>) getIntent().getSerializableExtra("key");     //obtains the ArrayList passed by the intent
        adapter = new RecyclerViewAdapter(this, TrialList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        displayResults();       //displays the linear regression results.
    }


    //Exports the list information using a string
    @Override
    public void onClick(View v) {
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fitts' law test");
        intent2.putExtra(Intent.EXTRA_TEXT, listToString(TrialList) );
        startActivity(Intent.createChooser(intent2, "Share via"));
    }

    //calculates the linear regression variables and displays them
    protected void displayResults(){
        double meanDifficulty = getDifficultyAvg();
        double meanTime = getTimeAvg();
        double difficultyStd = getDifficultyStd(meanDifficulty);
        double timeStd = getTimeStd(meanTime);
        double r = getR();

        double b = Math.round((r * timeStd / difficultyStd) * 1000000d) / 1000000d;
        double a = Math.round((meanTime - b * meanDifficulty) * 1000000d) / 1000000d;
        double r2 = Math.round((pow(r, 2)) * 1000000d) / 1000000d;

        TextView text = findViewById(R.id.thirdTextView);
        text.setText(getString(R.string.linearRegression, a, b, r2));
    }

    //returns the avg trial time
    protected double getTimeAvg(){
        double meanTime = 0;
        for(int i = 0; i < numberOfTrials; i++){
            meanTime += TrialList.get(i).time;
        }
        return meanTime / numberOfTrials;
    }

    //returns the avg difficulty across all trials
    protected double getDifficultyAvg(){
        double meanDifficulty = 0;
        for(int i = 0; i < numberOfTrials; i++){
            meanDifficulty += TrialList.get(i).difficulty;
        }
        return meanDifficulty / numberOfTrials;
    }

    //returns the avg trial time squared
    protected double getTime2Avg(){
        double mean2Time = 0;
        for(int i = 0; i < numberOfTrials; i++){
            mean2Time += pow(TrialList.get(i).time, 2);
        }
        return mean2Time / numberOfTrials;
    }

    //returns the avg difficulty squared
    protected double getDifficulty2Avg(){
        double mean2Difficulty = 0;
        for(int i = 0; i < numberOfTrials; i++){
            mean2Difficulty += pow(TrialList.get(i).difficulty, 2);
        }
        return mean2Difficulty / numberOfTrials;
    }

    //returns the avg time * difficulty value
    protected double getTimeXDifficultyAvg(){
        double meanTimeXDifficulty = 0;
        for(int i = 0; i < numberOfTrials; i++){
            meanTimeXDifficulty += (TrialList.get(i).difficulty) * (TrialList.get(i).time);
        }
        return meanTimeXDifficulty / numberOfTrials;
    }

    //returns the time standard deviation
    protected double getTimeStd(double meanTime){
        double timeStd = 0;
        for(int i = 0; i < numberOfTrials; i++){
            timeStd += Math.pow(TrialList.get(i).time - meanTime, 2);
        }
        return sqrt(timeStd / numberOfTrials);
    }

    //returns the difficulty standard deviation
    protected double getDifficultyStd(double meanDifficulty){
        double difficultyStd = 0;
        for(int i = 0; i < numberOfTrials; i++){
            difficultyStd += Math.pow(TrialList.get(i).difficulty - meanDifficulty, 2);
        }
        return sqrt(difficultyStd / numberOfTrials);
    }

    //returns the sample correlation coefficient
    protected double getR(){
        double num = getTimeXDifficultyAvg() - getDifficultyAvg() * getTimeAvg();
        double denum1 = getTime2Avg() - pow(getTimeAvg(), 2);
        double denum2 = getDifficulty2Avg() - pow(getDifficultyAvg(), 2);

        return num / sqrt(denum1 * denum2);
    }

    //method that stores the list information in a string
    //to be exported
    public String listToString(ArrayList<Trial> arr){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberOfTrials; i++) {
            builder.append(  getString(R.string.export_string,
                    arr.get(i).trial,
                    arr.get(i).difficulty,
                    arr.get(i).time)  );
        }
        return builder.toString();
    }

    //method that is called when the back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //method that restarts the game when called
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this, MainActivity.class);
        startActivity(intent3);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}

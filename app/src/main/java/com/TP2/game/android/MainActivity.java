package com.TP2.game.android;

//Mohamed Elayat, Pierre Luc Munger, Arnaud L'heureux

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Random;
import static java.lang.Math.*;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    //This activity handles the generation
    //of random button position and sizes.
    //It also calculates the duration and difficulty
    //of each trial. Upon reaching the last trial,
    //the activity launches the ResultActivity.

    Button button;      //main purple button
    Chronometer chrono;     //stopwatch
    ArrayList<Trial> Trials;        //ArrayList to store trial information
    DisplayMetrics metrics;

    boolean playMode;       //indicates if game has started
    boolean running;    //indicates if stopwatch is running or not.
    int count;      //to keep track of trials
    int screenWidth;
    int screenHeight;
    int smallestDim;
    int posX;       //stores last trial's click X position
    int posY;       //stores last trials' click Y position
    long pauseTime;     //indicates the user's reaction time.
    int numberOfTrials;
    int currentButtonSize;
    public static Resources resources;  //to access resources from non-activity classes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
    }

    //initializes the application variables
    protected void initializeVariables(){
        button = findViewById(R.id.button);
        chrono = findViewById(R.id.chrono);
        resources = getResources();
        count = 0;
        numberOfTrials = getResources().getInteger(R.integer.numberOfTrials);
        Trials = new ArrayList<>();
        running = false;
        playMode = false;
        getScreenDimensions();
        button.setOnTouchListener(this);
    }

    //listener for the main button
    //Checks if the game has started or not.
    //It performs different functions depending
    //on the count and the type of touch.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(playMode == false){
            if (event.getAction() == MotionEvent.ACTION_UP) {
                playMode = true;
                button.setText(null);
                endedTouch(event);
            }
        }
        else {
            if(count == numberOfTrials){
                startedTouch(event);
                count++;
            }
            else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startedTouch(event);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    endedTouch(event);
                }
            }
        }
        return false;
    }


    //ends a trial
    protected void startedTouch(MotionEvent event){
        intoArray(count, pauseChrono(), diffultyIndex(event.getX(), event.getY()));
        if(count == numberOfTrials){
            showResult();
        }
    }

    //starts a trial
    protected void endedTouch(MotionEvent event){
        count++;
        resetChrono();
        startChrono();
        savePosition(event.getX(), event.getY());
        if(count <=  numberOfTrials) {
            newPosition();
        }
    }

    //adds a trial to the arrayList
    protected void intoArray(int count, int time, double d){
        Trials.add(new Trial(count, time, d));
    }

    //returns the difficulty index rounded to 6 digits.
    protected double diffultyIndex(double x, double y){
        double distance = Math.sqrt(Math.pow(x-posX,2) + Math.pow(y-posY,2));
        return Math.round((log(distance / currentButtonSize + 1) / log(2))
                * 1000000d) / 1000000d;
    }

    //saves the current click position that will be needed
    //for next trial's difficulty index
    protected void savePosition(float x, float y){
        posX = (int)x;
        posY = (int)y;
    }

    //launches result activity and finishes the current activity
    protected void showResult(){
        Intent myIntent = new Intent(this, ResultActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra("key", Trials);
        startActivity(myIntent);
    }

    //gives the button a random size and location
    protected void newPosition(){
        LinearLayout.LayoutParams layout =
                new LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);

        currentButtonSize = randomButtonSize();
        layout.width = currentButtonSize;
        layout.height = currentButtonSize;
        layout.leftMargin = randomXOffset(layout.width);
        layout.topMargin = randomYOffset(layout.height);
        button.setLayoutParams(layout);
    }

    public int randomXOffset(int width){
        Random random = new Random();
        return random.nextInt(  screenWidth  - width  );
    }

    public int randomYOffset(int height){
        Random random = new Random();
        return random.nextInt(  screenHeight - height  );
    }

    //returns random button size of size in the interval
    //[smallestDim/25 , smallestDim/2]
    public int randomButtonSize(){
        Random random = new Random();
        int temp = random.nextInt(smallestDim / 2 - smallestDim / 25);
        return smallestDim / 25 + temp;
    }


    //sets the screen width, height and smallest dimension
    protected void getScreenDimensions(){
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels - getActionBarHeight() - getStatusBarHeight();
        if(screenWidth < screenHeight){
            smallestDim = screenWidth;
        }
        else{
            smallestDim = screenHeight;
        }
    }

    //returns the status bar height
    protected int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        else{
            return 0;
        }
    }

    //returns the action bar height
    protected int getActionBarHeight(){
        TypedValue dim = new TypedValue();
        if(getTheme().resolveAttribute(android.R.attr.actionBarSize, dim, true)){
            return TypedValue.complexToDimensionPixelSize(  dim.data, getResources().getDisplayMetrics()  );
        }
        else{
            return 0;
        }
    }

    //starts stopwatch
    public void startChrono(){
        if(!running){
            chrono.setBase(SystemClock.elapsedRealtime() - pauseTime);
            chrono.start();
            running = true;
        }
    }

    //returns the running time of stopwatch when paused.
    public int pauseChrono(){
        if(running){
            pauseTime = SystemClock.elapsedRealtime() - chrono.getBase();
            chrono.stop();
            running = false;
        }
        return (int)pauseTime;
    }

    //resets stopwatch
    public void resetChrono() {
        chrono.setBase(SystemClock.elapsedRealtime());
        pauseTime = 0;
    }


}


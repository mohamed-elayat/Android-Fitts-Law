package com.TP2.game.android;

import android.app.ActionBar;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Random;
import static java.lang.Math.*;



public class MainActivity extends AppCompatActivity {

    Button button;
    boolean playMode;
    int screenWidth;
    int screenHeight;
    int smallestDim;
    int posX;
    int posY;

    Chronometer chrono;
    boolean running;    //indicates if stopwatch is running or not.
    long pauseTime;     //indicates the user's reaction time.


    ArrayList<Trial> Trials;

    DisplayMetrics metrics;
    int count;

    //Initializes the application variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        chrono = findViewById(R.id.chrono);
        count = 0;
        Trials = new ArrayList<>();
        running = false;
        playMode = false;

        getScreenDimensions();

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                if(  playMode == false  ){

                    if (  event.getAction() == MotionEvent.ACTION_UP  ) {
                        playMode = true;
                        button.setText(null);
                        endedTouch(event);
                    }

                }

                else {
                    if (  event.getAction() == MotionEvent.ACTION_DOWN  ) {
                        startedTouch(event);
                    }

                    if (  event.getAction() == MotionEvent.ACTION_UP  ) {
                        endedTouch(event);
                    }
                }
                return false;
            }
        });

    }


    protected void startedTouch(MotionEvent event){
        intoArray(  count, pauseChrono(), diffultyIndex(event.getX())  );
        if( count == 20 ) {
            showResult();
        }
    }

    protected void endedTouch(MotionEvent event){
        count++;
        resetChrono();
        startChrono();
        savePosition(event.getX(), event.getY());
        newPosition();
    }

    protected void intoArray(  int count, int time, double d ){
        Trials.add(  new Trial(count, time, d)  );
    }

    protected double diffultyIndex(double x){
        return Math.round(  (  log( abs(x - posX) / button.getWidth() + 1  ) / log(2)  )
                * 1000000d  ) / 1000000d;
    }

    protected void savePosition(float x, float y){
        posX = (int)x;
        posY = (int)y;
    }


    protected void showResult(){
        Intent myIntent = new Intent(this, ResultActivity.class);
        myIntent.putExtra("key", Trials); //Optional parameters
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(  myIntent, 1  );
    }

    protected void newPosition(){
        LinearLayout.LayoutParams layout =
                new LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT  );

        int temp = randomButtonSize();
        layout.width = temp;
        layout.height = temp;
        layout.leftMargin = randomXOffset(  layout.width  );
        layout.topMargin = randomYOffset(  layout.height  );
        button.setLayoutParams(  layout  );
    }

    protected int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        else{
            return 0;
        }
    }

    protected int getActionBarHeight(){
        TypedValue dim = new TypedValue();
        if(  getTheme().resolveAttribute(android.R.attr.actionBarSize, dim, true)  ){
            return TypedValue.complexToDimensionPixelSize(  dim.data, getResources().getDisplayMetrics()  );
        }
        else{
            return 0;
        }
    }

    protected void getScreenDimensions(){
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(  metrics  );
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels - getActionBarHeight() - getStatusBarHeight();
        if(  screenWidth < screenHeight  ){
            smallestDim = screenWidth;
        }
        else{
            smallestDim = screenHeight;
        }
    }

    public int randomXOffset(  int width  ){
        Random random = new Random();
        return random.nextInt(  screenWidth  - width  );
    }

    public int randomYOffset(  int height  ){
        Random random = new Random();
        return random.nextInt(  screenHeight - height  );
    }

    public int randomButtonSize(){
        Random random = new Random();
        int temp = random.nextInt(  smallestDim / 2 - smallestDim / 25  );
        return smallestDim / 25 + temp;
    }

    public void startChrono(){
        if(  !running  ){
            chrono.setBase(  SystemClock.elapsedRealtime() - pauseTime  );
            chrono.start();
            running = true;
        }
    }

    //Method to pause stopwatch
    public int pauseChrono(){
        if(  running  ){
            pauseTime = SystemClock.elapsedRealtime() - chrono.getBase();
            chrono.stop();
            running = false;
        }
        return (int)pauseTime;
    }
    //Method to reset stopwatch
    public void resetChrono() {
        chrono.setBase(SystemClock.elapsedRealtime());
        pauseTime = 0;
    }


}


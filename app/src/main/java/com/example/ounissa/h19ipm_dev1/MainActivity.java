package com.example.ounissa.h19ipm_dev1;

//Mohamed Elayat, Ounissa Nait Amer

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    Handler handler;
    String state;

    int screenWidth;
    int screenHeight;

    int statusBarHeight;
    int actionBarHeight;

    DisplayMetrics metrics;


    //Initializes the application variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);


        getStatusBarHeight();
        getActionBarHeight();
        getScreenDimensions();

        stateZero();

    }


    //Listener for the application button.
    @Override
    public void onClick(View v) {

        if(  state == "stateZero"  ){
            playState();
        }

    }

    protected void stateZero(){

        state = "stateZero";
        handler = new Handler();
        button.setBackgroundColor(  getResources().getColor(R.color.purple)  );
        button.setText(  "CLIQUEZ POUR COMMENCER"  );

    }

    protected void playState(){

        newPosition();

    }

//    public long randomTime(){
//        Random random = new Random();
//        double randomDouble = 3 + random.nextDouble() * 7;
//        long randomTime = (long)  (  randomDouble * 1000  );
//        return randomTime;
//    }

    public int randomXOffset(  int width  ){

        Random random = new Random();
        return random.nextInt(  screenWidth  - width  );

    }

    public int randomYOffset(  int height  ){

        Random random = new Random();
        return random.nextInt(  screenHeight - height  );

    }

    protected void newPosition(){

        LinearLayout.LayoutParams layout2 =
                new LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT  );

        layout2.width = 600;
        layout2.height = 600;

        layout2.leftMargin = randomXOffset(  layout2.width  );
        layout2.topMargin = randomYOffset(  layout2.height  );

        button.setLayoutParams(  layout2  );

    }

    protected void getStatusBarHeight(){

        statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

    }

    protected void getActionBarHeight(){

        actionBarHeight = 0;
        TypedValue dim = new TypedValue();

        if(  getTheme().resolveAttribute(android.R.attr.actionBarSize, dim, true)  ){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(  dim.data, getResources().getDisplayMetrics()  );
        }

    }

    protected void getScreenDimensions(){

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(  metrics  );
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels - actionBarHeight - statusBarHeight;

    }


}


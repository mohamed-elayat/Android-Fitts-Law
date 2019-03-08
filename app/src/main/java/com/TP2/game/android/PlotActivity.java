package com.TP2.game.android;

//Mohamed Elayat, Pierre Luc Munger, Arnaud L'heureux

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

public class PlotActivity extends AppCompatActivity{
    ArrayList<Trial> TrialList;
    ActionBar actionBar;
    double a;
    double b;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        initializeVariables();
    }

    protected void initializeVariables(){
        actionBar = getSupportActionBar();

        //displays the back button
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        //Obtaining variables from last intent
        TrialList = (ArrayList<Trial>) getIntent().getSerializableExtra("key");
        a = bundle.getDouble("key_a");
        b = bundle.getDouble("key_b");

        drawGraph();
    }

    //Function generating the corresponding graph
    public void drawGraph(){
        ArrayList<Trial> sortedList = new ArrayList<>(TrialList);

        //Creating a copy of TrialList in order not to modify the display
        //from the last activity
        Collections.sort(sortedList);

        graph = findViewById(R.id.graph);

        DataPoint[] points = new DataPoint[sortedList.size()];

        for(int i = 0; i < points.length; i++){
            points[i] = new DataPoint(sortedList.get(i).difficulty, sortedList.get(i).time);
        }

        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(points);

        //x0,y0 being the y-intercept and x1,y1 the right-most point from the trend line
        double x0 = 0;
        double x1 = sortedList.get(sortedList.size()-1).difficulty;
        double y0 = b;
        double y1 = a*x1 + b;
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(x0,y0),
                new DataPoint(x1,y1)
        });

        graph.addSeries(series);
        graph.addSeries(series2);

        //Creating labels for axes
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Indice de difficulté");
        gridLabel.setVerticalAxisTitle("Temps (ms)");

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(x1+0.4);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        series.setShape(PointsGraphSeries.Shape.POINT);
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

    //method that goes back to the previous activity
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, ResultActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra("key", TrialList);
        startActivity(myIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
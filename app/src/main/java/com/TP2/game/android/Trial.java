package com.TP2.game.android;

//Mohamed Elayat, Pierre Luc Munger, Arnaud L'heureux

import android.util.Log;

import java.io.Serializable;

public class Trial implements Serializable, Comparable<Trial> {

    //Trial class that stores the information for
    //each individual trial.

    protected int trial;
    protected double difficulty;
    protected int time;

    public Trial(int trial, int time, double difficulty){
        this.trial = trial;
        this.time = time;
        this.difficulty = difficulty;
    }

    @Override
    public int compareTo(Trial t){
        Log.i("custom",""+this.trial +" and "+t.trial);
        return (this.difficulty > t.difficulty ? 1 : this.difficulty < t.difficulty ? -1
                : 0);
    }

}



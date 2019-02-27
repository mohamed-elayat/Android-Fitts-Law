package com.TP2.game.android;

//Mohamed Elayat, Pierre Luc Munger, Arnaud L'heureux

import java.io.Serializable;

public class Trial implements Serializable {

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

}



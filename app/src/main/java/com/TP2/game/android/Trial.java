package com.TP2.game.android;

import java.io.Serializable;

public class Trial implements Serializable {

    protected int trial;
    protected double difficulty;
    protected int time;
    protected static final String TRIAL_PREFIX = "Essai ";
    protected static final String DIFFICULTY_PREFIX = "Difficulté: ";
    protected static final String TIME_PREFIX = ", Durée: ";
    protected static final String TIME_SUFFIX = " ms";

    public Trial(  int trial, int time, double difficulty  ){
        this.trial = trial;
        this.time = time;
        this.difficulty = difficulty;
    }

}



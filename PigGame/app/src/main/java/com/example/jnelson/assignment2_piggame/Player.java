package com.example.jnelson.assignment2_piggame;

/**
 * Created by Jnelson on 7/1/2015.
 */
public class Player {

    private int mScore;
    private int mNum;
    //private boolean mActive;

    public Player(int x)
    {
        mScore = 0;
        mNum = x;
        //mActive = false;
    }

    public void AddToScore(int x) {  this.mScore += x;    }

    public void setmScore(int x) {  this.mScore = x;    }
    public void setmNum(int x) {  this.mNum = x;    }
    //public void setmActive(boolean bool) {  this.mActive = bool;    }
    public int getmNum() {        return mNum;    }
    public int getmScore() {        return mScore;    }
    //public boolean getmActive() {    return mActive;   }

}

package com.example.jnelson.assignment2_piggame;

import java.util.Scanner; // for Console Tester

/**
 * Created by Jnelson on 7/1/2015.
 */

/* **********************************************************************************************************************
*                                                HOW TO PLAY PIG                                                        *
* Each turn, a player repeatedly rolls a die until either a 1 is rolled or the player decides to "hold":                *
*   <> If the player rolls a 1, they score nothing and it becomes the next player's turn.                               *
*   <> If the player rolls any other number, it is added to their turn total and the player's turn continues.           *
*   <> If a player chooses to "hold", their turn total is added to their score, and it becomes the next player's turn.  *
* The first player to score 100 or more points wins.                                                                    *
                                 https://en.wikipedia.org/wiki/Pig_(dice_game)#Gameplay                                 *
 ***********************************************************************************************************************/
public class PigGame {

    private Player mPlayer1;
    private Player mPlayer2;
    private Player[] mPlayers;
    private int mActivePlayerIndex;

    private int mWinnerIndex;
    private int[] mDie;

    private int mTempScore;

    // Game Rules Variables
    private int SIDES_ON_DIE = 6;
    private int POINTS_TO_WIN = 100;
    private int NUMBER_OF_DICE = 1;

    public PigGame(int playtoscore, int dieSides)
    {
        POINTS_TO_WIN = playtoscore;
        SIDES_ON_DIE = dieSides;

        mDie = new int[3];
        for (int i = 0; i<3; i++) {
            mDie[i] = 0;
    }

        // Create Players
        mPlayer1 = new Player(0);
        mPlayer2 = new Player(1);
        // Create Players Array
        mPlayers = new Player[2];
        mPlayers[0] =  mPlayer1;
        mPlayers[1] =  mPlayer2;
        mTempScore = 0;

        // Make Player 1 Active
        mActivePlayerIndex = 0; // Player 1 starts first
        mWinnerIndex = -1;
    }
    /****************************************************************************************
     *                               Setters and Getters                                    *
     ***************************************************************************************/
    public void setPOINTS_TO_WIN(int x) {  this.POINTS_TO_WIN = x;  }
    public int getPOINTS_TO_WIN() {  return POINTS_TO_WIN;  }
    public void setSIDES_ON_DIE(int x) {  this.SIDES_ON_DIE = x;  }
    public int getSIDES_ON_DIE() {  return SIDES_ON_DIE;  }
    public void setNUMBER_OF_DICE(int x) {  this.NUMBER_OF_DICE = x;  }
    public int getNUMBER_OF_DICE() {  return NUMBER_OF_DICE;  }

    public void setmActivePlayerIndex(int x) {  this.mActivePlayerIndex = x;  }
    public void setmDie(int i, int x) {  this.mDie[i] = x;  }
    public int  getmDie(int i) { return mDie[i]; }

    public void setmWinnerIndex(int x) {  this.mWinnerIndex = x;  }
    public void setmTempScore(int x) {  this.mTempScore = x;  }
    public int getmActivePlayerIndex() {  return mActivePlayerIndex;  }
    public Player getActivePlayer() { return mPlayers[mActivePlayerIndex]; }


    public Player[] getPlayers() { return mPlayers; }

    public int getmTempScore() {    return mTempScore;    }
    public int getmWinnerIndex() {     return mWinnerIndex;    }

    public void RollDie() {
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            mDie[i] = (int) (Math.random() * SIDES_ON_DIE) + 1; // Returns a Die equal to a value from 1 to 6 to simulate a die roll
            if (mDie[i] != 1) {
                mTempScore += mDie[i];
            } else {
                mTempScore = 0;
                break; // exit loop because a 1 was rolled and no points will be scored
            }
        }
            // Check to see if the active player has won
            if ((mPlayers[mActivePlayerIndex].getmScore() + mTempScore) >= POINTS_TO_WIN) {
                mPlayers[mActivePlayerIndex].AddToScore(mTempScore); // Force Award Points to end game and prevent stupid user play
                EndGame();
            }
    }

    public void PassTurn()
    {
        mPlayers[mActivePlayerIndex].AddToScore(mTempScore); // Award Points
        mTempScore = 0; // reset mTempScore
        if (mActivePlayerIndex == 0) {
            mActivePlayerIndex = 1; // Change Active Player
        }
        else { // Active player must be 2
            mActivePlayerIndex =0; // Change Active Player
        }
    }

    public void EndGame() {
        mWinnerIndex = mActivePlayerIndex; // Declares the active player the winner
        System.out.println("{ GAME OVER } -- Player" + Integer.toString(mActivePlayerIndex) + " Wins with " + mPlayers[mActivePlayerIndex].getmScore());
    }


    /* *********************************************************************************************
     *                                   | Game Tester |                                           *
     * - Full console based test program                                                           *
     *                                                                                             *
     ***********************************************************************************************/

    public static void main(String[] args)
    {
        PigGame game = new PigGame(100, 6);

        /****************************************************************************************
         *                             Automated Game Simulation                                *
         * 	                         (uncomment to run simulation)                              *
         ***************************************************************************************/

        int rolls = 0;
    	/*
		while (game.mWinnerIndex == -1) {
			game.RollDie();
			System.out.println("Player "+game.getmActivePlayerIndex() + " rolled a: " + game.getmDie());
			System.out.println("Player "+game.getmActivePlayerIndex() + " score is: " + game.getPlayers()[game.getmActivePlayerIndex()].getmScore());
			if (game.getmDie() == 1 || rolls%3 == 2) {
				game.PassTurn(); System.out.println("Player " + game.getmActivePlayerIndex() + " is now the active player");
			}
			rolls++;
		}
      // */

        Scanner stdin = new Scanner(System.in);
        System.out.println("################################\n" +
                "######## WELCOME TO PIG ########\n" +
                "################################\n" +
                "::: Game Commands :::  \n" +
                "-----------------------\n" +
                "<1>  to roll the die\n<2>  to pass the turn\n<-1> to exit");
        while (stdin.hasNextInt())
        {
            int next = stdin.nextInt();

            switch (next) {
                case 1:
                    game.RollDie();
                    for (int i =0; i<game.NUMBER_OF_DICE; i++) {
                        System.out.println("You Rolled a: " + game.getmDie(i));
                    }
                        System.out.println("Your Score is: " + game.getActivePlayer().getmScore());
                         break;
                case 2:
                    game.PassTurn();
                    System.out.println("Player " + game.getmActivePlayerIndex() + " is now the active player");
                    break;
                case -1:
                    System.out.println("Exiting game");
                    break;
            }
        }
    }
}
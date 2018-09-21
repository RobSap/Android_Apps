package com.example.jnelson.assignment2_piggame;
/**
 * Created by Jnelson on 7/1/2015.
 * Development Environment:
 * - Android Studio 1.2.2 Build#AI-141.1980579, built on June 3, 2015
 * - JRE 1.7.0_71-b14 amd64
 * - Android OS version 15
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    public boolean logging = true;

    private SharedPreferences savedValues;
    private int PLAYTOSCORE = 100;
    private int SIDESONDIE = 6;
    private int NUMBEROFDICE = 1;
    private int[] mDieImages;
    private int BACKGROUND;
    private LinearLayout mLayout;


    private PigGame game;

    private TextView mP1Score;
    private TextView mP2Score;
    private TextView mTempScoreView;
    private int mTempScore;
    private TextView[] mPlayerScores;

    private EditText mP1Name;
    private EditText mP2Name;

    private TextView mWhosTurn;
    private TextView mTurnAux;

    private ImageView mDieImage;
    private ImageView mDieImage2;
    private ImageView mDieImage3;
    private ImageView[] mDice;

    private Button mRollDiceButton;
    private Button mPassTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (logging) Log.d("MainActivity", "Start: OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout =(LinearLayout)findViewById(R.id.main);

        game = new PigGame(PLAYTOSCORE, SIDESONDIE);

        // initialize image views
        mDieImage = (ImageView) findViewById(R.id.dieImage);
        mDieImage2 = (ImageView) findViewById(R.id.dieImage2);
        mDieImage3 = (ImageView) findViewById(R.id.dieImage3);

        mDice = new ImageView[3];

        mDice[0] = mDieImage; mDice[1]= mDieImage2; mDice[2]=mDieImage3;

        savedValues = PreferenceManager.getDefaultSharedPreferences(this);


        // Initialize UI elements
        mP1Score = (TextView) findViewById(R.id.p1ScoreValue);
        mP2Score = (TextView) findViewById(R.id.p2ScoreValue);

        mPlayerScores = new TextView[2]; // TO prevent excessive If statements. This interacts with Player.getmNum
        mPlayerScores[0] = mP1Score;
        mPlayerScores[1] = mP2Score;

        mP1Name = (EditText) findViewById(R.id.player1EditText);
        mP2Name = (EditText) findViewById(R.id.player2EditText);

        mTempScoreView = (TextView) findViewById(R.id.tempScore);
        mTempScore = 0;

        mWhosTurn = (TextView) findViewById(R.id.whosTurn);
        mWhosTurn.setText("Player 1");

        mTurnAux = (TextView) findViewById(R.id.turnAux);

        mRollDiceButton = (Button) findViewById(R.id.rollDie);
        mPassTurn = (Button) findViewById(R.id.passTurn);




        if( savedInstanceState != null ) {
            // Load Player and Temporary round scores
            game.getPlayers()[0].setmScore(savedInstanceState.getInt("mPlayer1_Score_save"));
            game.getPlayers()[1].setmScore(savedInstanceState.getInt("mPlayer2_Score_save"));
            game.setmTempScore(savedInstanceState.getInt("mTempScore_save"));
            UpdateScores(); // Update Scores UIs

            game.setmActivePlayerIndex(savedInstanceState.getInt("mActivePlayerIndex_Save"));
            UpdateActivePlayer(); // Update UI

            game.setmDie(savedInstanceState.getInt("mDie_Save1"),0);
            if (NUMBEROFDICE > 1) { game.setmDie(savedInstanceState.getInt("mDie_Save2"),1); }
            if (NUMBEROFDICE > 1) { game.setmDie(savedInstanceState.getInt("mDie_Save3"),2); }
            UpdateDie(mDice[0], 1); // Update Die UI

            game.setmWinnerIndex(savedInstanceState.getInt("mWinnerIndex_save")); //Update Game State

        }
        if (logging) Log.d("MainActivity", "Finish: OnCreate()");
    }

    @Override
    protected void onStart() {
        if (logging) Log.d("MainActivity", "Start: onStart()");

        super.onStart();

        if (logging) Log.d("MainActivity", "Finish: onStart()");
    }

    @Override protected void onResume()
    {
        if (logging) Log.d("MainActivity", "Start: OnResume()");

        super.onResume();
        /* ************************************************************** *
        *                       Load User Settings                        *
        * *************************************************************** */

        // PLAY TO SCORE
        PLAYTOSCORE = Integer.parseInt(savedValues.getString("pref_play_to_score", "20"));
        game.setPOINTS_TO_WIN(PLAYTOSCORE);

        // SIDES ON DIE
        SIDESONDIE = Integer.parseInt(savedValues.getString("pref_sides_to_die", "6"));
        game.setSIDES_ON_DIE(SIDESONDIE);

        mDieImages = new int[SIDESONDIE];

                switch (SIDESONDIE) {
                    case 6: mDieImages[0] = R.drawable.die1;
                        mDieImages[1] = R.drawable.die2;
                        mDieImages[2] = R.drawable.die3;
                        mDieImages[3] = R.drawable.die4;
                        mDieImages[4] = R.drawable.die5;
                        mDieImages[5] = R.drawable.die6;
                        break;
                    case 10: mDieImages[0] = R.drawable.tendie1;
                        mDieImages[1] = R.drawable.tendie2  ;
                        mDieImages[2] = R.drawable.tendie3;
                        mDieImages[3] = R.drawable.tendie4;
                        mDieImages[4] = R.drawable.tendie5;
                        mDieImages[5] = R.drawable.tendie6;
                        mDieImages[6] = R.drawable.tendie7;
                        mDieImages[7] = R.drawable.tendie8;
                        mDieImages[8] = R.drawable.tendie9;
                        mDieImages[9] = R.drawable.tendie10;
                        break;
                }

        // NUMBER OF DICE
        NUMBEROFDICE = Integer.parseInt(savedValues.getString("pref_number_of_dice", "1"));
        game.setNUMBER_OF_DICE(NUMBEROFDICE);

        mDice[0].setImageResource(mDieImages[4]);
        if (NUMBEROFDICE > 1) {
            mDice[1].setImageResource(mDieImages[4]);
            if (NUMBEROFDICE > 2) {
                mDice[2].setImageResource(mDieImages[4]);
            } else {
                mDice[2].setImageResource(0);
            }
        }
        else {
            mDice[1].setImageResource(0);
            mDice[2].setImageResource(0);
        }
        // SET BACKGROUND
        BACKGROUND = Integer.parseInt(savedValues.getString("pref_bgcolor", "0"));
        if (BACKGROUND == 2) {
            mLayout.setBackgroundColor(1);
        }
        else if (BACKGROUND == 1) {
        mLayout.setBackgroundResource(R.drawable.pigbackground);
        }

        if (logging) Log.d("MainActivity", "Finish: OnResume()");
    }

    @Override
    protected void onPause() {
        if (logging) Log.d("MainActivity", "Start: onPause()");
        super.onPause();
        //SharedPreferences.Editor editor = savedValues.edit();
        //editor.putInt("count", count);
        //editor.commit();
        if (logging) Log.d("MainActivity", "Finish: onPause()");

    }

    @Override
    protected void onStop() {
        if (logging) Log.d("MainActivity", "Start: onStop()");
        super.onStop();
        if (logging) Log.d("MainActivity", "Finish: onStop()");
    }

    @Override
    protected void onDestroy() {
        if (logging) Log.d("MainActivity", "Start: onDestroy()");
        super.onDestroy();
        if (logging) Log.d("MainActivity", "Finish: onDestroy()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Game Saves
        outState.putInt("mDie_Save1", game.getmDie(0));
        if (NUMBEROFDICE > 1) { outState.putInt("mDie_Save2", game.getmDie(1)); }
        if (NUMBEROFDICE > 2) { outState.putInt("mDie_Save3", game.getmDie(2)); }

        //outState.putInt("mDie_Save3", game.getmDie(2));
        outState.putInt("mActivePlayerIndex_Save", game.getmActivePlayerIndex());
        outState.putInt("mWinnerIndex_save", game.getmWinnerIndex());
        outState.putInt("mPlayer1_Score_save", game.getPlayers()[0].getmScore());
        outState.putInt("mPlayer2_Score_save", game.getPlayers()[1].getmScore());
        outState.putInt("mTempScore_Save", game.getmTempScore());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        else if (id== R.id.action_about) {
            Toast.makeText(this, " Game Rules can be found at https://en.wikipedia.org/wiki/Pig_(dice_game)#Gameplay", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /***********************************************************************************************
     *                                  UI Update Methods                                          *
     **********************************************************************************************/
    // Update Scores
    public void UpdateScores()
    {
        mTempScoreView.setText(Integer.toString(game.getmTempScore()));
        mP1Score.setText(Integer.toString(game.getPlayers()[0].getmScore()) );
        mP2Score.setText( Integer.toString(game.getPlayers()[1].getmScore()) );
    }

    public void  UpdateDie(ImageView imgv, int x)
    {
        System.out.println("Die Roll was: "+ game.getmDie(x)); // DEBUG
        switch (game.getmDie(x)) { // Update UI to display the dice roll
            case 1: imgv.setImageResource(mDieImages[0]);
                mTempScore = 0;
                mRollDiceButton.setEnabled(false);
                break;
            default: imgv.setImageResource(mDieImages[game.getmDie(x)-1]);
                break;
        }
    }
    public void UpdateActivePlayer()
    {
        switch (game.getmActivePlayerIndex()) {
            case 0:
                mWhosTurn.setText(mP1Name.getText());
                break;
            case 1:
                mWhosTurn.setText(mP2Name.getText());
                break;
        }
    }
    /***********************************************************************************************
     *                                  UI Event Listeners                                         *
     **********************************************************************************************/
    public void RollOnClick(View target) {
        game.RollDie();

        int outcome = 0;
        for (int i=0; i<NUMBEROFDICE; i++) {
            outcome += game.getmDie(i);
            UpdateDie(mDice[i],i);
        }


        if (outcome != 0)
            mTempScore += outcome;
        mTempScoreView.setText(Integer.toString(game.getmTempScore()));

        // If someone has Won the game disable play buttons and declare the winner
        if (game.getmWinnerIndex() != -1) {
            UpdateScores();
            mRollDiceButton.setEnabled(false);
            mPassTurn.setEnabled(false);
            mTurnAux.setText(" WINS!");
        }
    }

    public void PassTurnOnClick(View target) {
        game.PassTurn();

        UpdateScores(); // Update Scores
        UpdateActivePlayer(); // Update Whos turn it is indicator

        //System.out.println("Active Player: " + Integer.toString(game.getmActivePlayer().getmNum()) );

       mRollDiceButton.setEnabled(true);
   }

    public void NewGameOnClick(View target) {
        game = new PigGame(PLAYTOSCORE, SIDESONDIE);
        onStart();
        onResume();

        //enable play buttons
        mRollDiceButton.setEnabled(true);
        mPassTurn.setEnabled(true);

        // Update Text fields
        UpdateScores(); // Update Scores
        mTurnAux.setText("'s turn");
        mTempScoreView.setText("0");
    }
}

// Image.setImageResource(id) /// use for setting die images
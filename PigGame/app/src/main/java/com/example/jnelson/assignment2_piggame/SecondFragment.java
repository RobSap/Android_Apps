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

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


public class SecondFragment extends Fragment implements View.OnClickListener {
    public int logging = 2; // 0 for OFF // 1 for method status // 2 for print statements
    private SecondActivity activity;

    private SharedPreferences savedValues;
    private int PLAYTOSCORE = 100;
    private int SIDESONDIE = 6;
    private int NUMBEROFDICE = 1;
    private int[] mDieImages;
    private int BACKGROUND;
    private LinearLayout mLayout;


    private PigGame game;
    // WIDGETS //
    private TextView mP1Score;
    private TextView mP2Score;
    private TextView mTempScoreView;
    private int mTempScore;
    private TextView[] mPlayerScores;

    private TextView mP1Name;
    private TextView mP2Name;

    private TextView mWhosTurn;
    private TextView mTurnAux;

    private ImageView mDieImage;
    private ImageView mDieImage2;
    private ImageView mDieImage3;
    private ImageView[] mDice;

    private Button mRollDiceButton;
    private Button mPassTurnButton;
    private Button mNewGameButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (logging > 0) Log.d("SecondFragment", "Start: onCreateView()");
        setHasOptionsMenu(true); // Needed for settings to show up when clicked on
        //super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.second_fragment, container, false);

        mLayout = (LinearLayout) view.findViewById(R.id.main);

        game = new PigGame(PLAYTOSCORE, SIDESONDIE);

        // initialize image views
        mDieImage = (ImageView) view.findViewById(R.id.dieImage);
        mDieImage2 = (ImageView) view.findViewById(R.id.dieImage2);
        mDieImage3 = (ImageView) view.findViewById(R.id.dieImage3);

        mDice = new ImageView[3];

        mDice[0] = mDieImage;
        mDice[1] = mDieImage2;
        mDice[2] = mDieImage3;


        // Initialize UI elements
        mP1Score = (TextView) view.findViewById(R.id.p1ScoreValue);
        mP2Score = (TextView) view.findViewById(R.id.p2ScoreValue);

        mPlayerScores = new TextView[2]; // TO prevent excessive If statements. This interacts with Player.getmNum
        mPlayerScores[0] = mP1Score;
        mPlayerScores[1] = mP2Score;

        mP1Name = (TextView) view.findViewById(R.id.player1Text);
        mP2Name = (TextView ) view.findViewById(R.id.player2Text);


        mTempScoreView = (TextView) view.findViewById(R.id.tempScore);
        mTempScore = 0;

        mWhosTurn = (TextView) view.findViewById(R.id.whosTurn);
        mWhosTurn.setText("Player 1");

        mTurnAux = (TextView) view.findViewById(R.id.turnAux);

        mRollDiceButton = (Button) view.findViewById(R.id.rollDie);
        mRollDiceButton.setOnClickListener(this);
        mPassTurnButton = (Button) view.findViewById(R.id.passTurn);
        mPassTurnButton.setOnClickListener(this);
        mNewGameButton = (Button) view.findViewById(R.id.newgame);
        mNewGameButton.setOnClickListener(this);


        if (savedInstanceState != null) {
            // Load Player and Temporary round scores
            game.getPlayers()[0].setmScore(savedInstanceState.getInt("mPlayer1_Score_save"));
            game.getPlayers()[1].setmScore(savedInstanceState.getInt("mPlayer2_Score_save"));
            game.setmTempScore(savedInstanceState.getInt("mTempScore_save"));
            UpdateScores(); // Update Scores UIs

            game.setmActivePlayerIndex(savedInstanceState.getInt("mActivePlayerIndex_Save"));
            UpdateActivePlayer(); // Update UI

            game.setmDie(savedInstanceState.getInt("mDie_Save1"), 0);
            if (NUMBEROFDICE > 1) {
                game.setmDie(savedInstanceState.getInt("mDie_Save2"), 1);
            }
            if (NUMBEROFDICE > 1) {
                game.setmDie(savedInstanceState.getInt("mDie_Save3"), 2);
            }
            UpdateDie(mDice[0], 1); // Update Die UI

            game.setmWinnerIndex(savedInstanceState.getInt("mWinnerIndex_save")); //Update Game State

        }
        UpdateScores(); // Update Scores UIs
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (logging > 0) Log.d("FirstFragment", "Start: onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        if (logging > 0) Log.d("SecondFragment", "Start: onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        if (logging > 0) Log.d("SecondFragment", "Start: OnResume()");
        activity = (SecondActivity) getActivity(); // Get a references from the host activity
        activity.SetGame(game);
        savedValues = PreferenceManager.getDefaultSharedPreferences(activity); // saveValues stored in Activity ?

        if (logging > 1) System.out.println("Player 1 name: " + activity.p1name);
        mP1Name.setText(activity.p1name);
        if (logging > 1) System.out.println("Player 2 name: " + activity.p2name);
        mP2Name.setText(activity.p2name);

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
            case 6:
                mDieImages[0] = R.drawable.die1;
                mDieImages[1] = R.drawable.die2;
                mDieImages[2] = R.drawable.die3;
                mDieImages[3] = R.drawable.die4;
                mDieImages[4] = R.drawable.die5;
                mDieImages[5] = R.drawable.die6;
                break;
            case 10:
                mDieImages[0] = R.drawable.tendie1;
                mDieImages[1] = R.drawable.tendie2;
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
        } else {
            mDice[1].setImageResource(0);
            mDice[2].setImageResource(0);
        }
        // SET BACKGROUND
        BACKGROUND = Integer.parseInt(savedValues.getString("pref_bgcolor", "0"));
        if (BACKGROUND == 2) {
            mLayout.setBackgroundColor(1);
        } else if (BACKGROUND == 1) {
            mLayout.setBackgroundResource(R.drawable.pigbackground);
        }

    }

    @Override
    public void onPause() {
        if (logging > 0) Log.d("SecondFragment", "Start: onPause()");
        super.onPause();
        //SharedPreferences.Editor editor = savedValues.edit();
        //editor.putInt("count", count);
        //editor.commit();
    }

    @Override
    public void onStop() {
        if (logging > 0) Log.d("SecondFragment", "Start: onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (logging > 0) Log.d("SecondFragment", "Start: onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (logging > 0) Log.d("SecondFragment", "Start: onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        // Game Saves
        outState.putInt("mDie_Save1", game.getmDie(0));
        if (NUMBEROFDICE > 1) {
            outState.putInt("mDie_Save2", game.getmDie(1));
        }
        if (NUMBEROFDICE > 2) {
            outState.putInt("mDie_Save3", game.getmDie(2));
        }

        //outState.putInt("mDie_Save3", game.getmDie(2));
        outState.putInt("mActivePlayerIndex_Save", game.getmActivePlayerIndex());
        outState.putInt("mWinnerIndex_save", game.getmWinnerIndex());
        outState.putInt("mPlayer1_Score_save", game.getPlayers()[0].getmScore());
        outState.putInt("mPlayer2_Score_save", game.getPlayers()[1].getmScore());
        outState.putInt("mTempScore_Save", game.getmTempScore());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (logging > 0) Log.d("SecondFragment", "Start: onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (logging > 0) Log.d("SecondFragment", "Start: onOptionsItemSelected()");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(activity, "Setting", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SettingsActivity.class);
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(activity, " Game Rules can be found at https://en.wikipedia.org/wiki/Pig_(dice_game)#Gameplay", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ********************************************************************************************
     *                                 UI Update Methods                                          *
     * ********************************************************************************************
     */
    // Update Scores
    public void UpdateScores() {
        if (logging > 0) Log.d("SecondFragment", "Start: UpdateScores()");
        if (logging > 1)System.out.println("Temp score is: " + game.getmTempScore());
        if (logging > 1)System.out.println("Play 1 score is: " + game.getPlayers()[0].getmScore());
        if (logging > 1)System.out.println("Play 2 score is: " + game.getPlayers()[1].getmScore());
        mTempScoreView.setText(Integer.toString(game.getmTempScore()));
        mP1Score.setText(Integer.toString(game.getPlayers()[0].getmScore()));
        mP2Score.setText(Integer.toString(game.getPlayers()[1].getmScore()));
    }

    public void UpdateDie(ImageView imgv, int x) {
        if (logging > 0) Log.d("SecondFragment", "Start: UpdateDie()");
        if (logging > 1) System.out.println("Die Roll was: " + game.getmDie(x)); // DEBUG
        switch (game.getmDie(x)) { // Update UI to display the dice roll
            case 1:
                imgv.setImageResource(mDieImages[0]);
                mTempScore = 0;
                mRollDiceButton.setEnabled(false);
                break;
            default:
                imgv.setImageResource(mDieImages[game.getmDie(x) - 1]);
                break;
        }
    }

    public void UpdateActivePlayer() {
        if (logging > 0) Log.d("SecondFragment", "Start: UpdatingActivePlayer()");
        switch (game.getmActivePlayerIndex()) {
            case 0:
                mWhosTurn.setText(mP1Name.getText());
                break;
            case 1:
                mWhosTurn.setText(mP2Name.getText());
                break;
        }
    }

    /**
     * ********************************************************************************************
     *                                 UI Event Listeners                                         *
     * ********************************************************************************************
     */
    public void onClick(View target) {
        if (logging > 0) Log.d("SecondFragment", "Start: onClick()");

        switch (target.getId()) {

            /******************************
             *  Roll Dice Button Behavior  *
             ******************************/
            case (R.id.rollDie):
                game.RollDie();
                int outcome = 0;
                for (int i = 0; i < NUMBEROFDICE; i++) {
                    outcome += game.getmDie(i);
                    UpdateDie(mDice[i], i);
                }
                if (outcome != 0)
                    mTempScore += outcome;
                mTempScoreView.setText(Integer.toString(game.getmTempScore()));

                // If someone has Won the game disable play buttons and declare the winner
                if (game.getmWinnerIndex() != -1) {
                    UpdateScores();
                    mRollDiceButton.setEnabled(false);
                    mPassTurnButton.setEnabled(false);
                    mTurnAux.setText(" WINS!");
                }
                break;

            /******************************
             *  Pass Turn Button Behavior *
             ******************************/
            case (R.id.passTurn):
                game.PassTurn();
                UpdateScores(); // Update Scores
                UpdateActivePlayer(); // Update Whos turn it is indicator
                if (logging > 1) System.out.println("Active Player: " + game.getPlayers()[game.getmActivePlayerIndex()].getmName());
                mRollDiceButton.setEnabled(true);
                break;

            /******************************
             *  New Game Button Behavior  *
             ******************************/
            case (R.id.newgame):

                if (getActivity().findViewById(R.id.first_fragment) == null) {     // using two Activities
                    game = new PigGame(PLAYTOSCORE, SIDESONDIE);
                    onStart();
                    onResume();
                    //enable play buttons
                    mRollDiceButton.setEnabled(true);
                    mPassTurnButton.setEnabled(true);

                    // Update Text fields
                    UpdateScores(); // Update Scores
                    mTurnAux.setText("'s turn");
                    mTempScoreView.setText("0");
                    startActivity(new Intent(getActivity(), FirstActivity.class));
                }
                else { // using 1 Activity (two panes)
                    game = new PigGame(PLAYTOSCORE, SIDESONDIE);
                    onStart();
                    onResume();

                    //enable play buttons
                    mRollDiceButton.setEnabled(true);
                    mPassTurnButton.setEnabled(true);

                    // Update Text fields
                    UpdateScores(); // Update Scores
                    mTurnAux.setText("'s turn");
                    mTempScoreView.setText("0");
                }
                break;
        }
    }
}

// Image.setImageResource(id) /// use for setting die images
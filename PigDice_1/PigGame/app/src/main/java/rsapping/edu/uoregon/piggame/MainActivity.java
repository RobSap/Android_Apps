package rsapping.edu.uoregon.piggame;

import android.support.v7.app.AppCompatActivity;
import java.text.NumberFormat;
//import java.util.Random;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.MotionEvent;
//import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.inputmethod.InputMethodManager;


public class MainActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener, View.OnClickListener{


    // Brings in GameLogic Class
    GameLogic gameLogic = new GameLogic();



    // define variables for the widgets
    private EditText playerOneNameTextEdit;
    private EditText playerTwoNameTextEdit;

    private TextView playerOneScoreTextView;
    private TextView playerTwoScoreTextView;
    private TextView playersTurnTextView;
    private TextView pointsForThisTurnTextView;

    private Button   rollDieButton;
    private Button   endTurnButton;
    private Button   newGameButton;

    private ImageButton imageButton;

    //To save state
    private SharedPreferences savedValues;


    //Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playerOneNameTextEdit = (EditText) findViewById(R.id.playerOneNameTextEdit);
        playerTwoNameTextEdit = (EditText) findViewById(R.id.playerTwoNameTextEdit);

        playerOneScoreTextView = (TextView) findViewById(R.id.playerOneScoreTextView);
        playerTwoScoreTextView = (TextView) findViewById(R.id.playerTwoScoreTextView);
        playersTurnTextView = (TextView) findViewById(R.id.playersTurnTextView);
        pointsForThisTurnTextView = (TextView) findViewById(R.id.pointsForThisTurnTextView);

        rollDieButton = (Button) findViewById(R.id.rollDieButton);
        endTurnButton = (Button) findViewById(R.id.endTurnButton);
        newGameButton = (Button) findViewById(R.id.newGameButton);



        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setBackgroundResource(R.drawable.die1);


        // set the listeners
        playerOneNameTextEdit.setOnEditorActionListener(this);
        playerTwoNameTextEdit.setOnEditorActionListener(this);

        rollDieButton.setOnClickListener(this);
        endTurnButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);

        imageButton.setOnClickListener(this);

        //Default variables set, so have default values
        gameLogic.setCurrentDie(1) ;
        gameLogic.setCurrentPlayersTurn(1);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }


    //Get current status of game and display it in UI
    public void UpdateAndDisplay()
    {

        //Get current scores
        NumberFormat num = NumberFormat.getIntegerInstance();
        playerOneScoreTextView.setText(num.format(gameLogic.getPlayerOneScore()));
        playerTwoScoreTextView.setText(num.format(gameLogic.getplayerTwoScore()));


        //This displays whoevers turn it is, based on user entered field
       if( gameLogic.getCurrentPlayersTurn()==1)
            playersTurnTextView.setText(playerOneNameTextEdit.getText().toString() + "'s turn");

        else
           playersTurnTextView.setText(playerTwoNameTextEdit.getText().toString() + "'s turn");


        //Below will display whoever won
        //If Tie game
        if( gameLogic.checkPlayerOneWinConditions() == true & gameLogic.checkPlayerTwoWinConditions()==true)
        {
            pointsForThisTurnTextView.setText("Tie Game!");
            disbaleButtons();
        }
        //If player 1 wins
       else if(gameLogic.checkPlayerOneWinConditions() == true)
       {

           if(gameLogic.checkPlayerTwosLastChanceCondition()==true)
           {
               gameLogic.setPlayerTwoLastChance(1);
               pointsForThisTurnTextView.setText(num.format(gameLogic.getPlayerOneTurnScore() + gameLogic.getPlayerTwoTurnScore()));

           }
           else {

               pointsForThisTurnTextView.setText(playerOneNameTextEdit.getText().toString() + " wins");
               disbaleButtons();

           }

       }
        //If player 2 wins
        else if ( gameLogic.checkPlayerTwoWinConditions()==true)
       {
           pointsForThisTurnTextView.setText(playerTwoNameTextEdit.getText().toString() + " wins");
           disbaleButtons();
       }
        //Else Display normal message no one won
        else {

           pointsForThisTurnTextView.setText(num.format(gameLogic.getPlayerOneTurnScore() + gameLogic.getPlayerTwoTurnScore()));

       }
    }

    //Function to disable buttons when someone wins
    public void disbaleButtons()
    {
        rollDieButton.setEnabled(false);
        imageButton.setEnabled(false);
        endTurnButton.setEnabled(false);
    }



    //Save state of game
    @Override
    public void onPause() {
        // save the instance variables
         Editor editor = savedValues.edit();
        editor.putInt("getPlayerOneScore", gameLogic.getPlayerOneScore());
        editor.putInt("getPlayerOneTurnScore", gameLogic.getPlayerOneTurnScore());
        editor.putInt("getPlayerTwoScore", gameLogic.getplayerTwoScore());
        editor.putInt("getPlayerTwoTurnScore", gameLogic.getPlayerTwoTurnScore());
        editor.putInt("getCurrentDie", gameLogic.getCurrentDie());
        editor.putInt("getCurrentPlayersTurn", gameLogic.getCurrentPlayersTurn());
        editor.putInt("getPlayerTwoLastChance", gameLogic.getPlayerTwoLastChance());
        editor.putInt("getPlayerOneWins", gameLogic.getPlayerOneWins());

        //Save user inputed names
        editor.putString("player1name",playerOneNameTextEdit.getText().toString());
        editor.putString("player2name", playerTwoNameTextEdit.getText().toString() );




       // editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();
    }

    //Load state of game
    @Override
    public void onResume() {
        super.onResume();
        gameLogic.setPlayerOneScore(savedValues.getInt("getPlayerOneScore", 0));
        gameLogic.setPlayerOneTurnScore(savedValues.getInt("getPlayerOneTurnScore", 0));
        gameLogic.setPlayerTwoScore(savedValues.getInt("getPlayerTwoScore", 0));
        gameLogic.setPlayerTwoTurnScore(savedValues.getInt("getPlayerTwoTurnScore", 0));
        gameLogic.setCurrentDie(savedValues.getInt("getCurrentDie", 1));
        gameLogic.setCurrentPlayersTurn(savedValues.getInt("getCurrentPlayersTurn", 1));
        gameLogic.setPlayerTwoLastChance(savedValues.getInt("getPlayerTwoLastChance", 0));
        gameLogic.setPlayerOneWins(savedValues.getInt("getPlayerOneWins", 0));

        playerOneNameTextEdit.setText(savedValues.getString("player1name", "PigPlayer1"));
        playerTwoNameTextEdit.setText(savedValues.getString("player2name", "PigPlayer2"));


        // calculate and display
        UpdateAndDisplay();
    }


    //Listen for text edit
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

            //This allows people to press enter on the keyboard and change focus off the text exit
            // And back to the main layout, then i call on the function to close the keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(MainActivity.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            findViewById(R.id.mainLayout).requestFocus();
        }
        return false;
    }

    //Controls what die is displayed, and changes picture accordinly
    void rollDieVisual()
    {
        //Roll die actually calls the game logic to see the die
        switch(gameLogic.rollDie()) {
            case 1:
                imageButton.setBackgroundResource(R.drawable.die1);
                //Check for the unique case player one reached 100 and player 2 is on last chance roll, and rolls 1
                gameLogic.checkIfPlayerTwoRollOneOnLastChance();
                break;
            case 2:
                imageButton.setBackgroundResource(R.drawable.die2);
                break;
            case 3:
                imageButton.setBackgroundResource(R.drawable.die3);
                break;
            case 4:
                imageButton.setBackgroundResource(R.drawable.die4);
                break;
            case 5:
                imageButton.setBackgroundResource(R.drawable.die5);
                break;

            case 6:
                imageButton.setBackgroundResource(R.drawable.die6);
                break;

        }
        UpdateAndDisplay();
    }

    //Closes keyboard when touch outside text box and returns focus to background
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(MainActivity.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        findViewById(R.id.mainLayout).requestFocus();
        return true;
    }

    //Detects which button is clicked and acts accordingly
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.endTurnButton:
                //Switch flag to change turn
                gameLogic.playerEndTurn();
                gameLogic.switchPlayerTurn();
                UpdateAndDisplay();
                break;
            case R.id.newGameButton:
                //Call all setters setting to 0
                gameLogic.newGame();
                UpdateAndDisplay();
                rollDieButton.setEnabled(true);
                imageButton.setEnabled(true);
                endTurnButton.setEnabled(true);
                break;
            case R.id.imageButton:
                rollDieVisual();
                break;
            case R.id.rollDieButton:
                rollDieVisual();
                break;

        }
    }
}
package rsapping.edu.uoregon.piggame;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import java.text.NumberFormat;
//import java.util.Random;

import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;




public class MainActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener, View.OnClickListener{


    // Brings in GameLogic Class
    GameLogic gameLogic = new GameLogic();
    Handler handler = new Handler();
    AILogic AI = new AILogic();

    // define variables for the widgets
    private EditText playerOneNameTextEdit;
    private EditText playerTwoNameTextEdit;
    private  boolean AIEnabled = false;

    private TextView playerOneScoreTextView;
    private TextView playerTwoScoreTextView;
    private TextView playersTurnTextView;
    private TextView pointsForThisTurnTextView;

    private Button   rollDieButton;
    private Button   endTurnButton;
    private Button   newGameButton;

    private ImageButton imageButton;
    private ImageButton imageButton2;

    //To save state
    private SharedPreferences savedValues;


    //Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Below I get the ID for all widgets
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
        imageButton.setOnClickListener(this);


        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton2.setVisibility(View.INVISIBLE);


        // set the listeners
        playerOneNameTextEdit.setOnEditorActionListener(this);
        playerTwoNameTextEdit.setOnEditorActionListener(this);

        rollDieButton.setOnClickListener(this);
        endTurnButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);



        //Default variables set, so have default values
        gameLogic.setCurrentDie(1) ;
        gameLogic.setCurrentPlayersTurn(1);


        //Legecy code i used previously to set pref's
        // get SharedPreferences object
        //savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        //Not using default since I set default in java
        savedValues = PreferenceManager.getDefaultSharedPreferences(this);

        //If using more then 1 die, set defaults
        if(savedValues.getBoolean("pref_remember_number_of_die", false)==true)
        {
            playerTwoNameTextEdit.setText("AI");
            // Store EditText in Variable
            imageButton2.setBackgroundResource(R.drawable.die1);
            imageButton2.setOnClickListener(this);
            imageButton2.setVisibility(View.VISIBLE);
        }

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

    //Function to disable buttons when someone wins, only can start new game
    public void disbaleButtons()
    {
        rollDieButton.setEnabled(false);
        imageButton.setEnabled(false);
        imageButton2.setEnabled(false);
        endTurnButton.setEnabled(false);
    }

    //Ends turn (user hit a 1) so turns off UI, can only end turn or start new game
    public void endTurnUI()
    {
        rollDieButton.setEnabled(false);
        imageButton.setEnabled(false);
        imageButton2.setEnabled(false);
    }

    //Start turn, so turn back on all UI
    public void startTurnUI()
    {
        //Just in case check, if its enabled you should be able to take your turn
        //If end turn is off, that means game over and should not turn on roll buttons
        if(endTurnButton.isEnabled())
        {
            rollDieButton.setEnabled(true);
            imageButton.setEnabled(true);
            imageButton2.setEnabled(true);
        }
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
        editor.putInt("getCurrentDie2", gameLogic.getCurrentDie2());

        editor.putInt("getCurrentPlayersTurn", gameLogic.getCurrentPlayersTurn());
        editor.putInt("getPlayerTwoLastChance", gameLogic.getPlayerTwoLastChance());
        editor.putInt("getPlayerOneWins", gameLogic.getPlayerOneWins());

        //Save user inputed names
        editor.putString("player1name",playerOneNameTextEdit.getText().toString());
        editor.putString("player2name", playerTwoNameTextEdit.getText().toString() );

        //Get User preferenses last saved
        editor.putInt("getNumOfSidesOnDie" , gameLogic.getNumSidesOnDie() );
        editor.putInt("goalValue" , gameLogic.getGoalValue() );

        editor.putBoolean("AITurn", gameLogic.getAITurn());
        editor.putInt("numberOfDie", gameLogic.getNumberOfDie());
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
        gameLogic.setCurrentDie(savedValues.getInt("getCurrentDie", 0));
        gameLogic.setCurrentDie2(savedValues.getInt("getCurrentDie2", 0));
        AIEnabled = savedValues.getBoolean("pref_AI", false);
        dieVisual( savedValues.getInt("getCurrentDie", 0));
         dieVisual2( savedValues.getInt("getCurrentDie2", 0));
        gameLogic.setCurrentPlayersTurn(savedValues.getInt("getCurrentPlayersTurn", 1));
        gameLogic.setPlayerTwoLastChance(savedValues.getInt("getPlayerTwoLastChance", 0));
        gameLogic.setPlayerOneWins(savedValues.getInt("getPlayerOneWins", 0));

        playerOneNameTextEdit.setText(savedValues.getString("player1name", "PigPlayer1"));
        playerTwoNameTextEdit.setText(savedValues.getString("player2name", "PigPlayer2"));


        gameLogic.setNumSidesOnDie(savedValues.getInt("getNumOfSidesOnDie", 6));
        gameLogic.setGoalValue(savedValues.getInt("goalValue", 100));

        gameLogic.setAITurn(savedValues.getBoolean("AITurn", false));
        gameLogic.setNumberOfDie(savedValues.getInt("numberOfDie", 6));


        // calculate and display
        UpdateAndDisplay();
        //If it was the UI's turn, continue your turn, else it will ignore the call
        makeFakeClickDecision();
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
    void dieVisual(int die)
    {

        //Roll die actually calls the game logic to see the die
        switch(die) {
            case 1:
                imageButton.setBackgroundResource(R.drawable.die1);
                checkPlayerTwoLastTurn();
                endTurnUI();
                makeFakeClickEndTurn();
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
            case 7:
                imageButton.setBackgroundResource(R.drawable.die7);
                break;
            case 8:
                imageButton.setBackgroundResource(R.drawable.die8);
                break;
            case 9:
                imageButton.setBackgroundResource(R.drawable.die9);
                break;
            case 10:
                imageButton.setBackgroundResource(R.drawable.die10);
                break;
            case 11:
                imageButton.setBackgroundResource(R.drawable.die11);
                break;
            case 12:
                imageButton.setBackgroundResource(R.drawable.die12);
                break;


        }
        if(gameLogic.getNumberOfDie()==2)
        {
            dieVisual2(gameLogic.rollDie2());
        }
        else
            UpdateAndDisplay();
    }

    //Figure out which die to display for die 2
    void dieVisual2(int die)
    {
        switch (die) {
            case 1:
                imageButton2.setBackgroundResource(R.drawable.die1);
                endTurnUI();
                checkPlayerTwoLastTurn();
                makeFakeClickEndTurn();
                break;
            case 2:
                imageButton2.setBackgroundResource(R.drawable.die2);
                break;
            case 3:
                imageButton2.setBackgroundResource(R.drawable.die3);
                break;
            case 4:
                imageButton2.setBackgroundResource(R.drawable.die4);
                break;
            case 5:
                imageButton2.setBackgroundResource(R.drawable.die5);
                break;
            case 6:
                imageButton2.setBackgroundResource(R.drawable.die6);
                break;
            case 7:
                imageButton2.setBackgroundResource(R.drawable.die7);
                break;
            case 8:
                imageButton2.setBackgroundResource(R.drawable.die8);
                break;
            case 9:
                imageButton2.setBackgroundResource(R.drawable.die9);
                break;
            case 10:
                imageButton2.setBackgroundResource(R.drawable.die10);
                break;
            case 11:
                imageButton2.setBackgroundResource(R.drawable.die11);
                break;
            case 12:
                imageButton2.setBackgroundResource(R.drawable.die12);
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
                startTurnUI();
                //AFter player Clicks end turn, let the AI take its turn
                makeFakeClickDecision();
                break;

            case R.id.newGameButton:
                newGameCall();
                break;
            case R.id.imageButton:
                dieVisual(gameLogic.rollDie());
                break;
            case R.id.rollDieButton:
                dieVisual(gameLogic.rollDie());
                //If Entered rolldie by fake click, see if we want to roll again
                makeFakeClickDecision();
                break;
            case R.id.imageButton2:
                dieVisual(gameLogic.rollDie());
                break;

        }
    }

    //Creats options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activitiy_main_menu, menu);
        return true;
    }

    //Whats on optionsitemSelect
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Refresh might be implemented later
           /* case R.id.menu_refresh:
                //Need refresh display here
                Toast.makeText(this, "Calculation refreshed.", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            case R.id.menu_help:
                Toast.makeText(this, "Sorry! Help hasn't been implemented yet.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //Computers click to end turn
    public void fakeClickEndTurn()
    {
        new Handler().postDelayed(new Runnable(){ public void run() {endTurnButton.performClick(); }} , 1500);
    }

    //Computers click to roll the die
    public void fakeClickRollDie()
    {
        //Add a check just in case
        if(gameLogic.getCurrentPlayersTurn()==2 && rollDieButton.isEnabled())
            new Handler().postDelayed(new Runnable(){ public void run() {rollDieButton.performClick();; }} , 1500);
    }


    //Let the computer decide on which fake click to select
    void fakeClickSelector()
    {
        int tempNum = AI.decision(gameLogic.getPlayerTwoTurnScore());

        if(tempNum ==0)
            playersTurnTextView.setText("error");
        else if (tempNum ==1)
            fakeClickRollDie();
        else if(tempNum ==2 || gameLogic.getPlayerTwoTurnScore() + gameLogic.getplayerTwoScore() >= gameLogic.getGoalValue())
            fakeClickEndTurn();
        else
            playersTurnTextView.setText("error");
    }

    //Reset everything for a newgame, look up any settings changes, and update
    private void newGameCall()
    {
        imageButton.setBackgroundResource(R.drawable.die1);
        imageButton2.setBackgroundResource(R.drawable.die1);
        //Call all setters setting to 0
        gameLogic.newGame();
        UpdateAndDisplay();
        rollDieButton.setEnabled(true);
        imageButton.setEnabled(true);
        imageButton2.setEnabled(true);
        endTurnButton.setEnabled(true);

        //See if person wants usernames reset or not
        boolean resetPlayerNameEveryGame = savedValues.getBoolean("pref_reset_name_every_game", false);
        if(resetPlayerNameEveryGame == true) {
            playerOneNameTextEdit.setText("Player_1");
            playerTwoNameTextEdit.setText("Player_2");
        }

        //New settings only take affect on new game
        int dieSides = Integer.parseInt(savedValues.getString("pref_remember_number_of_side_on_die", "6"));
        int scoreGoal = Integer.parseInt(savedValues.getString("pref_goal", "10"));
        boolean twoDice = savedValues.getBoolean("pref_remember_number_of_die", false);

        //Load two die if true
        if(twoDice == true)
        {
            gameLogic.setNumberOfDie(2);

            //Turn this into a function
            imageButton2.setBackgroundResource(R.drawable.die1);
            imageButton2.setOnClickListener(this);
            imageButton2.setVisibility(View.VISIBLE);
        }
        else {
            gameLogic.setNumberOfDie(1);
            imageButton2.setVisibility(View.INVISIBLE);
            gameLogic.setCurrentDie2(0);
        }

        //Update scoregoal and dieSides if changed
        gameLogic.setGoalValue(scoreGoal) ;
        gameLogic.setNumSidesOnDie(dieSides);

        //Check if AI was turned on, if so enable it, and set name
        AIEnabled = savedValues.getBoolean("pref_AI", false);
        if(AIEnabled==true)
        {
            playerTwoNameTextEdit.setText("AI");
        }
    }

    //Check if can make fake click decision, if so do it
    private void makeFakeClickDecision()
    {
        if(gameLogic.getCurrentPlayersTurn() ==2 && AIEnabled == true)
            fakeClickSelector();
    }

    //Check if can make fake click end turn, if so do it
    private void makeFakeClickEndTurn()
    {
        if(gameLogic.getCurrentPlayersTurn() ==2 && AIEnabled == true)
            fakeClickEndTurn();
    }

    //Check players two if they had their last turn, when player 1 reached their goal, and P2 they got a 1
   private void checkPlayerTwoLastTurn()
   {
       gameLogic.checkIfPlayerTwoRollOneOnLastChance();
   }
}
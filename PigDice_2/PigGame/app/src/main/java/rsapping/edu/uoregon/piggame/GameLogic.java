package rsapping.edu.uoregon.piggame;


import java.util.Random;

public class GameLogic {


    //Random Number Generator
    Random rand = new Random();

    // define instance variables that should be saved
    //Save overall player scores
    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    //Tells me if its the AI's Turn or not
    private boolean AITurn = false;

    //Save scores on turn, for that turn only
    private int playerOneTurnScore = 0;
    private int playerTwoTurnScore = 0;

    //Keep track of what current die is and whos turn
    private int currentDie = 0;
    private int currentDie2 = 0;
    private int currentPlayersTurn = 1;

    //Check if playertwo is on their last chance, after player 1 reached 100
    private int playerTwoLastChance = 0;

    //Flat that player 1 reached 100
    private int playerOneWins = 0;
    private int numberOfDie = 1;

    //Score to get to win , legacy code
   // private int  SCOREGOAL = 100;

    //Default score to win
    private int goalValue = 100;
    private int numSidesOnDie = 6;


    //Below are my getters and setters for the variables

    //Controls if its AI's Turn
    public boolean getAITurn() {return AITurn;}
    public void setAITurn(boolean turn) {
        AITurn = turn;
    }

    //Keeps Track on how many Die I use
    public int getNumberOfDie() {
        return numberOfDie;
    }
    public void setNumberOfDie(int die) {
        numberOfDie = die;
    }


    //Keeps track of my Goal values to reach
    public int getGoalValue() {
        return goalValue;
    }
    public void setGoalValue(int goal) {
        goalValue = goal;
    }

    //Keeps track of the number of sides on a die
    public int getNumSidesOnDie() {
        return numSidesOnDie;
    }
    public void setNumSidesOnDie(int sides) {
        numSidesOnDie = sides;
    }


    //Keeps track of player ones current turns score
    public int getPlayerOneTurnScore() {
        return playerOneTurnScore;
    }
    public void setPlayerOneTurnScore(int score) {
        playerOneTurnScore = score;
    }

    //Keeps track of player twos current turns score
    public int getPlayerTwoTurnScore()
    {
        return playerTwoTurnScore;
    }
    public void setPlayerTwoTurnScore(int score) { playerTwoTurnScore = score; }

    //Checks flag that player one reached the goal
    public int getPlayerOneWins() {
        return playerOneWins;
    }

    //Sets the condition that player 1 reached 100
    public void setPlayerOneWins(int win) {
        playerOneWins = win;
    }

    //Checks to see if  player two is used their last chance, after player 1 reached 100
    public int getPlayerTwoLastChance() {
        return playerTwoLastChance;
    }

    //Sets that player two us using their last chance, after player 1 reached 100
    public void setPlayerTwoLastChance(int setter)
    {
        playerTwoLastChance = setter;
    }

    //Resets all values for new game
    public void newGame() {
        playerOneScore = 0;
        playerOneTurnScore = 0;
        playerTwoScore = 0;
        playerTwoTurnScore = 0;

        currentDie=0;
        currentDie2=0;
        currentPlayersTurn =1;

        setPlayerOneWins(0);
        setPlayerTwoLastChance(0);
        AITurn=false;
    }

    //Updates and calculates players turn score
    public void turnScoreUpdate()
    {
        //If player 1's turn
        if(currentPlayersTurn==1) {
            //If die is 1 don't add anything
            if (currentDie == 1 || currentDie2==1 ) {
                //Change turns
                playerOneTurnScore = 0;

                //Switch Turn if player lands on 1
                //currentPlayersTurn = 2;
                //Player Now has to end the turn themself

            } //Else add score
            else {
                playerOneTurnScore = playerOneTurnScore + currentDie + currentDie2;

            }
        }
        else {//Player 2's turn
            if (currentDie == 1 || currentDie2==1) { //If die is 1 don't add anything and change turns
                playerTwoTurnScore = 0;

                //Switch Turn if player lands on one
                //currentPlayersTurn = 1;
                //Player now has to end the turn themself

            }
            else //Else add score{
                playerTwoTurnScore = playerTwoTurnScore + currentDie + currentDie2;
        }
    }

    //Current players turn is over,
    public void playerEndTurn()
    {
        //Check if win condition exists

        // Check if player 1 wins
        if(getPlayerTwoLastChance()==1 & getPlayerOneScore() > getplayerTwoScore()) {
            setPlayerOneWins(1) ;
        }
        //Check if player 2 wins
        else if (getPlayerTwoLastChance()==1 & getPlayerOneScore() <= getplayerTwoScore()) {
            setPlayerOneWins(2);
        }
        //Check if there is a tie
        else if(getPlayerTwoLastChance()==1 & getPlayerOneScore() == getplayerTwoScore()) {
            setPlayerOneWins(1);
            setPlayerOneWins(2);
        }

        //Switch who's turn it is
        //And add up their score from their turns score
        if(currentPlayersTurn == 1) {

            playerOneScore = playerOneScore + playerOneTurnScore;
            playerOneTurnScore = 0;
        }
        else {
            playerTwoScore = playerTwoScore + playerTwoTurnScore;
            playerTwoTurnScore = 0;

        }
    }


    //Saves and gets player ones score (total score)
    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }
    public int getPlayerOneScore() {
        return playerOneScore;
    }

    //Saves and gets player twos score (total score)
    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }
    public int getplayerTwoScore() {
        return playerTwoScore;
    }


    ///Get and set which current die is showing or dice
    public void setCurrentDie(int currentDie) {
        this.currentDie = currentDie;
    }
    public int getCurrentDie( ) {
       return currentDie;
    }
    public void setCurrentDie2(int currentDie2) {
        this.currentDie2 = currentDie2;
    }
    public int getCurrentDie2( ) {
        return currentDie2;
    }



    //Switches current players turn to other player
    public void switchPlayerTurn()
    {
        if(currentPlayersTurn == 1)
            currentPlayersTurn = 2;
        else
            currentPlayersTurn = 1;

    }

    //Used to reload setting of players turn from save state
    public void setCurrentPlayersTurn(int currentPlayersTurn) { this.currentPlayersTurn = currentPlayersTurn; }
    //Keep track of which players turn it is
    public int getCurrentPlayersTurn( ) {
       return currentPlayersTurn;
    }

    //Check if player two got to go yet after player 1 win flag set
    public void checkIfPlayerTwoRollOneOnLastChance() {
        if (getPlayerTwoLastChance() == 1) {
            setPlayerOneWins(1);
        }
    }

    //Checks if player one met win conditions
    public boolean checkPlayerOneWinConditions()
    {
        if (getPlayerOneScore()>=goalValue & getPlayerOneScore() > getplayerTwoScore())
            return true;
        else
            return false;
    }

    //Checks if player two went yet on last chance or if player on has win condition
    public boolean checkPlayerTwosLastChanceCondition()
    {
        if (getPlayerOneWins()==0 || getPlayerTwoLastChance()==0)
            return true;
        else
            return false;
    }


    //Checks if player two met win conditions
    public boolean checkPlayerTwoWinConditions()
    {
        if(getplayerTwoScore()>=goalValue)
            return true;
        else
            return false;
    }

    //Rolls the die
    public int rollDie()
    {

        int tempNum = myRandom();
        setCurrentDie(tempNum) ;

        //Don't update score yet, if there is a second die, else do it
        if(getNumberOfDie() ==1)
            turnScoreUpdate();
        return tempNum;
    }

    //Rolls the second die
    public int rollDie2()
    {
        int tempNum = myRandom();
        setCurrentDie2(tempNum) ;
        turnScoreUpdate();
        return tempNum;
    }

    //Generates random die for roll.
    private int myRandom()
    {
        int tempNum = rand.nextInt(numSidesOnDie) + 1;
        return tempNum;
    }


    
}

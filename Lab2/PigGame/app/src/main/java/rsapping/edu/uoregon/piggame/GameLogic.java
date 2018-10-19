package rsapping.edu.uoregon.piggame;


import java.util.Random;

public class GameLogic {

    // define instance variables that should be saved

    //Save overall player scores
    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    //Save scores on turn, for that turn only
    private int playerOneTurnScore = 0;
    private int playerTwoTurnScore = 0;

    //Keep track of what current die is and whos turn
    private int currentDie = 1;
    private int currentPlayersTurn = 1;

    //Check if playertwo is on their last chance, after player 1 reached 100
    private int playerTwoLastChance = 0;

    //Flat that player 1 reached 100
    private int playerOneWins = 0;

    //Score to get to win
    private int SCOREGOAL = 100;


    public int getPlayerOneTurnScore() {
        return playerOneTurnScore;
    }

    public void setPlayerOneTurnScore(int score) {
        playerOneTurnScore = score;
    }

    public int getPlayerTwoTurnScore()
    {
        return playerTwoTurnScore;
    }

    public void setPlayerTwoTurnScore(int score)
    {
         playerTwoTurnScore = score;
    }

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

        currentDie=1;
        currentPlayersTurn =1;

        setPlayerOneWins(0);
        setPlayerTwoLastChance(0);
    }

    //Updates and calculates players turn score
    public void turnScoreUpdate()
    {
        //If player 1's turn
        if(currentPlayersTurn==1) {
            //If die is 1 don't add anything
            if (currentDie == 1) {
                //Change turns
                playerOneTurnScore = 0;
                currentPlayersTurn = 2;

            } //Else add score
            else {
                playerOneTurnScore = playerOneTurnScore + currentDie;

            }
        }
        else {//Player 2's turn
            if (currentDie == 1) { //If die is 1 don't add anything and change turns
                playerTwoTurnScore = 0;
                currentPlayersTurn = 1;


            }
            else //Else add score{
                playerTwoTurnScore = playerTwoTurnScore + currentDie;
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


    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }


    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public int getplayerTwoScore() {
        return playerTwoScore;
    }


    public void setCurrentDie(int currentDie) {
        this.currentDie = currentDie;
    }

    public int getCurrentDie( ) {
       return currentDie;
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
    public void setCurrentPlayersTurn(int currentPlayersTurn) {
        this.currentPlayersTurn = currentPlayersTurn;
    }

    public int getCurrentPlayersTurn( ) {
       return currentPlayersTurn;
    }

    //Check if player two got to go yet after player 1 win flag set
    public void checkIfPlayerTwoRollOneOnLastChance() {
        if (getPlayerTwoLastChance() == 1) {
            setPlayerOneWins(1);
        }
    }

    public boolean checkPlayerOneWinConditions()
    {
        if (getPlayerOneScore()>=SCOREGOAL & getPlayerOneScore() > getplayerTwoScore())
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


    public boolean checkPlayerTwoWinConditions()
    {
        if(getplayerTwoScore()>=SCOREGOAL)
            return true;
        else
            return false;
    }

    //Rolls the die
    public int rollDie()
    {
        Random rand = new Random();
        int tempNum = rand.nextInt(6) + 1;
        setCurrentDie(tempNum) ;
        turnScoreUpdate();
        return tempNum;
    }
    
}

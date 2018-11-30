package rsapping.edu.uoregon.piggame;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Alphadog1939 on 7/3/16.
 */
public class AILogic {

    Random rand = new Random();
    int tempNum=0;


    public int decision(int playerTwoScoreCurrentScore ) {


        //This is the AI brain. It randomly rolls to see if it will end the turn or continue on.
        tempNum= rand.nextInt(2) + 1;

        //From main activity choices
        //1 is roll die
        //2 is end turn

        //If player score is below 6, just roll again, ignore end turn roll
        if(playerTwoScoreCurrentScore <= 9)
            return 1;
       //If player score above 30 just save it, regardless of if roll again
        else if (playerTwoScoreCurrentScore>=30)
            return 2;
        //IF rolls roll, then roll :)
        else if(tempNum==1)
            return 1;
        //if roll end turn, then end turn
        else if (tempNum==2)
            return 2;
        //error
        else
            return 0;

    }

}
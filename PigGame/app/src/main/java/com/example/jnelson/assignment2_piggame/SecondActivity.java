package com.example.jnelson.assignment2_piggame;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends Activity
{
    private boolean logging = true;
    private PigGame game;
    public void SetGame(PigGame g)    {        this.game = g;    }
    protected String p1name = "";
    protected String p2name= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (logging) Log.d("SecondActivity", "Start: OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (logging) Log.d("SecondActivity", "Start: onNewIntent()");
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        if (logging) Log.d("SecondActivity", "Start: onResume()");
        super.onResume();

        // Get the game state sent from the FirstActivity
        Intent intent = getIntent();
        if (null != intent) {
            p1name= intent.getStringExtra("p1name");
            System.out.println("SecondActivity Player 1 name: "+ p1name);
            p2name= intent.getStringExtra("p2name");
            System.out.println("SecondActivity Player 2 name: "+ p2name);
        }
        // Get Extra saved values?

        if (game == null)   // We might already have a game object
            game = new PigGame(6, 1);
        //game.setHumanHand(humanHand);

        // Pass the fragment a game ref while calling the method invokes game play
        SecondFragment secondFragment = (SecondFragment)getFragmentManager().findFragmentById(R.id.second_fragment);
        //secondFragment.computerMove(game);
    }
}

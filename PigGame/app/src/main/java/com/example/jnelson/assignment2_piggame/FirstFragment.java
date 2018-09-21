package com.example.jnelson.assignment2_piggame;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Jnelson on 7/19/2015.
 */
public class FirstFragment extends Fragment
                           implements OnClickListener {
    private boolean logging = true;
    private boolean twoPaneLayout;

    private PigGame game; // Game Object

    private EditText player1Name;
    private EditText player2Name;
    private Button startGame;

    private FirstActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (logging) Log.d("FirstFragment", "Start: onCreateView()");
        View view = inflater.inflate(R.layout.first_fragment, container, false);

        // Set this fragment to listen for the Play button's click event
        startGame = (Button) view.findViewById(R.id.start);
        startGame.setOnClickListener(this);

        player1Name = (EditText) view.findViewById(R.id.player1name);
        player2Name = (EditText) view.findViewById(R.id.player2name);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {   if (logging) Log.d("FirstFragment", "Start: onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        activity = (FirstActivity) getActivity(); // Get a references from the host activity
        // Check to see if FirstActivity has loaded a single or dual pane layout
        twoPaneLayout = activity.findViewById(R.id.second_fragment) != null;

        /* ************************************************************** *
        *                     Load Saved Player Names                     *
        * *************************************************************** */
        if (savedInstanceState != null) {// Load Player and Temporary round scores
            player1Name.setText(savedInstanceState.getString("player1namesave", "@string/default_name"));
            player2Name.setText(savedInstanceState.getString("player2namesave", "@string/default_name"));


        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {  if (logging) Log.d("FirstFragment", "Start: onSaveInstanceState()");
        /* ************************************************************** *
        *                     Save Player Names                           *
        * *************************************************************** */
        super.onSaveInstanceState(outState);
        outState.putString("player1namesave", player1Name.getText().toString());
        outState.putString("player2namesave", player1Name.getText().toString());
    }

    @Override
    public void onClick(View v)
    { if (logging) Log.d("FirstFragment", "Start: onClick()");
        if(v.getId() == R.id.start) {
            Toast.makeText(activity, "Staring Game", Toast.LENGTH_SHORT).show();
            activity.SetName1(player1Name.getText().toString());  // Save Player1 entered name with game object
            activity.SetName2(player2Name.getText().toString());  // Save Player2 entered name with game object
        }

            if (!twoPaneLayout) {
                Intent intent = new Intent(activity, SecondActivity.class);
                intent.putExtra("p1name",player1Name.getText().toString() );
                intent.putExtra("p2name",player2Name.getText().toString() );
                //int humanHandNum = game.getHumanHand().ordinal();
                //intent.putExtra("humanHand", humanHandNum);  // send state to 2nd activity
                startActivity(intent);
            }
    }
}



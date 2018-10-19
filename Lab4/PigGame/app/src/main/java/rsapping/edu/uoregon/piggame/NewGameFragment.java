package rsapping.edu.uoregon.piggame;


import android.content.Intent;
;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import java.text.NumberFormat;

public class NewGameFragment extends Fragment
        implements TextView.OnEditorActionListener, View.OnClickListener {



    private Button newGameButton;
    private EditText playerOneNameTextEdit;
    private EditText playerTwoNameTextEdit;

    private NewGameActivity activity;

    //private Button newGameButton;
    private boolean prevLoad = true;
    private boolean twoPaneLayout;
    private SharedPreferences savedValues;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_game_main, container, false);
        //setContentView(R.layout.activity_main);

        // tell the fragment that we have an Options menu
        setHasOptionsMenu(true);
        //newGameButton.setOnClickListener(this);
        newGameButton = (Button) view.findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(this);

        playerOneNameTextEdit = (EditText) view.findViewById(R.id.playerOneNameTextEdit);
        playerTwoNameTextEdit = (EditText) view.findViewById(R.id.playerTwoNameTextEdit);
        // set the listeners
        playerOneNameTextEdit.setOnEditorActionListener(this);
        playerTwoNameTextEdit.setOnEditorActionListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
            if (v.getId() == R.id.newGameButton) {
                if(!twoPaneLayout)
                    //startActivity( new Intent(getActivity(), MainActivity.class));
                //If not two pane
                    //Send intent to the main activity, it main activitys fragment can use it
               {
                   Intent intent = new Intent(getActivity(), MainActivity.class);
                   intent.putExtra("name1", playerOneNameTextEdit.getText().toString());
                   intent.putExtra("name2", playerTwoNameTextEdit.getText().toString());
                    startActivity(intent);
                }
            }


    }
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        int keyCode = -1;
        if (event != null) {
            keyCode = event.getKeyCode();
        }

        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

            //This allows people to press enter on the keyboard and change focus off the text exit
            // And back to the main layout, then i call on the function to close the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(MainActivity.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( getActivity().getCurrentFocus().getWindowToken(), 0);
            getActivity().findViewById(R.id.mainLayout).requestFocus();
        }
        return false;
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
                startActivity(new Intent( getActivity(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent( getActivity(), AboutActivity.class));
                return true;
            case R.id.menu_help:
                Toast.makeText( getActivity(), "Sorry! Help hasn't been implemented yet.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

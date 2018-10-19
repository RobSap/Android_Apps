package rsapping.edu.uoregon.piggame;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import java.text.NumberFormat;
//import java.util.Random;

import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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





public class MainActivity extends AppCompatActivity{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view for the activity using XML
        setContentView(R.layout.activity_main);
        String message = getIntent().getStringExtra("name1");
        String message2 = getIntent().getStringExtra("name2");

        // The toast is just for testing
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Send the message to the fragment
        MainActivityFragment fragment =
                (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        fragment.setName1(message);
        fragment.setName2(message2);

    }

   /* @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }*/


}



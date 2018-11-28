package com.example.android.githubsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FastAdapter.ListItemClickListener {

    private RecyclerView results;
    private FastAdapter fAdapter;

    private EditText searchBoxEditText;

    private TextView errorMessage;

    private ProgressBar loadingIndicator;

    public Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        results = (RecyclerView) findViewById(R.id.github_search_results_json);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        results.setLayoutManager(layoutManager);
        results.setHasFixedSize(true);

        searchButton = (Button) findViewById(R.id.action_search);
        searchButton.setOnClickListener(this);

        searchBoxEditText = (EditText) findViewById(R.id.edit_search_box);

        errorMessage = (TextView) findViewById(R.id.error_message_display);

        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        fAdapter = new FastAdapter(this);

    }

    @Override
    public void onClick(View view) {
        int itemThatWasClickedId = searchButton.getId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
        }
    }


    private void makeGithubSearchQuery() {
        //Get search box edit test
        String githubQuery = searchBoxEditText.getText().toString();

        //Get Query URL
        URL githubSearchUrl = Network.buildUrl(githubQuery);
        //Run it
        new GithubQueryTask().execute(githubSearchUrl);
    }

    private void showJsonDataView() {
        // Make error message INVISIBLE
        errorMessage.setVisibility(View.INVISIBLE);

        //Display results
        results.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        // Hide current visible data
        results.setVisibility(View.INVISIBLE);
        // Show error
        errorMessage.setVisibility(View.VISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... params) {

            if (params.length == 0) {
                return null;
            }

            URL searchUrl = params[0];
            String githubSearchResults = null;
            String[] simpleJsonWeatherData = null;

            try  {
                    githubSearchResults = Network.getResponseFromHttpUrl(searchUrl);
                    simpleJsonWeatherData = OpenResponseJson.getSimpleWeatherStringsFromJson(MainActivity.this, githubSearchResults);
                    return simpleJsonWeatherData;
                 }
                 catch (IOException e) {
                    e.printStackTrace();
                 }
                 catch (JSONException e) {
                    e.printStackTrace();
                 }
            return null;
        }

        @Override
        protected void onPostExecute(String[] githubSearchResults) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {

                showJsonDataView();
                results.setAdapter(fAdapter);
                fAdapter.setRespository(githubSearchResults);
            } else {
                showErrorMessage();
            }
        }
    }
    @Override
    public void onListItemClick(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
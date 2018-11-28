package com.example.android.githubsearch;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {

    final static String GITHUB_URL = "https://api.github.com/search/repositories";

    final static String PARAM_QUERY = "q";

    //Add sort, and what to sort results by
    final static String PARAM_SORT = "sort";
    //final static String sortBy = "stars";
    final static String sortBy = "updated";

    //Sort Order
    final static String SORT_BY = "order";
    final static String SORT_ORDER = "desc";


    public static URL buildUrl(String githubSearchQuery) {
            Uri builtUri = Uri.parse(GITHUB_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                    .appendQueryParameter(PARAM_SORT, sortBy)
                    .appendQueryParameter(SORT_BY,SORT_ORDER)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
    }

      //This function comes from the basic URL example
        public static String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection url_connection = (HttpURLConnection) url.openConnection();
            try {
                InputStream input = url_connection.getInputStream();

                Scanner scanner = new Scanner(input);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                url_connection.disconnect();
            }
        }
}




package com.example.android.githubsearch;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

//Functions to handle  JSON data.
public final class OpenResponseJson {


    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        //Indicator to know format
        final String ID_ITEMS = "items";

        final String MESSAGE_CODE = "cod";

        //Hold the data
        String[] parsedData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        // Check for an error
        if (forecastJson.has(MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray projectArray = forecastJson.getJSONArray(ID_ITEMS);
        parsedData = new String[projectArray.length()];

        int length = projectArray.length();
        for (int i = 0; i < length; i++) {

            //Values to pull from Json
            String html_url = "";
            String name = "";
            String login = "";
            String description ="";

            // Get the JSON object representing 1 github repository
            JSONObject account = projectArray.getJSONObject(i);

            name = account.getString("name");
            html_url  = account.getString("html_url");
            description = account.getString("description");
            JSONObject businessObject = account.getJSONObject("owner");
            login = businessObject.getString("login");

            parsedData[i] = "number: " + (i+1) + "/"+String.valueOf(length)+"\nProject: " + name + "\nOwner: " + login + "\nDescription: " + description +"\nURL: " + html_url;
        }
        return parsedData;
    }
}
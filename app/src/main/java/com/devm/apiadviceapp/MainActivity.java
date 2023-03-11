package com.devm.apiadviceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //variable type similar to UI component
    TextView tv_advice;
    Button b_advice;

    //create string variable to parse json data
    String adviceString= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect variable to UI component
        tv_advice = findViewById(R.id.tv_advice);
        b_advice = findViewById(R.id.b_advice);

        //set variable to perform an onclick event
        b_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //execute AdviceLoader class
                new AdviceLoader().execute();
            }
        });

    }

    //advice loader to the screen
    //this specific method only deals with
    //connecting the data the the ui screen
    private class AdviceLoader extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //get json from url

                                                //just need to change this in the future
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");

            //get advice from json
            try {
                //create a json object for string type

                JSONObject jsonObject = new JSONObject(jsonString);

                //create a JSONObjects called adviceObject & include key
//                JSONObject adviceObject = jsonObject.getJSONObject("slip");

                //store json value in the adviceString variable
//                adviceString = adviceObject.getString("value");
                adviceString = jsonObject.getString("value");
            } catch (JSONException e) {
                //if it fails - show error message
                e.printStackTrace();
            }
            return null;
        }

        //before the message is loaded, this is shown on the scrren
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adviceString = "";
            tv_advice.setText("Loading...");
        }

        //add quotations to the text after it loads
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            //display the advice if any
            if(!adviceString.equals("")) {
                tv_advice.setText(adviceString);
            }
        }
    }

    //get json string
    //this method is only to accept json
    //it connects to the json data in the background
    private String getJson(String link) {
        //create a new variable
        String stream = "";
        try {
            //create an URL object as that is the format used
            URL url = new URL(link);
            //HttpURLConnect object is needed as it is HTTP format
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //this gets the response code
            //it connects with the website
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                //it reads and processes the data
                //the while loop is to separate the data
                //the method builds the data into a format readable
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                stream = stringBuilder.toString();
                urlConnection.disconnect();
            }
            //error messages to handle data input
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
package com.example.android.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    EditText userInput;
    TextView output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.editText);
        output = findViewById(R.id.textView);

    }


    public void getTheWeather(View view) {

        DownloadTask downloadTask = new DownloadTask();

        String city = userInput.getText().toString();

        try {

            String cityEncoder = URLEncoder.encode(city, "UTF-8");
            downloadTask.execute("https://openweathermap.org/data/2.5/weather?q=" + cityEncoder + "&appid=439d4b804bc8187953eb36d2a8c26a02");
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        } catch (Exception e) {
            e.printStackTrace();
            output.setText("We can't find the Weather :(");
        }


    }





    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    data = reader.read();
                    result += current;

                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");
                JSONArray weatherInfo = new JSONArray(weather);
                String message = "";


                for (int i = 0; i < weatherInfo.length(); i++) {
                    JSONObject jsonPart = weatherInfo.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals("")) {
                        message += "main: " + main + "\n" + "description: " + description;
                    }

                }

                if (!message.equals("")) {
                    output.setText(message);

                } else {
                    output.setText("We can't find the Weather :(");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



}
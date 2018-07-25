package com.example.viveksharma.weather;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AsyncTask extends android.os.AsyncTask<String,Void, String> {
    private MainActivity mainActivity;
    private HashMap<String, String> weatherData = new HashMap<>();
    private Bitmap bitmap;
    private final String weatherURL = "http://api.openweathermap.org/data/2.5/weather";
    private final String iconUrl = "http://openweathermap.org/img/w/";
    private final String yourAPIKey ="";
    private static final String TAG = "AsyncTask";
    public AsyncTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        if(strings[2].equals("")) {
            buildURL.appendQueryParameter("q", strings[0]);
        }
        else{

            Log.d(TAG, "doInBackground: myloc"+strings[0]);

            buildURL.appendQueryParameter("zip",strings[0]);
        }
        buildURL.appendQueryParameter("units",strings[1] );
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: url"+urlToUse);
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            return null;
        }
        Log.d(TAG, "doInBackground: "+sb.toString());
        parseJSON(sb.toString());
        return null;
    }

    private void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray weatherArray = jObjMain.getJSONArray("weather");

            JSONObject jWeatherObj = (JSONObject) weatherArray.get(0);
            if(jWeatherObj.has("main")) {
                weatherData.put("condition", jWeatherObj.getString("main"));
            }
            else{
                weatherData.put("condition","No Data Available");
            }
            if(jWeatherObj.has("description")) {
                weatherData.put("description", jWeatherObj.getString("description"));
            }
            else{
                weatherData.put("description","No Data Available");
            }
            String icon;
            if(jWeatherObj.has("icon")) {
                 icon = jWeatherObj.getString("icon");
            }
            else
            {
                icon ="No Data available";
            }
            JSONObject jObj = jObjMain.getJSONObject("main");
            if(jObj.has("temp")) {
                weatherData.put("temperature", jObj.getString("temp"));
            }
            else{
                weatherData.put("temperature", "No Data Available");
            }
            if(jObj.has("humidity")) {
                weatherData.put("humidity", jObj.getString("humidity"));
            }
            else
            {
                weatherData.put("humidity", "No Data Available");
            }
            if(jObj.has("pressure")) {
                weatherData.put("pressure", jObj.getString("pressure"));
            }
            else
            {
                weatherData.put("pressure", "No Data Available");
            }
            if(jObjMain.has("wind")) {
                JSONObject jWind = jObjMain.getJSONObject("wind");
                if(jWind.has("speed")) {
                    weatherData.put("windspeed", jWind.getString("speed"));
                }
                else{
                    weatherData.put("windspeed","No Data Available");
                }
                if(jWind.has("deg")) {
                    weatherData.put("winddirection", jWind.getString("deg"));
                }
                else
                {
                    weatherData.put("winddirection","No Data Available");
                }
            }
            if(jObjMain.has("clouds")) {
                JSONObject jcloud = jObjMain.getJSONObject("clouds");
                weatherData.put("cloudiness", jcloud.getString("all"));
            }
            else
            {
                weatherData.put("cloudiness","No Data Available");
            }
            if(jObjMain.has("rain"))
            {
            JSONObject jRain = jObjMain.getJSONObject("rain");
            weatherData.put("rain",jRain.getString("3h"));
            }
            else {
                weatherData.put("rain", "0");
            }
            if(jObjMain.has("snow")) {
                JSONObject jSnow = jObjMain.getJSONObject("snow");
                weatherData.put("snow", jSnow.getString("3h"));
            }
            else {
                weatherData.put("snow", "0");
            }
            if(jObjMain.has("name")) {
                weatherData.put("city", jObjMain.getString("name"));

            }
            else
            {
                weatherData.put("city","No Data Available");
            }
            JSONObject jSys = jObjMain.getJSONObject("sys");
            if(jSys.has("country")) {
                weatherData.put("country", jSys.getString("country"));
            }
            else{
                weatherData.put("country","No Data Available");
            }
            long sunr;
            long suns;
            String sunrise;
            String sunset;
            Calendar now = Calendar.getInstance();
            TimeZone tz =  now.getTimeZone();


            if(jSys.has("sunrise")) {
                sunr = jSys.getLong("sunrise");
                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                sunrise = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date(sunr*1000));
            }
            else {
                sunrise="";
            }
            if(jSys.has("sunset")) {
                suns = jSys.getLong("sunset");
                sunset = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date(suns*1000));
            }
            else{
                sunset ="";
            }

            weatherData.put("sunrise",sunrise);
            weatherData.put("sunset",sunset);
            if(jObjMain.has("dt")) {
                long dt = jObjMain.getLong("dt");

                TimeZone.setDefault(tz);
                String date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date(dt * 1000));
                date = date + " ("+TimeZone.getDefault().getDisplayName()+")";
                weatherData.put("date", date);
            }
            else{
                weatherData.put("date", "No Data Available");
            }
            if(jObjMain.has("visibility")) {
                String  visibility = jObjMain.getString("visibility");
                weatherData.put("visibility", visibility);
            }
            else{
                weatherData.put("visibility", "No Data Available");
            }
            InputStream input = new java.net.URL(iconUrl + icon + ".png").openStream();
            bitmap = BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        mainActivity.displayWeatherData(weatherData,bitmap);
    }
}

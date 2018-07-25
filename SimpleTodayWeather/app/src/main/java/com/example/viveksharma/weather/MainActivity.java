package com.example.viveksharma.weather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String units="metric";
    String cityName="";
    boolean flag =false;
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private Locator locator;
    boolean connected;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // get shared preference
        getSharedPreference();

        // check connectivity before doing async load
        checkconnectivity();
        if (connected == true) {
            doAsyncLoad();
        }
        // hide the display items
        else {
          noInternetDisplay();
        }

        // set swipe refresher
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // get shared preference
                getSharedPreference();

                // check connectivity before doing async load
                checkconnectivity();
                if (connected == true) {
                    doAsyncLoad();

                    RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
                    rg.setVisibility(View.VISIBLE);
                }
                // hide the display items
                else {
                   noInternetDisplay();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void noInternetDisplay(){
        TextView city = (TextView) findViewById(R.id.city);
        city.setText("");
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setVisibility(View.GONE);
        TextView temp = (TextView) findViewById(R.id.temp);
        temp.setText("No Internet Connection");


        TextView condition = (TextView) findViewById(R.id.condition);
        condition.setText("");

        TextView description = (TextView) findViewById(R.id.desc);
        description.setText("");

        TextView date = (TextView) findViewById(R.id.date);
        date.setText("");

        TextView wind = (TextView) findViewById(R.id.wind);
        wind.setText("");


        TextView direction = (TextView) findViewById(R.id.direction);
        direction.setText("");

        TextView rain = (TextView) findViewById(R.id.rain);
        rain.setText("");

        TextView snow = (TextView) findViewById(R.id.snow);
        snow.setText("");

        TextView cloudiness = (TextView) findViewById(R.id.cloudiness);
        cloudiness.setText("");

        TextView humidity = (TextView) findViewById(R.id.humidity);
        humidity.setText("");

        ImageView wimage = (ImageView) findViewById(R.id.imageView);
        wimage.setImageDrawable(null);

        TextView pressure = (TextView) findViewById(R.id.pressure);
        pressure.setText("");

        TextView sunrise = (TextView) findViewById(R.id.sunrise);
        sunrise.setText("");

        TextView sunset = (TextView) findViewById(R.id.sunset);
        sunset.setText("");

        TextView visibility = (TextView) findViewById(R.id.visibility);
        visibility.setText("");

    }
    public void getSharedPreference(){
        // shared preference
        sharedPref = getSharedPreferences("mypref", Context.MODE_PRIVATE);


        // get shared preference data that is committed

        if (sharedPref.contains("city")) {
            cityName = sharedPref.getString("city", "");
            Log.d(TAG, "onCreate: cc" + cityName);
            flag = true;
        }
        if (sharedPref.contains("units")) {
            units = sharedPref.getString("units", "");
            Log.d(TAG, "onCreate: ccin" + units);

            // set radio button according to shared preference

            RadioButton rbc = (RadioButton) findViewById(R.id.celsius);
            RadioButton rbf = (RadioButton) findViewById(R.id.fahrenheit);

            if (units.equals("metric")) {
                rbc.setChecked(true);
            } else {
                rbf.setChecked(true);
            }
        }
    }
    public void checkconnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return true;
    }

    private void doAsyncLoad() {
        TextView tv = (TextView) findViewById(R.id.city);
        if(flag==false) {
            //cityName = tv.getText().toString().trim().replaceAll(", ", ",");
            cityName = "delhi";
        }

        AsyncTask at = new AsyncTask(this);
        at.execute(cityName,  units,"");

    }

    private void doAsyncLoadLocation() {
        TextView tv = (TextView) findViewById(R.id.city);
        if(flag==false) {
            //cityName = tv.getText().toString().trim().replaceAll(", ", ",");
            cityName = "delhi";
        }

        AsyncTask at = new AsyncTask(this);
        at.execute(cityName,  units,"location");

    }

    public void unitSet(View v){
     switch (v.getId()){
         case R.id.fahrenheit:
             units="imperial";
             break;
         case R.id.celsius:
             units="metric";
             break;
     }
     doAsyncLoad();
    }

    // menu options

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.selectcity:
                showCityDialog();
            return true;

            case R.id.location:
            // start locator
            locator = new Locator(this);
            return  true;

            case R.id.about:
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void showCityDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(MainActivity.this);
        alert.setMessage("");
        alert.setTitle("Enter City Name");
        alert.setView(edittext);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                cityName = edittext.getText().toString().replaceAll(", ", ",");;
                flag = true;
                doAsyncLoad();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //
            }
        });

        alert.show();
    }


    public void displayWeatherData(HashMap<String, String> weatherData, Bitmap bitmap) {
        if (weatherData.isEmpty()) {
            Toast.makeText(this, "Invalid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView cityName = (TextView) findViewById(R.id.city);
        cityName.setText(weatherData.get("city") + ", " + weatherData.get("country"));

        TextView temperature = (TextView) findViewById(R.id.temp);
        if(units.equals("imperial")) {
            temperature.setText(weatherData.get("temperature")+" °F");
        }
        else{
            temperature.setText(weatherData.get("temperature")+" °C");
        }

        TextView condition = (TextView) findViewById(R.id.condition);
        condition.setText(weatherData.get("condition"));

        TextView description = (TextView) findViewById(R.id.desc);
        description.setText("(" + weatherData.get("description") + ")");

        TextView date = (TextView) findViewById(R.id.date);
        date.setText(weatherData.get("date"));

        TextView wind = (TextView) findViewById(R.id.wind);
        if(units.equals("imperial")) {
            wind.setText(weatherData.get("windspeed")+" mph");
        }
        else {
            wind.setText(weatherData.get("windspeed") + " mps");
        }

        TextView direction = (TextView) findViewById(R.id.direction);
        direction.setText(weatherData.get("winddirection")+" degrees");

        TextView rain = (TextView) findViewById(R.id.rain);
        rain.setText(weatherData.get("rain")+" mm");

        TextView snow = (TextView) findViewById(R.id.snow);
        snow.setText(weatherData.get("snow")+" mm");

        TextView cloudiness = (TextView) findViewById(R.id.cloudiness);
        cloudiness.setText(weatherData.get("cloudiness")+"%");

        TextView humidity = (TextView) findViewById(R.id.humidity);
        humidity.setText(weatherData.get("humidity")+"%");

        ImageView wimage = (ImageView) findViewById(R.id.imageView);
        wimage.setImageBitmap(bitmap);

        TextView pressure = (TextView) findViewById(R.id.pressure);
        pressure.setText(weatherData.get("pressure")+"hPa");

        TextView sunrise = (TextView) findViewById(R.id.sunrise);
        sunrise.setText(weatherData.get("sunrise") +" GMT");

        TextView sunset = (TextView) findViewById(R.id.sunset);
        sunset.setText(weatherData.get("sunset") +" GMT");

        TextView visibility = (TextView) findViewById(R.id.visibility);
        visibility.setText(weatherData.get("visibility")+" meter");

    }

    // commit the shared preference


    @Override
    protected void onStop() {
        editor= sharedPref.edit();
        editor.putString("city",cityName);
        editor.putString("units",units);
        Log.d(TAG, "onStop: instop"+cityName);
        editor.commit();

        // stop locator
        //locator.shutdown();
        super.onStop();
    }

    // for location

    public void setData(double lat, double lon) {
        Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
        String address = doAddress(lat, lon);
        cityName =address;
        Log.d(TAG, "setData: "+cityName);
        flag=true;
        doAsyncLoadLocation();
    }

    private String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //Log.d(TAG, "doAddress: myadd"+addresses);
                Log.d(TAG, "doAddress: Num addresses: " + addresses.size());
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Log.d(TAG, "doAddress: "+addresses.get(0).getPostalCode());
                    sb.append(addresses.get(0).getPostalCode());
                    sb.append(",");
                    sb.append(addresses.get(0).getCountryCode());
                }

                /*for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);

                    sb.append("\nAddress\n\n");
                    Log.d(TAG, "doAddress: " + ad.getMaxAddressLineIndex());
                    for (int i = 0; i <= ad.getMaxAddressLineIndex(); i++)
                        sb.append("\t" + ad.getAddressLine(i) + "\n");
                        sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");
                }*/
                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());
            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    @Override
    protected void onDestroy() {
        // remove shred preference data
        //editor.clear();
       // editor.commit();
        if(locator!=null) {
            locator.shutdown();
        }
        super.onDestroy();
    }
}

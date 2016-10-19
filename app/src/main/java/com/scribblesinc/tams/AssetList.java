package com.scribblesinc.tams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scribblesinc.tams.androidcustom.AssetItems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AssetList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        LoadData dataCaller = new LoadData();
        dataCaller.execute();

    }

    private class LoadData extends AsyncTask<String, Void, String>{
        String result = "";
        ProgressDialog pd;
        private static final String DEBUG_TAG = "LoadData class";
        private static final String SERVER_URL = "https://tams-142602.appspot.com/api/asset/list";
        BufferedReader bufferedReader;
        /*innvoked before task is executed*/
        @Override
        protected void onPreExecute(){
            //pd = ProgressDialog.show(AssetList.this, "","Getting Data from Server");
        }
        /*This method is used to perform background computation that can take a long/short time
        * The results of the computation must be returned by this step and will be passsed back
        *om
        * */
        protected String doInBackground(String...urls){
            try{
                //connect to server
                URL url = new URL  (SERVER_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);//millliseconds
                urlConnection.setConnectTimeout(15000);//in milliseconds
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);//connection is for reading
                //start query
                urlConnection.connect();
                //check if connected
                int response = urlConnection.getResponseCode();
                Log.d(DEBUG_TAG,"The response is: "+response);//shown when debugging

                //get the server json data by reading character-input stream
                //from android: creates a buffering character-input stream that uses a default-sized input buffer
                 bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
           //  Gson gson = new GsonBuilder().create();
          //   AssetItems items = gson.fromJson(bufferedReader,AssetItems.class);
           //start placing data on list
        //  System.out.println(items);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asset_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //when the edit button is pressed
        if(id == R.id.action_edit_asset){
            Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();
        }

        //if the add asset button is pressed
        if(id == R.id.action_add_asset){
            /*Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();*/
            Intent intent = new Intent(this, AssetAdd.class);
            startActivity(intent);
        }

        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){
            finish(); //goes back to the previous activity
        }

        return super.onOptionsItemSelected(item);
    }

}

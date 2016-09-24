package com.scribblesinc.tams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class NotesCapture extends AppCompatActivity {

    EditText my_notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //run parents method by extending the existing class or run this
        //class in addition to parent's class
        super.onCreate(savedInstanceState);
        //activity class creates window
        setContentView(R.layout.activity_notes);
        //initialize
        my_notes=(EditText)findViewById(R.id.my_notes);

        //Instantiating the toolbar of adding asset activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){

            //return notes provided by user
            /*
            String notes = my_notes.getText().toString();
            Intent notesIntent = new Intent(); // if problems with new Intent, use getIntent();
            notesIntent.putExtra("NOTES",notes);
            setResult(6,notesIntent);
            */
            finish(); //goes back to the previous activity
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.scribblesinc.tams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

public class NotesCapture extends AppCompatActivity {
    //used to get the entered title by the user
    private EditText enteredText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // run parents method by extending the existing class or run this
        // class in addition to parent's class
        super.onCreate(savedInstanceState);
        // activity class creates window
        setContentView(R.layout.activity_notes);
        //Instantiating the toolbar of adding asset activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // gets the action bar if not found
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // initializes the variable
        enteredText = (EditText) findViewById(R.id.my_notes);
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
        // gets the item id and initializes it to id
        int id = item.getItemId();
        // if the back button is pressed
        if(id == android.R.id.home) {
            // goes back to the previous activity
            finish();
        }
        if(id == R.id.my_notes) {
            // gets the notes entered by the user
            String notes = String.valueOf(enteredText.getText());
            // if nothing is entered do nothing, else get the notes entered by user
            if (notes.isEmpty()) {
                Toast.makeText(this, "No text has been entered ", Toast.LENGTH_SHORT).show();
            } else {
                // Creates new intent (an intent means it's something that the program intends to do)
                Intent sendToPreviousActivity = new Intent();
                // Used to send back to last activity. A key and whatever is being sent back is provided
                sendToPreviousActivity.putExtra("assetNotes", notes);
                //RESULT_Ok is used to show that everything is fine, the intent is passed as well
                setResult(RESULT_OK, sendToPreviousActivity);
                //Toast to show that it has been saved *will be deleted*
                Toast.makeText(this, "Notes has been captured ", Toast.LENGTH_SHORT).show();
                //Closes current activity
                finish();
            }
        }
        if(id == R.id.action_reset) {
            enteredText.setText("");
        }
        return super.onOptionsItemSelected(item);
    }
}

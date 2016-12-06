package com.scribblesinc.tams;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TitleofAsset extends AppCompatActivity {
    // used to get the entered title by the user
    private EditText enteredText;
    private String title = null;
    private Intent intent;
    private static final String ASSET_TITLE = "com.scribblesinc.tams";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // run parents method by extending the existing class or run this
        // class in addition to parent's class
        super.onCreate(savedInstanceState);
        // activity class creates window
        setContentView(R.layout.activity_titleofasset);
        // Instantiating the toolbar of adding asset activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // gets the action bar if not found
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // initializes the variable
        enteredText = (EditText) findViewById(R.id.title_name);


        //Get the information sent via intent
        intent = getIntent();
        title = intent.getStringExtra(ASSET_TITLE);
        //title = String.valueOf(enteredText.getText());
        enteredText.setText(title, TextView.BufferType.EDITABLE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(title != null){
         getMenuInflater().inflate(R.menu.menu_update_title,menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_title_of_asset, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // if the back button is pressed
        if(id == android.R.id.home) {
            // goes back to the previous activity
            finish();
        }
        // if done button is done item is pressed
        if(id == R.id.action_done||id == R.id.action_update) {
            // gets the title entered by the user
             title = String.valueOf(enteredText.getText());
            // if nothing is entered do nothing, else get the title entered by user
            if(title.isEmpty()){
                Toast.makeText(this, "No text has been entered ", Toast.LENGTH_SHORT).show();
            }else if(id==R.id.action_update) {
                //promt an alert for user to verify choice
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmationAlertDialogStyle);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        Intent sendToPreviousActivity = new Intent();
                        sendToPreviousActivity.putExtra(ASSET_TITLE,title);
                        setResult(RESULT_OK,sendToPreviousActivity);
                        Toast.makeText(getApplicationContext(),"Name has been captured ",Toast.LENGTH_LONG).show();
                        finish();;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        Toast.makeText(getApplicationContext(),"Canceling update ",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setMessage("Selecting 'Ok' will override current data. ");
                builder.setTitle("Attemping to Update Data");

                //Get the AlertDialog from create();
                AlertDialog d = builder.create();
                d.show();

            }else{//user is not updating data
                // Creates new intent (an intent means it's something that the program intends to do)
                Intent sendToPreviousActivity = new Intent();
                // Used to send back to last activity. A key and whatever is being sent back is provided
                sendToPreviousActivity.putExtra(ASSET_TITLE, title);
                // RESULT_Ok is used to show that everything is fine, the intent is passed as well
                setResult(RESULT_OK, sendToPreviousActivity);
                // Toast to show that it has been saved *will be deleted*
                Toast.makeText(this, title + " has been captured ", Toast.LENGTH_SHORT).show();
                // Closes current activity
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

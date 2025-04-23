package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable

    /**
     *
     **/


    //onCreate method gets called when this activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        //text view object that will show text
        messageText = findViewById(R.id.main_msg_txt);
        //sets the text to hello world when the activity is created
        //messageText.setText("An old silent pond..");

        messageText = findViewById(R.id.sub_msg_txt);
        //sets the text to Goodbye Moon when the activity is created
        //messageText.setText("A frog jumps into the pond,");

        messageText = findViewById(R.id.bot_msg_txt);
        //sets the text to Goodbye Moon when the activity is created
        //messageText.setText("splash! Silence again.");


    }
}
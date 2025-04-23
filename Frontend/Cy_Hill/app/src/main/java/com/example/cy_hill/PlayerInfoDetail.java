package com.example.cy_hill;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cy_hill.databinding.ActivityPlayerInfoDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerInfoDetail extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPlayerInfoDetailBinding binding;


    private EditText nameEdit1;
    private EditText nameEdit2;
    private EditText nameEdit3;
    private EditText nameEdit4;
    private EditText nameEdit5;
    private Button createButton;
    private Button deleteButton;
    private TextView messageView; // Use this TextView to display responses
    private Switch adminSwitch;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CmdConvert cmdConvert = new CmdConvert(getIntent().getStringExtra("mode"));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Make sure to use the correct context here
                Intent intent = new Intent(PlayerInfoDetail.this, PlayerInfo.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info_detail);


        //This whole block is just a major pain in the ass to get the keys of the JSON object
        Intent intent = getIntent();
        String object = intent.getStringExtra("object");
        Log.d("Server Response",object);
        ArrayList<String> keyList = new ArrayList<>();
        try {
            JSONObject JSON = new JSONObject(object);
            Iterator<String> keys = JSON.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                keyList.add(key);
                Log.d("Server Response Key", key); // Logs each key
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //This whole block is just a major pain in the ass to get the keys of the JSON object


        //Log.d("Server Response",JSON.keys());



        nameEdit1 = findViewById(R.id.editText1);
        nameEdit2 = findViewById(R.id.editText2);
        nameEdit3 = findViewById(R.id.editText3);
        nameEdit4 = findViewById(R.id.editText4);
        nameEdit5 = findViewById(R.id.editText5);

        messageView = findViewById(R.id.textView5);

        createButton= findViewById(R.id.button4);
        deleteButton = findViewById(R.id.deleteButton);
        //String userid = "";

        adminSwitch = findViewById(R.id.switch1);


        try {

            //ALL OF THIS JUST FILLS THE HINTS FOR THE TEXT FIELDS, IT SUCKS TO THINK ABOUT
            JSONObject person = new JSONObject(object);
            String userid = person.getString("id");
            EditText[] fields = new EditText[]{nameEdit1, nameEdit2, nameEdit3, nameEdit4,nameEdit5};
            int blank = fields.length-keyList.size();

            for(int i = 0; i < Math.min(keyList.size(), fields.length); i++){
                // Set the hint text for name
                if (person.getString(keyList.get(i)).isEmpty()){
                    fields[i].setHint(keyList.get(i));
                } else {
                    fields[i].setHint(keyList.get(i) + " : " + person.getString(keyList.get(i)));
                }
            }

            for(int i = 0; i < blank; i++){
                fields[keyList.size() + i].setVisibility(View.GONE);
            }

            if(getIntent().getStringExtra("mode").equals("player_info")){
                adminSwitch.setVisibility(View.VISIBLE);
                if(getIntent().getStringExtra("mode").equals("player_info") && !person.getBoolean("isAdmin")) {
                    adminSwitch.setChecked(false);
                } else {
                    adminSwitch.setChecked(true);
                }
            }else{
                adminSwitch.setVisibility(View.GONE);
            }
            //ALL OF THIS JUST FILLS THE HINTS FOR THE TEXT FIELDS, IT SUCKS TO THINK ABOUT




            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ArrayList<String> entry = new ArrayList<>();
                    for(int i = 0; i < Math.min(keyList.size(), fields.length); i++){
                            entry.add(fields[i].getText().toString());
                    }
                    JSONObject postBody = new JSONObject();
                    try {
                        for(int i = 0; i < keyList.size(); i++){
                            postBody.put(keyList.get(i),entry.get(i));
                        }
                        Log.d("Server Response",postBody.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Use StringRequest if the server expects form-urlencoded data
                    JsonObjectRequest jsonArrReq = new JsonObjectRequest(
                            Request.Method.PUT,
                          cmdConvert.getCmd("PUT", Integer.parseInt(userid)),
                            postBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    messageView.setTextColor(Color.GREEN);
                                    messageView.setText(response.toString());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    messageView.setTextColor(Color.RED);
                                    messageView.setText(error.getMessage());
                                }
                            }
                    );
                    // **Add this line to add the request to the request queue**
                    RequestQueue requestQueue = Volley.newRequestQueue(PlayerInfoDetail.this);
                    requestQueue.add(jsonArrReq);


                }
            });







            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //String url = "http://coms-3090-059.class.las.iastate.edu:8080/delete/" + userid;

                    JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                            Request.Method.DELETE,
                            cmdConvert.getCmd("DEL", Integer.parseInt(userid)),
                            null, // Pass null as the request body since it's a GET request
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    messageView.setTextColor(Color.GREEN);
                                    messageView.setText("Successfully Deleted");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    messageView.setTextColor(Color.RED);
                                    //messageView.setText(error.toString());
                                    Log.e("Volley Error", error.toString());
                                }
                    });
                    // **Add this line to add the request to the request queue**
                    RequestQueue requestQueue = Volley.newRequestQueue(PlayerInfoDetail.this);
                    requestQueue.add(jsonArrReq);

//                    Intent intent = new Intent(PlayerInfoDetail.this, PlayerInfo.class);
//                    startActivity(intent);
//                    finish(); // Close the current activity
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
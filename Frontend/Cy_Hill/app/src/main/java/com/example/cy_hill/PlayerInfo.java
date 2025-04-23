package com.example.cy_hill;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayerInfo extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter; // Correct adapter type for strings
    private TextView output;
    private FloatingActionButton newInfoButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PlayerInfo.this, AdminTableSelection.class);
                startActivity(intent);
                finish();
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        listView = findViewById(R.id.listView);
        output = findViewById(R.id.textView3);
        newInfoButton = findViewById(R.id.floatingActionButton2);

        // Initialize the adapter with an empty list
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, // predefined layout for list items
                new ArrayList<>()
        );

        newInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerInfo.this, PlayerInfoCreate.class);
                intent.putExtra("mode",getIntent().getStringExtra("mode"));
                //intent.putExtra("object", selectedItem);
                startActivity(intent);
            }
        });

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        // Set an OnItemClickListener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                Intent intent = new Intent(PlayerInfo.this, PlayerInfoDetail.class);
                intent.putExtra("object", selectedItem);
                intent.putExtra("mode",getIntent().getStringExtra("mode"));
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        adapter.clear();
        makeJsonArrayRequest();
    }


    private void makeJsonArrayRequest() {

        CmdConvert cmdConvert = new CmdConvert(getIntent().getStringExtra("mode"));


        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                cmdConvert.getCmd("LIST"),
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        output.setTextColor(Color.GREEN);
                        output.setText(response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                output.setText(response.toString());
                                JSONObject jsonObject = response.getJSONObject(i);
                                adapter.add(jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Notify adapter of data changes
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        output.setTextColor(Color.RED);
                        output.setText(error.toString());
                    }
                }


        );
        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(PlayerInfo.this);
        requestQueue.add(jsonArrReq);
    }
}

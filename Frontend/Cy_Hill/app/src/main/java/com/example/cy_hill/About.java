package com.example.cy_hill;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cy_hill.databinding.ActivityAboutBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class About extends AppCompatActivity implements MyRecyclerViewAdapterLeaderBoard.ItemClickListener {

    private ActivityAboutBinding binding;
    MyRecyclerViewAdapterLeaderBoard adapterOne;
    ArrayList<LeaderBoard> listOne;
    TextView currentWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);



        // Inflate layout and set content view
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView using binding
        binding.leaderBoard.setLayoutManager(new LinearLayoutManager(About.this));

        // Initialize the ArrayList and adapter
        listOne = new ArrayList<>();
        adapterOne = new MyRecyclerViewAdapterLeaderBoard(About.this, listOne);
        adapterOne.setClickListener(this);

        // Attach the adapter to RecyclerView
        binding.leaderBoard.setAdapter(adapterOne);

        // Transition setup
        Transition fade = new Fade();
        fade.excludeTarget(binding.toolbar, true);
        fade.excludeTarget(binding.bottomNavigation, true);
        getWindow().setEnterTransition(fade);

        // NAV_BAR LOGIC
        binding.bottomNavigation.setSelectedItemId(R.id.item_1);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.item_1:
                    intent = new Intent(this, About.class);
                    break;
                case R.id.item_2:
                    intent = new Intent(this, HillProgress.class);
                    break;
                case R.id.item_3:
                    intent = new Intent(this, GameScreen.class);
                    break;
                case R.id.item_4:
                    intent = new Intent(this, NewsScreeen.class);
                    break;
                case R.id.item_5:
                    intent = new Intent(this, Profile.class);
                    break;
                default:
                    return false;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
            return true;
        });

        // Populate the RecyclerView with JSON data
        //populateRecyclerView();
        buildList();


    }

    private void populateRecyclerView() {
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(new JSONObject().put("playerName", "mmm").put("totalDonation", 9).put("team", "ag"));
            jsonArray.put(new JSONObject().put("playerName", "lll").put("totalDonation", 31).put("team", "lib"));
            jsonArray.put(new JSONObject().put("playerName", "aaa").put("totalDonation", 15).put("team", "xyz"));
            jsonArray.put(new JSONObject().put("playerName", "bbb").put("totalDonation", 22).put("team", "abc"));
            jsonArray.put(new JSONObject().put("playerName", "ccc").put("totalDonation", 45).put("team", "def"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String playerName = jsonObject.getString("playerName");
                int totalDonation = jsonObject.getInt("totalDonation");
                String team = jsonObject.getString("team");
                listOne.add(new LeaderBoard(playerName, totalDonation, team));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(listOne);
        // Notify adapter
        adapterOne.notifyDataSetChanged();
        currentWinner = findViewById(R.id.textView9);
        currentWinner.setText("Current Leader is " + listOne.get(0).getName() + "!");
    }

    @Override
    public void onItemClick(View view, int position) {

    }


    public void buildList(){
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-3090-059.class.las.iastate.edu:8080/get/Leaderboard",
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        try {
                            // Loop through the JSONArray
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String playerName = jsonObject.getString("playerName");
                                int totalDonation = jsonObject.getInt("totalDonation");
                                String team = jsonObject.getString("team");
                                listOne.add(new LeaderBoard(playerName, totalDonation, team));
                            }
                            Log.d("listOne", listOne.toString());
                            Collections.sort(listOne);
                            adapterOne.notifyDataSetChanged();
                            currentWinner = findViewById(R.id.textView9);
                            currentWinner.setText("Current Leader is " + listOne.get(0).getName() + "!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }

        );
        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(About.this);
        Log.d("requestQueue", "Starting request");
        requestQueue.add(jsonArrReq);


    }
}
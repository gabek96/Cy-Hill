package com.example.cy_hill;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cy_hill.databinding.ActivityHillProgressBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.java_websocket.handshake.ServerHandshake;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class HillProgress extends AppCompatActivity implements MyRecyclerViewAdapterHillStats.ItemClickListener,WebSocketListener {


    MyRecyclerViewAdapterHillStats adapterOne;
    private ActivityHillProgressBinding binding;
    ArrayList<Hill> listOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        Transition fade = new Fade();
        fade.excludeTarget(R.id.toolbar, true);
        fade.excludeTarget(R.id.bottom_navigation, true);
        //getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        super.onCreate(savedInstanceState);

        binding = ActivityHillProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = findViewById(R.id.hillList);
        recyclerView.setLayoutManager(new LinearLayoutManager(HillProgress.this));




        listOne = new ArrayList<>();
        adapterOne = new MyRecyclerViewAdapterHillStats(HillProgress.this, listOne);
        adapterOne.setClickListener(HillProgress.this);
        recyclerView.setAdapter(adapterOne);


        String serverUrl = "ws://coms-3090-059.class.las.iastate.edu:8080/hill/" + generateFiveDigitString();

        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(HillProgress.this);

        //NAV_BAR LOGIC
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_2);
        AtomicReference<Intent> intent = new AtomicReference<>(new Intent());//I DO NOT LIKE WHAT GEMINI SUGGESTED FOR THIS
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_1:
                    // Handle item 1 click
                    Log.d("CLICK", "CLICKED ABOUT");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, About.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_2:
                    // Handle item 2 click
                    Log.d("CLICK", "CLICKED HILLS");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, HillProgress.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_3:
                    // Handle item 3 click
                    Log.d("CLICK", "CLICKED DONATE");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, GameScreen.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_4:
                    // Handle item 4 click
                    Log.d("CLICK", "CLICKED NEWS");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, NewsScreeen.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_5:
                    // Handle item 5 click
                    Log.d("CLICK", "CLICKED PROFILE");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, Profile.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                default:
                    return false;
            }
        });
        //END OF NAV_BAR LOGIC
    }


    @Override
    public void onItemClick(View view, int position) {
        adapterOne.getItem(position).toggleExpanded();
        adapterOne.notifyItemChanged(position);
        //Toast.makeText(this, "You clicked in hill progress", Toast.LENGTH_SHORT).show();
        //Log.d("CLICK", "CLICKED IN HILL PROGRESS");

    }

    public void refreshData() {
        // Clear the existing data
        WebSocketManager.getInstance().sendMessage("refresh");
        listOne.clear();
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        WebSocketManager.getInstance().sendMessage("FRONTEND: connected");

    }

    @Override
    public void onWebSocketMessage(String message) {
        try {
            if (new JSONArray(message).length() != 0) {
                listOne.clear();
                Hill hill = null;
                JSONArray JSONA = null;
                Log.d("MESSAGE", message);
                message = message.substring(0);
                Log.d("NEW MESSAGE", message);


                try {
                    JSONA = new JSONArray(message);
//                    Log.d("JSON", JSONA.toString());
//                    Log.d("JSON", JSONA.getJSONObject(0).getString("name"));
//                    hill = new Hill(JSONA.getJSONObject(0));
//                    Log.d("HILL", hill.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray finalJSONA = JSONA;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update data and notify adapter here
                        if (finalJSONA != null) {

                            for (int i = 0; i < finalJSONA.length(); i++) {
                                try {
                                    listOne.add(new Hill(finalJSONA.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //listOne.add(finalHill);
                            //adapterOne.notifyItemInserted(listOne.size() - 1);
                            adapterOne.notifyDataSetChanged();
                        }
                    }
                });
            }
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("ERROR", ex.toString());
    }

    public static String generateFiveDigitString() {
        Random random = new Random();
        int number = 10000 + random.nextInt(90000); // Generates a number between 10000 and 99999
        return String.valueOf(number);
    }
}
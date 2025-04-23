package com.example.cy_hill;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cy_hill.databinding.ActivityNewsScreeenBinding;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class NewsScreeen extends AppCompatActivity implements WebSocketListener, MyRecyclerViewAdapterNewsScreen.ItemClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNewsScreeenBinding binding;
    MyRecyclerViewAdapterNewsScreen adapterOne;
    ArrayList<News> listOne;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        Transition fade = new Fade();
        fade.excludeTarget(R.id.toolbar, true);
        fade.excludeTarget(R.id.bottom_navigation, true);
        //getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        binding = ActivityNewsScreeenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = findViewById(R.id.newsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewsScreeen.this));

        listOne = new ArrayList<>();
        adapterOne = new MyRecyclerViewAdapterNewsScreen(NewsScreeen.this, listOne);
        adapterOne.setClickListener(NewsScreeen.this);
        recyclerView.setAdapter(adapterOne);

        String serverUrl = "ws://coms-3090-059.class.las.iastate.edu:8080/news/frontendNEWS";
        //String serverUrl = "ws://10.0.2.2:8080/chat/NEWS";
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(NewsScreeen.this);


        //NAV_BAR LOGIC
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_4);
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
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        WebSocketManager.getInstance().sendMessage("connected");
    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("MESSAGE", message);

        // Only parse JSON if the message contains "Welcome back"
        if (!message.contains("[]") && !message.contains("Welcome back") && !message.contains("frontendNEWS") && !message.contains("id")) {
            try {
                Log.d("ATTEMPTING TO PARSE", message);
                JSONArray JSONA = new JSONArray(message);
                Log.d("JSON", JSONA.toString());


                listOne.clear();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < JSONA.length(); i++) {
                            try {
                                listOne.add(new News(JSONA.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterOne.notifyDataSetChanged();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("ERROR", ex.toString());
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
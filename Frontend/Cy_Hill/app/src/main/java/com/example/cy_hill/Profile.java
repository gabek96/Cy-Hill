package com.example.cy_hill;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cy_hill.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.util.concurrent.atomic.AtomicReference;

public class Profile extends AppCompatActivity {
    private ActivityProfileBinding binding;

    private String url;

    private TextView level;
    private TextView coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        //level = findViewById(R.id.playerLevel);
        //coins = findViewById(R.id.playerCoins);


        Transition fade = new Fade();
        fade.excludeTarget(R.id.toolbar, true);
        fade.excludeTarget(R.id.bottom_navigation, true);
        //getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getLevel();
        getCoins();

        //NAV_BAR LOGIC
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_5);
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

    private void getLevel() {
        url = "http://coms-3090-059.class.las.iastate.edu:8080/";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        level.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void getCoins() {
        url = "http://coms-3090-059.class.las.iastate.edu:8080/";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        coins.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}
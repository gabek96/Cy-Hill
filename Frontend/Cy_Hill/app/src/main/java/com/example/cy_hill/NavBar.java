package com.example.cy_hill;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

//import com.example.cy_hill.databinding.ActivityNavBar;
import com.example.cy_hill.databinding.ActivityNavbarBinding;
import com.example.cy_hill.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.atomic.AtomicReference;

public class NavBar extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNavbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_navbar);


        Transition fade = new Fade();
        fade.excludeTarget(R.id.toolbar, true);
        fade.excludeTarget(R.id.bottom_navigation, true);
        getWindow().setEnterTransition(fade);

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
}
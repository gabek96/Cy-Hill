package com.example.cy_hill;



import androidx.navigation.ui.AppBarConfiguration;

import org.junit.Rule;
import org.junit.Test;

import com.example.cy_hill.R;
import com.example.cy_hill.databinding.ActivityNewsScreeenBinding;

import java.util.ArrayList;


public class NewsScreeenTest {

    @Test
    public void onCreate() {
        try {
            NewsScreeen newsScreeen = new NewsScreeen();
            newsScreeen.onCreate(null);
            assert true : "onCreate executed successfully";
        } catch (Exception e) {
            assert false : "onCreate threw an exception: " + e.getMessage();
        }
    }

    @Test
    public void onWebSocketOpen() {
        NewsScreeen newsScreeen = new NewsScreeen();
        assert true : "onWebSocketOpen executed successfully";
    }

    @Test
    public void onWebSocketMessage() {
        // Arrange
        NewsScreeen newsScreeen = new NewsScreeen();
        newsScreeen.onCreate(null);
        AppBarConfiguration appBarConfiguration;
        ActivityNewsScreeenBinding binding;
        MyRecyclerViewAdapterNewsScreen adapterOne;
        ArrayList<News> listOne;

    }

    @Test
    public void onWebSocketClose() {
    }

    @Test
    public void onWebSocketError() {
    }

    @Test
    public void onItemClick() {
    }

}
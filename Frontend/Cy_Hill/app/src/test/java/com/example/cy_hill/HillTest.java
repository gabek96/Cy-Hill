package com.example.cy_hill;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28}, manifest = Config.NONE  ) // Specify a compatible Android SDK version
public class HillTest {

    @Test
    public void getName() {

    }

    @Test
    public void getLeader() {
    }

    @Test
    public void getGoal() {
    }

    @Test
    public void getAgPoints() {
    }

    @Test
    public void getBusPoints() {
    }

    @Test
    public void getDesPoints() {
    }

    @Test
    public void getEngPoints() {
    }

    @Test
    public void getHumPoints() {
    }

    @Test
    public void getLibPoints() {
    }

    @Test
    public void getVetPoints() {
    }

    @Test
    public void getGradPoints() {
    }

    @Test
    public void getEduPoints() {
    }

    @Test
    public void getAllPoints() {
        try {
            JSONObject hillJSON = new JSONObject();

            hillJSON.put("name", "Test Hill");
            hillJSON.put("leader", "Test Leader");
            hillJSON.put("goal", 100);
            hillJSON.put("agPoints", 20);
            hillJSON.put("busPoints", 20);
            hillJSON.put("desPoints", 20);
            hillJSON.put("engPoints", 20);
            hillJSON.put("humPoints", 20);
            hillJSON.put("libPoints", 0);
            hillJSON.put("vetsPoints", 0);
            hillJSON.put("gradPoints", 0);
            hillJSON.put("eduPoints", 0);

            Hill hill = new Hill(hillJSON);
            int progress = hill.getAllPoints();
            assertEquals(100, progress);

        } catch (JSONException e) {
            assertEquals(true, false);
        }
    }

    @Test
    public void getProgress() {
        try {
            JSONObject hillJSON = new JSONObject();

            hillJSON.put("name", "Test Hill");
            hillJSON.put("leader", "Test Leader");
            hillJSON.put("goal", 100);
            hillJSON.put("agPoints", 20);
            hillJSON.put("busPoints", 20);
            hillJSON.put("desPoints", 20);
            hillJSON.put("engPoints", 20);
            hillJSON.put("humPoints", 20);
            hillJSON.put("libPoints", 0);
            hillJSON.put("vetsPoints", 0);
            hillJSON.put("gradPoints", 0);
            hillJSON.put("eduPoints", 0);

            Hill hill = new Hill(hillJSON);
            int progress = hill.getProgress();
            assertEquals(100, progress);

        } catch (JSONException e) {
            assertEquals(true, false);
        }

    }

    @Test
    public void isExpanded() {
    }

    @Test
    public void toggleExpanded() {
    }
}
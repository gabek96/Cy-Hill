package com.example.cy_hill;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Hill {
    private final String name;
    private final String leader;
    private int goal;


    private final int agPoints;
    private final int busPoints;
    private final int desPoints;
    private final int engPoints;
    private final int humPoints;
    private final int libPoints;
    private final int vetsPoints;
    private final int gradPoints;
    private final int eduPoints;

    private boolean isExpanded = false;

    public Hill(JSONObject hillJSON) throws JSONException {
        name = hillJSON.getString("name");
        leader = hillJSON.getString("leader");
        goal = hillJSON.getInt("goal");
        agPoints = hillJSON.getInt("agPoints");
        busPoints = hillJSON.getInt("busPoints");
        desPoints = hillJSON.getInt("desPoints");
        engPoints = hillJSON.getInt("engPoints");
        humPoints = hillJSON.getInt("humPoints");
        libPoints = hillJSON.getInt("libPoints");
        vetsPoints = hillJSON.getInt("vetsPoints");
        gradPoints = hillJSON.getInt("gradPoints");
        eduPoints = hillJSON.getInt("eduPoints");
    }

    public String getName(){
        return name;
    }

    public String getLeader(){
        return leader;
    }

    public int getGoal(){
        return goal;
    }

    public int getAgPoints(){
        return (int)(((float) agPoints / Math.max(1, getGoal())) * 100);
    }

    public int getBusPoints(){
        return (int)(((float) busPoints / Math.max(1, getGoal())) * 100);
    }

    public int getDesPoints(){
        return (int)(((float) desPoints / Math.max(1, getGoal())) * 100);
    }

    public int getEngPoints(){
        return (int)(((float) engPoints / Math.max(1, getGoal())) * 100);
    }

    public int getHumPoints(){
        return (int)(((float) humPoints / Math.max(1, getGoal())) * 100);
    }

    public int getLibPoints(){
        return (int)(((float) libPoints / Math.max(1, getGoal())) * 100);
    }

    public int getVetPoints(){
        return (int)(((float) vetsPoints / Math.max(1, getGoal())) * 100);
    }

    public int getGradPoints(){
        return (int)(((float) gradPoints / Math.max(1, getGoal())) * 100);
    }

    public int getEduPoints(){
        return (int)(((float) eduPoints / Math.max(1, getGoal())) * 100);
    }

    public int getAllPoints(){
        return agPoints + busPoints + desPoints + engPoints + humPoints + libPoints + vetsPoints + gradPoints + eduPoints;
    }

    public int getProgress(){
        Log.d("TOTAL POINTS", String.valueOf(getAllPoints()));
        Log.d("GOAL", String.valueOf(getGoal()));
        Log.d("MAX", String.valueOf(Math.max(1, getGoal())));
        Log.d("Pre 100", String.valueOf(((float) getAllPoints() / Math.max(1, getGoal()))));
        Log.d("PERCENTAGE", String.valueOf(((float) getAllPoints() / Math.max(1, getGoal())) * 100));
        Log.d("POST 100", String.valueOf(((int)((float) getAllPoints() / Math.max(1, getGoal())) * 100)));
        return (int)(((float) getAllPoints() / Math.max(1, getGoal())) * 100);


    }

    public boolean isExpanded() {
        return isExpanded;
    }

    void toggleExpanded() {
        isExpanded = !isExpanded;
    }

}
package com.example.cy_hill;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class News {
    private final String statement;
    private final String graphic;
    private final String timestamp;

    public News(JSONObject json) throws JSONException {
        this.statement = json.getString("message");

        this.graphic = json.getString("graphic");
        this.timestamp = json.getString("timestamp");
    }

    public String getStatement() {
        return statement;
    }
    public int getGraphic() {
        switch (graphic){
            case "notify" :
                return R.drawable.baseline_notifications_24;
            case "skull" :
                return R.drawable.outline_skull_24;
            case "run" :
                return R.drawable.rounded_directions_run_24;
            case "flag" :
                return R.drawable.rounded_flag_2_24;
            case "taunt" :
                return R.drawable.rounded_taunt_24;
            case "trophy" :
                return R.drawable.rounded_trophy_24;
        }



        return R.drawable.baseline_notifications_24;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

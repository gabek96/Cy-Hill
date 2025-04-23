package com.example.cy_hill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class AdminTableSelection extends AppCompatActivity {

    private Button playerInfoButton;
    private Button playerStatsButton;
    private Button hillStatsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_table_selection);

        playerInfoButton = findViewById(R.id.button);
        playerStatsButton = findViewById(R.id.button2);
        hillStatsButton = findViewById(R.id.button3);


        CmdConvert cmdConvert = new CmdConvert("player_info");
        String[] modes = {"player_info", "player_stats", "hill_stats"};
        Log.d("----","---------------------------------------");
        for(int i=0;i< modes.length;i++){
            cmdConvert = new CmdConvert(modes[i]);

            Log.d("LIST TEST",modes[i] + "-LIST-" + cmdConvert.getCmd("LIST"));
            Log.d("POST TEST",modes[i] + "-POST-" + cmdConvert.getCmd("POST"));
            Log.d("GET TEST",modes[i] + "-GET-" + cmdConvert.getCmd(("GET"),1));
            Log.d("PUT TEST",modes[i] + "-PUT-" + cmdConvert.getCmd(("PUT"),1));
            Log.d("DEL TEST",modes[i] + "-DEL-" + cmdConvert.getCmd(("DEL"),1));
            Log.d("----","---------------------------------------");
        }


        playerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminTableSelection.this, PlayerInfo.class);
                intent.putExtra("mode","player_info");
                startActivity(intent);
                finish();
            }
        });

        playerStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminTableSelection.this, PlayerInfo.class);
                intent.putExtra("mode","player_stats");
                startActivity(intent);
                finish();
            }
        });

        hillStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminTableSelection.this, PlayerInfo.class);
                intent.putExtra("mode","hill_stats");
                startActivity(intent);
                finish();
            }
        });
    }


}
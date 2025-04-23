package com.example.cy_hill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerInfoCreate extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private Button createButton;
    private TextView messageView; // Use this TextView to display responses

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PlayerInfoCreate.this, PlayerInfo.class);
                startActivity(intent);
                finish();
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info_create);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        messageView = findViewById(R.id.textView5);
        createButton= findViewById(R.id.button4);

        editText1.setHint("name/name/current_owner");
        editText2.setHint("birthday/coins/lat");
        editText3.setHint("email/lon");
        editText4.setHint("password/name");
        editText5.setHint("radius");

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();
                String text3 = editText3.getText().toString();
                String text4 = editText4.getText().toString();
                postRequest(text1, text2, text3, text4);
            }
        });
    }

    private void postRequest(String username, String birthday, String email, String password/*, String confirmPassword*/) {

        Intent intent = getIntent();
        String object = intent.getStringExtra("object");

        CmdConvert cmdConvert = new CmdConvert(getIntent().getStringExtra("mode"));

        // Create the JSON object to send in the POST request
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("name", username);//TODO: NEEDS RENAMED
            postBody.put("birthday", birthday);//TODO: NEEDS RENAMED
            postBody.put("email", email);//TODO: NEEDS RENAMED
            postBody.put("password", password);//TODO: NEEDS RENAMED

            Log.d("POST BODY",postBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use StringRequest if the server expects form-urlencoded data
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                //url,
                cmdConvert.getCmd("POST"),
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        messageView.setTextColor(Color.BLACK);
                        messageView.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        messageView.setTextColor(Color.RED);
                        messageView.setText(error.getMessage());
                        Log.e("Volley Error", error.toString());
                    }
                }
        );
        // **Add this line to add the request to the request queue**
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}

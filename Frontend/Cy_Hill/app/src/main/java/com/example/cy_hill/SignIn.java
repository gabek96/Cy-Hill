package com.example.cy_hill;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignIn extends AppCompatActivity {

    //private String url = "https://10.26.133.222:8080/users/create";
    //private String url = "https://efd52c8a-2478-40f7-9c6c-8cc5373f3268.mock.pstmn.io";
    private String url = "http://coms-3090-059.class.las.iastate.edu:8080/login/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextInputEditText nameText = findViewById(R.id.name);
        TextInputEditText passwordText = findViewById(R.id.password);
        Button backButton = findViewById(R.id.back_button);
        Button signinButton = findViewById(R.id.create_button);
        TextView forgotPassword = findViewById(R.id.forget_password);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = nameText.getText().toString();
                String password = passwordText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    if (username.isEmpty()) {
                        nameText.setError("Name is required");
                    }
                    if (password.isEmpty()) {
                        passwordText.setError("Password is required");
                    }
                   return;
                }

                url = "http://coms-3090-059.class.las.iastate.edu:8080/login/" + username + "/" + password;
                Log.d("URL", url);

                // Use StringRequest if the server expects form-urlencoded data
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Volley", response.toString());
                                Intent intent = new Intent(SignIn.this, GameScreen.class);

                                try {
                                    intent.putExtra("id", response.getString("id"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    Log.e("Volley Error", error.toString());
                                    Log.e("Volley Error", "Status Code: " + error.networkResponse.statusCode);
                                    Log.e("Volley Error", "Response Data: " + new String(error.networkResponse.data));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if(new String(error.networkResponse.data).equals("{\"id\":\"0\"}")){
                                    nameText.setError("Invalid username or password");
                                    passwordText.setError("Invalid username or password");
                                }
                            }
                        }
                );
                // **Add this line to add the request to the request queue**
                RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
                requestQueue.add(request);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
}



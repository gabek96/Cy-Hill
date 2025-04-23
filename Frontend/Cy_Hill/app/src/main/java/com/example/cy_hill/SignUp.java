package com.example.cy_hill;

import static com.google.gson.internal.$Gson$Types.arrayOf;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        TextInputEditText textname = findViewById(R.id.name);
        TextInputEditText textemail = findViewById(R.id.email);
        TextInputEditText textDate = findViewById(R.id.date);
        TextInputEditText textPassword = findViewById(R.id.password);
        TextInputEditText textConfirmPassword = findViewById(R.id.confirmPassword);
        Button back_button = findViewById(R.id.back_button);
        Button create_button = findViewById(R.id.create_button);

        String[] items = {getString(R.string.ag), getString(R.string.bus), getString(R.string.des), getString(R.string.eng), getString(R.string.hum), getString(R.string.lib), getString(R.string.vet), getString(R.string.grad), getString(R.string.ed)};

// Find the AutoCompleteTextView inside the TextInputLayout
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

// Set up the ArrayAdapter with a predefined layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);

// Attach the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);

// Optional: Set threshold to 1 to show dropdown on the first character
        autoCompleteTextView.setThreshold(1);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = textname.getText().toString();
                String email = textemail.getText().toString();
                String date = textDate.getText().toString();
                String password = textPassword.getText().toString();
                String confirmPassword = textConfirmPassword.getText().toString();


                // Check if any of the fields are empty
                if ((name.isEmpty() || email.isEmpty() || date.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())) {
                    if (name.isEmpty()) {
                        textname.setError("Name is required");
                    }
                    if (email.isEmpty()) {
                        textemail.setError("Email is required");
                    }
                    if (date.isEmpty()) {
                        textDate.setError("Date is required");
                    }
                    if (password.isEmpty()) {
                        textPassword.setError("Password is required");
                    }
                    if (confirmPassword.isEmpty()) {
                        textConfirmPassword.setError("Confirm Password is required");
                    }
                    if (autoCompleteTextView.getText().toString().isEmpty()) {
                        autoCompleteTextView.setError("Select an item");
                    }
                    return;
                }

                // Check if the password and confirm password match
                if (!password.equals(confirmPassword)) {
                    textConfirmPassword.setError("Passwords do not match");
                    Log.d("password", password + "does not match");
                    return;
                }

                String team;
                if (autoCompleteTextView.getText().toString().equals(getString(R.string.ag))) {
                    team = "ag";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.bus))) {
                    team = "bus";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.des))) {
                    team = "des";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.eng))) {
                    team = "eng";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.hum))) {
                    team = "hum";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.lib))) {
                    team = "lib";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.vet))) {
                    team = "vet";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.grad))) {
                    team = "grad";
                } else if (autoCompleteTextView.getText().toString().equals(getString(R.string.ed))) {
                    team = "ed";
                } else {
                    team = "";
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", name);
                    jsonObject.put("birthday", date);
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.d("name", name);
                Log.d("email", email);
                Log.d("date", date);
                Log.d("password", password);
                Log.d("confirmPassword", confirmPassword);
                Log.d("team", team);
                Log.d("JSON", jsonObject.toString());

                String url = "http://coms-3090-059.class.las.iastate.edu:8080/player/create/" + team;

                Log.d("url", url);


                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("response", response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("error", error.toString());
                            }
                        }
                );
                // **Add this line to add the request to the request queue**
                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                requestQueue.add(request);


            }
        });


        back_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
package com.example.cy_hill;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONObject;

public class ForgotPasswordLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_login);


        TextInputEditText texttoken = findViewById(R.id.token);
        TextInputEditText textpassword = findViewById(R.id.password);
        TextInputEditText textconfirmPassword = findViewById(R.id.confirmPassword);
        Button back_button = findViewById(R.id.back_button);
        Button create_button = findViewById(R.id.create_button);


        create_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 //PostMapping /{username}/resetpassword/{token}/{newPassword}

                 String token = texttoken.getText().toString();
                 String password = textpassword.getText().toString();
                 String confirmPassword = textconfirmPassword.getText().toString();
                 String username = getIntent().getStringExtra("name");
                 String domain = "http://coms-3090-059.class.las.iastate.edu:8080";
                 String url = domain + "/" + username + "/resetpassword/" + token + "/" + password;
                 Log.d("url", url);

                 // Check if the password and confirm password match
                 if (!password.equals(confirmPassword)) {
                     textconfirmPassword.setError("Passwords do not match");
                     Log.d("password", password + "does not match");
                     return;
                 }

                 JsonObjectRequest request = new JsonObjectRequest(
                         Request.Method.POST,
                         url,
                         null,
                         new Response.Listener<JSONObject>() {
                             @Override
                             public void onResponse(JSONObject response) {
                                 Log.d("response", response.toString());
                                 Intent intent = new Intent(ForgotPasswordLogin.this, SignIn.class);
                                 startActivity(intent);
                             }
                         },
                         new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {
                                 Log.e("error", error.toString());
                                 //TODO This needs to be removed once error in how its sent is fixed
                                 Intent intent = new Intent(ForgotPasswordLogin.this, SignIn.class);
                                 startActivity(intent);
                             }
                         }
                 );
                 // **Add this line to add the request to the request queue**
                 RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordLogin.this);
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
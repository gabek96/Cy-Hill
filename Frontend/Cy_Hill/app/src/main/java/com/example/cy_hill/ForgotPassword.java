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

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Postmapping "/{username}/forgotpassword/{email}"

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        TextInputEditText textname = findViewById(R.id.name);
        TextInputEditText textemail = findViewById(R.id.email);
        Button back_button = findViewById(R.id.back_button);
        Button submit_button = findViewById(R.id.submit_button);


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = textname.getText().toString();
                String email = textemail.getText().toString();
                String url = "http://coms-3090-059.class.las.iastate.edu:8080/" + name + "/forgotpassword/" + email;
                Log.d("url", url);


                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("response", response.toString());
                                Intent intent = new Intent(ForgotPassword.this, ForgotPasswordLogin.class);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error", error.toString());
                                //TODO This needs to be removed once error in how its sent is fixed
                                Intent intent = new Intent(ForgotPassword.this, ForgotPasswordLogin.class);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        }
                );
                // **Add this line to add the request to the request queue**
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
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
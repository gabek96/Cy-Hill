package com.example.cy_hill;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cy_hill.databinding.ActivityGameScreenBinding;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*
* Google Maps API key for COMS 309 CyHill Game
* AIzaSyBkrea0Q0zu700qjIhbt0rxkU0fq1iThwA
*/

public class GameScreen extends FragmentActivity implements OnMapReadyCallback,WebSocketListener {

    private String url = "https://d3c5f2a9-e29b-4876-befd-4375ee29b80d.mock.pstmn.io";
    //private String url = "http://coms-3090-059.class.las.iastate.edu:8080/7/donate/3/1";

    private GoogleMap mMap;
    private ActivityGameScreenBinding binding;
    private Button donate;
    private int coins;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng currLocation;
    private UiSettings mUiSettings;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long REFRESH_TIME = 1000; //How often the listener updates location, in milliseconds
    private final long REFRESH_DIST = 1; //Listener updates location, in meters

    private TextView messageView; // Use this TextView to display responses
    private Handler handler = new Handler();
    private boolean mAutoIncrement = false;
    Runnable runnable;

    View increaseView;
    int DELAY = 1;

    private Circle circle;


    ArrayList<MarkerOptions> hills = new ArrayList<>();
    String hillId = "";
    JSONObject idList = new JSONObject();




    @Override
    protected void onCreate(Bundle savedInstanceState) {



        String userId = this.getIntent().getStringExtra("id");




        Log.d("THIS IS YOUR ID",userId);

        super.onCreate(savedInstanceState);

        binding = ActivityGameScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView coinLeft = findViewById(R.id.textView8);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);


        //Websocket for Hills
        String serverUrl = "ws://coms-3090-059.class.las.iastate.edu:8080/hillLocation/" + generateFiveDigitString();
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(GameScreen.this);





        //NAV_BAR LOGIC
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.item_3);
        AtomicReference<Intent> intent = new AtomicReference<>(new Intent());//I DO NOT LIKE WHAT GEMINI SUGGESTED FOR THIS
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_1:
                    // Handle item 1 click
                    Log.d("CLICK", "CLICKED ABOUT");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, About.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_2:
                    // Handle item 2 click
                    Log.d("CLICK", "CLICKED HILLS");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, HillProgress.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_3:
                    // Handle item 3 click
                    Log.d("CLICK", "CLICKED DONATE");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, GameScreen.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_4:
                    // Handle item 4 click
                    Log.d("CLICK", "CLICKED NEWS");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, NewsScreeen.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                case R.id.item_5:
                    // Handle item 5 click
                    Log.d("CLICK", "CLICKED PROFILE");
                    //overridePendingTransition(0,0);
                    intent.set(new Intent(this, Profile.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                        startActivity(intent.get(), options.toBundle());
                    } else {
                        startActivity(intent.get());
                    }
                    return true;

                default:
                    return false;
            }
        });
        //END OF NAV_BAR LOGIC


        coins = 0;
        donate = findViewById(R.id.donatebtn);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://coms-3090-059.class.las.iastate.edu:8080/" + userId +  "/donate/" + hillId + "/1";
                Log.d("URL TO DONATE", url);
                Toast.makeText(GameScreen.this, "Donation sent", Toast.LENGTH_SHORT).show();

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                coins++;
                                //coins.setText(response);
                                try {
                                    Log.d("response", response.toString());
                                    Integer coinsRemain = response.getInt("Remaining coins");
                                    Log.d("Coins to be entered", coinsRemain.toString());
                                    coinLeft.setText(String.valueOf(coinsRemain));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.d("response", response.toString());

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error", error.toString());
                            }
                        }
                );
                // **Add this line to add the request to the request queue**
                RequestQueue requestQueue = Volley.newRequestQueue(GameScreen.this);
                requestQueue.add(request);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(GameScreen.this, "Location was found", Toast.LENGTH_SHORT).show();
                getLastLocation();
            } else {
                Toast.makeText(GameScreen.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    //Toast.makeText(GameScreen.this, "Location set", Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                    mapFragment.getMapAsync(GameScreen.this);
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Ames, Iowa.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        mUiSettings = mMap.getUiSettings();
        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setScrollGesturesEnabled(false);
        mUiSettings.setZoomGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(true);


        mMap.setOnMarkerClickListener(clickedMarker -> {

            return false;
        });


        if (currLocation != null) {
            currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        else {
            currLocation = new LatLng(42.0267, -93.6465);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // Hides any open info window (if there is one)
                if (mMap != null) {
                    // Hides the info window for any currently displayed marker
                    mMap.setInfoWindowAdapter(null);
                    mMap.setOnCircleClickListener(null);

                }
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;  // Update currentLocation
                currLocation = new LatLng(location.getLatitude(), location.getLongitude());

                // Update the marker on the map
                mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(currLocation).title("My Location"));
                //DrawHills();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 17));

                Log.d("hill count", String.valueOf(hills.size()));
                if (hills.size() > 0) {

                    for (int i = 0; i < hills.size(); i++) {
                        Marker marker = mMap.addMarker(hills.get(i).position(hills.get(i).getPosition()).title(hills.get(i).getTitle()));
                        Log.w("MARKER", hills.get(i).toString());
                        Log.w("position", hills.get(i).getPosition().toString());
                        Log.w("title", hills.get(i).getTitle());
                        Log.w("snippet", hills.get(i).getSnippet());

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                // Handle the marker click here
                                // You can get the marker's title, snippet, tag, etc.


                                // Return false to allow the default behavior (camera moves and info window opens if available)
                                // Return true if you’ve handled the click and don’t want the default behavior.
                                try {
                                    hillId = (String) idList.get(marker.getTitle());// Remove previous circle
                                    Log.d("HILL ID", hillId);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                if (circle != null) {
                                    circle.remove();
                                }

                                LatLng icon = marker.getPosition();
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(icon)
                                        .radius(50) // Radius in meters
                                        .strokeColor(0xFF0000FF)
                                        .fillColor(0x220000FF)
                                        .strokeWidth(5f));

                                // Check if user is within radius
                                checkProximity(icon, 50);

                                return false;
                            }
                        });



                    }
                }
            }

        };




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, REFRESH_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //Adds a Circle around the provided Place item.
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }
    }

    private void postRequest(int coins) {

        Intent intent = getIntent();
        String object = intent.getStringExtra("object");

        CmdConvert cmdConvert = new CmdConvert(getIntent().getStringExtra("mode"));

        // Create the JSON object to send in the POST request
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("coins", coins);
            Log.d("POST BODY",postBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use StringRequest if the server expects form-urlencoded data
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
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

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("MESSAGE", message);
        if(!message.contains("Welcome")){
            try {
            hills.clear();
            JSONArray JSONA = new JSONArray(message);
            Log.d("JSON", JSONA.toString());
            Log.d("JSON", JSONA.getJSONObject(0).getString("name"));
            for (int i = 0; i < JSONA.length(); i++) {
                Log.d("----","-------------------------------------");
                Log.d("HILL COUNT", JSONA.length() + "");
                Log.d("ATTEMPTING TO ADD HILL", JSONA.getJSONObject(i).getString("name"));
                String id = JSONA.getJSONObject(i).getString("id");
                Log.d("ID", id);
                String name = JSONA.getJSONObject(i).getString("name");
                Log.d("NAME", name);
                Double lat = JSONA.getJSONObject(i).getDouble("lat");
                Log.d("LAT", lat + "");
                Double lon = JSONA.getJSONObject(i).getDouble("lon");
                Log.d("LON", lon + "");
                Integer coins = JSONA.getJSONObject(i).getInt("totalCoins");
                Log.d("COINS", coins + "");
                Integer goal = JSONA.getJSONObject(i).getInt("goal");
                Log.d("GOAL", goal + "");

                idList.put(name, id);

                LatLng latLng = new LatLng(lon, lat);
                hills.add(new MarkerOptions()
                        .position(latLng)
                        .title(name)
                        .snippet("Progress: " + coins + "/" + goal)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hill_icon)));


            }
            Log.d("HILL COUNT", hills.size() + "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }

    public static String generateFiveDigitString() {
        Random random = new Random();
        int number = 10000 + random.nextInt(90000); // Generates a number between 10000 and 99999
        return String.valueOf(number);
    }

    private void checkProximity(LatLng center, double radius) {
        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the user's current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                // Calculate the distance to the center of the circle
                float[] distance = new float[1];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        center.latitude, center.longitude, distance);

                // Check if the user is within the radius
                if (distance[0] <= radius) {
                    donate.setVisibility(View.VISIBLE);
                } else {
                    donate.setVisibility(View.GONE);
                }
            } else {
                // Handle case where location is null
                donate.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            // Handle failure to get location
            donate.setVisibility(View.GONE);
        });
    }



}
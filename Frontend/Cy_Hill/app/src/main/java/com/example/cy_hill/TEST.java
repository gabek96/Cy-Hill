//package com.example.cy_hill;
//
//import android.content.Intent;
//import android.gesture.GestureOverlayView;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.os.Bundle;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.tabs.TabLayout;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
//
//public class TEST extends AppCompatActivity implements MyRecyclerViewAdapterHillStats.ItemClickListener {
//
//    MyRecyclerViewAdapterHillStats adapterOne;
//    MyRecyclerViewAdapterHillStats adapterTwo;
//    MyRecyclerViewAdapterHillStats adapterThree;
//    private FloatingActionButton createButton;
//    private String currentList = "player_info";
//    List<String> list = new ArrayList<>();
//
//
//
//
////    public ItemTouchHelper helperBuilder(ArrayList<String> list, MyRecyclerViewAdapterHillStats adapter){
////        ItemTouchHelper helper = new ItemTouchHelper(
////                new ItemTouchHelper.Callback() {
////                    @Override
////                    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
////                        return makeMovementFlags(
////                                ItemTouchHelper.UP | ItemTouchHelper.DOWN
////                                ,ItemTouchHelper.START | ItemTouchHelper.END);
////                    }
////
////                    @Override
////                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
////                        return false;
////                    }
////
////                    @Override
////                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
////                        switch (direction){
////                            case ItemTouchHelper.START:
////                                Toast.makeText(TEST.this,list.get(viewHolder.getAdapterPosition()) + " removed", Toast.LENGTH_SHORT).show();
////                                list.remove(viewHolder.getAdapterPosition());
////                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
////                                break;
////                            case ItemTouchHelper.END:
////                                break;
////                        }
////                    }
////
////                    @Override
////                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
////                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
////                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(TEST.this, R.color.RED))
////                                .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
////                                .addSwipeRightBackgroundColor(ContextCompat.getColor(TEST.this, R.color.GREEN))
////                                .addSwipeRightActionIcon(R.drawable.baseline_edit_24)
////
////                                .create()
////                                .decorate();
////                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
////                    }
////                }
////        );
////
////        return helper;
////    }
//
//    public void buildList(String mode){
//        CmdConvert cmdConvert = new CmdConvert(mode);
//        list.clear();
//        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
//                Request.Method.GET,
//                cmdConvert.getCmd("LIST"),
//                null, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("Response", response.toString());
//                        try {
//                            // Loop through the JSONArray
//                            for (int i = 0; i < response.length(); i++) {
//                                // Add each string from the JSONArray to the list
//                                list.add(response.getString(i));  // Assuming it's a list of strings
//                                adapterOne.notifyDataSetChanged();
//                            }
//                            //recyclerView.setAdapter(adapterOne);
//                            //return adapterOne;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
//                    }
//                }
//
//        );
//        // Add the request to the request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(TEST.this);
//        Log.d("requestQueue", "Starting request");
//        requestQueue.add(jsonArrReq);
//    }
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        createButton = findViewById(R.id.floatingActionButton);
//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//
//        // Set the default tab to the first one
//        tabLayout.selectTab(tabLayout.getTabAt(0));
//        ArrayList<String> listOne = new ArrayList<>(Arrays.asList("Horse", "Cow", "Camel", "Sheep", "Goat"));
//        ArrayList<String> listTwo = new ArrayList<>(Arrays.asList("Apple", "Banana", "Orange", "Mango", "Grapes"));
//        ArrayList<String> listThree = new ArrayList<>(Arrays.asList("Rose", "Tulip", "Sunflower", "Fern", "Cactus"));
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(TEST.this));
//
//
//        adapterOne = new MyRecyclerViewAdapterHillStats(TEST.this, list);
//        adapterOne.setClickListener(TEST.this);
//        adapterTwo = new MyRecyclerViewAdapterHillStats(TEST.this, listTwo);
//        adapterTwo.setClickListener(TEST.this);
//        adapterThree = new MyRecyclerViewAdapterHillStats(TEST.this, listThree);
//        adapterThree.setClickListener(TEST.this);
//
//        buildList("player_info");
//        recyclerView.setAdapter(adapterOne);
//        tabLayout.selectTab(tabLayout.getTabAt(0));
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);  // 2 columns
//        recyclerView.setLayoutManager(gridLayoutManager);
//
//
//
//
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                // This is called when a tab is selected (toggled)
//                int position = tab.getPosition(); // You can use the position to determine which tab is selected
//                String tabText = tab.getText().toString(); // Get the text of the selected tab
//                // Do something when a tab is selected
//                Log.d("Tab Selected", "Tab: " + tabText + " at position: " + position);
//                switch (position) {
//                    case 0:
//                        //recyclerView.swapAdapter(adapterOne, true);
//                        currentList = "player_info";
//                        buildList("player_info");
//                        recyclerView.setAdapter(adapterOne);
//                        break;
//                    case 1:
//                        //recyclerView.swapAdapter(adapterTwo, true);
//                        currentList = "player_stats";
//                        buildList("player_stats");
//                        recyclerView.setAdapter(adapterOne);
//                        break;
//                    case 2:
//                        //recyclerView.swapAdapter(adapterThree, true);
//                        currentList = "hill_stats";
//                        buildList("hill_stats");
//                        recyclerView.setAdapter(adapterOne);
//                        break;
//                }
//            }
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                // This is called when the previously selected tab is unselected
//            }
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                // This is called when a tab is reselected
//            }
//        });
//
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch(currentList){
//                    case "player_info":
//                        Toast.makeText(TEST.this, "Create Button Clicked for " + currentList, Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(TEST.this, PlayerInfoCreateNew.class);
//                        startActivity(intent);
//                        break;
//                    case "player_stats":
//                        Toast.makeText(TEST.this, "Create Button Clicked for " + currentList, Toast.LENGTH_SHORT).show();
//                        break;
//                    case "hill_stats":
//                        Toast.makeText(TEST.this, "Create Button Clicked for " + currentList, Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//    }
//    @Override
//    public void onItemClick(View view, int position) {
//        switch(currentList){
//            case "Player_Info":
//                Toast.makeText(this, "You clicked " + adapterOne.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
//                break;
//            case "Player_Stats":
//                Toast.makeText(this, "You clicked " + adapterTwo.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
//                break;
//            case "Hill_Stats":
//                Toast.makeText(this, "You clicked " + adapterThree.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//}
//
////    @Override
////    public boolean onSupportNavigateUp() {
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test);
////        return NavigationUI.navigateUp(navController, appBarConfiguration)
////                || super.onSupportNavigateUp();
////    }
//
//
///**
// * TODO
// * turn JSONArray reponse to a list of strings(JSONObjects)
// * Turn strings into JSONobjects
// * populate recycler view with JSONObjects
// *
// * enable options for GET,DEL,POST,PUT,LIST
// */


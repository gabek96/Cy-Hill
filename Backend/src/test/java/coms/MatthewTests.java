package coms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort; // SBv3


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MatthewTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void CreatePlayerTest() {
        // Create a new Player JSON object
        JSONObject newPlayer = new JSONObject();
        try {
            newPlayer.put("name", "tester1");
            newPlayer.put("birthday", "2000-01-01");
            newPlayer.put("email", "bluer34@gmail.com");
            newPlayer.put("password", "1234echoecho");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Specify the team
        String team = "lib";

        // Send POST request to create new player
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/player/create/" + team);

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for success message
        String responseName = response.jsonPath().getString("name");
        String id = response.jsonPath().getString("id");
        assertEquals("tester1", responseName);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .delete("/delete/" + id);
    }

    @Test
    public  void CreateHillTest(){
        JSONObject newHill = new JSONObject();
        try{
            newHill.put("name", "Test Hill");
            newHill.put("lon", 10.123);
            newHill.put("lat", 20.456);
            newHill.put("radius", 50);
            newHill.put("goal", 100);
        }catch(JSONException e){
            e.printStackTrace();
        }
        // Send POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newHill.toString())
                .when()
                .post("/hill/create");

        // Assert status code
        assertEquals(201, response.getStatusCode());

        // Assert success message
        String responseName = response.jsonPath().getString("name");
        String id = response.jsonPath().getString("id");

        assertEquals("Test Hill", responseName);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/hill/delete/" + id);
    }

    @Test
    public void DonateToInactiveHill() throws JSONException {
        //create the hill
        JSONObject newHill = new JSONObject();
        newHill.put("name", "Test Hill");
        newHill.put("lon", 10.123);
        newHill.put("lat", 20.456);
        newHill.put("radius", 50);
        newHill.put("goal", 100);
        //create the player
        JSONObject newPlayer = new JSONObject();
        newPlayer.put("name", "test30");
        newPlayer.put("birthday", "2000-01-01"); // Ensure this matches your date format
        newPlayer.put("email", "bluer34@gmail.com");
        newPlayer.put("password", "1234echoecho");
        String team = "lib";
        Response hill_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newHill.toString())
                .when()
                .post("/hill/create");
        Response player_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/player/create/" + team);


        String player_id = player_response.jsonPath().getString("id");
        String hill_id = hill_response.jsonPath().getString("id");

        Response donation_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/" + player_id +"/donate/"+ hill_id + "/1");


        String responseBody = donation_response.getBody().asString();
        assertEquals("{\"message\":\"Hill is not active, can't donate\"}", responseBody);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/delete/" + player_id);
        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/hill/delete/" + hill_id);
    }

    @Test
    public void DonateOneToHill() throws JSONException {
        //create the hill
        JSONObject newHill = new JSONObject();
        newHill.put("name", "Testemfkfkkfrffffffhillll");
        newHill.put("lon", 10.123);
        newHill.put("lat", 20.456);
        newHill.put("radius", 50);
        newHill.put("goal", 100);
        //create the player
        JSONObject newPlayer = new JSONObject();
        newPlayer.put("name", "testpffkfkfkfffffereson");
        newPlayer.put("birthday", "2000-01-01"); // Ensure this matches your date format
        newPlayer.put("email", "bluer34@gmail.com");
        newPlayer.put("password", "1234echoecho");
        String team = "lib";
        Response hill_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newHill.toString())
                .when()
                .post("/hill/create");
        Response player_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/player/create/" + team);


        String player_id = player_response.jsonPath().getString("id");
        String hill_id = hill_response.jsonPath().getString("id");

        //make sure the hill is activated
        Response hill_activated_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .put("/hill/activate/" + hill_id);


        Response donation_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/" + player_id + "/donate/" + hill_id + "/1");


        String responseBody = donation_response.getBody().asString();
        assertEquals("{\"Remaining coins\": 99, \"Xp Gained\": 10}", responseBody);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .delete("/" + player_id + "/hill/delete/" + hill_id);
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .delete("/" + player_id + "/delete/" + player_id);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/delete/" + player_id);
        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/hill/delete/" + hill_id);

    }

    @Test
    public void DonateMoreThanPlayerHas() throws JSONException {
        //create the hill
        JSONObject newHill = new JSONObject();
        newHill.put("name", "Testfwjfwiojwe Hill");
        newHill.put("lon", 10.123);
        newHill.put("lat", 20.456);
        newHill.put("radius", 50);
        newHill.put("goal", 100);
        //create the player
        JSONObject newPlayer = new JSONObject();
        newPlayer.put("name", "mawejfowefjiweott");
        newPlayer.put("birthday", "2000-01-01"); // Ensure this matches your date format
        newPlayer.put("email", "bluer34@gmail.com");
        newPlayer.put("password", "1234echoecho");
        String team = "lib";
        Response hill_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newHill.toString())
                .when()
                .post("/hill/create");
        Response player_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/player/create/" + team);


        String player_id = player_response.jsonPath().getString("id");
        String hill_id = hill_response.jsonPath().getString("id");

        //make sure the hill is activated
        Response hill_activated_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .put("/hill/activate/" + hill_id);


        Response donation_response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/" + player_id + "/donate/" + hill_id + "/101");


        String responseBody = donation_response.getBody().asString();
        assertEquals("{\"Remaining coins\": 0, \"Xp Gained\": 1000}", responseBody);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/delete/" + player_id);
        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/hill/delete/" + hill_id);
    }
}

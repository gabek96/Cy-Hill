package coms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort; // Spring Boot v3
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GabeTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    // Utility method to create a player
    private String createPlayer(String name, String birthday, String email, String password, String team) {
        JSONObject newPlayer = new JSONObject();
        try {
            newPlayer.put("name", name);
            newPlayer.put("birthday", birthday);
            newPlayer.put("email", email);
            newPlayer.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newPlayer.toString())
                .when()
                .post("/player/create/" + team);

        assertEquals(200, response.getStatusCode(), "Failed to create player");

        String playerId = response.jsonPath().getString("id");
        assertNotNull(playerId, "Player ID should not be null");
        return playerId;
    }

    // Utility method to delete a player
    private void deletePlayer(String playerId) {
        Response deleteResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/player/delete/" + playerId);

        assertEquals(200, deleteResponse.getStatusCode(), "Failed to delete player");

        Response getResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/player/" + playerId);

        assertEquals(404, getResponse.getStatusCode(), "Player was not properly deleted");
    }

    // Test for creating a player
    @Test
    public void CreatePlayerTest() {
        String team = "lib";
        String playerId = createPlayer("Gabet2", "2000-01-01", "bluer34@gmail.com", "1234echoecho", team);
    }

    // Test for donating more coins than a player has
    @Test
    public void donateMoreThanPlayerHasTest() throws JSONException {
        // Create a hill
        JSONObject newHill = new JSONObject();
        newHill.put("name", "Test hsada12");
        newHill.put("lon", 10.123);
        newHill.put("lat", 20.456);
        newHill.put("radius", 50);
        newHill.put("goal", 100);

        Response hillResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newHill.toString())
                .when()
                .post("/hill/create");

        assertEquals(201, hillResponse.getStatusCode(), "Failed to create hill");
        String hillId = hillResponse.jsonPath().getString("id");

        // Create a player
        String team = "lib";
        String playerId = createPlayer("Gabet4", "2000-01-01", "bluer34@gmail.com", "1234echoecho", team);

        // Activate the hill
        RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .put("/hill/activate/" + hillId)
                .then()
                .statusCode(200);

        // Attempt to donate more coins than the player has
        Response donationResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/" + playerId + "/donate/" + hillId + "/1");

        String expectedResponse = "{\"Remaining coins\": 99, \"Xp Gained\": 10}";
        assertEquals(expectedResponse, donationResponse.getBody().asString(), "Unexpected donation response");
    }


    // Test for creating a hill
    @Test
    public void createHillTest() {
        JSONObject newHill = new JSONObject();
        try {
            newHill.put("name", "Blue Hill");
            newHill.put("lon", 10.123);
            newHill.put("lat", 20.456);
            newHill.put("radius", 50);
            newHill.put("goal", 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newHill.toString())
                .when()
                .post("/hill/create");

        assertEquals(201, response.getStatusCode(), "Failed to create hill");

        String responseName = response.jsonPath().getString("name");
        assertEquals("Blue Hill", responseName);
    }

    // Test for updating a player's coin balance
    @Test
    public void updateCoinsTest() throws JSONException {
        // Create a new player
        String team = "lib";
        String playerId = createPlayer("Gabet5", "2000-01-01", "bluer35@gmail.com", "1234echoecho", team);

        // Create initial stats for the player
        JSONObject newStats = new JSONObject();
        newStats.put("coins", 100);
        newStats.put("team", "lib");
        newStats.put("level", 1);
        newStats.put("xp", 0);

        // Apply stats to the player
        Response statsResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(newStats.toString())
                .when()
                .post("/stats/apply");

        assertEquals(200, statsResponse.getStatusCode(), "Failed to apply stats");

        // Update the player's coins
        int newCoinBalance = 200;
        Response updateCoinsResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .param("coins", newCoinBalance)
                .when()
                .put("/stats/" + playerId + "/update-coins");

        assertEquals(200, updateCoinsResponse.getStatusCode(), "Failed to update coins");

        // Verify the updated coin balance
        String updatedCoins = updateCoinsResponse.jsonPath().getString("coins");
        assertEquals(String.valueOf(newCoinBalance), updatedCoins, "Coin balance was not updated correctly");

        // Cleanup: Delete the player
        deletePlayer(playerId);
    }

}


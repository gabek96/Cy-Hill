package coms.websocket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import coms.model.Hill;
import coms.model.News;
import coms.services.NewsService;
import io.swagger.v3.core.util.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ServerEndpoint("/news/{playerName}")
@Component
public class NewsServer {


    private static NewsService newsservice;

    @Autowired
    public void setNewsService(NewsService newsservice) {
        this.newsservice = newsservice;
    }


    private static Map<Session, String> sessionPlayerMap = new Hashtable<>();
    private static List<String> eventHistory = new LinkedList<>();
    private static final int MAX_EVENTS = 50;

    private final Logger logger = LoggerFactory.getLogger(NewsServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("playerName") String playerName) throws IOException {
        logger.info("[onOpen] " + playerName);
        sessionPlayerMap.put(session, playerName);
        //send all of the news
        broadcast(newsservice.sendlast30());
    }

    public void Hillcaptured(Hill h){
        List<JSONObject> jsonResponseList = new ArrayList<>();
        String hillInfo = "hill captured" + h.getName();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "A hill," + h.getName() + " has been captured by Team: " + h.getOwner());
        jsonResponse.put("graphic", "trophy");
        jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
        jsonResponseList.add(jsonResponse);
        JSONArray jsonArray = new JSONArray(jsonResponseList);
        broadcast(jsonArray.toString());
        addEvent(jsonArray.toString());
        newsservice.saveNews(jsonResponse.toString());
    }

    public void NewLeader(Hill h, String team){
        List<JSONObject> jsonResponseList = new ArrayList<>();
        String hillInfo = "new leader" + h.getName();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "Hill " + h.getName() + " has a new leader: " + team);
        jsonResponse.put("graphic", "run");
        jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
        jsonResponseList.add(jsonResponse);
        JSONArray jsonArray = new JSONArray(jsonResponseList);
        broadcast(jsonArray.toString());
        addEvent(jsonArray.toString());
        newsservice.saveNews(jsonResponse.toString());
    }
    public void AlmostFinished(Hill h){
        List<JSONObject> jsonResponseList = new ArrayList<>();
        String hillInfo = "hill close" + h.getName();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "Hill " + h.getName() + " is almost full!!!");
        jsonResponse.put("graphic", "taunt");
        jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
        jsonResponseList.add(jsonResponse);
        JSONArray jsonArray = new JSONArray(jsonResponseList);
        broadcast(jsonArray.toString());
        addEvent(jsonArray.toString());
        newsservice.saveNews(jsonResponse.toString());
    }
    public void NewHill(Hill h){
        List<JSONObject> jsonResponseList = new ArrayList<>();
        String hillInfo = "new hill" + h.getName();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "New hill :" + h.getName() + " is active!");
        jsonResponse.put("graphic", "run");
        jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
        jsonResponseList.add(jsonResponse);
        JSONArray jsonArray = new JSONArray(jsonResponseList);
        broadcast(jsonArray.toString());
        addEvent(jsonArray.toString());
        newsservice.saveNews(jsonResponse.toString());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        List<JSONObject> jsonResponseList = new ArrayList<>();

        if (message.startsWith("hill captured")) {
            String hillInfo = message.substring("hill captured".length());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "A hill has been captured by Team: " + hillInfo);
            jsonResponse.put("graphic", "trophy");
            jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
            jsonResponseList.add(jsonResponse);

        } else if (message.startsWith("hill leading")) {
            String hillInfo = message.substring("hill leading".length());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", hillInfo + " is currently winning this hill!");
            jsonResponse.put("graphic", "run");
            jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
            jsonResponseList.add(jsonResponse);

        } else if (message.startsWith("hill almost done")) {
            String hillInfo = message.substring("hill almost done".length());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", hillInfo + " There's not much left until the hill is captured! Keep Donating! 💸");
            jsonResponse.put("graphic", "taunt");
            jsonResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));
            jsonResponseList.add(jsonResponse);
        }

        // Convert List to JSONArray and broadcast
        JSONArray jsonArray = new JSONArray(jsonResponseList);
        broadcast(jsonArray.toString());
        addEvent(jsonArray.toString());
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String playerName = sessionPlayerMap.get(session);
        logger.info("[onClose] " + playerName);
        sessionPlayerMap.remove(session);

        JSONObject disconnectMessage = new JSONObject();
        disconnectMessage.put("message", playerName + " has disconnected.");
        disconnectMessage.put("graphic", "disconnect");
        disconnectMessage.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE M/d/yy")));

        // Broadcast the disconnect message as a JSON array
        broadcast(new JSONArray().put(disconnectMessage).toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String playerName = sessionPlayerMap.get(session);
        logger.info("[onError] " + playerName + ": " + throwable.getMessage());
    }

    private void sendMessageToUser(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[Send Message Exception] " + e.getMessage());
        }
    }

    private void broadcast(String jsonMessage) {
        sessionPlayerMap.forEach((session, playerName) -> {
            try {
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    private void addEvent(String event) {
        if (eventHistory.size() >= MAX_EVENTS) {
            eventHistory.remove(0); // Remove the oldest event
        }
        eventHistory.add(event);
    }

    private void sendLastEvents(Session session) {
        JSONArray jsonArray = new JSONArray();

        // Determine the start index to always get the last 5 events (or less if not enough events)
        int startIndex = Math.max(0, eventHistory.size() - 5);

        // Add the events starting from the startIndex up to the end of the event history
        for (int i = startIndex; i < eventHistory.size(); i++) {
            jsonArray.put(new JSONObject(eventHistory.get(i)));
        }

        // Send the JSON array to the user
        sendMessageToUser(session, jsonArray.toString());
    }


}

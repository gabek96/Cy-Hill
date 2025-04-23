package coms.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import coms.DataTransferObject.*;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import coms.model.*;
import coms.*;
import coms.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import coms.services.*;
import java.util.List;

@ServerEndpoint("/hillLocation/{username}")
@Component
public class HillLocationWS extends TextWebSocketHandler{
    // Logger initialization
    private static final Logger logger = LoggerFactory.getLogger(HillWS.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static HillScreenService HSS;

    @Autowired
    public void setMessageRepository(HillScreenService HSS) {
        this.HSS = HSS;
    }

    // Maps to store the session and username associations
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

    /**
     * Called when a new connection is opened
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        System.out.println("new user joined");
        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        }
        else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);
            System.out.println("new user joined");
            //send hill location
            List<HillLocationDTO> h_list= HSS.sendHillLocationData();
            HillScreenLocal(h_list);

            // send to the user joining in
            sendMessageToParticularUser(username, "Welcome to the chat server, "+username);

        }
    }

    /**
     * Send a direct message to a specific user (by username)
     */
    private void sendMessageToParticularUser(String username, String message) {
        try {
            Session userSession = usernameSessionMap.get(username);
            if (userSession != null) {
                userSession.getBasicRemote().sendText(message);
            } else {
                logger.warn("User with username " + username + " not found.");
            }
        } catch (IOException e) {
            logger.error("[sendMessageToParticularUser] Error sending message to user with username " + username, e);
        }
    }

    /**
     * Called when a client sends a message
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        // logger.info("[onMessage] Received message from session: " + session.getusername() + " -> " + message);

        // Process the incoming message and handle different cases as needed
        // For now, simply send a response
        try {
            String jsonResponse = "{\"response\": \"Message received!\"}";
            session.getBasicRemote().sendText(jsonResponse);
        } catch (IOException e) {
            logger.error("[onMessage] Error responding to message", e);
        }
    }

    /**
     * Broadcast a message to all connected users
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    /**
     * Method to send JSON data of hill status to all users
     */
    public void HillScreenData(List<HillScreenDTO> HillList) {
        try {
            // Convert HillScreen[] to a JSON string using the existing static ObjectMapper
            String jsonMessage = objectMapper.writeValueAsString(HillList);
            // Log the JSON message for debugging purposes
            logger.info("Sending JSON to all users: " + jsonMessage);

            // Send this message via WebSocket to all connected users
            broadcast(jsonMessage);
        } catch (IOException e) {
            // Log the error if serialization fails
            logger.error("[HillScreenData] Error serializing object to JSON", e);
        }
    }

    /**
     * Method to send JSON data of hill location to all userse
     */
    public void HillScreenLocal(List<HillLocationDTO> HillList) {
        try {
            // Convert HillScreen[] to a JSON string using the existing static ObjectMapper
            String jsonMessage = objectMapper.writeValueAsString(HillList);
            // Log the JSON message for debugging purposes
            logger.info("Sending JSON to all users: " + jsonMessage);

            // Send this message via WebSocket to all connected users
            broadcast(jsonMessage);
        } catch (IOException e) {
            // Log the error if serialization fails
            logger.error("[HillScreenData] Error serializing object to JSON", e);
        }
    }

    /**
     * Called when a session is closed
     */
    @OnClose
    public void onClose(Session session) {
        String username = sessionUsernameMap.get(session);
        if (username != null) {
            usernameSessionMap.remove(username);
            sessionUsernameMap.remove(session);
            logger.info("[onClose] Session closed for user with username " + username);
        }
    }

    /**
     * Called when a session has an error
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }
}

package coms.services;

import coms.model.News;
import coms.repository.NewsRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository NR;

    public void saveNews(String msg){
        News n = new News(msg);
        NR.save(n);
    }

    public String sendlast30() {
        List<News> events = NR.findAll();
        JSONArray jsonArray = new JSONArray(); //for holding the mesages we will send
        for(News n : events){
            // Grab the message from the News object using the getMessage() method
            String messageJsonString = n.getMessage();  // This contains a JSON string

            // Parse the messageJsonString into a JSONObject
            JSONObject messageJson = new JSONObject(messageJsonString);

            String statement = messageJson.getString("message");
            String graphic = messageJson.getString("graphic");
            String timestamp = messageJson.getString("timestamp");

            // Create a new JSONObject to hold the extracted data
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", statement);  // Add the statement as the message
            jsonObject.put("graphic", graphic);    // Add the graphic type
            jsonObject.put("timestamp", timestamp); // Add the timestamp

            // Add the JSON object to the JSON array
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }
}

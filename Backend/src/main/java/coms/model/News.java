package coms.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.json.JSONObject;

import java.time.LocalDateTime;


@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    public News(String message) {
        this.message = message;
    }

    public News(){}

    public String getMessage() {return this.message;}
    public void setMessage(String message) {this.message = message;}

}


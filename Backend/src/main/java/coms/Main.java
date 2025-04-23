package coms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * This is the Springboot aplication's main class
 */
@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Cy-Hill Backend");
        SpringApplication.run(Main.class, args);

    }
}



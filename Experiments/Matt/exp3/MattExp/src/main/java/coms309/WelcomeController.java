package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * PetClinic Spring Boot Application.
 * 
 * @author Matthew Etnyre
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{input}")
    public String welcome(@PathVariable String input) {
        if (isNumeric(input)) {
            return print_name(Integer.parseInt(input));
        } else {
            return input + " is not a number";
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String print_name(int input) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < input; i++) {
            s.append("Matt").append("<br>");
        }
        return s.toString();
    }
}

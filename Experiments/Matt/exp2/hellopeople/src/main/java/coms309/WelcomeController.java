package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Matthew Etnyre
 */

@RestController
class WelcomeController {

    @GetMapping("/{input}")
    String multiply(@PathVariable int input) {
        // Multiply the input by the multiplier
        int multiplier = 2;
        int result = multiplier * multiplier;
        // Return the result as a string
        return "The result of multiplying " + input + " by " + multiplier + " is: " + result;
    }
}

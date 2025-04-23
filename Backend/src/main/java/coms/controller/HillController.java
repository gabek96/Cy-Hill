package coms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import coms.repository.HillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import coms.model.Hill;

/**
 * This class handles HTTP requests endpoints that start with /hill/...
 * @Author Matthew Etnyre
 * @Author Gabe Kiveu
 */
@RestController
@RequestMapping("/hill")
public class HillController {

    @Autowired
    private HillRepository HR;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * Handles Get request for Hill, responds to /hill/get
     * @return returns all Hills in JSON format
     */
    @GetMapping("/get")
    List<Hill> getAllHills() {
        return HR.findAll();
    }


    /**
     * Handles Get request for Hill by name, responds to /hill/get/name
     *
     * @param name name of the Hill to search for
     * @return returns a list of Hills with the specified name or an empty list if not found
     */
    @GetMapping("/{name}")
    Optional<Hill> getHillByName(@PathVariable String name) {
        // Use the repository to find hills by name
        return HR.findByName(name);
    }

    /**
     * Handles Get request for Hill, responds to hill/get/{id}
     * @param id id of the Hill we want to get
     * @return returns the Hill in a JSON format or a failure message
     */
    @GetMapping("/get/{id}")
    Hill getHillById(@PathVariable Long id) {
        return HR.findById(id).orElse(null);
    }

    /**
     * Handles Post request for Hill, responds to hill/create endpoint
     * @param h the hill we want to create
     * @return returns a success or failure message
     */
    @PostMapping("/create")
    ResponseEntity<Object> createHill(@RequestBody Hill h) {
        if (h == null) {
            return new ResponseEntity<>("Hill name cannot be empty", HttpStatus.BAD_REQUEST);
        } else {
            Hill hill = new Hill(h.getName(), h.getLon(), h.getLat(), h.getRadius(), h.getGoal());
            HR.save(hill);
            return new ResponseEntity<>(hill, HttpStatus.CREATED);
        }
    }

    @PutMapping("/activate/{id}")
    public String activateHill(@PathVariable Long id){
        Optional<Hill> existingHill = HR.findById(id);
        if(existingHill.isPresent()){
            Hill activate = existingHill.get();
            activate.setActive(true);
            HR.save(activate);
            return success;
        }
        return failure;
    }

    /**
     * Handles Put request for Hill, responds to hill/update/{id} endpoint
     * @param id id of the Hill we want to update
     * @param h the new Hill we want to use to update
     * @return returns a success or failure message
     */
    @PutMapping("/update/{id}")
    public String updateHill(@PathVariable Long id, @RequestBody Hill h) {
        Optional<Hill> existingHill = HR.findById(id);
        if (existingHill.isPresent()) {
            Hill replace = existingHill.get();
            replace.setName(h.getName());
            replace.setLon(h.getLon());
            replace.setLat(h.getLat());
            replace.setRadius(h.getRadius());
            replace.setOwner(h.getOwner());

            HR.save(replace);
            return success;
        } else {
            return failure;
        }
    }

    /**
     * Handles Delete request for Hill, responds to hill/delete/{id} endpoint
     * @param id id of the Hill we wish to delete
     * @return returns a success or failure message
     */
    @DeleteMapping("/delete/{id}")
    public String deleteHill(@PathVariable Long id) {
        HR.deleteById(id);
        return success;
    }

    /**
     * Handles Get request for Hill, responds to hill/getOwner/{id} endpoint
     * @param id id of the Hill
     * @return returns the owner or a not found message
     */
    @GetMapping("/getOwner/{id}")
    String getOwner(@PathVariable Long id) {
        return HR.findById(id).map(Hill::getOwner).orElse("Owner not found");
    }
}

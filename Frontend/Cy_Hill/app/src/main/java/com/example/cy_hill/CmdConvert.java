package com.example.cy_hill;

public class CmdConvert {
    private String mode = "";
    private String url = "http://coms-3090-059.class.las.iastate.edu:8080";



    public CmdConvert(String mode) {
        this.mode = mode;
    }

    public CmdConvert(String mode,String url) {
        this.mode = mode;
    }

    /**
     * Retrieves the URL for performing specific actions (LIST, POST) based on the provided command and current mode.
     *
     * This method generates a URL by appending the appropriate path to a base URL, depending on the
     * value of the command (`cmd`) and the current `mode`. The command can be one of the following:
     * - "LIST": Retrieves a list of all data
     * - "POST": Submits new data to the end
     *
     * The mode specifies the context, such as "player_info", "player_stats", or "hill_stats", and determines
     * the URL path that is appended to execute the desired action.
     *
     * @param cmd The command specifying the desired action ("LIST" to retrieve data, "POST" to submit data).
     * @return The complete URL corresponding to the specified command and mode.
     */


    public String getCmd(String cmd){

        String newCmd = url;

        if(mode.equals("player_info")){
            switch (cmd){
                case "LIST":
                    newCmd += "/players";
                    break;
                case "POST":
                    newCmd += "/player/create";
                    break;
            }
        }else if (mode.equals("player_stats")){
            switch (cmd){
                case "LIST":
                    newCmd += "/stats";
                    break;
                case "POST":
                    newCmd += "/stats/apply";
                    break;
            }
        }else if (mode.equals("hill_stats")){
            switch (cmd){
                case "LIST":
                    newCmd += "/hill/Get";
                    break;
                case "POST":
                    newCmd += "/hill/Create";
                    break;
            }
        }

        return newCmd;
    }


    /**
     * Retrieves the URL for performing specific actions (GET, PUT, DELETE) on an element identified by its ID.
     *
     * This overloaded method generates a URL based on the provided command (`cmd`), the element's ID (`id`),
     * and the current `mode`. The command can be one of the following:
     * - "GET": Retrieves the data for the specified element.
     * - "PUT": Updates the data for the specified element.
     * - "DEL": Deletes the specified element.
     *
     * The mode specifies the context, such as "player_info", "player_stats", or "hill_stats", and determines
     * the URL path that is appended to interact with the element.
     *
     * @param cmd The command specifying the desired action ("GET", "PUT", "DEL").
     * @param id The ID of the element to perform the action on.
     * @return The complete URL corresponding to the specified command, mode, and element ID.
     */

    public String getCmd(String cmd, int id){

        String newCmd = url;

        if(mode.equals("player_info")){
            switch (cmd){
                case "GET":
                    newCmd += ("/player/" + id);
                    break;
                case "PUT":
                    newCmd += ("/player/" + id);
                    break;
                case "DEL":
                    newCmd += ("/delete/" + id);
                    break;
            }
        }else if (mode.equals("player_stats")){
            switch (cmd){
                case "GET":
                    newCmd += "/TODO";
                    break;
                case "PUT":
                    newCmd += "/change/" + id;
                    break;
                case "DEL":
                    newCmd += "/clear/" + id;
                    break;
            }
        }else if (mode.equals("hill_stats")){
            switch (cmd){
                case "GET":
                    newCmd += "/hill/Get/" + id;
                    break;
                case "PUT":
                    newCmd += "/hill/Update/" + id;
                    break;
                case "DEL":
                    newCmd += "/hill/Delete/" + id;
                    break;
            }
        }

        return newCmd;
    }
}

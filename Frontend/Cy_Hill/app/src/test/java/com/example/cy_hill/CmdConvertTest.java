package com.example.cy_hill;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmdConvertTest {

    @Test
    public void getCmd() {
        CmdConvert cmdConvert = new CmdConvert("player_info");
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/players", cmdConvert.getCmd("LIST"));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/player/create", cmdConvert.getCmd("POST"));
        CmdConvert cmdConvert2 = new CmdConvert("player_stats");
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/stats", cmdConvert2.getCmd("LIST"));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/stats/apply", cmdConvert2.getCmd("POST"));
        CmdConvert cmdConvert3 = new CmdConvert("hill_stats");
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/hill/Get", cmdConvert3.getCmd("LIST"));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/hill/Create", cmdConvert3.getCmd("POST"));
    }

    @Test
    public void GetCmdId() {
        CmdConvert cmdConvert = new CmdConvert("player_info");
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/player/1", cmdConvert.getCmd(("GET"),1));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/player/1", cmdConvert.getCmd(("PUT"),1));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/delete/1", cmdConvert.getCmd(("DEL"),1));
        CmdConvert cmdConvert2 = new CmdConvert("player_stats");
        //assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/TODO", cmdConvert2.getCmd(("GET")));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/change/1", cmdConvert2.getCmd(("PUT"),1));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/clear/1", cmdConvert2.getCmd(("DEL"),1));
        CmdConvert cmdConvert3 = new CmdConvert("hill_stats");
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/hill/Get/1", cmdConvert3.getCmd(("GET"),1));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/hill/Update/1", cmdConvert3.getCmd(("PUT"),1));
        assertEquals("http://coms-3090-059.class.las.iastate.edu:8080/hill/Delete/1", cmdConvert3.getCmd(("DEL"),1));

    }
}
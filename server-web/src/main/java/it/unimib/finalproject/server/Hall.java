package it.unimib.finalproject.server;

import java.util.stream.*;
import java.time.*;
import java.util.*;

/**
 * Rappresenta una Prenotazione.
 */
public class Hall {

    private String id;

    private String name;

    private int floor;

    private String postiRiga;

    private int postiColonna;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getpostiColonna() {
        return postiColonna;
    }

    public void setpostiColonna(int postiColonna) {
        this.postiColonna = postiColonna;
    }

    public String getpostiRiga() {
        return postiRiga;
    }

    public void setpostiRiga(String postiRiga) {
        this.postiRiga = postiRiga;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean Deserialize(String s){
        if(s.trim() != ""){
            String[] splitId = s.split(":");
            this.id = splitId[0];
            String[] splitParams = String.join(":", Arrays.copyOfRange(splitId, 1, splitId.length)).split("#");

            for (int i = 0; i < splitParams.length; i++) {
                switch (i) {
                case 0:
                    //System.out.println(this.getClass().getSimpleName());
                    if(!splitParams[i].equals(this.getClass().getSimpleName()))
                        return false;
                    break;
                case 1:
                    name = splitParams[i]; break;
                case 2:
                    floor = Integer.parseInt(splitParams[i]); break;
                case 3:
                    postiColonna = Integer.parseInt(splitParams[i]); break;
                case 4:
                    postiRiga = splitParams[i]; break;
                }
            }
            return true;
        }
        return false;
    }
}

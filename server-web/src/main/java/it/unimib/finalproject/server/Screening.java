package it.unimib.finalproject.server;

import java.time.*;

import java.util.stream.*;

import java.time.*;
import java.util.*;
/**
 * Rappresenta una Proiezione.
 */
public class Screening implements Serializable {

    private String id;

    //1
    // id della Sala che ospita la Proiezione.
    private String idHall;

    //2
    // id del Film che viene Proiettato.
    private String idMovie;

    //3
    // Data associata alla Proiezione.
    private LocalDate date;

    //4
    // Ora associata alla Proiezione.
    private LocalTime time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getIdHall() {
        return idHall;
    }

    public void setIdHall(String idHall) {
        this.idHall = idHall;
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
                    idHall = splitParams[i]; break;
                case 2:
                    idMovie = splitParams[i]; break;
                case 3:
                    date = LocalDate.parse(splitParams[i], ReservationResource.formatterDate);
                    break;
                case 4:
                    time = LocalTime.parse(splitParams[i], ReservationResource.formatterTime);
                    break;
                }
            }
            return true;
        }
        return false;
    }

}

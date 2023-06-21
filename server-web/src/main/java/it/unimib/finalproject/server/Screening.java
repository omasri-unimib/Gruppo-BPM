package it.unimib.finalproject.server;

import java.time.*;

import java.util.stream.*;
import java.time.*;
import java.util.*;
/**
 * Rappresenta una Proiezione.
 */
public class Screening {
    // Identificativo univoco della Prenotazione.
    private String id;

    // Nome del Cliente associato alla Prenotazione.
    private String idScreening;

    // Nome del Cliente associato alla Prenotazione.
    private String idMovie;

   // data associata alla prenotazione
    private LocalDate date;

    // ora associata alla prenotazione
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

    public String getIdScreening() {
        return idScreening;
    }

    public void setIdScreening(String idScreening) {
        this.idScreening = idScreening;
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
                    idScreening = splitParams[i]; break;
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

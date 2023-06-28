package it.unimib.finalproject.server;

import java.util.stream.*;
import java.time.*;
import java.util.*;

/**
 * Rappresenta una Prenotazione.
 */
public class Movie implements Serializable {

    private String id;

    private String name;

    private String genre;

    private String director;

    private double duration;

    private LocalDate releaseDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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
                    genre = splitParams[i]; break;
                case 3:
                    director = splitParams[i]; break;
                case 4:
                    duration = Double.parseDouble(splitParams[i]); break;
                case 5:
                    releaseDate = LocalDate.parse(splitParams[i], ReservationResource.formatterDate);
                    break;
                }
            }
            return true;
        }
        return false;
    }
}

package it.unimib.finalproject.server;

import java.time.*;

/**
 * Rappresenta una Prenotazione.
 */
public class Screening {
    // Identificativo univoco della Prenotazione.
    private int id;

    // Nome del Cliente associato alla Prenotazione.
    private String nameMovie;

   // data associata alla prenotazione
    private LocalDate date;

    // ora associata alla prenotazione
    private LocalTime time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameMovie() {
        return nameMovie;
    }

    public void setNameMovie(String nameMovie) {
        this.nameMovie = nameMovie;
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

}

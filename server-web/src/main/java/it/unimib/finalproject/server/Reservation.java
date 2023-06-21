package it.unimib.finalproject.server;

import java.util.Objects;
import java.util.stream.*;
import java.time.*;

/**
 * Rappresenta una Prenotazione.
 */
public class Reservation {
    // Nome del Cliente associato alla Prenotazione.
    private String nameCustomer;

    // Cognome del Cliente associato alla Prenotazione.
    private String surnameCustomer;

    // Sala associata alla Prenotazione.
    private String hall;

    // posizione nella sala associata alla prenotazione.
    private int position;

    // data associata alla prenotazione
    private String date;

    // ora associata alla prenotazione
    private String time;

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getSurnameCustomer() {
        return surnameCustomer;
    }

    public void setSurnameCustomer(String surnameCustomer) {
        this.surnameCustomer = surnameCustomer;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String number) {
        this.hall = number;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Reservation(String nameCustomer, String surnameCustomer, String hall, int position, String date, String time){
        this.nameCustomer = nameCustomer;
        this.surnameCustomer = surnameCustomer;
        this.hall = hall;
        this.position = position;
        this.date = date;
        this.time = time;
    }

    public Reservation(){

    }

    public boolean anyUnset(){
        return !Stream.of(nameCustomer, surnameCustomer, hall, position, date, time)
            .allMatch(Objects::nonNull);
    }
}

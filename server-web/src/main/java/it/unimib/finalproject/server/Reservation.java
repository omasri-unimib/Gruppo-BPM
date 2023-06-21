package it.unimib.finalproject.server;

import java.util.stream.*;
import java.time.*;
import java.util.*;

/**
 * Rappresenta una Prenotazione.
 */
public class Reservation {

    private String id;

    // Nome del Cliente associato alla Prenotazione.
    private String nameCustomer;

    // Cognome del Cliente associato alla Prenotazione.
    private String surnameCustomer;

    // Sala associata alla Prenotazione.
    private String screening;

    // data associata alla prenotazione
    private LocalDate date;

    // ora associata alla prenotazione
    private LocalTime time;

    private String stringDate;

    private String stringTime;

    // posizione nella sala associata alla prenotazione.
    private List<String> positions;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


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

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public String getStringTime() {
        return stringTime;
    }

    public void setStringTime(String stringTime) {
        this.stringTime = stringTime;
    }

    public List<String> getPositions() {
        return positions;
    }

    public String getScreening() {
        return screening;
    }

    public void setScreening(String screening) {
        this.screening = screening;
    }

    public void addPosition(String position) {
        this.positions.add(position);
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
                    nameCustomer = splitParams[i]; break;
                case 2:
                    surnameCustomer = splitParams[i]; break;
                case 3:
                    screening = splitParams[i]; break;
                case 4:
                    date = LocalDate.parse(splitParams[i], ReservationResource.formatterDate);
                    stringDate = splitParams[i];
                    break;
                case 5:
                    time = LocalTime.parse(splitParams[i], ReservationResource.formatterTime);
                    stringTime = splitParams[i];
                    break;
                default:
                    addPosition(splitParams[i]);
                }
            }
            return true;
        }
        return false;
    }

    public String Serialize(){
        if(!anyUnset()){
            return this.getClass().getSimpleName() + "#" +
                nameCustomer + "#" + surnameCustomer + "#" + screening + "#" +
                stringDate + "#" +
                stringTime + "#" +
                String.join("#", positions);
        }
        return "";
    }

    public Reservation(){
        this.positions = new ArrayList<String>();
    }

    public boolean anyUnset(){
        return !Stream.of(nameCustomer, surnameCustomer, screening, positions, stringDate, stringTime)
            .allMatch(Objects::nonNull);
    }
}

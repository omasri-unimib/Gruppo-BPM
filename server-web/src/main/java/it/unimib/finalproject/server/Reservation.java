package it.unimib.finalproject.server;

import java.util.stream.*;
import java.time.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

/**
 * Rappresenta una Prenotazione.
 */
public class Reservation implements Serializable {

    private String id;

    //1
    //Nome del Cliente associato alla Prenotazione.
    private String nameCustomer;

    //2
    //Cognome del Cliente associato alla Prenotazione.
    private String surnameCustomer;

    //3
    //Proiezione associata alla Prenotazione.
    private String screening;

    //4
    //data associata alla prenotazione
    private String date;

    //5
    //ora e minuti associata alla prenotazione
    private String time;

    //6
    //posizioni nella sala associati alla prenotazione.
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

    public List<String> getPositions() {
        return positions;
    }

    public String getScreening() {
        return screening;
    }

    public void setScreening(String screening) {
        this.screening = screening;
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
                    date = splitParams[i];
                    break;
                case 5:
                    time = splitParams[i];
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
                date + "#" +
                time + "#" +
                String.join("#", positions);
        }
        return "";
    }

    public Reservation(){
        this.positions = new ArrayList<String>();
    }

    public boolean anyUnset(){
        return !Stream.of(nameCustomer, surnameCustomer, screening, positions, date, time)
            .allMatch(Objects::nonNull) && Resource.dateIsValid(date) && Resource.timeIsValid(time);
    }
}

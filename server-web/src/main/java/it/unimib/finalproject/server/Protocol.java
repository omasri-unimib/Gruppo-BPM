package it.unimib.finalproject.server;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import java.net.*;


public class Protocol{
    public static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final String READ_ID_COMMAND = "READ-VALUE";
    public static final String READ_TYPE_COMMAND = "READ-VALUE-IF-CONTAINS";
    public static final String READ_QUERY_COMMAND = "READ-VALUE-QUERY";
	public static final String WRITE_VALUE_COMMAND = "WRITE-VALUE";
	public static final String WRITE_KEY_VALUE_COMMAND = "WRITE-KEY-VALUE";
	public static final String GEN_KEY_COMMAND = "GEN-KEY";
	public static final String TRANSM_DEL = "%";
	public static final String SEP_DEL = ":";
    public static final String ATTR_DEL = "#";

    /* Non riguarda il protocollo, ma è specifico a questa applcazione
	   Rappresenta la posizione in cui viene specificato il tipo di dato
	   (sala, prenotazione, film ... ) nel DataBase specifico a
	   questa applicazione.
	 */
	public static final int TYPE_OFFSET_VALUE = 0;

	public static final int DB_PORT = 8081;

    // to get the type we remove Resource from the name of the class
    public String TYPE = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 8);

    protected Socket socketDB;

    public static boolean dateIsValid(String value){
        LocalDate ldt = null;

        try {
            ldt = LocalDate.parse(value, formatterDate);
            String result = ldt.format(formatterDate);
            return result.equals(value);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean timeIsValid(String value){
        LocalTime ldt = null;

        try {
            ldt = LocalTime.parse(value, formatterTime);
            String result = ldt.format(formatterTime);
            return result.equals(value);
        } catch (Exception e) {
            return false;
        }
    }
}

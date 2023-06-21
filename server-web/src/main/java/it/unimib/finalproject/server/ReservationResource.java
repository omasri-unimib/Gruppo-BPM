package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.core.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Path("reservation")
public class ReservationResource {

    public static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:m");

    public static final String READ_TYPE_COMMAND = "READ-VALUE-IF-CONTAINS";
	public static final String READ_ID_COMMAND = "READ-VALUE";
	public static final String WRITE_VALUE_COMMAND = "WRITE-VALUE";
	public static final String WRITE_KEY_VALUE_COMMAND = "WRITE-KEY-VALUE";
	public static final String GEN_KEY_COMMAND = "GEN-KEY";
	public static final String TYPE = "Reservation";
	public static final String TRANSM_DEL = "%";
	public static final String SEP_DEL = ":";


	/* Non riguarda il protocollo, ma è specifico a questa applcazione
	   Rappresenta la posizione in cui viene specificato il tipo di dato
	   (sala, prenotazione, film ... ) nel DataBase specifico a
	   questa applicazione.
	 */
	public static final int TYPE_OFFSET_VALUE = 0;

	public static final int DB_PORT = 8081;

	Socket socketDB;

    /**
     * Implementazione  di GET "/reservation".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@QueryParam("screening") String screening, @QueryParam("date") String date, @QueryParam("time") String time) {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.
        List<Reservation> result = new ArrayList<Reservation>();

        try {
    		socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		PrintWriter out = new PrintWriter(socketDB.getOutputStream());
    		BufferedReader in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));


    		out.println(READ_TYPE_COMMAND + TRANSM_DEL + TYPE + TRANSM_DEL + TYPE_OFFSET_VALUE);
    		out.println(".");
    		out.flush();


            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                String[] splitObjects = inputLine.split(TRANSM_DEL);
                for(String s : splitObjects){
                    if(s.trim() != ""){
                        Reservation temp = new Reservation();
                        if(temp.Deserialize(s))
                            result.add(temp);
                    }
                }
                if (".".equals(inputLine)) {
                    break;
                }
            }

    		in.close();
            out.close();
            socketDB.close();

		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}

        System.out.println(date);

        result.removeIf(x -> (screening != null && !screening.equals(x.getScreening())));
        result.removeIf(x -> (date != null && !date.equals(x.getStringDate())));
        result.removeIf(x -> (time != null && !time.equals(x.getStringTime())));

        return Response.ok(result).build();
    }

    /**
     * Implementazione di GET "/reservation/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@PathParam("id") String id) {
        // Si apre una socket verso il database, si ottiene il contatto con
        // l'ID specificato.

        Reservation result = new Reservation();
        boolean flag = false;

    	try {
    		socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		PrintWriter out = new PrintWriter(socketDB.getOutputStream());
    		var in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));


    		out.println(READ_ID_COMMAND + TRANSM_DEL + id);
    		out.println(".");
    		out.flush();

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                String[] splitObjects = inputLine.split(TRANSM_DEL);
                String s = splitObjects[0];
                if(s.trim() != ""){
                    if(result.Deserialize(s))
                        flag = true;
                }
                if (".".equals(inputLine)) {
                    break;
                }
            }

            in.close();
            out.close();
            socketDB.close();

		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}

        if(flag == true)
            return Response.ok(result).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

    /**
     * Implementazione di POST "/reservation".
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response PostReservation(String body) {

        var reservation = new Reservation();
        System.out.println("POST");
        // Si apre una socket verso il database, si ottiene un nuovo ID, lo si
        // applica al contatto e lo si aggiunge.

        PrintWriter out;
        BufferedReader in;
        try {
            socketDB = new Socket("localhost", DB_PORT);
            System.out.println("Connected");
            out = new PrintWriter(socketDB.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));

    		out.println("GEN-KEY");
    		out.println(".");
    		out.flush();


            String inputLine;
            String key = "";
            while ((inputLine = in.readLine()) != null) {
            	if (key.equals("")) {
            		key = inputLine;
            	}
            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine)) {
                    break;
                }
            }
    		in.close();
            out.close();
            socketDB.close();

    		socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		out = new PrintWriter(socketDB.getOutputStream());
    		in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));

            var mapper = new ObjectMapper();
            reservation = mapper.readValue(body, Reservation.class);

            // Il nome e il numero ci devono essere.
            System.out.println(reservation.anyUnset());
            if (reservation.anyUnset())
                return Response.status(Response.Status.BAD_REQUEST).build();

            out.println("WRITE-KEY-VALUE" + TRANSM_DEL + key + TRANSM_DEL + reservation.Serialize());
            out.println(".");
            out.flush();

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read-Response: " + inputLine);

                if(inputLine.equals("ERROR")){
                    throw new Exception();
                }

                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
            }

            in.close();
            out.close();
            socketDB.close();

            try {
                var uri = new URI("/reservations/" + key);

                return Response.created(uri).build();
            } catch (URISyntaxException e) {
                System.out.println(e);
                return Response.serverError().build();
            }

		} catch (JsonParseException | JsonMappingException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            System.out.println(e);
            return Response.serverError().build();
        }

    }
}

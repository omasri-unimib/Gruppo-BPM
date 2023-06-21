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

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m");

    public static HashMap<Integer, Reservation> reservations
        = new HashMap<Integer, Reservation>() {{
            put(1, new Reservation("Omar", "Masri", "S1", 1, LocalDate.now().toString(), LocalTime.now().format(formatter)));
            put(2, new Reservation("Besugo","Sassi","S1",2, LocalDate.now().plusDays(10).toString(), LocalTime.now().format(formatter)));
            put(3, new Reservation("Bes","ssi","S1",2, LocalDate.now().plusDays(1).toString(), LocalTime.now().format(formatter)));
            put(4, new Reservation("asdasdo","sssAssi","S2",3, LocalDate.now().toString(), LocalTime.now().format(formatter)));
            put(5, new Reservation("Cristopher","benedux","S2",3, LocalDate.now().toString(), LocalTime.now().format(formatter)));
        }};

    /**
     * Implementazione  di GET "/reservation".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@QueryParam("hall") String hall, @QueryParam("date") String date, @QueryParam("time") String time) {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.
        List<Reservation> result = new ArrayList<Reservation>(reservations.values());
        System.out.println("1 "+LocalDate.now() + " " + hall + " " + date + " " + time);

        result.removeIf(x -> (hall != null && !hall.equals(x.getHall())));
        result.removeIf(x -> (time != null && !time.equals(x.getTime())));
        result.removeIf(x -> (date != null && !date.equals(x.getDate())));
        //result.removeIf(x -> (time == x.getTime()));
        //result.removeIf(x -> (date == x.getDate()));


        return Response.ok(result).build();
    }

    /**
     * Implementazione di GET "/reservation/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@PathParam("id") int id) {
        // Si apre una socket verso il database, si ottiene il contatto con
        // l'ID specificato.

        /*
        if (contact == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(contact).build();
        */

        Reservation result = reservations.get(id);
        if(result != null)
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
        var contact = new Reservation();
        System.out.println("POST");

        try {
            var mapper = new ObjectMapper();
            contact = mapper.readValue(body, Reservation.class);

            // Il nome e il numero ci devono essere.
            System.out.println(contact.anyUnset());
            if (contact.anyUnset())
                return Response.status(Response.Status.BAD_REQUEST).build();

        } catch (JsonParseException | JsonMappingException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException e) {
            System.out.println(e);
            return Response.serverError().build();
        }

        // Si apre una socket verso il database, si ottiene un nuovo ID, lo si
        // applica al contatto e lo si aggiunge.
        int newid = Collections.max(reservations.keySet())+1;
        reservations.put(Collections.max(reservations.keySet())+1, contact);

        try {
            var uri = new URI("/reservation/" + newid);

            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return Response.serverError().build();
        }
    }

}

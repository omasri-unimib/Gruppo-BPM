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

@Path("reservation")
public class ReservationResource {

    public static List<Reservation> reservations
        = new ArrayList<Reservation>(
            Arrays.asList(new Reservation(1, "Omar", "Masri", "S1", 1, LocalDate.now().toString(), LocalTime.now().toString()),
                          new Reservation(2,"Besugo","Sassi","S1",2, LocalDate.now().plusDays(10).toString(), LocalTime.now().toString()),
                          new Reservation(3,"Bes","ssi","S1",2, LocalDate.now().plusDays(1).toString(), LocalTime.now().toString()),
                          new Reservation(4,"asdasdo","sssAssi","S1",3, LocalDate.now().toString(), LocalTime.now().toString())));

    /**
     * Implementazione di GET "/reservation".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@QueryParam("hall") String hall, @QueryParam("date") String date, @QueryParam("time") String time) {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.
        List<Reservation> result = new ArrayList<Reservation>(reservations);
        System.out.println("1 "+LocalDate.now() + " " + hall + " " + date + " " + time);

        result.removeIf(x -> (hall != null && !hall.equals(x.getHall())));
        result.removeIf(x -> (time != null && !time.equals(x.getTime())));
        result.removeIf(x -> (date != null && !date.equals(x.getDate())));
        //result.removeIf(x -> (time == x.getTime()));
        //result.removeIf(x -> (date == x.getDate()));


        return Response.ok(result).build();
    }

    /**
     * Implementazione di GET "/reservation/hall/{hall}".
     */
    @Path("/hall/{hall}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContacts(@PathParam("hall") int hall) {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.
        System.out.println("2");
        String prova = "hall"+hall;
        return Response.ok(prova).build();
    }

    /**
     * Implementazione di GET "/contacts/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContact(@PathParam("id") int id) {
        // Si apre una socket verso il database, si ottiene il contatto con
        // l'ID specificato.

        /*
        if (contact == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(contact).build();
        */
        return Response.serverError().build();
    }

    /**
     * Implementazione di POST "/contacts".
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getContacts(String body) {
        var contact = new Reservation();

        try {
            var mapper = new ObjectMapper();
            contact = mapper.readValue(body, Reservation.class);

            // Il nome e il numero ci devono essere.
            if (contact.getHall() == null || contact.getNameCustomer() == null)
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

        try {
            var uri = new URI("/contacts/" + contact.getId());

            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return Response.serverError().build();
        }
    }
}

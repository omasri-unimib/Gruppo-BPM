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
import java.time.LocalDate;

@Path("screening")
public class ScreeningResource extends Protocol {

    /**
     * Implementazione di GET "/screening".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScreening(@QueryParam("date") String date, @QueryParam("time") String time) {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.

        List<Screening> result = new ArrayList<Screening>();

    	try {

            if(date != null && !dateIsValid(date) || time != null && !timeIsValid(time))
                return Response.status(400).build();

            String command =
                // TYPE
                (READ_QUERY_COMMAND +
                TRANSM_DEL + ATTR_DEL +
                TRANSM_DEL + 0 +
                TRANSM_DEL + "EQ" +
                TRANSM_DEL + TYPE)
                +
                (date != null ?
                TRANSM_DEL + 3 +
                TRANSM_DEL + "GTE" +
                TRANSM_DEL + date : "")
                +
                (time != null ?
                TRANSM_DEL + 4 +
                TRANSM_DEL + "GTE" +
                TRANSM_DEL + time : "") ;

            result = readObject(command, Screening.class);

		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}

    	return Response.ok(result).build();
    }



    /**
     * Implementazione di GET "/screening/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScreening(@PathParam("id") String id) {
        // Si apre una socket verso il database, si ottiene il contatto con
        // l'ID specificato.

        List<Screening> result = new ArrayList<Screening>();
        boolean flag = false;

    	try {
    		String command = READ_ID_COMMAND + TRANSM_DEL + id;

            result = readObject(command, Screening.class);

            if(result.size() > 0)
                flag = true;
            else
                flag = false;socketDB = new Socket("localhost", DB_PORT);

		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}

        if(flag == true)
            return Response.ok(result.get(0)).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }
}

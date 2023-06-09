package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;

@Path("reservation")
public class ReservationResource extends Resource {

    /**
     * Implementazione  di GET "/reservation".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@QueryParam("screening") String screening, @QueryParam("date") String date, @QueryParam("time") String time) {
        List<Reservation> result = new ArrayList<Reservation>();

        System.out.println("hello");
        try {
            System.out.println(dateIsValid(date)+ " " +date);

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
                (screening != null ?
                TRANSM_DEL + 3 +
                TRANSM_DEL + "EQ" +
                TRANSM_DEL + screening : "")
                +
                (date != null ?
                TRANSM_DEL + 4 +
                TRANSM_DEL + "GTE" +
                TRANSM_DEL + date : "")
                +
                (time != null ?
                TRANSM_DEL + 5 +
                TRANSM_DEL + "GTE" +
                TRANSM_DEL + time : "") ;

            System.out.println(command);
            result = readObject(command, Reservation.class);


		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}

        return Response.ok(result).build();

    }

    /**
     * Implementazione di POST "/reservation".
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postReservation(String body) {

        var reservation = new Reservation();
        System.out.println("POST " + body);

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
            	if (key.equals(""))
            		key = inputLine;

            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine))
                    break;

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

            if (reservation.anyUnset())
                return Response.status(Response.Status.BAD_REQUEST).build();

            out.println("WRITE-KEY-VALUE" + TRANSM_DEL + key + TRANSM_DEL + reservation.Serialize());
            out.println(".");
            out.flush();

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read-Response: " + inputLine);

                if(inputLine.equals("ERROR"))
                    throw new Exception();
                

                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
            }

            in.close();
            out.close();
            socketDB.close();

            try {
                var uri = new URI("/reservation/" + key);

                return Response.created(uri).header("Access-Control-Expose-Headers", "Location").build();
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

    /**
     * Implementazione di GET "/reservation/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@PathParam("id") String id) {

        List<Reservation> result = new ArrayList<Reservation>();
        boolean flag = false;

    	try {

            String command = READ_ID_COMMAND + TRANSM_DEL + id;

            result = readObject(command, Reservation.class);

            if(result.size() > 0)
                flag = true;
            else
                flag = false;

		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}

        if (flag == true)
            return Response.ok(result.get(0)).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

    /**
     * Implementazione di PUT "/reservation".
     */
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putReservation(String body, @PathParam("id") String id) {

        var reservation = new Reservation();
        System.out.println("PUT" + body);

        PrintWriter out;
        BufferedReader in;
        try {
            socketDB = new Socket("localhost", DB_PORT);
            System.out.println("Connected");
            out = new PrintWriter(socketDB.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));

            var mapper = new ObjectMapper();

            reservation = mapper.readValue(body, Reservation.class);

            if (reservation.anyUnset() || id == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            out.println("WRITE-KEY-VALUE" + TRANSM_DEL + id + TRANSM_DEL + reservation.Serialize());
            out.println(".");
            out.flush();

            String inputLine;
            int flag = 1;
            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read-Response: " + inputLine);

                if(inputLine.equals("ERROR"))
                    throw new Exception();
                else if(inputLine.startsWith("OVERWRITTEN"))
                    flag = 1;
                else if(inputLine.startsWith("CREATED"))
                    flag = 2;

                if (".".equals(inputLine)) {
                    break;
                }
            }

            in.close();
            out.close();
            socketDB.close();

            try {
                if(flag == 2){
                    var uri = new URI("/reservation/" + reservation.getId());

                    return Response.created(uri).build();
                }
                else{
                    return Response.status(204).build();
                }
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

    /**
     * Implementazione di DELETE "/reservation/{id}".
     */
    @Path("/{id}")
    @DELETE
    public Response deleteReservation(@PathParam("id") String id) {
        System.out.println("ssss");
        PrintWriter out;
        BufferedReader in;
        int flag = 2;
    	try {

            socketDB = new Socket("localhost", DB_PORT);
            System.out.println("Connected");
            out = new PrintWriter(socketDB.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));

            String command = DELETE_COMMAND + TRANSM_DEL + id;

            out.println(command);
            out.println(".");
            out.flush();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read-Response: " + inputLine);

                if(inputLine.startsWith("DONE"))
                    flag = 1;
                else if(inputLine.startsWith("NOT-FOUND"))
                    flag = 2;

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

        if (flag == 1)
            return Response.status(204).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }
}

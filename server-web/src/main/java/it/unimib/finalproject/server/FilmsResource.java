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

@Path("films")
public class FilmsResource {
	
	public static final String TYPE = "FILM";
	public static final int DB_PORT = 8081;
	
	Socket socketDB;
	
	
	
    /**
     * Implementazione di GET "/films".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilms() {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.
    	
    	try {
    		socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		PrintWriter out = new PrintWriter(socketDB.getOutputStream());
    		var in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));
    		
    		
    		out.println("GET#" + TYPE + "#*");
    		out.flush();
    		
    		
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
            }
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }

    /**
     * Implementazione di POST "/contacts".
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setFilms(String body) {
        var contact = new Contact();

        try {
            var mapper = new ObjectMapper();
            contact = mapper.readValue(body, Contact.class);

            // Il nome e il numero ci devono essere.
            if (contact.getNumber() == null || contact.getName() == null)
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

    /**
     * Implementazione di GET "/contacts/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm(@PathParam("id") int id) {
        // Si apre una socket verso il database, si ottiene il contatto con
        // l'ID specificato.
    	
    	
    	try {
    		socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		PrintWriter out = new PrintWriter(socketDB.getOutputStream());
    		var in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));
    		
    		
    		out.println("GET#" + TYPE + "#" + id);
    		out.flush();
    		
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
            }
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    	

        //if (contact == null)
          //  return Response.status(Response.Status.NOT_FOUND).build();

        //return Response.ok(contact).build();
    	
    }
}


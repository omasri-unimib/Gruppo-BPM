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
	
	public static final String READ_TYPE_COMMAND = "READ-VALUE-IF-CONTAINS";
	public static final String READ_ID_COMMAND = "READ-VALUE";
	public static final String WRITE_VALUE_COMMAND = "WRITE-VALUE";
	public static final String WRITE_KEY_VALUE_COMMAND = "WRITE-KEY-VALUE";
	public static final String GEN_KEY_COMMAND = "GEN-KEY";
	public static final String TYPE = "FILM";
	public static final String TRANSM_DEL = "%";
	public static final String SEP_DEL = ":";
	
	
	/* Non riguarda il protocollo, ma è specifico a questa applcazione
	   Rappresenta la posizione in cui viene specificato il tipo di dato
	   (sala, prenotazione, film ... ) nel DataBase specifico a 
	   questa applicazione. 
	 */
	public static final int TYPE_OFFSET_VALUE = 3; 
	
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
    		BufferedReader in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));
    		
    		
    		out.println(READ_TYPE_COMMAND + TRANSM_DEL + TYPE + TRANSM_DEL + TYPE_OFFSET_VALUE);
    		out.println(".");
    		out.flush();
    		
    		
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine)) {
                    break;
                }
            }
            
    		in.close();
            out.close();
            socketDB.close();
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }

    /**
     * Implementazione di POST "/films".
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setFilms(String body) {
        var contact = new Contact();
        /*try {
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
        */
        
        
        
        

        // Si apre una socket verso il database, si ottiene un nuovo ID, lo si
        // applica al contatto e lo si aggiunge.
        try {
    		socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		PrintWriter out = new PrintWriter(socketDB.getOutputStream());
    		BufferedReader in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));
    		
    		
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
            
            
            socketDB = new Socket("localhost", DB_PORT);
    		System.out.println("Connected");
    		out = new PrintWriter(socketDB.getOutputStream());
    		in = new BufferedReader(new InputStreamReader(socketDB.getInputStream()));
            
            out.println("WRITE-KEY-VALUE" + TRANSM_DEL + key + TRANSM_DEL + "TEST");
            out.println(".");
            out.flush();
            
            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine)) {
                    break;
                }
            }
            
            in.close();
            out.close();
            socketDB.close();
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        try {
            var uri = new URI("/films/" + contact.getId());

            return Response.created(uri).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return Response.serverError().build();
        }
    }

    
    
    /**
     * Implementazione di GET "/films/{id}".
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
    		
    		
    		out.println(READ_ID_COMMAND + TRANSM_DEL + id);
    		out.println(".");
    		out.flush();
    		
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Read: " + inputLine);
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
            }
            
            in.close();
            out.close();
            socketDB.close();
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    	

        //if (contact == null)
          //  return Response.status(Response.Status.NOT_FOUND).build();

        //return Response.ok(contact).build();
    	
    }
}
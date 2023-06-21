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

@Path("hall")
public class HallResource {
	
	public static final String READ_TYPE_COMMAND = "READ-VALUE-IF-CONTAINS";
	public static final String READ_ID_COMMAND = "READ-VALUE";
	public static final String WRITE_VALUE_COMMAND = "WRITE-VALUE";
	public static final String WRITE_KEY_VALUE_COMMAND = "WRITE-KEY-VALUE";
	public static final String GEN_KEY_COMMAND = "GEN-KEY";
	public static final String TYPE = "Hall";
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
     * Implementazione di GET "/halls".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHall() {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.

        List<Hall> result = new ArrayList<Hall>();

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
                        Hall temp = new Hall();
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
    	
    	return Response.ok(result).build();
    }

    
    /**
     * Implementazione di GET "/halls/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHall(@PathParam("id") String id) {
        // Si apre una socket verso il database, si ottiene il contatto con
        // l'ID specificato.

        Hall result = new Hall();
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
}

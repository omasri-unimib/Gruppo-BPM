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
public class HallResource extends Resource {

    /**
     * Implementazione di GET "/hall".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHall() {

        List<Hall> result = new ArrayList<Hall>();

    	try {
    		String command = READ_TYPE_COMMAND + TRANSM_DEL + TYPE + TRANSM_DEL + TYPE_OFFSET_VALUE;
    		
            result = readObject(command, Hall.class);
    		
		} catch (Exception e) {
			e.printStackTrace();
            return Response.serverError().build();
		}
    	
    	return Response.ok(result).build();
    }

    
    /**
     * Implementazione di GET "/hall/{id}".
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHall(@PathParam("id") String id) {

        List<Hall> result = new ArrayList<Hall>();
        boolean flag = false;

    	try {

            String command = READ_ID_COMMAND + TRANSM_DEL + id;

            result = readObject(command, Hall.class);

            if(result.size() > 0)
                flag = true;
            else
                flag = false;
    		
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

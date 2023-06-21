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

@Path("screening")
public class ScreeningResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation() {
        // Si apre una socket verso il database, si ottengono i dati e si
        // costruisce la risposta.
        //


        return Response.ok(result).build();
    }
}

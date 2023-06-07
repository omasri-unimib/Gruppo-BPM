package it.unimib.finalproject.database;

import java.net.*;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
/**
 * Classe principale in cui parte il database.
 */
public class Main {
    /**
     * Porta di ascolto.
     */
    public static final int PORT = 80;
    private static Map db;
    
   
    
    

    /**
     * Avvia il database e l'ascolto di nuove connessioni.
     *
     * @return Un server HTTP Grizzly.
     */
    public static void startServer() {
    	db = new HashMap();
    	db.put("sala1", "*2\r\n$2\r\nF1\r\n$2\r\nF2\r\n");
    	
    	
    	ServerSocket server = null;
    	
        try {
            server = new ServerSocket(PORT);
            System.out.println("Database listening at localhost:" + PORT);
           
            while (true)
                new Handler(server.accept()).start();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    
    
    
    /**
     * Handler di una connessione del client.
     */
    private static class Handler extends Thread {
        private Socket client;

        public Handler(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
            	System.out.println("Client connected."  + client.getRemoteSocketAddress());
            	
            	
                var out = new PrintWriter(client.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                	System.out.println("Read: " + inputLine);
                    if ("".equals(inputLine)) {
                        //out.println("bye");
                    	System.out.println("Command terminated");
                        break;
                    }
                }
                
                out.println(db.get("sala1"));
                System.out.println("Written");

                in.close();
                out.close();
                client.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    

    /**
     * Metodo principale di avvio del database.
     *
     * @param args argomenti passati a riga di comando.
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        startServer();
    }
}


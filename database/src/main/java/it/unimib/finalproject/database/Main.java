package it.unimib.finalproject.database;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

/**
 * Classe principale in cui parte il database.
 */
public class Main {
    /**
     * Porta di ascolto.
     */
	public static final String HALL_TYPE = "HALL";
	public static final String SCR_TYPE = "SCR";
	public static final String RES_TYPE = "RES";
	public static final String FILM_TYPE = "FILM";
	public static final String TRANSM_DEL = "\r\n";
    public static final int PORT = 8081;
    private static List<Pair> db;
    private static Map dbHalls; //colonne, righe
    private static Map dbReservations;
    private static Map dbScreenings;
    private static Map dbFilms; //nome, regista
    
   
    
    

    /**
     * Avvia il database e l'ascolto di nuove connessioni.
     *
     * @return Un server HTTP Grizzly.
     */
    public static void startServer() {
    	
    	System.out.println("Path: " + Paths.get(".").toAbsolutePath().normalize().toString());
    	
    	db = new ArrayList<Pair>();
    	
    	try {
    		String s = Files.readString(Paths.get("data.json"));
    		String[] records = s.split("\r\n");
    		for(int i = 0; i < records.length; i++) {
    			System.out.println(records[i].charAt(records[i].length() - 1));
    			db.add(new Pair(records[i].split(",")[0], records[i].split(",")[1])); 
    		}
    		
    		
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

    	ServerSocket server = null;
    	
        try {
            server = new ServerSocket(PORT);
            System.out.println("Database listening at localhost:" + PORT);
           
            while (true) {
                new Handler(server.accept()).start();
            }
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
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
                    
                    switch(inputLine.split("#")[0]) {
                    
                    	case "GET":
                    		switch(inputLine.split("#")[1]) {
                    			case SCR_TYPE: 
                    				if(inputLine.split("#")[2].equals("*")) {
                    					out.println(getAllOfType(db, SCR_TYPE));
                    				}
                    				else {
                    					out.println(getById(db, inputLine.split("#")[2]));
                    				}
                    			break;
                    			case FILM_TYPE: 
                    				if(inputLine.split("#")[2].equals("*")) {
                    					out.println(getAllOfType(db,  FILM_TYPE));
                    				}
                    				else {
                    					out.println(getById(db, inputLine.split("#")[2]));
                    				}
                    			break;
                    			case HALL_TYPE: 
                    				if(inputLine.split("#")[2].equals("*")) {
                    					out.println(getAllOfType(db, HALL_TYPE));
                    				}
                    				else {
                    					out.println(getById(db, inputLine.split("#")[2]));
                    				}
                    			break;
                    			case RES_TYPE: 
                    				if(inputLine.split("#")[2].equals("*")) {
                    					out.println(getAllOfType(db, RES_TYPE));
                    				}
                    				else {
                    					out.println(getById(db, inputLine.split("#")[2]));
                    				}
                    			break;
                    				
                    		}
                    		
                            System.out.println("Written");
                    		break;
                    		
                    	default:
                    		out.println("Command Not Recognized");
                            System.out.println("Written");
                    		break;
                    	
                    }
                }
               

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
    
    
    
    
    public static String getAllOfType(List<Pair> db, String type) {
    	String s = "";
    	for(int i = 0; i < db.size(); i++) {
    		String k = db.get(i).getKey();
    		String v = db.get(i).getValue();
    		if(v.split("#")[1].equals(type)) {
    			if(s.equals("")) {
    				s += v + "#" + k;
    			}
    			else {
    				s += TRANSM_DEL + v + "#" + k;
    			}
    		}
    	}
    	
    	return s;
    }
    
    public static String getById(List<Pair> db, String id) {
    	for(int i = 0; i < db.size(); i++) {
    		String k = db.get(i).getKey();
    		if(k.equals(id)) {
				return db.get(i).getValue() + "#" + k;
    		}
    	}
    	return "";
    }
}


class Pair{
	private String key;
	private String value;
	
	public Pair(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.key + "," + this.value;
	}
}


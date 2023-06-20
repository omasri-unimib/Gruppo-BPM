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
	public static final String TRANSM_DEL = "%";
	public static final String SEP_DEL = ":";
	
	// Non riguarda il protocollo, ma è specifico a questa applcazione
	// Rappresenta la posizione in cui viene specificato il tipo di dato
	// (sala, prenotazione, film ... ) nel DataBase specifico a 
	// questa applicazione. 
	public static final int TYPE_OFFSET_VALUE = 3; 
	
    public static final int PORT = 8081;
    private static List<Pair> db;
    
   
    
    

    /**
     * Avvia il database e l'ascolto di nuove connessioni.
     *
     * @return Un server HTTP Grizzly.
     */
    public static void startServer() {
    	
    	System.out.println("Path: " + Paths.get(".").toAbsolutePath().normalize().toString());
    	
    	db = new ArrayList<Pair>();
    	
    	try {
    		String s = Files.readString(Paths.get("data.csv"));
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
                String rcv = "";
                while ((inputLine = in.readLine()) != null) {
                	System.out.println("Read: " + inputLine);
         
                    if (".".equals(inputLine)) {
                    	System.out.println("Command terminated");
                        break;
                    }
                    else {
                    	rcv = rcv + inputLine;
                    }
                }
                
                System.out.println("RCV: " + rcv);

                switch(rcv.split(TRANSM_DEL)[0]) {
                
                	case "READ-VALUE-IF-CONTAINS":
                		
    					out.println(readAllContaining(db, 
    							rcv.split(TRANSM_DEL)[1], 
							    Integer.parseInt(rcv.split(TRANSM_DEL)[2])));
                        System.out.println("Written");
                		break;
                		
                	case "READ-VALUE":
                		out.println(readByKey(db, rcv.split(TRANSM_DEL)[1]));
                		System.out.println("Written");
                		break;
                		
                	case "WRITE-VALUE":
                		String key = generateKey(db);
                		out.println(writeKeyValue(db, key, rcv.split(TRANSM_DEL)[1]));
                		
                	case "WRITE-KEY-VALUE":
                		out.println(writeKeyValue(db, rcv.split(TRANSM_DEL)[1], rcv.split(TRANSM_DEL)[2]));
                		System.out.println("Written");
                		break;
                		
                	case "GEN-KEY":
                		out.println(generateKey(db));
                		out.println(".");
                		System.out.println("Written");
                		break;
                		
                	default:
                		out.println("Command Not Recognized");
                        System.out.println("Written");
                		break;
                	
                }

                in.close();
                out.close();
                client.close();
                
            } catch (IOException e) {
                System.err.println(e);
                e.printStackTrace();
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
    
    
    
    
    public static String readAllContaining(List<Pair> db, String content, int pos) {
    	String s = "";
    	for(int i = 0; i < db.size(); i++) {
    		String k = db.get(i).getKey();
    		String v = db.get(i).getValue();
    		if(v.length() > pos + content.length() && 
    		   v.startsWith(content, pos) /*v.split("#")[1].equals(type)*/) {
    			if(s.equals("")) {
    				s += k + SEP_DEL + v;
    			}
    			else {
    				s += TRANSM_DEL + k + SEP_DEL + v;
    			}
    		}
    	}
    	
    	return s;
    }
    
    public static String readByKey(List<Pair> db, String key) {
    	for(int i = 0; i < db.size(); i++) {
    		String k = db.get(i).getKey();
    		if(k.equals(key)) {
    			System.out.println("Found");
				return k + SEP_DEL + db.get(i).getValue();
    		}
    	}
    	return "";
    }
    
    public static String generateKey(List<Pair> db) {
    	int max = (db.size() == 0) ? -1 : Integer.parseInt(db.get(0).getKey());
    	for (int i = 0; i < db.size(); i ++) {
    		int val = Integer.parseInt(db.get(i).getKey());
			if (val > max) {
				max = val;
			}
		}
    	
    	return "" + (max + 1);
    }
    
    public static String writeKeyValue(List<Pair> db, String key, String value) {
    	String search = readByKey(db, key);
    	if (search.equals("")) {
    		db.add(new Pair(key, value));
    		return "CREATED";
    	}
    	else {
    		for (int i = 0; i < db.size(); i ++) {
        		if (db.get(i).getKey().equals(key)) {
        			db.remove(i);
        			db.add(new Pair(key, value));
            		return "OVERWRITTEN";
        		}
    		}
    	}
    	return "ERROR";
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


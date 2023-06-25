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

    // \r\n on Windows and \n on UNIX
    public static final String EOL = System.lineSeparator();
	public static final String TRANSM_DEL = "%";
	public static final String SEP_DEL = ":";
	
	// Non riguarda il protocollo, ma è specifico a questa applcazione
	// Rappresenta la posizione in cui viene specificato il tipo di dato
	// (sala, prenotazione, film ... ) nel DataBase specifico a 
	// questa applicazione. 
	public static final int TYPE_OFFSET_VALUE = 3; 
	
    public static final int PORT = 8081;
    private static HashMap<String, String> db;


    /**
     * Avvia il database e l'ascolto di nuove connessioni.
     *
     * @return Un server HTTP Grizzly.
     */
    public static void startServer() {
    	
    	System.out.println("Path: " + Paths.get(".").toAbsolutePath().normalize().toString());
    	
    	db = new HashMap<String, String>();
    	
    	
    	try {
    		String s = Files.readString(Paths.get("data.csv"));
    		String[] records = s.split(EOL);
    		for(int i = 0; i < records.length; i++) {
    			System.out.println(records[i]);
    			db.put(records[i].split(",")[0], records[i].split(",")[1]); 
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
                
                	case "READ-VALUE-QUERY":
					out.println(readValueQuery(db, rcv));
                    System.out.println("Written");
            		break;
            		
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

    public static synchronized String readAllContaining(HashMap<String, String> db, String content, int pos) {
    	String s = "";
    	String[] keys = db.keySet().toArray(new String[db.size()]);
    	for(int i = 0; i < db.size(); i++) {
    		String k = keys[i];
    		String v = db.get(k);
    		if(v.length() > pos + content.length() && 
               v.startsWith(content, pos)) {
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
    
    
    public static synchronized String readValueQuery(HashMap<String, String> db, String rcv ) {
    	
    	String[] queryArray = rcv.split(TRANSM_DEL);
    	
    	//Query sbagliata - il numero di parametri deve essere un multiplo di 3
    	// (posizione, operatore, valore)
    	if ((queryArray.length - 2) % 3 != 0) {
    		return "ERROR";
    	}
    	
    	String delimiter = queryArray[1];
    	String s = "";
    	String[] values = Arrays.copyOf(db.values().toArray(), db.values().toArray().length, String[].class);
    	String[] keys = Arrays.copyOf(db.keySet().toArray(), db.keySet().toArray().length, String[].class);
    	HashMap alreadyFound = new HashMap();
    	
    	for (int i = 2; i < queryArray.length; i+=3) {
			alreadyFound = new HashMap();
    		int position = Integer.parseInt(queryArray[i]);
    		String operator = queryArray[i + 1];
    		String valueToCompare = queryArray[i + 2];

    				
			for (int j = 0; j < values.length; j++) {
				if(values[j].split(delimiter).length > position) {
	
					String v = values[j];
					String extractedValue = v.split(delimiter)[position];
					String k = keys[j];
					
					if (compareWithOperator(extractedValue, valueToCompare, operator) && 
						!alreadyFound.containsKey(k)) {
						alreadyFound.put(k, v);
						
					}
				}
			}
			
			values = Arrays.copyOf(alreadyFound.values().toArray(), alreadyFound.values().toArray().length, String[].class);
			keys = Arrays.copyOf(alreadyFound.keySet().toArray(), alreadyFound.keySet().toArray().length, String[].class);
		}
    	
    	for (int i = 0; i < keys.length; i++) {
    		if(s.equals("")) {
    			s += keys[i] + SEP_DEL + db.get(keys[i]);
    		}
    		else {
    			s += TRANSM_DEL + keys[i] + SEP_DEL + db.get(keys[i]);
    		}
		}
		
		
    	return s;
    }
    
    public static synchronized String readByKey(HashMap<String, String> db, String key) {
    	String v = db.get(key);
    	
    	return (v != null) ? key + SEP_DEL + v : "NOT-FOUND";
    }
    
    public static synchronized String generateKey(HashMap<String, String> db) {
    	String k = UUID.randomUUID().toString();
    	while(db.containsKey(k)) {
    		k = UUID.randomUUID().toString();
		} 
    	return k;
    }
    
    public static synchronized String writeKeyValue(HashMap<String, String> db, String key, String value) {
    	if (!db.containsKey(key)) {
    		db.put(key, value);
    		return "CREATED" + TRANSM_DEL  + key;
    	}
    	else {
    		db.remove(key);
    		db.put(key, value);
    		return "OVERWRITTEN" + TRANSM_DEL  + key;
    	}
    }
    
    public static boolean compareWithOperator(String s1, String s2, String op) {
    	
    	switch (op.toUpperCase()){
    	
	    	case "EQ":
	    		return s1.equals(s2);
	    	case "LT":
	    		return (s1.compareTo(s2) < 0) ? true : false;
	    	case "GT":
	    		return (s1.compareTo(s2) > 0) ? true : false;
            case "LTE":
	    		return (s1.compareTo(s2) <= 0) ? true : false;
	    	case "GTE":
	    		return (s1.compareTo(s2) >= 0) ? true : false;
    	}
    	
    	return false;
    }
}


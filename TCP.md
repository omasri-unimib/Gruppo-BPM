# Progetto Sistemi Distribuiti 2022-2023 - TCP

# TCP Protocol (Database)

##Properties
*Database Port number* = **8081**

*Transmission values delimiter* = ** % **

*Key and Value separation delimiter* = **:**


## Commands

`READ-VALUE`

	Syntax: READ-VALUE%<key>
	
	Descriprion: Reads the element (key and value) from database by its <key>.
	
    Responses:
    	- <key>:<value>
    	- NOT-FOUND
     
    Example: READ-VALUE%123

***

`READ-VALUE-IF-CONTAINS`

	Syntax: READ-VALUE-IF-CONTAINS%<string to search>#<start position>
	
	Descriprion: Reads all elements (key and value) in database where value contains a <string to search> starting from a <start position>.
	
    Responses:
     - <key 1>:<value 1>%...%<key N>:<value N>
     - NOT-FOUND

    Example: READ-VALUE-IF-CONTAINS%FILM%3

***

`READ-VALUE-QUERY`

	Syntax: 
		READ-VALUE-QUERY%<delimiter>%<position>%<EQ | LT | GT | LTE | GTE>%<value>{%<position>%<EQ | LT | GT | LTE | GTE>%<value>}
	
	Descriprion: Reads all elements (key and value) in database where value, splitted in tokens (pieces) using <delimiter>, satisfies all of the specified conditions of the query. Each condition in a query contains: 
			1) a <position> of token to use as a first operand of a comparison
			2) a comparison operator (EQuals, Less Than, Greater Than, Less Than or Equal, Greater Than or Equal)
			3) a <value> to use in a comparison as a second operand (it can be any string value)
	
	Responses:
     - <key 1>:<value 1>%...%<key N>:<value N>
     - NOT-FOUND
     - ERROR      (query syntax is wrong)

    Example: READ-VALUE-QUERY%#%12%GT%SomeString

***

`WRITE-VALUE`

    Syntax: WRITE-VALUE%<value>

	Descriprion: Write a new element in database, associating a <value> to an automatically generated unique key.

	Responses: 
	 - CREATED%<key> 
	 - OVERWRITTEN%<key>
     
    Example: WRITE-VALUE%SomeValue

***

`WRITE-KEY-VALUE`

	Syntax: WRITE-KEY-VALUE%<key>%<value>
	
	Descriprion: Write a new element in database, associating a <value> to the specified <key>. If there is already an element with this <key> in the database, the previous value is overwritten with a new one.
	
	Responses: 
	 - CREATED%<key> 
	 - OVERWRITTEN%<key>
     
    Example: WRITE-KEY-VALUE%123%SomeValue

***

`GEN-KEY`

	Syntax: GEN-KEY
	
	Descriprion: Generate and returns a unique (UUID) key
	
    Responses: 
     - <key>
     
    Example: GEN-KEY
    
***

`DELETE`

	Syntax: DELETE%<key>
	
	Descriprion: Removes an element that has a specified <key> from a database.
	
    Responses: 
     - DONE
     - NOT-FOUND
     
    Example: DELETE%123

***


# Formato in cui in questo progetto vengono memorizzati i valori 
#### Nota: Il formato scelto NON influisce in alcun modo sul funzionamento del database o sul protocollo, che rimane generico. Riguarda solo il modo in cui si salvano i dati di questa specifica applicazione.
Ogni valore associato a una chiave univoca ha la seguente struttura: 
Tipo#Dato1#Dato2# ... #DatoN 
dove Tipo può assumere uno tra i seguenti 4 valori: Reservation, Screening, Movie, Hall

In particolare,
* Se il tipo è "Movie"(Film), il valore è così strutturato:
	Movie#NomeFilm#Genere#Regista#DataUscita

* Se il tipo è "Screening" (Proiezione), il valore è così strutturato:
	Screening#IdHall#IdMovie#DataProiezione#OreProiezione

* Se il tipo è "Hall" (Sala), il valore è così strutturato:
	Hall#NomeSala#PianoSala#NumMassimoPostiRiga#LetteraMassimaPostiColonna

* Se il tipo è "Reservation" (Prenotazione), il valore è così strutturato:
	Reservation#NomeUtente#CognomeUtente#IdScreening#DataProiezione#OreProiezione#Posto1#Posto2 ... #PostoN


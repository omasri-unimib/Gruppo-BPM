# Progetto Sistemi Distribuiti 2022-2023 - TCP

# TCP Protocol (Database)
## Commands

#### READ-VALUE
	READ-VALUE%<key>
    Response:
     - <key>:<value>
     - NOT-FOUND
     
    Example: READ-VALUE%123



#### READ-VALUE-IF-CONTAINS
	READ-VALUE-IF-CONTAINS%<string to search>#<start position>
    Response:
     - <key 1>:<value 1>%...%<key N>:<value N>
     - NOT-FOUND

    Example: READ-VALUE-IF-CONTAINS%FILM%3




#### READ-VALUE-QUERY
	READ-VALUE-QUERY%<delimiter>%<position>%<EQ | LT | GT>%<value>{%<position>%<EQ | LT | GT>%<value>}
    Response:
     - <key 1>:<value 1>%...%<key N>:<value N>
     - NOT-FOUND
     - ERROR      - Query syntax is wrong

    Example: READ-VALUE-QUERY%#%123%GT%SomeString



#### WRITE-VALUE
    WRITE-VALUE%<value>
	Responses: 
	 - CREATED%<key> 
	 - OVERWRITTEN%<key>
     
    Example: WRITE-VALUE%SomeValue



#### WRITE-KEY-VALUE
	WRITE-KEY-VALUE%<key>%<value>
	Responses: 
	 - CREATED%<key> 
	 - OVERWRITTEN%<key>
     
    Example: WRITE-KEY-VALUE%123%SomeValue



#### GEN-KEY
	GEN-KEY
    Response: 
     - <key>
     
    Example: GEN-KEY
    
    
# Proposta Formato Dati nel Database (NON COMPLETO!)
Esempio:
Reservation nel DB: <ArrayLength>#<RES>#<Screening Id>#<Seat 1 Id># ... #<Seat(ArrayLength -2) Id>
Reservation trasmesso: <ArrayLength>#<RES>#<Screening Id>#<Seat 1 Id># ... #<Seat(ArrayLength -2) Id>#id


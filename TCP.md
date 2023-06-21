# Progetto Sistemi Distribuiti 2022-2023 - TCP

# TCP Protocol (Database)
## Commands

#### READ-VALUE
	READ-VALUE%<key>
    Response:
     - DONE%<key>:<value>
     - ERROR
     
    Example: READ-VALUE%123

#### READ-VALUE-IF-CONTAINS
	READ-VALUE-IF-CONTAINS%<string to search>#<start position>
    Response:
     - DONE%<key 1>:<value 1>%...%<key N>:<value N>
     - ERROR

    Example: 	READ-VALUE-IF-CONTAINS%FILM%3

#### WRITE-VALUE
    WRITE-VALUE#<value>
	Responses: 
	 - CREATED 
	 - OVERWRITTEN
	 - ERROR
     
    Example: WRITE-VALUE%SomeValue

#### WRITE-KEY-VALUE
	WRITE-KEY-VALUE%<key>%<value>
	Responses: 
	 - CREATED 
	 - OVERWRITTEN
	 - ERROR
     
    Example: WRITE-KEY-VALUE%123%SomeValue

#### GEN-KEY
	GEN-KEY
    Response: 
     - <key>
     - ERROR
     
    Example: GEN-KEY
    
    
# Proposta Formato Dati nel Database (NON COMPLETO!)
Esempio:
Reservation nel DB: <ArrayLength>#<RES>#<Screening Id>#<Seat 1 Id># ... #<Seat(ArrayLength -2) Id>
Reservation trasmesso: <ArrayLength>#<RES>#<Screening Id>#<Seat 1 Id># ... #<Seat(ArrayLength -2) Id>#id


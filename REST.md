# Esempio

Documentazione delle API REST di esempio. Si assume che i dati vengano scambiati in formato JSON.

## `/contacts`

Ogni risorsa ha la sua sezione. In questo caso la risorsa da documentare è quella dei contatti.

### GET

**Descrizione**: una breve descrizione di cosa fa il metodo applicato alla risorsa. In questo caso restituisce l'elenco dei contatti salvati.

**Parametri**: un elenco dei parametri se previsti, sia nel percorso (esempio `/contacts/{id}`) che nella richiesta (esempio `/contacts?id={id}`) o anche negli header. In questo caso non sono previsti.

**Body richiesta**: cosa ci deve essere nel body della richiesta. In questo caso nulla perché è una GET.

**Risposta**: cosa viene restituito in caso di successo. In questo caso viene restituito la rappresentazione in JSON del contatto, un oggetto JSON con i campi `id` (un intero), `name` e `number` (due stringhe).

**Codici di stato restituiti**: elenco dei codici di stato, se necessario dettagliare e non elencare quelli già previsti da Jackarta in automatico. In questo caso c'è solo lo stato `200 OK` da segnalare:



# Progetto Sistemi Distribuiti 2022-2023 - API REST

Documentazione delle API REST di esempio. Si assume che i dati vengano scambiati in formato JSON.

## Prenotazioni DA FARE

### `/reservation` 

#### GET

**Descrizione**: Restituisce l'elenco delle Prenotazioni

**Parametri**: Sono previsiti 3 parametri 

- `/reservation?screening={idScreening}` filtra per tutte le Prenotazioni relative ad una Proiezione.
  - E.G. /reservation?screening=id
- `/reservation?date={data}` filtra per tutte le Prenotazioni per data.
  - E.G. /reservation?date=2023-10-01
- `/reservation?time={tempo}` filtra per tutte le Prenotazioni per ora e secondi.
  - E.G. /reservation?date=09:33


**Body richiesta**: nulla

**Risposta**: viene restituita la rappresentazione in JSON della Prenotazione, un oggetto JSON con i campi `id` (un intero), `name` e `number` (due stringhe).

**Codici di stato restituiti**: elenco dei codici di stato, se necessario dettagliare e non elencare quelli già previsti da Jackarta in automatico. In questo caso c'è solo lo stato `200 OK` da segnalare:

* 200 OK

### POST

**Descrizione**: aggiunge un contatto alla rubrica telefonica.

**Parametri**: ci deve essere l'header `Content-Type: application/json`.

**Body richiesta**: rappresentazione in formato JSON del contatto con i campi `name` e `number` che sono due stringhe.

**Risposta**: in caso di successo il body è vuoto e la risorsa creata è indicata nell'header `Location`.

**Codici di stato restituiti**:

* 201 Created
* 400 Bad Request: c'è un errore del client (JSON, campo mancante o altro).

## `/contacts/{id}`

### GET

**Descrizione**: restituisce il contatto con l'id fornito.

**Parametri**: un parametro nel percorso `id` che rappresenta l'identificativo del contatto da restituire.

**Body richiesta**: vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON del contatto, un oggetto JSON con i campi `id` (un intero), `name` e `number` (due stringhe).

**Codici di stato restituiti**:

* 200 OK
* 400 Bad Request: c'è un errore del client (ID non valido).
* 404 Not Found: contatto non trovato.


## Proiezione

### `/screening`

#### GET

**Descrizione**: Restituisce l'elenco di tutte le Proiezioni.

**Parametri**: Nessuno.

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Proiezioni, dove l'oggetto Proiezione e' un oggetto JSON con i campi `id`, `idScreening` e `idMovie` (stringhe), `date` (date-da-sistemare), `time` (time-da-sistemare).

**Codici di stato restituiti**:

* 200 OK
* 500 500 Internal Server Error

### `/screening/{id}`

#### GET

**Descrizione**: Restituisce la Sala indentificata da `{id}` 

**Parametri**: path parameter `{id}`

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una Proiezione, un oggetto JSON con i campi `id`, `idScreening` e `idMovie` (stringhe), `date` (date-da-sistemare), `time` (time-da-sistemare).

**Codici di stato restituiti**:

* 200 OK
* 400 Bad Request: errore del client (ID non valido).
* 500 500 Internal Server Error


## Sala

### `/hall`

#### GET

**Descrizione**: Restituisce l'elenco di tutte le Sale.

**Parametri**: Nessuno.

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Sale, dove l'oggetto Sala e' un oggetto JSON con i campi `floor`, `postiColonna` (interi), `id`, `name` e `postiRiga` (stringhe).

**Codici di stato restituiti**:

* 200 OK
* 500 500 Internal Server Error

### `/hall/{id}`

#### GET

**Descrizione**: Restituisce la Sala indentificata da `{id}` 

**Parametri**: path parameter `{id}`

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una Sala, un oggetto JSON con i campi `floor`, `postiColonna` (interi), `id`, `name` e `postiRiga` (stringhe).

**Codici di stato restituiti**:

* 200 OK
* 400 Bad Request: errore del client (ID non valido).
* 500 500 Internal Server Error

## Film

### `/movie`

#### GET

**Descrizione**: Restituisce l'elenco di tutti i Film.

**Parametri**: Nessuno.

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Film, dove l'oggetto Film e' un oggetto JSON con i campi `duration` (double) (in ore e minuti da convertire in sessagesimale), `id`, `name`, `genre`, `director` (stringhe) releaseDate (date-da-sistemare).

**Codici di stato restituiti**:

* 200 OK
* 500 500 Internal Server Error

### `/movie/{id}`

#### GET

**Descrizione**: Restituisce il Film indentificata da `{id}` 

**Parametri**: path parameter `{id}`

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di un Film, un oggetto JSON con i campi `duration` (double) (in ore e minuti da convertire in sessagesimale), `id`, `name`, `genre`, `director` (stringhe) releaseDate (date-da-sistemare).

**Codici di stato restituiti**:

* 200 OK
* 400 Bad Request: errore del client (ID non valido).
* 500 500 Internal Server Error

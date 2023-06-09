# Progetto Sistemi Distribuiti 2022-2023 Gruppo BPM - API REST

## Prenotazioni

### `/reservation` 

#### GET

**Descrizione**: Restituisce l'elenco delle Prenotazioni

**Parametri**: Sono previsiti 3 parametri 

- `/reservation?screening={idScreening}` filtra per tutte le Prenotazioni afferenti ad una Proiezione identificata da idScreening.
  - E.G.) /reservation?screening=1
- `/reservation?date={data: yyyy-MM-dd}` filtra per tutte le Prenotazioni la cui data e' maggiore di quella inserita per parametro.
  - E.G.) /reservation?date=2023-10-01
- `/reservation?time={tempo: HH:mm:ss}` filtra per tutte le Prenotazioni le cui ore e secondi sono maggiori di quelli inseriti per parametro.
  - E.G.) /reservation?time=09:33:00

**Body richiesta**: nulla

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Prenotazioni, dove l'oggetto Prenotazione e' un oggetto JSON del tipo:

```json
[
    {
          "id": "1",
          "nameCustomer": "Name",
          "surnameCustomer": "surname",
          "screening": "1",
          "date": "2023-10-01",
          "time": "09:33:00",
          "positions": [
              "A1",
              "B2"
          ]
    },
    ...
]
```

**Codici di stato restituiti**:

* 200 OK
* 400 Bad Request (parametri non validi)
* 500 Internal Server Error

#### POST

**Descrizione**: Aggiunge una Prenotazione 

**Parametri**: ci deve essere l'header `Content-Type: application/json`.

**Body richiesta**: rappresentazione in formato JSON della Prenotazione (senza id):

```json
{
        "nameCustomer": "Name",
        "surnameCustomer": "surname",
        "screening": "1",
        "date": "2023-10-01",
        "time": "09:33:00",
        "positions": [
            "A1",
            "B2"
        ]
}
```

**Risposta**: in caso di successo il body è vuoto e la risorsa creata è indicata nell'header `Location`.

**Codici di stato restituiti**:

* 201 Created
* 400 Bad Request (JSON non valido)
* 500 Internal Server Error

### `/reservation/{id}`

#### GET

**Descrizione**: restituisce la Prenotazione con l'id fornito.

**Parametri**: un path paramteter `id` che rappresenta l'identificativo della Prenotazione da restituire.

**Body richiesta**: vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una Prenotazione, dove l'oggetto Prenotazione e' un oggetto JSON del tipo:

```json
{
        "id": "{id}",
        "nameCustomer": "Name",
        "surnameCustomer": "surname",
        "screening": "1",
        "date": "2023-10-01",
        "time": "09:33:00",
        "positions": [
            "A1",
            "B2"
        ]
}
```

**Codici di stato restituiti**:

* 200 OK
* 404 Not Found (Prenotazione non trovata)
* 500 Internal Server Error

#### DELETE

**Descrizione**: Elimina la Prenotazione identificata dal'id fornito.

**Parametri**: un path paramteter `id` che rappresenta l'identificativo della Prenotazione da eliminare.

**Body richiesta**: vuoto.

**Risposta**: nulla.

**Codici di stato restituiti**:

* 204 No Content (Prenotazione eliminata con successo)
* 404 Not Found (Prenotazione non trovata)
* 500 Internal Server Error

#### PUT

**Descrizione**: Fa una Update (con eventual upsert) di una Prenotazione identificata da un id

**Parametri**: ci deve essere l'header `Content-Type: application/json` e un path paramteter `id` che rappresenta l'identificativo della Prenotazione da aggiornare.

**Body richiesta**: rappresentazione in formato JSON della Prenotazione: (il body deve contenere l'id della Prenotazione da modificare)

```json
{
        "nameCustomer": "Name",
        "surnameCustomer": "surname",
        "screening": "1",
        "date": "2023-10-01",
        "time": "09:33:00",
        "positions": [
            "A1",
            "B2"
        ]
}
```

**Risposta**: Se esiste una Prenotazione identificata dal `id` mandato, viene modificata la prenotazione e non viene restituito nulla nel body. 
In caso non esiste una Prenotazione identificata dal id viene creata una nuova Prenotazione (upsert), il body è vuoto e la risorsa creata è indicata nell'header `Location`.

**Codici di stato restituiti**:

* 201 Created (Upsert)
* 204 No Content (Modificato)
* 400 Bad Request (JSON non valido)
* 500 Internal Server Error

## Proiezione

### `/screening`

#### GET

**Descrizione**: Restituisce l'elenco di tutte le Proiezioni.

**Parametri**: Sono previsiti 2 parametri 

- `/screening?date={data: yyyy-MM-dd}` filtra per tutte le Proiezioni la cui data e' maggiore di quella inserita per parametro.
  - E.G. /reservation?date=2023-10-01
- `/screening?time={tempo: HH:mm:ss}` filtra per tutte le Proiezioni le cui ore e secondi sono maggiori di quelli inseriti per parametro.
  - E.G. /reservation?time=09:33:00

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Proiezioni, dove l'oggetto Proiezione e' un oggetto JSON del tipo:

```json
[
    {
        "id": "1",
        "idHall": "S1",
        "idMovie": "1",
        "date": "2023-10-29",
        "time": "22:00:00"
    },
    ...
]
```

**Codici di stato restituiti**:

* 200 OK
* 500 500 Internal Server Error

### `/screening/{id}`

#### GET

**Descrizione**: Restituisce la Sala indentificata da `{id}` 

**Parametri**: path parameter `{id}`

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una Proiezione, un oggetto JSON del tipo:

```json
{
    "id": "{id}",
    "idHall": "S1",
    "idMovie": "1",
    "date": "2023-10-29",
    "time": "22:00:00"
}
```

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

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Sale, dove l'oggetto Sala e' un oggetto JSON del tipo:

```json
[
    {
        "id": "S1",
        "name": "Sala 1",
        "floor": 1,
        "postiRiga": "D",
        "postiColonna": 20
    },
    ...
]
```

**Codici di stato restituiti**:

* 200 OK
* 500 500 Internal Server Error

### `/hall/{id}`

#### GET

**Descrizione**: Restituisce la Sala indentificata da `{id}` 

**Parametri**: path parameter `{id}`

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una Sala, un oggetto JSON del tipo:

```json
{
    "id": "{id}",
    "name": "Sala 1",
    "floor": 1,
    "postiRiga": "D",
    "postiColonna": 20
},
```

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

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di una lista di Film, dove l'oggetto Film e' un oggetto JSON del tipo:

```json
[
    {
        "id": "1",
        "name": "Interstellar",
        "genre": "Science fiction",
        "director": "Christopher Nolan",
        "duration": 2.0,
        "releaseDate": "2014-10-26"
    },
    ...
]
```

**Codici di stato restituiti**:

* 200 OK
* 500 500 Internal Server Error

### `/movie/{id}`

#### GET

**Descrizione**: Restituisce il Film indentificata da `{id}` 

**Parametri**: path parameter `{id}`

**Header**: Nessuno.

**Body richiesta**: Vuoto.

**Risposta**: In caso di successo viene restituita la rappresentazione in JSON di un Film, un oggetto JSON del tipo:

```json
{
        "id": "{id}",
        "name": "Interstellar",
        "genre": "Science fiction",
        "director": "Christopher Nolan",
        "duration": 2.0,
        "releaseDate": "2014-10-26"
}
```

**Codici di stato restituiti**:

* 200 OK
* 400 Bad Request: errore del client (ID non valido).
* 500 500 Internal Server Error

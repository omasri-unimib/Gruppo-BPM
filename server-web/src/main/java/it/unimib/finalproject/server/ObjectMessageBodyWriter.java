package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * Genera un JSON valido con una sola stringa.
 *
 * È alternativo a quello predefinito di Jackson. Un JSON che contiene una sola
 * stringa con le virgolette è valido dall'RFC 7159. Jackson a quanto pare non
 * supporta questa cosa.
 *
 * Riferimento e crediti:
 *
 *  - https://stackoverflow.com/a/57468899
 *  - https://memorynotfound.com/jax-rs-messagebodywriter/
 */

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMessageBodyWriter implements MessageBodyWriter<Object> {

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations,
            final MediaType mediaType) {

        return MediaType.APPLICATION_JSON_TYPE.equals(mediaType);
    }

    @Override
    public void writeTo(final Object t, final Class<?> type, final Type genericType,
            final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream out) throws IOException, WebApplicationException {

        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.writeValue(out, t);
    }
}

package org.riwi.eventcatalog.dominio.exception;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String entityName) {
        super("No se encontró un registro en la entidad " + entityName + " con el ID proporcionado.");
    }
}

package ru.otus.exceptions;

public class CellOperationException extends RuntimeException {
    public CellOperationException(String message) {
        super(message);
    }

    public CellOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

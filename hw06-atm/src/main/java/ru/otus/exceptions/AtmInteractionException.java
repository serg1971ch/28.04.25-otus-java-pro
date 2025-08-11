package ru.otus.exceptions;

public class AtmInteractionException extends RuntimeException {
    public AtmInteractionException() {}

    public AtmInteractionException(String message) {
        super(message);
    }

    public AtmInteractionException(String message, Throwable cause) {
        super(message, cause);
    }
}

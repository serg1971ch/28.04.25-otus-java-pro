package ru.otus.exceptions;

public class ClusterOperationException extends RuntimeException {
    public ClusterOperationException() {}

    public ClusterOperationException(String message) {
        super(message);
    }

    public ClusterOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

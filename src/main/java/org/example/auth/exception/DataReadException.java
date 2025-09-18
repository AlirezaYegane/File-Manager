package org.example.auth.exception;

public class DataReadException extends RuntimeException{
    public DataReadException() {
    }

    public DataReadException(Throwable cause) {
        super(cause);
    }

    public DataReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataReadException(String message) {
        super(message);
    }
}

package org.example.auth.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException() {
    }

    public NoDataFoundException(Throwable cause) {
        super(cause);
    }

    public NoDataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataFoundException(String message) {
        super(message);
    }
}

package com.flightdata.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FlightErrorHandling {

    public static class NotFoundFlightsException extends RuntimeException {
        public NotFoundFlightsException(String message) {
            super(message);
        }
    }

    public static class FailedToRetrievalException extends RuntimeException {
        public FailedToRetrievalException(String message) {
            super(message);
        }
    }

    public static class MaxProcessingTimeoutException extends RuntimeException {
        public MaxProcessingTimeoutException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(NotFoundFlightsException.class)
    public ResponseEntity<String> handleNotFoundFlightsException(NotFoundFlightsException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    }

    @ExceptionHandler(FailedToRetrievalException.class)
    public ResponseEntity<String> handleFailedToRetrievalException(FailedToRetrievalException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(MaxProcessingTimeoutException.class)
    public ResponseEntity<String> handleMaxProcessingTimeoutException(MaxProcessingTimeoutException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
    }
}

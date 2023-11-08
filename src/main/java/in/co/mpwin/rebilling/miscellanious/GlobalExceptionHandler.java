package in.co.mpwin.rebilling.miscellanious;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.postgresql.util.PSQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

/*    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Log the exception
       // log.error("Global exception handler caught an exception: " + ex.getMessage());

        // Create an error response and return it to the client
        ErrorResponse errorResponse = new ErrorResponse("An error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }*/

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<?> handlePSQLException(PSQLException ex) {
        // Log the exception
       // log.error("PSQLException occurred: " + ex.getMessage());
       return new ResponseEntity<>(new Message("Database connection error. Please try again later or contact database admin."), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> handleJsonParseException(JsonParseException ex) {
        // Log the exception
        // You can use a logging framework like Log4j or SLF4J for logging
        ex.printStackTrace();

        // Create an error response and return it to the client
        String errorMessage = "JsonParseException:" + ex.getOriginalMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<String> handleJsonMappingException(JsonMappingException ex) {
        // Log the exception
        // You can use a logging framework like Log4j or SLF4J for logging
        ex.printStackTrace();

        // Create an error response and return it to the client
        String errorMessage = "JsonMappingException:" + ex.getOriginalMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}

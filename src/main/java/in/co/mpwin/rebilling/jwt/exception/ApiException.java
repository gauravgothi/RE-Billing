package in.co.mpwin.rebilling.jwt.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    private HttpStatus httpStatus;
    private String message;
    private ApiException apiException;

    public ApiException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ApiException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


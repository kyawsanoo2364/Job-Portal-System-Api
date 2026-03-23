package com.javadev.jobportal.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomWebServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CustomWebServiceException(String message) {
        super(message);
        this.httpStatus = null;
    }

    public CustomWebServiceException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = null;
    }

    public CustomWebServiceException(Throwable cause) {
        super(cause);
        this.httpStatus =null;
    }

    public CustomWebServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = null;
    }

    public CustomWebServiceException() {
        super();
        this.httpStatus = null;
    }

    public CustomWebServiceException(HttpStatus httpStatus,String message,Throwable cause) {
        super(message,cause);
        this.httpStatus = httpStatus;
    }

    public static CustomWebServiceException of(HttpStatus httpStatus,String message) {
        return new CustomWebServiceException(httpStatus,message,null);
    }

}

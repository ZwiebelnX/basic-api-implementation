package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Error;
import com.thoughtworks.rslist.exception.CustomException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentNotValidException.class, CustomException.class})
    public ResponseEntity<Error> exceptionHandler(Exception e) {
        String errMessage = "";
        if (e instanceof MethodArgumentNotValidException) {
            errMessage = "invalid argument";
        }
        if (e instanceof CustomException) {
            errMessage = ((CustomException) e).getErrorMessage();
        }
        Error error = new Error();
        error.setError(errMessage);
        return ResponseEntity.badRequest().body(error);
    }
}

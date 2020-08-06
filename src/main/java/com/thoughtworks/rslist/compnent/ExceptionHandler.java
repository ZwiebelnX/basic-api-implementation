package com.thoughtworks.rslist.compnent;

import com.thoughtworks.rslist.domain.Error;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CustomException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentNotValidException.class, CustomException.class})
    public ResponseEntity<Error> exceptionHandler(Exception e) {
        String errMessage = "";
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof RsEvent) {
                errMessage = "invalid param";
            }
            if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof User) {
                errMessage = "invalid user";
            }
        }
        if (e instanceof CustomException) {
            errMessage = ((CustomException) e).getErrorMessage();
        }
        logger.error(errMessage);
        Error error = new Error();
        error.setError(errMessage);
        return ResponseEntity.badRequest().body(error);
    }
}

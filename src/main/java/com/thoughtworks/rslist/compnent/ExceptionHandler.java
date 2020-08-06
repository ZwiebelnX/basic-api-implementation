package com.thoughtworks.rslist.compnent;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.ErrorDto;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.model.dto.UserDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MethodArgumentNotValidException.class, CustomException.class})
    public ResponseEntity<ErrorDto> exceptionHandler(Exception e) {
        String errMessage = "";
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof RsEventDto) {
                errMessage = "invalid param";
            }
            if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof UserDto) {
                errMessage = "invalid user";
            }
        }
        if (e instanceof CustomException) {
            errMessage = ((CustomException) e).getErrorMessage();
        }
        logger.error(errMessage);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(errMessage);
        return ResponseEntity.badRequest().body(errorDto);
    }
}

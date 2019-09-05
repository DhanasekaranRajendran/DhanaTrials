package com.target.myretail.productapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = PricingNotFoundException.class)
    public ResponseEntity<ErrorMessage> errorHandler(PricingNotFoundException pEx){
        log.error("Product does not exist in DB.", pEx);
        ErrorMessage errorMessage= new ErrorMessage(pEx.getMessage());
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<ErrorMessage> errorHandler(InvalidInputException iEx){
        log.error("Invalid input for product info.", iEx);
        ErrorMessage errorMessage= new ErrorMessage(iEx.getMessage());
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ErrorMessage> errorHandler(Exception ex){
        log.error("IO Exception.", ex);
        ErrorMessage errorMessage= new ErrorMessage(ex.getMessage());
        return new ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<ErrorMessage> errorHandler(HttpClientErrorException hEx){
        log.error("HttpClientErrorException.", hEx);
        ErrorMessage errorMessage= new ErrorMessage("Product not found in RedSky Service");
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> errorHandler(MethodArgumentNotValidException ex){
        BindingResult result = ex.getBindingResult();
        List<ObjectError> fieldErrors  = result.getAllErrors();
        List<String> errors=new ArrayList<String>();
        for(ObjectError objectError:fieldErrors){
            errors.add(objectError.getDefaultMessage());
        }
        Map errorMap = new HashMap();
        errorMap.put("validation_error", errors);
        ErrorMessage errorMessage= new ErrorMessage("Validation Errors");
        return new ResponseEntity(errorMap, HttpStatus.NOT_FOUND);
    }
}


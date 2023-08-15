package uz.pdp.citypaymentservice.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;
import uz.pdp.citypaymentservice.exception.NotAcceptable;
import uz.pdp.citypaymentservice.exception.NotEnoughBalance;
import uz.pdp.citypaymentservice.exception.RequestValidationException;

public class GlobalExceptionHandler {
    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<String>dataNotFoundExp(DataNotFoundException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }
    @ExceptionHandler(value = {RequestValidationException.class})
    public ResponseEntity<String> requestValidationException(RequestValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(value = {NotEnoughBalance.class})
    public ResponseEntity<String> notEnoughCredits(NotEnoughBalance e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {NotAcceptable.class})
    public ResponseEntity<String> notAcceptable(NotAcceptable e) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }
}

package uz.pdp.citypaymentservice.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;

public class GlobalExceptionHandler {
    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<String>dataNotFoundExp(DataNotFoundException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

}

package uz.pdp.citypaymentservice.exception;

public class NotEnoughBalance extends RuntimeException{
    public NotEnoughBalance(String message) {
        super(message);
    }
}

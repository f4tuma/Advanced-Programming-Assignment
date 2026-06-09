// QUESTION 3 - Custom Exception Class 
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
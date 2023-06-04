package nlu.edu.vn.ecommerce.exception;

public class ResponseNotFoundException extends RuntimeException {
    public ResponseNotFoundException (String message) {
        super(message);
    }
}


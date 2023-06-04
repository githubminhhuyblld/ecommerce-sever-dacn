package nlu.edu.vn.ecommerce.exception;

public class DuplicateRecordException extends  RuntimeException{
    public DuplicateRecordException(String message){
        super(message);
    }
}

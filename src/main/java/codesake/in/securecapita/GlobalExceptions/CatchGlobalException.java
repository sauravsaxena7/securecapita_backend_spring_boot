package codesake.in.securecapita.GlobalExceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CatchGlobalException extends Exception{
    private String errorMessage;
    private  String error;
    private int StatusCode;

    public CatchGlobalException(String errorMessage,String error,int StatusCode){
        super(errorMessage);
        this.errorMessage=errorMessage;
        this.error=error;
        this.StatusCode=StatusCode;
    }

}

package codesake.in.securecapita.ApiResponse;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpApiResponse {
    public String timeStamp;
    public int statusCode;
    public String message;
    public String error;
    public String reason;
    public String developerMessage;
    public Boolean success;
    public Map<?,?> data;
}

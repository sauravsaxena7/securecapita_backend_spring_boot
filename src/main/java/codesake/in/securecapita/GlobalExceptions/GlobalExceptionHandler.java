package codesake.in.securecapita.GlobalExceptions;


import codesake.in.securecapita.ApiResponse.HttpApiResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CatchGlobalException.class)
    public ResponseEntity<HttpApiResponse> handleExceptions(CatchGlobalException ex, WebRequest webRequest) {
        HttpApiResponse response = HttpApiResponse.getExceptionHttpApiResponse(ex.getErrorMessage(),null,ex.getStatusCode(), ex.getError());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(ex.getStatusCode()));
    }


}

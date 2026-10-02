package cz.tomas.alibi.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO create RestControllerAdvice to handle this exception and return proper ProblemDetail response
@ResponseStatus(HttpStatus.CONFLICT)
public class CrewManagementException extends RuntimeException {

    public CrewManagementException(String message) {
        super(message);
    }
}

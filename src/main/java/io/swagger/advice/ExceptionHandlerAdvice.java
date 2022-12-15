package io.swagger.advice;

import io.swagger.exception.ApiException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleOtherException(NestedRuntimeException e, final NativeWebRequest request){
        return handleExceptionInternal(e, e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException e, final NativeWebRequest request){
        return handleExceptionInternal(e, e.getMessage(), null, HttpStatus.valueOf(e.getCode()), request);
    }
}

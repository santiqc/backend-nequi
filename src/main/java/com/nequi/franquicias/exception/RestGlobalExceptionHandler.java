package com.nequi.franquicias.exception;


import com.nequi.franquicias.service.impl.FranquiciaServicioImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestGlobalExceptionHandler {

    private static Logger LOG = LoggerFactory.getLogger(FranquiciaServicioImpl.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        String detailedMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(
                "Error in operation",
                detailedMessage,
                String.valueOf(ex.getStatus().value())
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        LOG.error("Error interno: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "Error interno del servidor",
                ex.getMessage(),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
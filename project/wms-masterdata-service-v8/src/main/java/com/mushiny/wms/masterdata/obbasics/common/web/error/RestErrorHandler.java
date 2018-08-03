package com.mushiny.wms.masterdata.obbasics.common.web.error;

import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public final class RestErrorHandler {

    private final ContextService contextService;

    public RestErrorHandler(ContextService contextService) {
        this.contextService = contextService;
    }

    private static final Logger log = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ResponseMessage> handleConflictException(IllegalArgumentException ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling IllegalArgumentException...");
        }
        return new ResponseEntity<>(new ResponseMessage(ResponseMessage.Type.ERROR, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {ConcurrencyFailureException.class})
    public ResponseEntity<ResponseMessage> handleConcurrencyException(ConcurrencyFailureException ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling ConcurrencyFailureException...");
        }
        return new ResponseEntity<>(new ResponseMessage(ResponseMessage.Type.ERROR, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseMessage> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling MethodArgumentNotValidException...");
        }

        ResponseMessage alert = new ResponseMessage(ResponseMessage.Type.ERROR, "INVALID_REQUEST", "INVALID_REQUEST");

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        if (!fieldErrors.isEmpty()) {
            fieldErrors.stream().forEach((e) -> alert.addError(e.getField(), e.getCode(), e.getDefaultMessage()));
        }

        return new ResponseEntity<>(alert, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {FacadeException.class})
    public ResponseEntity<ResponseMessage> handleFacadeException(FacadeException ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling FacadeException...");
        }
        return new ResponseEntity<>(new ResponseMessage(ResponseMessage.Type.ERROR, ex.getKey(), ex.getLocalizedMessage(contextService.getCallersLocale())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ResponseMessage> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling AccessDeniedException...");
        }
        return new ResponseEntity<>(new ResponseMessage(ResponseMessage.Type.ERROR, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseMessage> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling HttpRequestMethodNotSupportedException...");
        }
        return new ResponseEntity<>(new ResponseMessage(ResponseMessage.Type.ERROR, ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseMessage> handleRuntimeException(Exception ex, WebRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("handling Exception...");
        }

        BodyBuilder builder;
        ResponseMessage responseMessage;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            responseMessage = new ResponseMessage(ResponseMessage.Type.ERROR, String.valueOf(responseStatus.value().value()), responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            responseMessage = new ResponseMessage(ResponseMessage.Type.ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
        }
        return builder.body(responseMessage);
    }
}

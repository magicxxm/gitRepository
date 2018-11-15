package com.mushiny.wcs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ServerApplicationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ServerApplicationExceptionHandler.class);

    // 默认拦截所有异常
    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(HttpServletRequest request, Throwable ex) {
        HttpStatus httpStatus = getStatus(request);
        String uri = request.getRequestURI();

        LOG.error("##############################Start##############################");
        LOG.error(ex.getMessage(), ex);

        LOG.error("###############################End###############################");
        return new ResponseEntity<>("出错", httpStatus);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
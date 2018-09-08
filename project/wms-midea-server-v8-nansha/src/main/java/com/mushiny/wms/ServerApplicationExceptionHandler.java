package com.mushiny.wms;

import com.mushiny.wms.common.context.ApplicationBeanContextAware;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.BaseException;
import com.mushiny.wms.common.exception.ExceptionEntity;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@ControllerAdvice
public class ServerApplicationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ServerApplicationExceptionHandler.class);

    // 默认拦截所有异常
    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(HttpServletRequest request, Throwable ex) {
        HttpStatus httpStatus = getStatus(request);
        String uri = request.getRequestURI();
        if (request.getQueryString() != null) {
            uri += '?' + request.getQueryString();
        }
        String url = String.format("%s %s", request.getMethod(), uri);


        ApplicationContext applicationContext = ApplicationBeanContextAware.getBean(ApplicationContext.class);
        Message me=new Message();
        me.setCode("0");
        me.setMessage("fail");
        //me.setDescribe(ex.getCause().getMessage());

        me.setDescribe(ex.toString()+ex.getMessage());
        String message = JSONUtil.toJSon(me);

       /* if (ex.getClass().getName().contains(BaseException.class.getName())) {
            message = ex.getMessage();
        } else {
            ApplicationContext applicationContext = ApplicationBeanContextAware.getBean(ApplicationContext.class);
            ExceptionEntity exceptionEntity = new ExceptionEntity();
            exceptionEntity.setKey(ExceptionEnum.EX_SERVER_ERROR.toString());
            message = applicationContext.getErrorMessage(ExceptionEnum.EX_SERVER_ERROR.toString());
            exceptionEntity.setMessage(message);
            exceptionEntity.setValues(new ArrayList<>());
            message = ConversionUtil.toJson(exceptionEntity);
        }*/
        LOG.error("##############################Start##############################");
        LOG.error(url);
        LOG.error(message, ex);
        LOG.error("###############################End###############################");
        return new ResponseEntity<>(message, httpStatus);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    private class Message{
        private String code;
        private String message;
        private Object data;
        private String describe;
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
package com.mushiny.wcs.common.exception;

import com.mushiny.wcs.common.context.ApplicationBeanContextAware;
import com.mushiny.wcs.common.context.ApplicationContext;
import com.mushiny.wcs.common.utils.ConversionUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiException extends RuntimeException {

    public ApiException(String key) {
        handleExceptions(key);
    }

    public ApiException(String key, Object... params) {
        handleExceptions(key, params);
    }

    private void handleExceptions(String key, Object... params) {
        ApplicationContext applicationContext = ApplicationBeanContextAware.getBean(ApplicationContext.class);
        ExceptionEntity exceptionEntity = new ExceptionEntity();
        exceptionEntity.setKey(key);
        String message = applicationContext.getErrorMessage(key);
        List<Object> values = new ArrayList<>();
        if (params != null) {
            message = MessageFormat.format(message, params);
            Collections.addAll(values, params);
        }
        exceptionEntity.setMessage(message);
        exceptionEntity.setValues(values);
        throw new BaseException(ConversionUtil.toJson(exceptionEntity));
    }
}

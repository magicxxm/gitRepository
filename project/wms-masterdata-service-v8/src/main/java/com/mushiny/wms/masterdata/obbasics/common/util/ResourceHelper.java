package com.mushiny.wms.masterdata.obbasics.common.util;


import com.mushiny.wms.masterdata.obbasics.repository.ResourceRepository;

import java.util.Optional;

public class ResourceHelper {

    public static final String resolve(String message, String key, Object[] parameters, String locale) {
        if (key == null) {
            return message;
        }

        ResourceRepository resourceRepository = ApplicationContextProvider.getApplicationContext().getBean(ResourceRepository.class);
        return Optional.ofNullable(resourceRepository.getByKey(key, locale))
                .map(res -> {
                    String s = res.getResourceValue();
                    return String.format(s, parameters);
                }).orElse(message);
    }
}

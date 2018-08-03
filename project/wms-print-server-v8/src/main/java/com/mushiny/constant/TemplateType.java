package com.mushiny.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/8.
 */
public enum TemplateType {
    TOOLS("tools"), SHUN_FENG("shunfeng"),
    ZHONG_TONG("zhongtong")
    ;
    private static Map<String, TemplateType> typeNameLookup = new HashMap<>();

    static {
        for (TemplateType type : TemplateType.values()) {
            typeNameLookup.put(type.typeName, type);
        }
    }

    public final String typeName;

    TemplateType(String typeName) {
        this.typeName = typeName;
    }

    public static TemplateType forTypeName(String typeName) {
        return typeNameLookup.get(typeName);
    }

}

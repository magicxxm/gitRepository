package com.mushiny.wms.masterdata.obbasics.common.util;


import com.mushiny.wms.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class TypeResolver {

    @SuppressWarnings("unchecked")
    public static boolean isEnumType(Class valueType) {
        return valueType.isEnum();
    }

    @SuppressWarnings("unchecked")
    public static boolean isNumericType(Class c) {
        if (isLongType(c))
            return true;
        if (isIntegerType(c))
            return true;
        if (isDoubleType(c))
            return true;
        if (isByteType(c))
            return true;
        if (isFloatType(c))
            return true;
        if (isBigDecimalType(c))
            return true;
        return false;

    }

    @SuppressWarnings("unchecked")
    public static boolean isBooleanType(Class c) {
        if (c.equals(Boolean.class))
            return true;
        if (c.equals(Boolean.TYPE))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isLongType(Class c) {
        if (c.equals(Long.class))
            return true;
        if (c.equals(Long.TYPE))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isIntegerType(Class c) {
        if (c.equals(Integer.class))
            return true;
        if (c.equals(Integer.TYPE))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isByteType(Class c) {
        if (c.equals(Byte.class))
            return true;
        if (c.equals(Byte.TYPE))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isFloatType(Class c) {
        if (c.equals(Float.class))
            return true;
        if (c.equals(Float.TYPE))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isDoubleType(Class c) {
        if (c.equals(Double.class))
            return true;
        if (c.equals(Double.TYPE))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isDateType(Class c) {
        if (c.equals(Date.class))
            return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isStringType(Class c) {
        if (c.equals(String.class))
            return true;

        return false;
    }

    /**
     * @return true
     * <code>if (isStringType() || isNumericType() || isBooleanType())</code>
     */
    @SuppressWarnings("unchecked")
    public static boolean isPrimitiveType(Class c) {
        return (isStringType(c) || isNumericType(c) || isBooleanType(c));
    }

    /**
     * @return true if property type is instanceof BasicEntity
     */
    @SuppressWarnings("unchecked")
    public static boolean isBusinessObjectType(Class c) {
        if (BaseEntity.class.isAssignableFrom(c))
            return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isCollectionType(Class c) {
        if (Collection.class.isAssignableFrom(c))
            return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    private static boolean isBigDecimalType(Class c) {
        if (BigDecimal.class.isAssignableFrom(c))
            return true;

        return false;
    }
}

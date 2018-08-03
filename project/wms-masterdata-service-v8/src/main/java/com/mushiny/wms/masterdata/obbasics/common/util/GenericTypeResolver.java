package com.mushiny.wms.masterdata.obbasics.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Resolves the generic parameterized type <code>Class<T></code>
 * of the generic type.
 *
 * @author Elen Shen
 */
public final class GenericTypeResolver<T> {

    /**
     * Resolves the generic parameterized type <code>Class<T></code>
     * of the generic type <code>T</code>.
     *
     * @param clazz a Class
     * @return the generic parameterized typed class
     */
    @SuppressWarnings("unchecked")
    public Class<T> resolveGenericType(Class clazz) {
        Class<T> ret = null;
        Type[] types;
        Class[] clazzes;

        // has this interface or class a Generic Type?
        types = clazz.getTypeParameters();
        ret = resolveTypes(types);
        if (ret != null) {
            return ret;
        }

        // No? Test extend interfaces
        types = clazz.getGenericInterfaces();
        clazzes = clazz.getInterfaces();
        ret = resolveTypes(types);
        if (ret != null) {
            return ret;
        }
        //Not found yet: start recursion
        for (int i = 0; i < clazzes.length; i++) {
            try {
                ret = resolveGenericType(clazzes[i]);
                return ret;
            } catch (RuntimeException rex) {
                //
            }
        }

        throw new RuntimeException("Can't resolve Type");
    }

    /**
     * Resolves the first matching generic parameterized type
     * <code>Class<T></code> for a given array of <code>Type</code>.
     * <p>
     * Matching means:
     * <p>
     * <code>
     * (types[i] instanceof ParameterizedType) &&
     * (Class<T>)types[i] // throws no ClassCastException
     * </code>
     *
     * @param types array of Types
     * @return the first castable class
     */
    @SuppressWarnings("unchecked")
    public Class<T> resolveTypes(Type[] types) {
        Class<T> ret;

        for (int i = 0; i < types.length; i++) {
            if (types[i] instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) types[i];
                Type[] pTypes = pType.getActualTypeArguments();
                for (int j = 0; j < pTypes.length; j++) {
                    try {
                        ret = (Class<T>) pTypes[j];
                        return ret;
                    } catch (ClassCastException ccex) {
                        //
                    }
                }
            }
        }
        return null;
    }
}

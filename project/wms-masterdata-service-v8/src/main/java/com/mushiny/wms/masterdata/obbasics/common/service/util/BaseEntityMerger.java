package com.mushiny.wms.masterdata.obbasics.common.service.util;


import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.common.exception.BaseEntityMergeException;

import javax.persistence.EntityManager;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Merges one instance of BasicEntity into another.
 * <p>
 * Why is this? Because when providing an instance as template for
 * creating a new entity, this has no ID field set.
 *
 * @author Elen Shen
 */
public class BaseEntityMerger<T extends BaseEntity> {

    EntityManager entityManager = null;

    public BaseEntityMerger() {
    }

    public BaseEntityMerger(EntityManager entityManager) {
    }

    /**
     * Override to gain performance. This method uses reflections. In an extended class where
     * the type <code><T></code> of BasicEntity is known you can copy property values directly.
     */
    public void mergeInto(T from, T to) throws BaseEntityMergeException {
        try {
            BeanInfo infoTo = Introspector.getBeanInfo(from.getClass());

            PropertyDescriptor[] d = infoTo.getPropertyDescriptors();

            for (int i = 0; i < d.length; i++) {
                try {
                    mergePropertyInto(from, to, d[i]);
                } catch (Throwable ex) {
                    continue;
                }
            }
        } catch (Throwable ex) {
            throw new BaseEntityMergeException(from, to);
        }

        return;
    }

    protected void mergePropertyInto(
            T from,
            T to,
            PropertyDescriptor d
    ) throws IllegalAccessException, InvocationTargetException {

        if (skipMergeIntoProperty(d)) {
            return;
        }

        Object value = d.getReadMethod().invoke(from, new Object[0]);
        if (d.getWriteMethod() != null) {
            d.getWriteMethod().invoke(to, new Object[]{value});
        } else {
            return;
        }
    }

    /**
     * Ignores given property. Standard implementation ignores properties
     * <ul>
     * <li>class property obtained by <code>getClass</code>
     * </ul>id property obtained by <code>getId</code>
     *
     * @param d the inspected property.
     * @return true if this property should be ignored
     */
    protected boolean skipMergeIntoProperty(PropertyDescriptor d) {
        if (d.getPropertyType().equals(Class.class)) {
            return true;
        } else if (d.getName().equalsIgnoreCase("id")) {
            return true;
        }
        return false;
    }
}

package com.mushiny.wms.masterdata.obbasics.exception;

import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;

import java.util.Arrays;

public class InventoryException extends FacadeException {

    private static final long serialVersionUID = 1L;

    private InventoryExceptionKey inventoryExceptionKey;

    public InventoryException(InventoryExceptionKey key, Object[] parameters) {
        super(key.name() + ": " + Arrays.toString(parameters), key.name(), parameters);
        inventoryExceptionKey = key;
    }

    public InventoryException(InventoryExceptionKey key, String param1) {
//        super(key.name() + ":" + param1, key.name(), new Object[]{param1});
        super(param1, key.name(), new Object[]{param1});
        inventoryExceptionKey = key;
    }

    public InventoryException(String msg, InventoryExceptionKey key, Object[] parameters) {
        super(msg, key.name(), parameters);
        inventoryExceptionKey = key;
    }

    public InventoryException(Throwable t, InventoryExceptionKey key, Object[] parameters) {
        super(t, key.name(), parameters);
        inventoryExceptionKey = key;
    }

    public InventoryExceptionKey getMaterialExceptionKey() {
        return inventoryExceptionKey;
    }
}

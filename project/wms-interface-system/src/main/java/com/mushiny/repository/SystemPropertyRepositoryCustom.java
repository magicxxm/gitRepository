package com.mushiny.repository;

public interface SystemPropertyRepositoryCustom {

    String getString(String warehouse, String key);

    boolean getBoolean(String warehouse, String key);

    long getLong(String warehouse, String key);
}

package com.mushiny.common.repository.rsql;

import com.github.tennaito.rsql.misc.ArgumentFormatException;
import com.github.tennaito.rsql.misc.DefaultArgumentParser;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class CustomizedArgumentParser extends DefaultArgumentParser {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parse(String argument, Class<T> type) throws ArgumentFormatException, IllegalArgumentException {
        if (type.isAssignableFrom(LocalDate.class)) {
            LocalDate parsed = LocalDate.parse(argument);
            return (T) parsed;
        } else if (type.isAssignableFrom(ZonedDateTime.class)) {
            ZonedDateTime parsed = ZonedDateTime.parse(argument);
            return (T) parsed;
        }
        return super.parse(argument, type);
    }
}
package com.maryato.dimas.example.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DateSqlConverter implements Converter<String, Date> {

    @Override
    public Date convert(String value) {
        return Date.valueOf(value);
    }
}

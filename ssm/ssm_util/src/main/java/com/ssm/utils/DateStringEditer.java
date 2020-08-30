package com.ssm.utils;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.util.Date;


public class DateStringEditer extends PropertiesEditor {
    public void setAsText(@Nullable String text) throws IllegalArgumentException {
        try {
            Date date = DateUtils.string2Date(text, "yyyy-MM-dd HH:mm");
            super.setValue(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

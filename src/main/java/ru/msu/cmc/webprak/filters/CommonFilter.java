package ru.msu.cmc.webprak.filters;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommonFilter {
    public Map<String, Object> getClassMap() {
        Map<String, Object> map = new HashMap<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            try
            {
                map.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        return map;
    }
}

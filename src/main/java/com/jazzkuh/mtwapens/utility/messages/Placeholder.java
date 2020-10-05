package com.jazzkuh.mtwapens.utility.messages;

public class Placeholder {
    String key;
    Object value;

    private Placeholder(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value.toString();
    }

    public static Placeholder of(String key, Object value) {
        return new Placeholder(key, value);
    }
}

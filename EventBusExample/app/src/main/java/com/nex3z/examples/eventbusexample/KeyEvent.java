package com.nex3z.examples.eventbusexample;

public class KeyEvent {
    private String mKey;

    public KeyEvent(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    @Override
    public String toString() {
        return "KeyEvent: " + mKey;
    }
}

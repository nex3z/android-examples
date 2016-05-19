package com.nex3z.examples.eventbuswithrxjava;

public class CountEvent {
    private int mCount;

    public CountEvent(int count) {
        mCount = count;
    }

    public int getCount() {
        return mCount;
    }
}

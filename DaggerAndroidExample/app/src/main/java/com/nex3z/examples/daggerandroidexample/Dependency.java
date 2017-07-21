package com.nex3z.examples.daggerandroidexample;

import javax.inject.Inject;

public class Dependency {

    @Inject
    public Dependency() {}

    public String getMessage() {
        return "Message from dependency";
    }
}

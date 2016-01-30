package com.nex3z.examlpes.mvp.domain.exception;


public class DefaultErrorBundle implements ErrorBundle {

    private static final String DEFAULT_ERROR_MSG = "Unknown error";

    private final Exception mException;

    public DefaultErrorBundle(Exception exception) {
        this.mException = exception;
    }

    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        if (mException != null) {
            return mException.getMessage();
        } else {
            return DEFAULT_ERROR_MSG;
        }
    }

}

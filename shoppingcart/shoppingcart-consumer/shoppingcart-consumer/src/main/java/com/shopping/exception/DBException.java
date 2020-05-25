package com.shopping.exception;

public class DBException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errormessage;

    public DBException(String errormessage, Throwable exception) {
        super(exception);
        this.errormessage = errormessage;
    }

    public String getErrormessage() {
        return errormessage;
    }
}

package com.shopping.constants;

public enum Constants {


    POC_BIZ("POC_BIZ"),
    POC_CAMEL("POC_CAMEL"),
    POST("POST"),
    MONGOCLIENTURI("mongoclienturi"),
    DATABASENAME("databasename"),
    NOT_VALID("POC_VALIDATION_ERROR"),
    NOT_EXIST("POC_ERROR");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

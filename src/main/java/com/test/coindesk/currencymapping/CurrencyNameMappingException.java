package com.test.coindesk.currencymapping;

public class CurrencyNameMappingException extends RuntimeException{

    public CurrencyNameMappingException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurrencyNameMappingException(String msg) {
        super(msg);
    }
}

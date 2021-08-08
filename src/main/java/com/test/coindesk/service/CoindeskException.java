package com.test.coindesk.service;

public class CoindeskException extends RuntimeException{
    public CoindeskException(String msg, Throwable thr) {
        super(msg, thr);
    }
}

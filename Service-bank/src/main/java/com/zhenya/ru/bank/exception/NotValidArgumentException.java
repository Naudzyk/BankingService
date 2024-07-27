package com.zhenya.ru.bank.exception;

public class NotValidArgumentException extends RuntimeException{

    public NotValidArgumentException(String message) {
        super(message);
    }
}

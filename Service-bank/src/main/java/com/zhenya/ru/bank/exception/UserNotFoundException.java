package com.zhenya.ru.bank.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String msg){
        super(msg);

    }
}

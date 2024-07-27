package com.zhenya.ru.bank.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException (String
                                         msg){
        super(msg);
    }
}

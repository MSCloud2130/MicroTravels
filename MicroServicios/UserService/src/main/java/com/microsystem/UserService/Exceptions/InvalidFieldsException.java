package com.microsystem.UserService.Exceptions;

public class InvalidFieldsException extends RuntimeException{
    public InvalidFieldsException(){
        super("You must complete all fields!");
    }
}

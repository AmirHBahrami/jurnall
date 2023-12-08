package com.amirh.jurnall.model;

public class UserException extends Exception{

  public UserException(Exception cause){
    super(cause);
  }

  public UserException(String msg){
    super(msg);
  }

  public UserException(){
    super("UserException");
  }
}

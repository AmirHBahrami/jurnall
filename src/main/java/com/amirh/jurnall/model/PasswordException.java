package com.amirh.jurnall.model;

public class PasswordException extends Exception{
  
  public PasswordException(String msg){
    super(msg);
  }
  
  public PasswordException(){
    super("PasswordException");
  }
}

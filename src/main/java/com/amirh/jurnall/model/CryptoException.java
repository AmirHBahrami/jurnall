package com.amirh.jurnall.model;

public class CryptoException extends Exception{

  public CryptoException(Exception e){
    super("CryptoException",e);
  }
  
}

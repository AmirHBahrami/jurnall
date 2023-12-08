package com.amirh.jurnall.model;

public class DataSourceNotFoundException extends Exception{
    public DataSourceNotFoundException(){
      super("DataSourceNotFoundException");
    }
    public DataSourceNotFoundException(String msg){
      super(msg);
    }
}

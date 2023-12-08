package com.amirh.jurnall.model;

import java.util.List;
import java.util.LinkedList;

/**
* managed class. I didn't want to necessarily 
* bother with contstructor stuff but did want
* this to be abstract, so please attend the
* subscribe method
*/
public abstract class Observable{

  private List<Observer> observers;
  
  public boolean subscribe(Observer o){
    if(this.observers==null)
      this.observers=new LinkedList<Observer>();
    return this.observers.add(o);
  }

  public boolean unsubscribee(Observer o){
    if(this.observers==null)
      this.observers=new LinkedList<Observer>();
    return this.observers.remove(o);
  }
  
  public void notifyAll(String msg,Object data){
    if(this.observers!=null)
      for(Observer o:observers)
        o.update(msg,data);
  }
}

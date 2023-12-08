package com.amirh.jurnall.control;

import javafx.scene.control.TextArea;

import javafx.fxml.FXML;

import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.Observer;
import com.amirh.jurnall.model.DataSource;

public class TextAreaController{}/* implements DataSource,Observer,Controller{

  @FXML private TextArea entryEditorArea;

  @Override
  public Object collect(){
    return entryEditorArea.getText();
  }

  @Override
  public void update(String msg,Object data){
    if(msg.equals(State.EVENT_ENTRY_UPDATED)){
      System.out.println("TextAreaController.update():updated!");
      Entry e=(Entry)data;
      if(e!=null && e.getTxt()!=null) // meaning: it's a new entry!
        entryEditorArea.setText(e.getTxt());
    }
  }

}*/

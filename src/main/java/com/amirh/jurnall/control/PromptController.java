package com.amirh.jurnall.control;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.Observer;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.Arrays;

public class PromptController implements Controller,Observer,Initializable{

  private static final String PROMPT_TEXT="$";

	@FXML private TextArea promptArea;
	
	@Override
	public void initialize(URL url,ResourceBundle resourceBundle){
		State st=State.getInstance();
		st.subscribe(this);
    this.promptArea.clear();
    this.promptArea.setText(PROMPT_TEXT+"\n");
	}
	
	public void update(String msg,Object ...datas){

		Entry temp=null;

    // when notifyAll is called from state
    // by other UI components, directly
    if(msg.equals(State.EVENT_NEW_PROMPT)){
      if(!(datas[0] instanceof String))
        prompt("<Unreadable Message>");
			else prompt((String)datas[0]);
    }

    else if(msg.equals(State.EVENT_CLEAR_PRMOPT))
			prompt("Jurnall>");

    else if(msg.equals(State.EVENT_ENTRY_PUT_FAILED)
		|| msg.equals(State.EVENT_ENTRY_PUT_SUCCESS)
		|| msg.equals(State.EVENT_ENTRY_DELETED)){
			temp=State.getInstance().getCurrentEntry();
      prompt(msg+" : "+temp.getTitle());
    }

		else if(msg.equals(State.EVENT_ENTRY_UPDATED)){
			temp=State.getInstance().getCurrentEntry();
			prompt("'"+temp.getTitle()+"' selected");
		}

	}

	private void prompt(String s){
		this.promptArea.clear();
		this.promptArea.setText(PROMPT_TEXT+s+"\n");
	}

}

package com.amirh.jurnall.control;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.Observer;
import com.amirh.jurnall.model.DataSource;

import java.net.URL;

import java.util.ResourceBundle;

import java.util.Arrays;

import java.io.File;

import java.awt.event.MouseEvent;

import com.amirh.jurnall.model.Observer;

import com.amirh.jurnall.App;

public class EditorController implements Controller,Observer,Initializable{

	@FXML private TextArea entryEditorArea;
	
	@Override	
	public void initialize(URL url,ResourceBundle resourceBundle){
		State st=State.getInstance();
		st.subscribe(this);
	}
	
	public void update(String msg,Object ...data){
		String temp;
		if(msg.equals(State.EVENT_ENTRY_UPDATED)){
			temp=State.getInstance().getCurrentEntry().getTxt();
			this.entryEditorArea.setText(temp);
		}
	}

	public String getText(){
		return this.entryEditorArea.getText();
	}
		
}

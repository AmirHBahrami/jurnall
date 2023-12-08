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

public class FileListController implements Controller,Observer,Initializable{

	@FXML private ListView filenamesListView;

	private String current;
	
	@Override	
	public void initialize(URL url,ResourceBundle resourceBundle){
		State st=State.getInstance();
		st.subscribe(this);
		this.updateFilenames();

		// TODO make this an fxml thing instead of this wank!
		this.filenamesListView.setOnMouseClicked(me->{
			selectNewItem();
		});
	}
	
	public void update(String msg,Object ...data){
		if(msg.equals(State.EVENT_ENTRY_PUT_SUCCESS) || msg.equals(State.EVENT_ENTRY_DELETED))
			this.updateFilenames(); // if nothing new is added - no problem!
	}

	private void updateFilenames(){
		State st=State.getInstance();
		String[] filenames=st.getUserEntriesHandler().getUserFilenames();
		for(int i=0;i<filenames.length;i++)
			filenames[i]=filenames[i].replaceAll(".txt","");
		this.filenamesListView.getItems().setAll(Arrays.asList(filenames));
	}	

	private void selectNewItem(){
		String newFilename=this.filenamesListView.getSelectionModel().getSelectedItem().toString();
		if(current!=null && 
			(current.equals(newFilename) 
			|| newFilename==null 
			|| newFilename.isEmpty())) return;
		current=newFilename;

		// XXX Entry there is sutitable for passing information, ONLY bc 
		// of it's "getFullPath()" method, which will be used in State
		State.getInstance().selectNewEntry(new Entry(newFilename,""));
	}


	/*
	@FXML public void fuckYou(MouseEvent arg0){

		// XXX
		// ISSUE - #1 : does not fucking work when it's set via fxml,
		// instead I have to set it manually like a retard in initialize()
		
		// at the moment handling r/l click as the same
		this.selectNewItem();
		
		// TODO handle right-click to show a context-menu with +/- and edit options
	}
	*/
	
}

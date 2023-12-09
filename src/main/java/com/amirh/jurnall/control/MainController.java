package com.amirh.jurnall.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Observer;

import java.net.URL;

import java.util.ResourceBundle;

import com.amirh.jurnall.App;

public class MainController implements Initializable,Observer,Controller{
  
  private static final String PROMPT_TEXT="$";
  
  @FXML private TextField usernameField;
  @FXML private TextField titleField;

  @FXML private VBox filenamesListHolder;
	
	// TODO fix
  @FXML private TextArea entryEditorAreaController;
  
  // TODO add controller for menubar
  // TODO make these into their own controller
  @FXML private Button deleteBtn;
  @FXML private Button saveBtn;

  @Override
  public void initialize(URL url,ResourceBundle resourceBundle){
    State st=State.getInstance();
    st.subscribe(this);
    this.usernameField.setText(State.getInstance().getUserEntriesHandler().getUsername());
  }

  @Override
  public void update(String msg,Object ...datas){
		if(msg.equals(State.EVENT_ENTRY_UPDATED))
			this.titleField.setText(
				State.getInstance().getCurrentEntry().getTitle()
			);
  }
  
  @FXML
  public void saveAction(){
    State.getInstance().save();
  }
  
  @FXML
  public void deleteAction(){
    State.getInstance().deleteCurrentEntry();
  }
  
  public String getTitle(){
		return titleField.getText();
  }

  public String getText(){
		return entryEditorAreaController.getText();
  }

}

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
	
	// TODO fix this fuuuuuucker
  @FXML private TextArea entryEditorAreaController;
	// @FXML private EditorController entryEditorAreaController;
  @FXML private TextArea promptArea;
  
  // TODO add controller for menubar
  // TODO make these into their own controller
  @FXML private Button deleteBtn;
  @FXML private Button saveBtn;

  @Override
  public void initialize(URL url,ResourceBundle resourceBundle){
    State st=State.getInstance();
    st.subscribe(this);
    this.usernameField.setText(State.getInstance().getUserEntriesHandler().getUsername());
    this.promptArea.clear();
    this.promptArea.setText(PROMPT_TEXT);
  }

  @Override
  public void update(String msg,Object ...datas){

		Entry temp;

    // when notifyAll is called from state
    // by other UI components, directly
    if(msg.equals(State.EVENT_NEW_PROMPT)){
      if(!(datas[0] instanceof String))
        this.promptArea.appendText(PROMPT_TEXT+"<Unreadable Message>\n");
      this.promptArea.appendText(PROMPT_TEXT+(datas[0]+"\n"));
    }

    else if(msg.equals(State.EVENT_CLEAR_PRMOPT)){
      this.promptArea.clear();
      this.promptArea.setText(PROMPT_TEXT);
    }

    else if(msg.equals(State.EVENT_ENTRY_PUT_FAILED) || msg.equals(State.EVENT_ENTRY_PUT_SUCCESS) || msg.equals(State.EVENT_ENTRY_DELETED) ){
      this.promptArea.clear();
			temp=State.getInstance().getCurrentEntry();
      this.promptArea.setText(PROMPT_TEXT+msg+" : "+temp.getTitle()+"\n");
    }

		else if(msg.equals(State.EVENT_ENTRY_UPDATED)){
			this.promptArea.clear();
			temp=State.getInstance().getCurrentEntry();
			this.promptArea.setText(PROMPT_TEXT+"'"+temp.getTitle()+"' selected\n");
			this.titleField.setText(temp.getTitle());
		}
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

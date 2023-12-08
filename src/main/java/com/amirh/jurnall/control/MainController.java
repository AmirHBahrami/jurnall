package com.amirh.jurnall.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;

import javafx.scene.layout.VBox;

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

  @FXML private TextArea entryEditorArea;
  @FXML private TextArea promptArea;
  
  // TODO make these into their own controller
  @FXML private Button deleteBtn;
  @FXML private Button saveBtn;

  // TODO add controller for menubar

  @Override
  public void initialize(URL url,ResourceBundle resourceBundle){
    State st=State.getInstance();
    st.subscribe(this);
    this.usernameField.setText(State.getInstance().getUserEntriesHandler().getUsername());
    this.promptArea.clear();
    this.promptArea.setText(PROMPT_TEXT);
    // st.getDataSources().set("")
  }

  @Override
  public void update(String msg,Object ...datas){

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

    else if(msg.equals(State.EVENT_ENTRY_PUT_FAILED) || msg.equals(State.EVENT_ENTRY_PUT_SUCCESS)){
      this.promptArea.clear();
      this.promptArea.setText(PROMPT_TEXT+msg+"\n");
    }
  }
  
  @FXML
  public void saveAction(){
    State st=State.getInstance();
    ButtonType bt=null;
    if(st.currentEntryExists()){
      bt=App.showConfirmBox( // I'll just ask user if he wants to change title to save a new
      "overwrite '"+st.getCurrentEntry().getTitle()
      +"' ?");
      if(bt==ButtonType.OK || bt==ButtonType.YES || bt==ButtonType.FINISH)
        st.save(); // overwrite
      else if (bt==ButtonType.NO)
        st.save(false); // save anew
      else if (bt==ButtonType.CANCEL)
        return ; // do nothing
    }
    else st.save();
  }
  
  @FXML
  public void deleteAction(){
    
    // TODO if failed -> tell user, if succeeded -> clear the textarea
    State.getInstance().deleteCurrentEntry();
  }
  
  public String getTitle(){
    return this.titleField.getText();
  }

  public String getText(){
    return this.entryEditorArea.getText();
  }
  
  // NOTE: when a file is deleted, simply clear the children, and call this method again
  // TODO : edit main.fxml and add a ListView instead of vbox
  public void showFileNames(){
    String[] fnames=st.getInstance().getUserEntriesHandler().getUserFilenames();
    Label lb=null;
    for(String str:fnames){
      lb=new Label(str);
      this.filenamesListHolder.getChildren().add(lb);
    }
  }

}

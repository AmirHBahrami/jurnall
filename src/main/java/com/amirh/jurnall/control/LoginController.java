package com.amirh.jurnall.control;

import javafx.scene.control.Button;

import javafx.fxml.FXML;

import com.amirh.jurnall.App;

import com.amirh.jurnall.model.User;
import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Observer;
import com.amirh.jurnall.model.UserException;
import com.amirh.jurnall.model.PasswordException;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;

import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController implements Controller{
  
  @FXML private TextField usernameTextField;
  @FXML private TextField passwordTextField;
  @FXML private TextField unhiddenPasswordField;
  @FXML private RadioButton showPasswordRadio;
  @FXML private Button loginBtn;

  private Observer initiator;
  private boolean passwordRevealed;
  private int tries;

  /**
  * since at the time of this scene, user isn't 
  * initted yet, there's no State instance
  * so the scene changing should happen directly
  */
  public LoginController(Observer initiator){
    this.initiator=initiator;
    this.passwordRevealed=true;
    this.tries=3;
  }
  
  @FXML
  public void doLogin(ActionEvent e){
    User u=null;
    try{
      if( passwordTextField.getText().isEmpty()
      ||  usernameTextField.getText().isEmpty()
      ){
        App.showError("Please Provide full username & password");
        return; // no penalties
      }
      u=new User(usernameTextField.getText(),passwordTextField.getText());
      u.init();
      State.init(u);
      this.initiator.update(App.LOGIN_SUCCESS,u);
    }catch(PasswordException | UserException exc){
      exc.printStackTrace();
      App.showError(exc.getMessage());
      tries--;
    }
    if (u==null && this.tries<=3) this.initiator.update(App.LOGIN_FAILED);
  }

  @FXML
  public void doUnhide(){
    
    // set it in motion if it's selected
    if(this.showPasswordRadio.isSelected())
      passwordKeyTyped();
    else this.unhiddenPasswordField.clear();
  }
  
  @FXML
  public void passwordKeyTyped(){
    if(this.showPasswordRadio.isSelected()){
      this.unhiddenPasswordField.setText(this.passwordTextField.getText());
      this.unhiddenPasswordField.selectEnd();
    }
  }

  @FXML
  public void doNewUser(){
    this.initiator.update(App.REQUEST_REGISTER_USER);
  }

}

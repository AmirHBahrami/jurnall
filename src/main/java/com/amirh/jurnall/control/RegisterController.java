package com.amirh.jurnall.control;

import javafx.scene.control.Button;

import javafx.fxml.FXML;

import com.amirh.jurnall.App;

import com.amirh.jurnall.model.User;
import com.amirh.jurnall.model.Observer;
import com.amirh.jurnall.model.UserException;
import com.amirh.jurnall.model.PasswordException;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import java.io.IOException;

public class RegisterController implements Controller{
  
  @FXML private TextField usernameTextField;
  @FXML private TextField passwordTextField;
  @FXML private TextField passwordRepeatTextField;
  @FXML private Button registerBtn;

  private Observer initiator;
  private int tries;

  /**
  * since at the time of this scene, user isn't 
  * initted yet, there's no State instance
  * so the scene changing should happen directly
  */
  public RegisterController(Observer initiator){
    this.initiator=initiator;
    this.tries=3;
  }
  
  @FXML
  public void doRegister(ActionEvent e){
    User u=null;
    try{
      if( usernameTextField.getText().isEmpty()
      ||  passwordTextField.getText().isEmpty()
      ||  passwordRepeatTextField.getText().isEmpty() ){
        App.showError("Please Provide full username,password & repeated password!");
        return; // no penalty
      }
      if(!passwordTextField.getText().equals(passwordRepeatTextField.getText())){
        App.showError("Password don't match!");
        passwordRepeatTextField.clear();
        return; // no penalties
      }
      u=new User(usernameTextField.getText(),passwordTextField.getText());
      u.create();
      this.initiator.update(App.REGISTER_SUCCESS);
    }catch(UserException | IOException exc){
      exc.printStackTrace();
      App.showError(exc.getMessage());
      tries--;
    }
    if (u==null && this.tries==0) this.initiator.update(App.REGISTER_FAILED);
  }

  @FXML
  public void doGoBack(){
    this.initiator.update(App.REQUEST_LOGIN_USER);
  }

  private void resetFields(){
    usernameTextField.clear();
    passwordTextField.clear();
    passwordRepeatTextField.clear();   
  }

}

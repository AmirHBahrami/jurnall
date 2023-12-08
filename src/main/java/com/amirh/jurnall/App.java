package com.amirh.jurnall;

import java.util.Optional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXMLLoader;

import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.application.Application;

import com.amirh.jurnall.control.Controller;
import com.amirh.jurnall.control.MainController;
import com.amirh.jurnall.control.LoginController;
import com.amirh.jurnall.control.RegisterController;

import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Observer;

public class App extends Application implements Observer{
 
  public static final String 
    LOGIN_SUCCESS="LOGIN_SUCCESS",
    LOGIN_FAILED="LOGIN_FAILED",
    REQUEST_REGISTER_USER="REQUEST_REGISTER_USER",
    REGISTER_SUCCESS="REGISTER_SUCCESS",
    REGISTER_FAILED="REGISTER_FAILED",
    REQUEST_LOGIN_USER="REQUEST_LOGIN_USER";

  public static void showError(String msg){
    showMsgBox(msg,Alert.AlertType.ERROR);
  }

  public static void showInfo(String msg){
    showMsgBox(msg,Alert.AlertType.INFORMATION);
  }

  public static ButtonType showConfirmBox(String question){
    return showMsgBox(
      question,Alert.AlertType.CONFIRMATION
    );
  }

  public static ButtonType showMsgBox(String msg,Alert.AlertType at){
    Alert alert=new Alert(at,msg);
    Optional<ButtonType> result = alert.showAndWait();
    if(result.isPresent())
      return result.get();
    return null;
  }
    
  public static void main( String[] args ){
    launch(args);
  }
 
  private Stage primSt;
  private Scene prevScene;
  private MainController mainController; // just in case...
 
  @Override
  public void update(String msg,Object ...data){
    
    if(msg.equals(App.LOGIN_SUCCESS)){
      this.mainController=new MainController();
      State.getInstance().setMainController(this.mainController);
      goToScene("/Main.fxml",mainController);
    }

    else if(msg.equals(App.REQUEST_REGISTER_USER))
      goToScene("/Register.fxml",new RegisterController(this));

    else if(msg.equals(App.REGISTER_SUCCESS)){ 
      App.showInfo("register successful"); // TODO remove
      goToScene("/Login.fxml",new LoginController(this));
    }

    else if(msg.equals(App.REQUEST_LOGIN_USER)){
      goToScene("/Login.fxml",new LoginController(this));
      App.showInfo("register cancelled"); // TODO remove
    }

    else if(msg.equals(App.REGISTER_FAILED)){
      App.showError("More than 3 tries to Register means you're an Embicile! restart the program if you care");
      System.exit(1);
    }

  }

  public void start(Stage primStage){

    // ONLY LOGIN at first
    this.primSt=primStage;
    goToScene("/Login.fxml",new LoginController(this));
    this.primSt.show();
  }
  
  
  private void goToScene(String path,Controller controller){

		// TODO clean the previous scene from heap
 
    // TODO p.getStylesheets().add("/MenuBar.css");
    Scene sc=toScene(path,controller);
    this.primSt.setScene(sc);
  }
  
  private static Scene toScene(String resourcePath,Controller controller){
    FXMLLoader fl=new FXMLLoader(App.class.getResource(resourcePath));
    Parent p=null;
    Scene sc=null;
    try{
      fl.setController(controller);
      p=fl.load();
      if(p==null) throw new IOException("couldn't read the file '"+resourcePath+"'");
      sc=new Scene(p); 
    }catch(IOException ioe){
      ioe.printStackTrace();
      System.exit(1);
    }
    return sc;
  }
  
}


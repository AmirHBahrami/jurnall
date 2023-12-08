package com.amirh.jurnall.model;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.HashMap;

import com.amirh.jurnall.control.MainController;

/***
* TODO ADD CURRENT ENTRY == NULL SUPPORT
* all the main backend functionalities of the program should go here
* a number of functions in this class are quite small and handle very
* small concern, 2 be modular and reusable, in both: this class and
* other user classes

* communications happen mostly through Observable API: State object
* calls super.notify with a message and a number of objects, Each
* corresponding controller or gui component should then react to it

* TODO [complete the following] the instances where the communication
* happens directly is ...
*/
public final class State extends Observable{
  
  private static State instance=null;

  public static final DateTimeFormatter 
    STANDARD_DATETIME_FORMAT=DateTimeFormatter.ofPattern("YYYY_MM_dd");

  // NOTE : integer message type is harder to debug => String
  public static final String
    EVENT_USER_CHANGED="EVENT_USER_CHANGED",
    EVENT_ENTRY_PUT_SUCCESS="EVENT_ENTRY_PUT_SUCCESS",
    EVENT_ENTRY_PUT_FAILED="EVENT_ENTRY_PUT_FAILED",
    EVENT_TEST="EVENT_TEST",
    EVENT_ENTRY_UPDATED="EVENT_USER_CHANGED",
		EVENT_NEW_FILE_SELECTED="EVENT_NEW_FILE_SELECTED",
    EVENT_NEW_PROMPT="EVENT_NEW_PROMPT",
    EVENT_CLEAR_PRMOPT="EVENT_CLEAR_PRMOPT",
    EVENT_SCENE_CHANGE="EVENT_SCENE_CHANGE",
    EVENT_ENTRY_DELETED="EVENT_ENTRY_DELETED",
    ERROR_DATASOURCE_NOT_INITTED="ERROR_DATASOURCE_NOT_INITTED";

  public static void init(User u){

    if(u==null)
      throw new NullPointerException("User not Initialized!");
    if(instance==null) // that is - if the User u 
      instance=new State(u);
  }

  public static State getInstance(){
    if(instance==null)
      throw new AssertionError("State instance not initialized, call init(User u) first!");
    return instance;
  }

  // ----------------------------------------------- MEMBERS --------------------------------------->

  private UserEntriesHandler userEntriesHandler;
  private Entry currentEntry;
  private MainController mainController; // 2 communicate and get title and text from

  private State(User u){
    this.userEntriesHandler=new UserEntriesHandler(u);
    this.currentEntry=null;
  }

  /*public void changeUser(User u){
    this.userEntriesHandler=new UserEntriesHandler(u);
    this.currentEntry=null;
    this.notifyAll(EVENT_USER_CHANGED,u);
  }*/

  public void save(){
    this.save(true);
  }

  public void save(boolean overwrite){
    String msg=EVENT_ENTRY_PUT_FAILED;
    if(overwrite) msg=this.overwrite();
    else msg=this.saveNew();
    super.notifyAll(msg,this.currentEntry); 
  }
  
  // handling shit easier
	public UserEntriesHandler getUserEntriesHandler(){return this.userEntriesHandler;}
  public Entry getCurrentEntry(){
    if(this.currentEntry==null)return null;
    return (Entry) this.currentEntry.getCopy();
  }
  
  // used by controller of "filenames" to select a new Entry
  public boolean selectNewEntry(Entry e){
    Entry newE=null;
    try{
      newE=this.userEntriesHandler.readEntryFromFile(
        this.userEntriesHandler.getEntryFullPath(e)
      );
      if(newE!=null){
        this.currentEntry=newE; // the whole point of this fucking method
        super.notifyAll(EVENT_ENTRY_UPDATED,this.currentEntry);
      }
    }catch(IOException | CryptoException exc){
      exc.printStackTrace(); // TODO prmopt user about it
      return false;
    }
    return newE==null;
  }

  public void deleteCurrentEntry(){
    this.updateCurrentEntry();
    if(this.userEntriesHandler.delEntryFile(this.currentEntry))
      super.notifyAll(EVENT_ENTRY_DELETED,this.currentEntry.getTitle());
  }

  public void setMainController(MainController mc){
    this.mainController=mc;
  }

  // ----------------------------------------------- PVT.PARTS! --------------------------------------->

  private String saveNew(){ // change the title to a new one
    Entry oldE=(Entry) this.currentEntry.getCopy();
    this.updateCurrentEntry();
    try{
      this.userEntriesHandler.updateEntry(
      oldE,this.currentEntry);
      return EVENT_ENTRY_PUT_SUCCESS;
    }catch(CryptoException | IOException exc){
      exc.printStackTrace();
      return EVENT_ENTRY_PUT_FAILED;
    }
  }

  private String overwrite(){
    this.updateCurrentEntry();
    try{
      this.userEntriesHandler
      .putEntry( this.currentEntry );
      return EVENT_ENTRY_PUT_SUCCESS;
    }catch(IOException | CryptoException exc){
      exc.printStackTrace();
      return EVENT_ENTRY_PUT_FAILED;
    }
  }

  private void updateCurrentEntry(){
    if(this.mainController==null)
      throw new NullPointerException("State::updateCurrentEntry - mainController not set!");
    this.currentEntry=new Entry(mainController.getTitle(),mainController.getText());
  }
  
}

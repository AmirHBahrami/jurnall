package com.amirh.jurnall.model;

import com.amirh.jurnall.io.FileUtils;

import com.amirh.jurnall.Cryptos;

import java.io.File;
import java.io.IOException;
// import java.io.FileNotFoundException;

// XXX big convention: NO '/' AT THE PATHS THAT ARE SAVED ANYWHERE AT ALL!
// XXX passw will not be saved, only used to generate symkey, so only h2 and salt are saved 
//     tho it's kept in-memory and it's a TODO to make sure it goes into some security context
public class User{
  
  public static final String DEFAULT_STORAGE_DIR=
    System.getProperty("user.home")
    +System.getProperty("file.separator")
    +"jurnall";
 
  /* -----------------------MEMBERS--------------------- */

  private String storageDir;
  private String username;
  private String passw; // TODO make sure this won't be readable from in-memory shite or goes throug security contexts
  private String symKey;
  
  public User(String username,String passw,String storageDir){
    this.storageDir=
      storageDir
      +System.getProperty("file.separator")
      +username;
    this.username=username;
    this.passw=passw;
    this.symKey=null;
  }

  public User(String username,String passw){
    this(username,passw,User.DEFAULT_STORAGE_DIR);
  }

  public void create()throws UserException,IOException{ // for register

    File tmp=null;
    if(this.exists())
      throw new UserException("User Already Exists:"+username);

    else{
      
      this.makeUserStorage();

      // write salt - at this level, salt file should exist
      tmp=this.getSavedSaltFile();
      tmp.createNewFile();
      FileUtils.writeF(
        tmp,
        Cryptos.bytesToHex(Cryptos.getSalt())
      ); 
      
      // write h2
      tmp=this.getSavedHashFile();
      tmp.createNewFile();
      FileUtils.writeF(
        tmp,
        Cryptos.sha1(passw+this.readSavedSalt())
      );
    }
  }

  public void init() throws UserException,PasswordException{ // for log-in
    if(!this.exists())
      throw new UserException("User '"+username+"' does not exist, call 'create()' first");
    if(this.isPasswordValid(passw))
      throw new PasswordException(username+"'s password is not valid!"); // TODO, catch & wrap the cause
    this.symKey=Cryptos.sha1(passw); // TODO make sure this gets into some security context or sth
  }
  
  public boolean exists(){
    

    // TODO move file functinoalities to FileUtils
    // TODO : check if the the salt.hash is present
    File f=new File(
      this.storageDir
      +System.getProperty("file.separator")
      +"h2.hash"
    );
    String read=null;
    try{
      read=FileUtils.readF(f);

      // for some reason this was a bug!
      if(read==null||read.isEmpty())return false;
    }catch(IOException ioe){return false;} 
    return f.exists();
  }

  public boolean isInitted(){ // XXX this can change in future
    return this.symKey==null;
  }

  // main functionalities of this class 
  public String enc(String msg) throws CryptoException { return Cryptos.encrypt(msg,this.symKey); }
  public String dec(String msg) throws CryptoException { return Cryptos.decrypt(msg,this.symKey); }

  /* GETTERS */
	public String getStorageDir(){ return this.storageDir; }
	public String getUsername(){ return this.username; }

  /* SETTERS */
  // public void setPassw(String passw){this.passw=passw;}

  /* -----------------------PRIVATES--------------------- */
  
  private boolean isPasswordValid(String passw){
    
    String h2=null,savedH2=null; // current (made from passw) and saved h2

    // passw + salt is the saved format
    try{
      h2=Cryptos.sha1(passw+this.readSavedSalt());
      savedH2=readSavedHash();
    }catch(IOException ioe){
      ioe.printStackTrace(); // TODO log
      return false;
    }
    return h2!=null && savedH2!=null && !savedH2.isEmpty() && savedH2.length()>=0 && h2.equals(savedH2);
  }

  private String readSavedHash() throws IOException{
    return FileUtils.readF(this.getSavedHashFile());
  }

  private String readSavedSalt() throws IOException{
    return (Cryptos.hexToBytes(FileUtils.readF(this.getSavedSaltFile()))).toString();
  }

  private void makeUserStorage(){
    try{
      FileUtils.mkdir(this.storageDir);
      FileUtils.makeFile(
        this.storageDir+System.getProperty("file.separator")
        +"h2.hash"
      );
    }catch(IOException ioe){ioe.printStackTrace();} // if file already exists, nothing is done
  }

  private File getSavedHashFile(){
    return new File(
      this.storageDir+System.getProperty("file.separator")
      +"h2.hash"
    );
  }

  private File getSavedSaltFile(){
    return new File(
      this.storageDir+System.getProperty("file.separator")
      +"salt.hash"
    );
  }

}

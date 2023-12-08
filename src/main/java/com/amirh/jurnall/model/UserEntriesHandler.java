package com.amirh.jurnall.model;

import com.amirh.jurnall.io.FileUtils;

import com.amirh.jurnall.model.User;
import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.CryptoException;

import java.io.File;
import java.io.IOException;

/**
* Wraps User class to use it's functionalities and CRUD Entries for 
* the user. This class connects the functionalities of a User Obejct
* and the Entry Objects it's based to operate upon
* NOTE: this class COULD work with java.nio, but there's no need for
* that, since the unit tests are already passing
*/
public class UserEntriesHandler{ 

  private User user;

  public UserEntriesHandler(User user){this.user=user;}

  /**
  * @param String fpath path string to a file 
  * @throws IOException,CryptoException
  * @return Entry object read from a file resolving to filePath
  */
  public Entry readEntryFromFile(String fpath)throws IOException, CryptoException{
    return this.readEntryFromFile(new File(fpath));
  }

  /**
  * this method is there, to avoid each time having to pass user to a static
  * method in either this class or in Entry class.
  * @return String entry path relative to user.getStorageDir()
  */
  public String getEntryFullPath(Entry e){
    String s=user.getStorageDir()
    +System.getProperty("file.separator")
    +e.getFilename();
    return s; // mind oh mind the fucking null!
  }
  
  public File getEntryFile(Entry e){
    return new File(
      this.getEntryFullPath(e)
    ); // mind the null!
  }

  /**
  * Doesn't matter if the file exists already 
  * @return boolean writing/encryption was succesful or not
  */ 
  public void putEntry(Entry e) throws IOException, CryptoException {
    File f=this.getEntryFile(e);
    if(f!=null)
      FileUtils.writeF(f,this.user.enc(e.getTxt()));
  }
 
  /**
  * @return boolean false, if the file didn't exist. 
  * true if it did, and got updated through the put method
  */
  public void updateEntry(Entry oldE,Entry newE) throws IOException, CryptoException{
    File f=getEntryFile(oldE);
    if(f!=null && f.delete())
      this.putEntry(newE);
    else throw new IOException("no such entry"); // XXX is it necessary though?
  }
  
  /**
  * this method is used for listing a user's entry files 
  * in a list-able manner
  * @return String[] filenames belonging to a user
  */
  public String[] getUserFilenames(){
		String[] fnames=null; 
    File f=new File(user.getStorageDir());
    if(f==null || !f.exists()) return null;
    fnames=f.list((d,n)->{
			return n.endsWith(".txt");
		});
    for(int i=0;i<fnames.length;i++)
			fnames[i]=Entry.getTitleFromFilename(fnames[i]);
    return fnames; // mind the length, it might be empty!
  }

  public boolean delEntryFile(Entry e){
    File found=getEntryFile(e);
    if(found==null)return false;
    return found.delete();
  }

  /**
  * @param File f
  * @throws IOException,CryptoException
  * @return Entry object read from a file resolving to filePath
  */
  public Entry readEntryFromFile(File f)throws IOException, CryptoException{
    String txt,title;
    txt=FileUtils.readF(f);
    title=Entry.getTitleFromFilename(f.getName());
    return new Entry(title,this.user.dec(txt));
  }
  
  /**
  * @return a copied String of the username object (not useful now, maybe later
  * we need multithreading)
  */
  public String getUsername(){
    return String.copyValueOf(
      this.user.getUsername().toCharArray()
    );
  }

}

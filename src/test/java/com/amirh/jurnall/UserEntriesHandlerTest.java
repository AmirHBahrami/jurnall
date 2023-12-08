package com.amirh.jurnall;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.File;

import com.amirh.jurnall.model.User;
import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.DataSource;
import com.amirh.jurnall.model.UserException;
import com.amirh.jurnall.model.CryptoException;
import com.amirh.jurnall.model.PasswordException;
import com.amirh.jurnall.model.UserEntriesHandler;

import com.amirh.jurnall.io.FileUtils;

// TODO add cleaning 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserEntriesHandlerTest{

  private User u;
  private UserEntriesHandler ueh;

  @Before
  public void before(){ 
    u=new User("_tester_userEntriesHandler","_tester_userEntriesHandler");
    try{
      u.create();
    }catch(UserException | IOException exc){
      // no need to do anything here
    }
    try{
      u.init();
    }catch(PasswordException |  UserException exc){
      // here neither!
    }
    ueh=new UserEntriesHandler(u);
  }
  
  @Test
  public void t0_put(){

    Entry e=new Entry("HOLY SHIT","FUCK OFF, MISTER!");

    // initted user -- should work
    File f=null;
    try{
      ueh.putEntry(e);
      f=ueh.getEntryFile(e);
      e=ueh.readEntryFromFile(f);
      Assert.assertTrue("entry_put_really_exists",
        f.exists() && !e.getTxt().isEmpty() && !e.getTitle().isEmpty()
      );
    }catch(CryptoException | IOException exc){Assert.fail(
      "t0_1:put_failed[f]"
    );}

  }

  @Test
  public void t1_read(){

    Entry e=new Entry("HOLY SHIT","FUCK OFF, MISTER!"),temp=null;

    // initted user -- should work
    File f=null;
    try{
      f=ueh.getEntryFile(e);
      temp=ueh.readEntryFromFile(f);
      Assert.assertTrue("entry_put",
        f.exists() && 
        !temp.getTitle().isEmpty() &&
        !temp.getTxt().isEmpty()
      );
    }catch(CryptoException | IOException exc){
      exc.printStackTrace();
      Assert.fail(
        "t1_1:put_failed[f]"
      );
    }

  }

  @AfterClass
  public static void afterClassFace(){
    User uShit=new User("_tester_userEntriesHandler","_tester_userEntriesHandler");

    // clean up for the next time
    FileUtils.delFTree(
      uShit.getStorageDir()
    );
  }

}

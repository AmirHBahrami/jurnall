package com.amirh.jurnall;

import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.File;

import com.amirh.jurnall.io.FileUtils;

import com.amirh.jurnall.Cryptos;
import com.amirh.jurnall.model.User;
import com.amirh.jurnall.model.State;
import com.amirh.jurnall.model.Entry;
import com.amirh.jurnall.model.DataSource;
import com.amirh.jurnall.model.UserException;
import com.amirh.jurnall.model.CryptoException;
import com.amirh.jurnall.model.DataSourceNotFoundException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateTest{

  private static final String UNAME="._test_state",PASSW="WitSchank";

  @BeforeClass
  public static void createUser(){
    afterClassFace(); // to make sure such user doesn't exist  ( XXX WARNING:things might go wrong XXX)
    UserTest.createUser(UNAME,PASSW);
  }

  @AfterClass
  public static void afterClassFace(){
    User u=new User(UNAME,PASSW);

    // clean up for the next time
    FileUtils.delFTree(
      u.getStorageDir()
    ); 
  }
  
  @Test
  public void t0_testInits(){
      
    State st=null;
    try{
      State.init(null);
    }catch(NullPointerException npe){Assert.assertTrue(
      "null_user_causes_exception",true
    );}

    try{
      st=State.getInstance();
    }catch(AssertionError ae){Assert.assertTrue(
      "uninitted_state_caught_correcty",true
    );}
    
    // now correctly
    User u=new User(UNAME,PASSW);
    try{
      State.init(u);
      st=State.getInstance();
    }catch(AssertionError ae){Assert.fail(
      "0:initting_user_failed[f]:"+ae.getMessage()
    );}

  }
  
  @Test
  public void t1_testEntryIO(){
    
    // TODO .. fuck it! fuck writing bullshit tests, i'll do it later
  }
    /*
    // instance to work with
    State st=State.getInstance();
    Assert.assertTrue("state_instance_not_null",st!=null);

    try{st.save();}
    catch(DataSourceNotFoundException dsnfe){Assert.assertTrue(
      "!state.save_without_datasources",true
    );}
    
    // fake example - should not exist
    Entry e=new Entry("title0","text0");
    File f=new File(st.getUserEntriesHandler().getEntryFullPath(e));
    f.delete(); // make reset from the last time this test is run

    // real working DataSources
    DataSource dsText,dsTitle;
    dsText=new DataSource(){
      @Override
      public Object collect(){
        return "text0";
      }
    };
    dsTitle=new DataSource(){
      @Override
      public Object collect(){
        return "title0";
      }
    };
    
    // no ds for title -> should fail
    st.getDataSources().put("entryText",dsText);
    try{st.save();}
    catch(DataSourceNotFoundException dsnfe){Assert.assertTrue(
      "!entry_saved_without_datasource_dsText",true
    );}

    f=new File(st.getUserEntriesHandler().getEntryFullPath(e));
    Assert.assertTrue("no_wrong_files_written_dsText",!f.exists());
    
    // not ds for text -> should fail
    st.getDataSources().remove("entryText");
    st.getDataSources().put("entryTitle",dsTitle);
    try{st.save();}
    catch(DataSourceNotFoundException dsnfe){Assert.assertTrue(
      "!entry_saved_without_datasource_dsTitle",true
    );}

    f=new File(st.getUserEntriesHandler().getEntryFullPath(e));
    Assert.assertTrue("no_wrong_files_written_dsTitle",!f.exists());
    
    // this time it'll work
    st.getDataSources().put("entryText",dsText);
    st.getDataSources().put("entryTitle",dsTitle);
    try{
      st.save();
      Assert.assertTrue("entry_saved_with_both_ds",true);
    }
    catch(DataSourceNotFoundException dsnfe){Assert.fail(
      "entry_saved_succesfully[f]"
    );}
    
    // System.out.println(st.getUserEntriesHandler().getEntryFullPath(e));
    f=new File(st.getUserEntriesHandler().getEntryFullPath(e));
    Assert.assertTrue("shitlord",f.exists());

    // check if the saved entry is correct
    Entry e1=null;
    try{
      e1=st.getUserEntriesHandler().readEntryFromFile(f);
      Assert.assertTrue("entry_read_succesfully",e1.equals(e));
    }catch(IOException | CryptoException exc){Assert.fail(
      "entry_read_successfully[f]"
    );}
  }

  */

 
}

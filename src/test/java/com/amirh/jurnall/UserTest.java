package com.amirh.jurnall;

import org.junit.Test;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

import com.amirh.jurnall.Cryptos;
import com.amirh.jurnall.model.User;
import com.amirh.jurnall.model.UserException;
import com.amirh.jurnall.model.CryptoException;
import com.amirh.jurnall.model.PasswordException;

import com.amirh.jurnall.io.FileUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest{
  
  private static final String correctUname="some_user",correctPassw="some_password";
  
  /**
  * to make it easier for other Test Classes to have a correct user, without dealing with 
  * bullshit
  */
  public static void createUser(String uname,String passw){
    User u=new User(uname,passw);
    try{
      u.create();
    }catch(UserException | IOException exc){
      Assert.fail(exc.getMessage());
    }
  }

  @Test
  public void t0_initUser(){
    User u=new User("non-existent_shit","some_password");
    Assert.assertFalse("non_existent_user_not_found",u.exists());
    try{
      u.init();
    }catch(UserException ue){
      Assert.assertTrue("user_not_initted_before_creation",true);
    }catch(PasswordException exc){Assert.fail(
      "t0_0:wrong_user_init_exception[f]"
    );}

    u=new User(correctUname,correctPassw); // so that it's cleaned correctly at the end
    try{
      u.create();
      Assert.assertTrue("user_created",true);
    }catch(Exception exc){Assert.fail(
      "t0_1:user_not_created[f]"
    );}

    Assert.assertTrue("user_exists_after_creation",u.exists());
    
    try{
      u.create();
    }catch(Exception exc){Assert.assertTrue(
      "user_not_created_twice",true
    );}
    
    try{
      u.init();
      Assert.assertTrue("user_initted_with_correct_password",true);
    }catch(Exception exc){Assert.fail(
      "user_not_initted_with_correct_password"
    );}

    u=new User(correctUname,"wrong_password");
    try{
      u.init();
    }catch(Exception exc){Assert.assertTrue(
      "user_not_initted_with_wrong_password",true
    );}

  }

  @Test
  public void t1_testEncDec(){
    User u=new User(correctUname,correctPassw);

    // initialize the user for usage
    try{
      if(!u.exists())
        u.create();
      u.init();
    }catch(IOException | UserException | PasswordException exc){Assert.fail(
      "t1_0:user_init_failed"
    );}
    
    String plainT="this is some plainT";
    String cipherT=null,temp=null;
    try{
      cipherT=u.enc(plainT);
      Assert.assertTrue("encryption_correct",!plainT.equals(cipherT));
    }catch(CryptoException ce){Assert.fail(
      "t1_1:encryption_failed[f]"
    );}

    try{
      temp=u.enc(plainT);
      Assert.assertTrue(
        "new_and_old_cipher_differ_alot",
        !cipherT.contains(temp)
        && !temp.contains(cipherT)
      );
    }catch(CryptoException ce){Assert.fail(
      "t1_2:encryption_failed[f]"
    );}

    try{
      temp=u.dec(cipherT);
      Assert.assertTrue("decryption_correct",temp.equals(plainT));
    }catch(CryptoException ce){Assert.fail(
      "t1_3:encryption_failed[f]"
    );}

    try{
      temp=u.dec(cipherT);
      Assert.assertTrue("decryption_correct",temp.equals(plainT));
    }catch(CryptoException ce){Assert.fail(
      "t1_4:encryption_failed[f]"
    );}

  }


  @AfterClass
  public static void afterClassFace(){

    // clean up for the next time
    FileUtils.delFTree(
      User.DEFAULT_STORAGE_DIR
      +System.getProperty("file.separator")
      +correctUname
    );
  }

}


package com.amirh.jurnall;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.amirh.jurnall.Cryptos;
import com.amirh.jurnall.model.CryptoException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CryptosTest{

  @Test
  public void t0_testSha(){
    String sha=null;
    String sha1="7d0a8468ed220400c0b8e6f335baa7e070ce880a37e2ac5995b9a97b809026de626da636ac7365249bb974c719edf543b52ed286646f437dc7f810cc2068375c"; // no pun intended!
    String plain="this is a test";

    sha=Cryptos.sha512(plain);
    Assert.assertTrue("sha_not_null",sha!=null && !sha.isEmpty());
    Assert.assertTrue("correct_sha_produced",sha.equals(sha1));
    Assert.assertTrue("same_plain_same_sha"
      ,sha.equals(Cryptos.sha512(plain))
    );
    sha1=Cryptos.sha512(plain+'s'); // re-used the variable
    Assert.assertTrue("sha_small_palin_text_change_big_difference_in_sha"
      ,!sha1.equals(sha) && !sha1.contains(sha) && !sha.contains(sha1)
    );
  }

  @Test
  public void t1_testEncrypt(){

    String  cipherTemp=null,
            cipher=null,
            plain="holy shitwanklord!",
            passw="fuck0ffry!";
    
    try{
      cipher=Cryptos.encrypt(plain,passw);
      Assert.assertTrue("encryption_no_problem",true);
    }catch(CryptoException ce){Assert.fail("crypto_exception_t1_testEncrypt_0[f]");}

    try{
      cipherTemp=Cryptos.encrypt(plain,passw);
      Assert.assertTrue(
        "same_plain&pass_ciphers_not_similar",
        !cipherTemp.contains(cipher) &&
        !cipher.contains(cipherTemp)
      );
    }catch(CryptoException ce){Assert.fail("crypto_exception_t1_testEncrypt_1[f]");}

    try{
      cipherTemp=Cryptos.encrypt(plain+'s',passw);
      Assert.assertTrue(
        "plain_slightly_different_cipher_massively_different",
        !cipherTemp.contains(cipher) &&
        !cipher.contains(cipherTemp)
      );
    }catch(CryptoException ce){Assert.fail("crypto_exception_t1_testEncrypt_2[f]");}

    try{
      cipherTemp=Cryptos.encrypt(plain,passw+'s');
      Assert.assertTrue(
        "different_password_different_ciphers",
        !cipherTemp.contains(cipher) &&
        !cipher.contains(cipherTemp)
      );
    }catch(CryptoException ce){Assert.fail("crypto_exception_t1_testEncrypt_3[f]");}
  }

  @Test 
  public void t2_testDecrypt(){
    String  plain="holy shitwanklord!",
            passw="fuck0ffry!",
            cipher="889782676caf6a938c1754af05cd9280070178989221b82e74b1db679cae837d7f56a466bca2039bcdaf446c8f534d9b1aa66f755eb21d7d54628c57d394";
    try{
      Assert.assertTrue(
        "dec(cipher,passw)==plain"
        ,Cryptos.decrypt(cipher,passw).equals(plain)
      );
    }catch(CryptoException ce){Assert.fail("crypto_exception_t2_testDecrypt_0[f]");}

    try{
      Cryptos.decrypt(cipher+"this_is_some_dipshit",passw).equals(plain);
    }catch(CryptoException ce){Assert.assertTrue(
      "dec(wrong_cipher,passw)->exception",true
    );}

    try{
      Cryptos.decrypt(cipher,passw+"fuck_off").equals(plain);
    }catch(CryptoException ce){Assert.assertTrue(
      "dec(cipher,wrong_passw)->exception",true
    );}

  }
  

}

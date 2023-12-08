package com.amirh.jurnall;

import com.amirh.jurnall.model.CryptoException;

import java.math.BigInteger;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;

import java.security.spec.KeySpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

import java.nio.ByteBuffer;

import java.util.Base64;
import java.util.Arrays;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Cryptos{

  public static final String SALT="nO1f=i?)D#1{_vPC";
  // private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
  // private static final int TAG_LENGTH_BIT = 128;
  // private static final int IV_LENGTH_BYTE = 12;
  // private static final int SALT_LENGTH_BYTE = 16;
  // private static final Charset UTF_8 = StandardCharsets.UTF_8;

  public static byte[] getRandomNonce(int numBytes) {
    byte[] nonce = new byte[numBytes];
    new SecureRandom().nextBytes(nonce);
    return nonce;
  }

  public static String sha1(String msg){return sha512(msg);}
  
  /**
  * get sha-512 hash of anything
  * @param msg input string
  * @return hashed text
  */
  public static String sha512(String msg){
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
      byte[] hashedData = messageDigest.digest(msg.getBytes(StandardCharsets.UTF_8));

      // Convert byte array to hex string
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b : hashedData)
        stringBuilder.append(String.format("%02x", b));
      return stringBuilder.toString();
    } catch (NoSuchAlgorithmException e) {e.printStackTrace();}
    return null;
  }

  public static String encrypt(String message, String password) throws CryptoException{
    byte[] result;
    try{
      byte[] salt = getSalt();
      byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

      // Generate a random 128-bit IV
      SecureRandom random = new SecureRandom();
      byte[] iv = new byte[12];
      random.nextBytes(iv);

      // Derive the encryption key from the password
      SecretKey key = deriveKeyFromPassword(password, salt);

      // Initialize the cipher in encryption mode with GCM parameters
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
      cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

      // Encrypt the message
      byte[] ciphertext = cipher.doFinal(messageBytes);

      // Concatenate the salt, IV, and ciphertext for storage or transmission
      result= new byte[salt.length + iv.length + ciphertext.length];
      System.arraycopy(salt, 0, result, 0, salt.length);
      System.arraycopy(iv, 0, result, salt.length, iv.length);
      System.arraycopy(ciphertext, 0, result, salt.length + iv.length, ciphertext.length);
    }catch(Exception exc){
      throw new CryptoException(exc); // TODO
    }

    // Encode the result as a string
    return bytesToHex(result);
  }

  public static String decrypt(String encrypted, String password) throws CryptoException {
  byte[] decryptedBytes;
    try{
      byte[] encryptedBytes = hexToBytes(encrypted);

      // Extract the salt, IV, and ciphertext from the encrypted bytes
      byte[] salt = Arrays.copyOfRange(encryptedBytes, 0, 16);
      byte[] iv = Arrays.copyOfRange(encryptedBytes, 16, 28);
      byte[] ciphertext = Arrays.copyOfRange(encryptedBytes, 28, encryptedBytes.length);

      // Derive the encryption key from the password and salt
      SecretKey key = deriveKeyFromPassword(password, salt);

      // Initialize the cipher in decryption mode with GCM parameters
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
      cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

      // Decrypt the ciphertext
      decryptedBytes =cipher.doFinal(ciphertext);

    }catch(Exception exc){throw new CryptoException(exc);} // TODO

    // Decode the decrypted bytes as a string
    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }
  
  public static byte[] getSalt(){
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }

  public static String bytesToHex(byte[] bytes){
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public static byte[] hexToBytes(String hex){
    int len = hex.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) 
        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                + Character.digit(hex.charAt(i + 1), 16));
    return data;
  }

  private static SecretKey deriveKeyFromPassword
  (String password, byte[] salt) throws Exception{
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    return new SecretKeySpec(tmp.getEncoded(), "AES");
  }

}

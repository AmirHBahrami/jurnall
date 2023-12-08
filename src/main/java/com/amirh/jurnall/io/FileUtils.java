package com.amirh.jurnall.io;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.nio.charset.StandardCharsets;

import java.util.Stack;

public class FileUtils{

  /**
  * delete an entire tree in no time!
  * @param path String
  */
  public static void delFTree(String path){
    delFTree(new File(path));
  }
  
  /**
  * short-hand way of getting an existing file's name without absolute path and extension
  * @param f file to get it's name
  * @throws IOException if file doesn't exist
  * @return String file's base name
  */
  public static String getFilename(File f){
    if(!f.exists()) return null;
    return getFilename(f.getName());
  }
    
  /**
  * a quick way to check and make a directory
  * @param path absolute or relative
  * @throws IOException
  * @return File 
  */
  public static File mkdir(String path)throws IOException{
    File f=new File(path);
    if(f.exists()){
      if(f.isDirectory())return f;

      // TODO change this to proper error handling
      throw new IOException("path exists and is NOT a directory:'"+path+"'");
    }
    f.mkdirs();
    return f;
  }

  /**
  * a quick way to check and make a file
  * @param path absolute or relative
  * @throws IOException
  * @return File 
  */
  public static File makeFile(String path)throws IOException{
    File f=new File(path);
    if(f.exists())return f;
    f.createNewFile();
    return f;
  }
  
  /**
  * read a file in a buffered manner
  * @param f file address
  * @throws IOException
  * @return String 
  */
  public static String readF(String f) throws IOException{
    return readF(new File(f));
  }
  
  /**
  * write contents of o (using .toString()) into f 
  * @param f file to write to
  * @param o Object to write to file 
  * @throws IOException
  */
  public static void writeF(String f,Object o) throws IOException{
    writeF(new File(f),o.toString());
  }

  public static void writeF(File f,String s) throws IOException{
    BufferedWriter bw=new BufferedWriter(
      new FileWriter(f,StandardCharsets.UTF_8));
    bw.write(s);
    bw.close();
  }

  public static String readF(File f) throws IOException{
    StringBuilder sb=new StringBuilder();
    String line=null;
    BufferedReader br=new BufferedReader(new FileReader(f));
    while((line=br.readLine())!=null)sb.append(line);
    br.close();
    return sb.toString();
  }

  public static String getFilename(String name){
    String fname=name; // just to be on the safe-side
    int pos = fname.lastIndexOf(".");
    if (pos > 0) fname = fname.substring(0, pos);
    return fname;
  }

  public static void delFTree(File dir){
    File[] currList;
    Stack<File> stack = new Stack<File>();
    stack.push(dir);
    while(! stack.isEmpty()){
      if(stack.lastElement().isDirectory()){
        currList = stack.lastElement().listFiles();
        if(currList.length > 0)for(File curr: currList)stack.push(curr);
        else stack.pop().delete();
      }else{stack.pop().delete();}
    }
  }

}

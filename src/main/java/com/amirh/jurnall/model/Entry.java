package com.amirh.jurnall.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Just a Bean!
public class Entry{
  
  public static final String ENTRY_FILE_EXTENSION=".txt";

  public static String getTitleFromFilename(String fname){
    return fname.replace("_"," ").replace(ENTRY_FILE_EXTENSION,"");
  }
  
  private String title;
  private String txt;

  public Entry(String title,String txt){
    this.title=title;
    this.txt=txt;
  }
  
  public boolean equals(Entry e){
    return ( this.title.equals(e.getTitle()) && this.txt.equals(e.getTxt()) );
  }

  public String getFilename(){
    if(this.title==null) this.title="UNTITLED"; // TODO implement UNTITLED_x
    return this.title.trim().replace(" ","_")+ENTRY_FILE_EXTENSION;
  }

  /* GETTERS */
  public String getTitle(){return this.title;}
  public String getTxt(){return this.txt;}
  
  /** to be used in State class to save old entry */
  public Entry getCopy(){
    return new Entry(new String(this.title),new String(this.txt)); // for some reason this should still be written
  }
}

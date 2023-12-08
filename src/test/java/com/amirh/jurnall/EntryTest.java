package com.amirh.jurnall;

import org.junit.Test;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.amirh.jurnall.model.Entry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntryTest{
  
  @Test
  public void t0_equals(){

    Entry e=new Entry("shit","wank"),e1=new Entry("wank","shit");
    Assert.assertTrue("!equals(not_equals_entries)_0",!e.equals(e1));
    Assert.assertTrue("!equals(not_equals_entries)_1",!e1.equals(e));

    e1=new Entry("shit","shit");
    Assert.assertTrue("!equals(not_equal_texts)",!e1.equals(e));

    e1=new Entry("wank","wank");
    Assert.assertTrue("!equals(not_equal_titles)",!e1.equals(e));

    e1=new Entry("shit","wank");
    Assert.assertTrue("equals(equal_entries)_0",e1.equals(e));
    Assert.assertTrue("equals(equal_entries)_1",e.equals(e1));
  }
  
  @Test
  public void t1_copy(){
    Entry e=new Entry("shit","wank");
    Entry e1=e.getCopy();
    Assert.assertTrue("deep_copy_titles",e1.getTitle()!=e.getTitle());
    Assert.assertTrue("deep_copy_texts",e1.getTxt()!=e.getTxt());
    Assert.assertTrue("equals(entry_copies)_0",e1.equals(e));
    Assert.assertTrue("equals(entry_copies)_1",e.equals(e1));
  }
  
}

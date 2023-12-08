package com.amirh.jurnall.model;

/**
* usecase: where there's a stream of data, whose most 
* updated version is shared, but we don't want to call
* set() on an Observer every two miliseconds, this interface
* comes in handy:

* Instead of an Observer there's a Consumer class (not 
* implemented through the code tho) which calls the collect
* when it wants to use the streamed data. this way, the buffered
* data remains the DataSource implementing class and the consumer
* only accesses it when needed.

* so intead of placing the "Push" mechanism on the Producer part,
* we place it on the consumer side

* concrete example: 
* StateManager wants to access the most up to date text of an entry
* which is being held in a TextArea, without the TextAreaController
* having to call StateManager.update() every milisecond. TextAreaController
* implements this interface and StateManager calls the collect whenever
* it wants to write the final version of an Entry. This way the preserved
* internal Buffer of TextArea is also lavereged in a non-shit way, and without
* no head-aches
 
*/
public interface DataSource{
  public Object collect();
}

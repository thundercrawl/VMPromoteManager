/*
 *----------------------------------------------------------------------------
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * (C) Copyright IBM Corp. 2017
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with
 * the U.S. Copyright Office.
 *
 * Source file, component, release :  %W%
 * Version:                           %R%.%L%.%B%.%S%
 * Last Changed:                      %G% at %U%
 *
 *----------------------------------------------------------------------------
 */
package org.zst.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorkerManager implements Servicable
{
   private Log                        log        = LogFactory.getLog(WorkerManager.class);

   private LinkedBlockingQueue<Event> queue      = new LinkedBlockingQueue<Event>();
   private ThreadPoolExecutor         ex         = null;
   private static WorkerManager       self;
   int                                minThreads = 20;
   int                                maxThreads = 200;
   Long                               timeout    = 5L;
   private Thread                     eventThread;

   private WorkerManager()
   {
   }

   public static WorkerManager getInstance()
   {
      if (self == null) self = new WorkerManager();
      return self;
   }

   public void addEvent(Event e)
   {

      queue.add(e);
   }

   public Event getEvent()
   {
      try
      {
         return (Event) queue.take();
      }
      catch (InterruptedException e)
      {

         e.printStackTrace();
      }
      return null;
   }

   @Override
   public void startUp()
   {
      log.debug("WorkManager startup");
      ex = new ThreadPoolExecutor(minThreads, maxThreads, timeout, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
      eventThread = new EventThread();
      eventThread.start();
   }

   public void submit(Event e)
   {
      ex.execute(e.r);

   }

   @Override
   public void shutDown()
   {

      eventThread.interrupt();

   }

   @Override
   public boolean ready()
   {
      // TODO Auto-generated method stub
      return false;
   }

}

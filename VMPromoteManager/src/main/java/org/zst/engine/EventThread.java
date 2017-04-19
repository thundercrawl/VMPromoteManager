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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventThread extends Thread
{
   private static Log log = LogFactory.getLog(EventThread.class);

   public void run()
   {
      while (!Thread.interrupted())
      {
         log.debug("Retrieve object from event queue");
         Event e = WorkerManager.getInstance().getEvent();
         if (e != null)
         {
            log.debug(e.toString());
            WorkerManager.getInstance().submit(e);
         }
         else
            log.error("error found , event is empty");
      }
   }

   public EventThread()
   {
      this.setName("EventManager");

   }

   public EventThread(Runnable target)
   {
      super(target);

   }

   public EventThread(String name)
   {
      super(name);
      // TODO Auto-generated constructor stub
   }

   public EventThread(ThreadGroup group, Runnable target)
   {
      super(group, target);
      // TODO Auto-generated constructor stub
   }

   public EventThread(ThreadGroup group, String name)
   {
      super(group, name);
      // TODO Auto-generated constructor stub
   }

   public EventThread(Runnable target, String name)
   {
      super(target, name);
      // TODO Auto-generated constructor stub
   }

   public EventThread(ThreadGroup group, Runnable target, String name)
   {
      super(group, target, name);
      // TODO Auto-generated constructor stub
   }

   public EventThread(ThreadGroup group, Runnable target, String name, long stackSize)
   {
      super(group, target, name, stackSize);
      // TODO Auto-generated constructor stub
   }

}

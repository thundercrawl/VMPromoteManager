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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zst.RestService.objects.RestHttpStatus;

public class Event
{
   public static long                           EVENT_VM_PROMOTE = 0x0001;
   public static long                           EVENT_VM_DELETE  = 0x0002;
   Runnable                                     r                = null;
   private String                               latch;
   private final static Map<String, RestHttpStatus> l2rMap           = new HashMap<String, RestHttpStatus>();
   private final static Map<String, Event>      l2eMap           = new HashMap<String, Event>();
   private static Map<String, Condition>        waitQueue        = new HashMap<String, Condition>();

   private ReentrantLock                        lock             = new ReentrantLock();
   private Condition                            cd               = lock.newCondition();
   private volatile boolean                     start            = false;
   private static Log                           log              = LogFactory.getLog(Event.class);

   public Event()
   {
   }

   public boolean isStart()
   {
      return this.start;
   }

   public void SetStart()
   {
      log.info("set event processing,latch=" + latch);
      this.start = true;
      l2rMap.get(latch).setMessage(RestHttpStatus.MESSAGE_PROCESSING);
   }

   public String toString()
   {
      return latch + " runnable=" + r.toString();
   }

   public Event(Runnable r, String latch)
   {
      if (r == null)
      {
         r = new Runnable()
         {

            @Override
            public void run()
            {
               System.err.println("System Level Issue found, should exit");
               System.exit(-1);
            }
         };
      }

      log.info("Create event latch=" + latch);
      if (l2eMap.get(latch) == null)
      {
         l2rMap.put(latch, new RestHttpStatus(RestHttpStatus.HTTP_200, RestHttpStatus.MESSAGE_INITIAL));
         l2eMap.put(latch, this);
         waitQueue.put(latch, cd);
         this.r = r;
         this.latch = latch;
      }
   }

   public synchronized void updateStatus(String message)
   {
      l2rMap.get(latch).setMessage(message);
      l2rMap.remove(latch);
   }

   public RestHttpStatus getStatus()
   {
      synchronized (l2rMap)
      {
         RestHttpStatus rt = l2rMap.get(latch);
         return rt;
      }

   }

   public RestHttpStatus getStatAfterInit(String latch)
   {
      log.info("event result size=" + l2eMap.size() + ",start=" + l2eMap.get(latch).isStart());
      synchronized (l2eMap)
      {
         if (getEventByLatch(latch).isStart())
         {
            log.info("Event processing wait, latch=" + latch);
            return l2rMap.get(latch);
         }
         else
            return null;
      }
   }

   public Event getEventByLatch(String latch)
   {
      synchronized (l2eMap)
      {
         return l2eMap.get(latch);
      }
   }

   public void run()
   {
      r.run();
   }
}

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
package org.zst.engine.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zst.RestService.objects.HttpStatus;
import org.zst.engine.Event;

public class PromoteVm implements Runnable
{
   private static Log log   = LogFactory.getLog(PromoteVm.class);
   private Event      e     = new Event();
   String             latch = null;

   public PromoteVm(String latch)
   {
      this.latch = latch;
   }

   @Override
   public void run()
   {
      log.debug("Start promote VM latch=" + latch);
      try
      {
         Event statEvent = e.getEventByLatch(latch);
         statEvent.SetStart();

         Thread.sleep(1000 * 10);
         statEvent.getStatus().setMessage(HttpStatus.MESSAGE_DONE);
         log.debug("Promote VM Finished, latch=" + latch + " start=" + e.getEventByLatch(latch).isStart());
      }
      catch (InterruptedException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}

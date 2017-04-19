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

public interface Servicable
{

   public void startUp();

   public void shutDown();

   public boolean ready();

   public default void checkReady(Servicable sv, Log log)
   {
      if (!sv.ready())
      {
         log.error("Failed to start");
         System.exit(-1);
      }
   }
}

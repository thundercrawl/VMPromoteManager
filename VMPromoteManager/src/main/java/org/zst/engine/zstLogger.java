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
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

public class zstLogger implements Servicable
{

   //private static Level     level;
   private static zstLogger self;

   private zstLogger()
   {
   }

   public static zstLogger getInstance()
   {
      if (self == null) self = new zstLogger();
      return self;
   }

   @Override
   public void startUp()
   {

   }

   @Override
   public void shutDown()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public boolean ready()
   {
      // TODO Auto-generated method stub
      return false;
   }

   public static Log getLogger(String name)
   {
      Log lg = LogFactory.getLog(name);
      System.out.println(LogFactory.getFactory().toString());
      ;
      return lg;
   }

   public void setLogger(String pk, String level)
   {

      LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
      context.getLogger(pk).setLevel(ch.qos.logback.classic.Level.toLevel(level));

   }

   public static void main(String[] args)
   {
      zstLogger.getInstance().setLogger("org.zst.engine", "info");

   }
}

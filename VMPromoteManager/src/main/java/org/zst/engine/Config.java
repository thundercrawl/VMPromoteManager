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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Config implements Servicable
{
   private static Config      self;

   private static PrintWriter pw;

   private Config()
   {
      // TODO Auto-generated constructor stub
   }

   public static PrintWriter getDefaultPW()
   {

      return pw;
   }

   public static Config getInstance()
   {
      if (self == null) self = new Config();

      return self;
   }

   public final static String DB_SCHEMA              = "20170401";
   public final static String DB_DIR_ROOT            = "./data";
   public final static String DB_DIR_HSQL            = DB_DIR_ROOT + "/HSQL";

   public final static String LOG_DIR                = "./log";
   public final static String Error_File             = LOG_DIR + "/error.log";
   public final static String File_DIR               = "./file";
   public final static String Report_DIR             = File_DIR + "/report";

   public final static String TEST_PASS              = "TSL";

   public final static String DB_Column_Type_String  = "String";
   public final static String DB_Column_Type_Integer = "Integer";

   public final static String DB_OJBECTS_PACKAGE     = "org.zst.dbs.objects";
   public final static int    DB_TEST_LEVEL          = 100;

   public final static String WEB_DIR_PUBLIC         = "./public";

   private static void buildFS()
   {
      String dirs[] = { DB_DIR_ROOT, DB_DIR_HSQL, LOG_DIR, WEB_DIR_PUBLIC, File_DIR, Report_DIR };

      for (String dir : dirs)
      {
         File fdir = new File(dir);
         if (!fdir.exists())
         {
            fdir.mkdirs();
         }
      }

   }

   private static void setupExceptionStream()
   {
      FileWriter fw;
      try
      {
         fw = new FileWriter(Error_File, true);
         fw.close();
         OutputStream st = new FileOutputStream(Error_File);
         PrintStream ps = new PrintStream(st);
         System.setErr(ps);
      }
      catch (IOException e)
      {

         e.printStackTrace();
      }

   }

   @Override
   public void startUp()
   {
      zstLogger.getInstance().startUp();
      buildFS();
      setupExceptionStream();
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
}

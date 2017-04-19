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
package org.zst.RestService.actions;

import java.sql.SQLException;

import org.zst.dbs.InstantiateDB;
import org.zst.dbs.objects.Test;
import org.zst.engine.Config;

public class TestAction
{
   private String pass;

   public TestAction(String pass)
   {
      this.pass = pass;
   }

   public boolean runTestDataInitite(int level)
   {
      if (checkPass())
      {
         try
         {
            Test.setInitUserHSQL(InstantiateDB.getInstance().getDS().getConnection(), level);
         }
         catch (SQLException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         return true;
      }
      return false;
   }

   private boolean checkPass()
   {
      if (Config.TEST_PASS.equalsIgnoreCase(pass))

         return true;
      return false;
   }

}

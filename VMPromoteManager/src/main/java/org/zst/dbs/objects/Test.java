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
package org.zst.dbs.objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zst.engine.Config;
import org.zst.engine.DBColumn;

public class Test
{
   private static Log log = LogFactory.getLog(Test.class);
   private String     Test_Status;
   private int        Test_Init;

   private Test()
   {
      // TODO Auto-generated constructor stub
   }

   @DBColumn(type = "string", value = 50)
   public String getStatus()
   {
      return Test_Status;
   }

   @DBColumn(type = "integer", value = 50)
   public int getInit()
   {
      return Test_Init;
   }

   protected static String getRandomString()
   {

      String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
      StringBuilder salt = new StringBuilder();
      Random rnd = new Random();
      while (salt.length() < 8)
      { // length of the random string.
         int index = (int) (rnd.nextFloat() * SALTCHARS.length());
         salt.append(SALTCHARS.charAt(index));
      }
      String saltStr = salt.toString();
      return saltStr;

   }

   public static void setInitUserHSQL(Connection cn, int userLevel)
   {

      int level = 0;
      if (userLevel > 0)
         level = userLevel;
      else
         level = Config.DB_TEST_LEVEL;
      try
      {

         ResultSet rt = cn.prepareStatement("select * from Test").executeQuery();
         if (!rt.next())
         {
            Statement st = cn.createStatement();
            log.debug("No data found in Test record,level=" + level);
            st.addBatch("insert into Test(Init,Status) values(0,'On going')");

            while (level-- > 0)
            {
               String sql = "insert into  zstUser(UserName,PhoneNumber) values('" + getRandomString() + "',"
                     + (int) (Math.random() * 10000) + ")";
               log.info(sql);
               st.addBatch(sql);
            }
            int[] sqlRT = st.executeBatch();

            log.debug("finished generate test data,sqlcodes=" + Arrays.toString(sqlRT));
         }
         else
            log.debug("Test initial records already exists");
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
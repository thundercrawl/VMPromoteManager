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
package org.zst.dbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.jdbc.JDBCDataSource;
import org.zst.engine.Config;
import org.zst.engine.DBObjectEngine;
import org.zst.engine.Servicable;

public class InstantiateDB implements Servicable
{

   private static InstantiateDB self;
   Log                          log    = LogFactory.getLog(InstantiateDB.class);
   private JDBCDataSource       ds;
   private boolean              ready  = false;

   private final String         DB_URL = "jdbc:hsqldb:file:" + Config.DB_DIR_HSQL + "/zstVMDB";

   private InstantiateDB()
   {

   }

   public JDBCDataSource getDS()
   {
      return ds;
   }

   public static InstantiateDB getInstance()
   {
      if (self == null) self = new InstantiateDB();

      return self;
   }

   @Override
   public void startUp()
   {
      log.info("create local embedDB zstVMDB not testdb");
      Connection cn = null;

      ds = new JDBCDataSource();
      ds.setDatabase(DB_URL);

      try
      {
         cn = DriverManager.getConnection(DB_URL);
         //cn = ds.getConnection();
         //Class.forName("org.hsqldb.jdbcDriver");
         // cn = DriverManager.getConnection("jdbc:hsqldb:file:./DB/data/zstVMDB", // filenames
         //       "sa", // username
         //      ""); // p

      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      finally
      {
         if (cn != null)
         {
            log.debug("Connection to DB have made");
            ready = true;
         }
      }
      if (!checkDBSechema())
      {
         buildDBTables();
      }
      checkReady(getInstance(), log);
   }

   @Override
   public void shutDown()
   {

   }

   @Override
   public boolean ready()
   {
      // TODO Auto-generated method stub
      return ready;
   }

   private void buildSchema() throws SQLException
   {
      int rt = ds.getConnection().prepareStatement(
            "create table if not exists schema  ( version varchar(20) not null )").executeUpdate();
      log.debug("create schema return=" + rt);
      if (rt == -1)
      {
         log.error("failed to create schema");
         ready = false;
         return;
      }
      ds.getConnection().prepareStatement("insert into schema (version) values ( " + Config.DB_SCHEMA
            + " )").executeUpdate();
      log.debug("create schema version " + Config.DB_SCHEMA);
   }

   private void buildUsers()
   {

   }

   private void buildDBTables()
   {
      try
      {
         buildSchema();
         List<String> sqls = DBObjectEngine.getInstance().GenerateSQLForPackage(Config.DB_OJBECTS_PACKAGE);
         log.debug("Total running sql=" + sqls.size());
         for (String sql : sqls)
         {
            if (sql.equals("")) continue;
            log.debug("execute sql=" + sql);
            ds.getConnection().prepareStatement(sql).executeUpdate();
         }
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }

   private boolean checkDBSechema()
   {

      try
      {
         ResultSet rt = ds.getConnection().prepareStatement("select * from schema").executeQuery();
         while (rt.next())
            log.debug("DB schema version is : " + rt.getString("version"));
      }
      catch (SQLException e)
      {
         e.printStackTrace();
         log.debug("check schema failed, then create it");
         return false;
         //just return false;
      }

      return true;
   }
}

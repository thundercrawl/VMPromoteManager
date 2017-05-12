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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class DBObjectEngine
{
   private Log                   log = LogFactory.getLog(DBObjectEngine.class);
   private static DBObjectEngine self;

   public static DBObjectEngine getInstance()
   {
      if (self == null) self = new DBObjectEngine();
      return self;
   }

   private DBObjectEngine()
   {
   }

   private String createColumnSQL(Method m)
   {
      String rt = "";
      // m.getAnnotation(annotationClass)
      return rt;
   }

   public List<String> GenerateSQLForPackage(String ObjectPackage)
   {
      ArrayList<String> ar = new ArrayList<String>();
      Reflections reflections = new Reflections(ObjectPackage, new SubTypesScanner(false));

      Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
      for (Class<? extends Object> cl : allClasses)
      {
         log.debug("class=" + cl.getName());
         ar.add(GenerateCreateTableSQL(cl));
      }
      return ar;
   }

   public String GenerateCreateTableSQL(Class cl)
   {
      Method mt[] = cl.getDeclaredMethods();

      ArrayList<String> fields = new ArrayList<String>();
      for (Method m : mt)
      {
         int i = m.getName().indexOf("get");
         if (i != -1)
         {
            DBColumn an = m.getAnnotation(DBColumn.class);
            log.debug("handle method=" + m.getName());
            if (null == an)
            {
               log.error("check DB objects set the dbcolumn mapping corretcly for method " + m.getName());
               continue;
            }
            log.debug(m.getName() + "type=" + an.type() + " value=" + an.value());
            fields.add(m.getName());
         }
      }
      if (fields.isEmpty()) return "";
      log.debug("Found columns=" + fields.size());
      return createHSQLCreateTableSQL(fields, cl);
   }

   private String createHSQLCreateTableSQL(List<String> fields, Class cl)
   {
      String columns = "";

      for (String field : fields)
      {
         log.debug(field);
         try
         {
            Method m = cl.getMethod(field);
            DBColumn ca = m.getAnnotation(DBColumn.class);
            log.debug("build db type" + ca.type());
            if (ca != null)
            {
               if (ca.type().equalsIgnoreCase(Config.DB_Column_Type_String))
               {

                  if (columns.equals(""))
                     columns += field.substring(3, field.length()) + " varchar(" + ca.value() + ")";
                  else
                     columns += "," + field.substring(3, field.length()) + " varchar(" + ca.value() + ")";
               }
               else if (ca.type().equalsIgnoreCase(Config.DB_Column_Type_Integer))
               {

                  if (columns.equals(""))
                     columns += field.substring(3, field.length()) + " integer";
                  else
                     columns += "," + field.substring(3, field.length()) + " integer";
               }
            }

            DBPrimary pa = m.getAnnotation(DBPrimary.class);
            if (pa != null && ca != null)
            {

            }

         }
         catch (NoSuchMethodException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (SecurityException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      String[] cm = cl.getName().split("\\.");
      String sql = "create table if not exists " + cm[cm.length - 1] + " ( " + columns + " )";
      log.debug(sql);
      return sql;
   }

   public static void main(String[] args)
   {
      DBObjectEngine.getInstance().GenerateSQLForPackage(Config.DB_OJBECTS_PACKAGE);
   }

}

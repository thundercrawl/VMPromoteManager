package org.zst.dbs.objects;

import java.util.ArrayList;
import java.util.List;

import org.zst.engine.Config;
import org.zst.engine.DBColumn;
import org.zst.engine.DBPrimary;

public class zstUser
{
   private String username;
   private String phonenumber;
   private String password;
   private int    approved;

   private String id;

   public zstUser(String username, String phonenumber, String password)
   {
      this.username = username;
      this.phonenumber = phonenumber;
      this.password = password;
   }

   @DBPrimary
   @DBColumn(type = Config.DB_Column_Type_String, value = 32)
   public String getID()
   {
      return id;
   }

   @DBColumn(type = Config.DB_Column_Type_String, value = 20)
   public String getUserName()
   {
      return username;
   }

   @DBColumn(type = Config.DB_Column_Type_Integer, value = 20)
   public int getApproved()
   {
      return approved;
   }

   @DBColumn(type = Config.DB_Column_Type_String, value = 20)
   public String getPhoneNumber()
   {
      return phonenumber;
   }

   @DBColumn(type = Config.DB_Column_Type_String, value = 20)
   public String getPassword()
   {
      return password;
   }

   public List<zstUser> getUsers()
   {
      ArrayList<zstUser> users = new ArrayList<zstUser>();

      return users;
   }

   public static zstUser findByUsername(String un)
   {
      return new zstUser(un, "10086", "passw0rd");

   }

}

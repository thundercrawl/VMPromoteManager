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
package org.zst.vms.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.compute.QuotaSetService;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.QuotaSet;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.User;
import org.openstack4j.openstack.OSFactory;

public class VMManagerV2
{
   private org.openstack4j.api.OSClient.OSClientV2 os;
   private static VMManagerV2                      self;

   public static VMManagerV2 getInstance()
   {
      if (self == null) self = new VMManagerV2();

      return self;
   }

   public OSClient getOS()
   {
      return os;
   }

   public void getDefaulQuota()
   {
      QuotaSetService qs = os.compute().quotaSets();
      List<? extends User> users = os.identity().users().list();
      if (users.isEmpty()) System.out.println("users is empty");
      Tenant id = os.identity().tenants().getByName("admin");
      /*
      os.identity().domains().list();
      List<? extends Project> ps = os.identity().projects().list();
      
      String id = null;
      for (Project p : ps)
      {
         if (p.getName().equals("admin"))
         {
            id = p.getId();
         }
      }
      */
      QuotaSet st = qs.get(id.getId());
      int n = st.getInstances();
      System.out.println("instance=" + n);
   }

   public String createVM(String vmName)
   {

      //flavor = os.compute().flavors().create(flavor);
      List<Flavor> flavors = (List<Flavor>) os.compute().flavors().list();
      List<? extends Image> images = os.compute().images().list();
      System.out.println(images.size());
      String image_id = null;
      String flavor_id = null;

      // Create a Server Model Object
      ServerCreate sc = Builders.server().name(vmName).flavor(flavors.get(0)).image(
            (org.openstack4j.model.compute.Image) images.get(0)).build();
      // Boot the Server
      Server server = os.compute().servers().boot(sc);
      return server.getId();

   }

   public void stopVM(String id)
   {
      os.compute().servers().action(id, Action.STOP);

   }

   public void stopAllServers()
   {
      List<Server> servers = (List<Server>) VMManagerV2.getInstance().getOS().compute().servers().list();
      for (Server s : servers)
      {
         stopVM(s.getId());
      }

   }

   public void listAllServerStatus()
   {
      List<Server> servers = (List<Server>) VMManagerV2.getInstance().getOS().compute().servers().list();
      for (Server s : servers)
      {
         System.out.println(s.getName() + "=" + s.getPowerState());
      }
   }

   public boolean CheckServerStatusReady(String id, Status st)
   {
      boolean rt = false;
      Server sr = VMManagerV2.getInstance().getOS().compute().servers().get(id);
      int retry = 30;

      while (sr == null)
      {
         try
         {
            Thread.sleep(3000);
            retry--;
         }
         catch (InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         sr = VMManagerV2.getInstance().getOS().compute().servers().get(id);
         if (retry <= 0)
         {
            rt = false;
            break;
         }

      }

      retry = 3;
      while (sr != null)
      {
         System.out.println(sr.getTaskState());
         Status status = sr.getStatus();
         if (status.equals(st))
         {
            rt = true;
            break;
         }
         try
         {
            Thread.sleep(5000);
            retry--;
         }
         catch (InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         if (retry <= 0)
         {
            rt = false;
            break;
         }
         sr = VMManagerV2.getInstance().getOS().compute().servers().get(id);

      }
      return rt;
   }

   public void common()
   {

      // List all Servers
      List<? extends Server> servers = os.compute().servers().list();
      for (Server s : servers)
      {
         System.out.println("s=" + s.getName() + " ip4=" + s.getAccessIPv4() + " id=" + s.getId());
      }
   }

   public void deleteVM(String id)
   {
      os.compute().servers().action(id, Action.FORCEDELETE);
   }

   public List<String> getAllServerIDs()
   {
      List<Server> servers = (List<Server>) VMManagerV2.getInstance().getOS().compute().servers().list();

      List<String> ids = new ArrayList<String>();

      for (Server s : servers)
         ids.add(s.getId());
      return ids;
   }

   private VMManagerV2()
   {

      //  os = OSFactory.builderV3().endpoint("http://9.37.201.216/identity/v3").credentials("admin", "passw0rd",
      //        Identifier.byId("default")).authenticate();
      os = OSFactory.builderV2().endpoint("http://9.37.201.216/identity/v2.0").credentials("admin",
            "passw0rd").tenantName("admin").authenticate();

   }

   public void test1()
   {
      //List<? extends Project> projectList = os.identity().projects().list();
      String id = VMManagerV2.getInstance().createVM("特斯拉控股");
      System.out.println("id=" + id);
      boolean ready = VMManagerV2.getInstance().CheckServerStatusReady(id, Status.ACTIVE);
      if (ready) System.out.println("VM is ready now");
      ;

   }

   public static void main(String[] args) throws IOException
   {
      //VMManager.getInstance().createVM("2222");
      VMManagerV2.getInstance().listAllServerStatus();
   }

}

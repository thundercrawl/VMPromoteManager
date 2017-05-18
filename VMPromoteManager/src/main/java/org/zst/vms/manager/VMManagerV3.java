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
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.compute.QuotaSetService;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.QuotaSet;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.identity.v3.domain.KeystoneService;

public class VMManagerV3
{
   private OSClientV3         os;
   private static VMManagerV3 self;
   private String             AUTH_URL = null;

   public static VMManagerV3 getInstance()
   {
      if (self == null) self = new VMManagerV3();

      return self;
   }

   public OSClient getOS()
   {
      return os;
   }

   public void getDefaulQuota()
   {
      QuotaSetService qs = os.compute().quotaSets();
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
      QuotaSet st = qs.get("admin");
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
      List<Server> servers = (List<Server>) VMManagerV3.getInstance().getOS().compute().servers().list();
      for (Server s : servers)
      {
         stopVM(s.getId());
      }

   }

   public void listAllServerStatus()
   {
      List<Server> servers = (List<Server>) VMManagerV3.getInstance().getOS().compute().servers().list();
      for (Server s : servers)
      {
         System.out.println(s.getName() + "=" + s.getPowerState());
      }
   }

   public boolean CheckServerStatusReady(String id, Status st)
   {
      boolean rt = false;
      Server sr = VMManagerV3.getInstance().getOS().compute().servers().get(id);
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
         sr = VMManagerV3.getInstance().getOS().compute().servers().get(id);
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
         sr = VMManagerV3.getInstance().getOS().compute().servers().get(id);

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
      List<Server> servers = (List<Server>) VMManagerV3.getInstance().getOS().compute().servers().list();

      List<String> ids = new ArrayList<String>();

      for (Server s : servers)
         ids.add(s.getId());
      return ids;
   }

   private VMManagerV3()
   {

      AUTH_URL = "http://travelerVM.rtp.raleigh.ibm.com:5000/v3";
      //OSFactory.enableHttpLoggingFilter(true);
      //os = OSFactory.builderV3().endpoint(AUTH_URL).credentials("admin", "5e0009e55b834e51").scopeToProject(
      //Identifier.byId("c6cebba1c551452da4c2c3967b202ae8")).authenticate().perspective(Facing.ADMIN);
      os = OSFactory.builderV3().endpoint(AUTH_URL).credentials("demo", "5684f4ee8ae9436f", Identifier.byName(
            "Default")).scopeToProject(Identifier.byId("670cc4653a4d42d38c5f00282bc8ae23")).authenticate();

   }

   private void listAllServices()
   {
      List<? extends KeystoneService> ss = (List<? extends KeystoneService>) os.getToken().getCatalog();
      for (KeystoneService s : ss)
      {
         System.out.println("Service:" + s.getName());
         List<? extends Endpoint> endpoints = s.getEndpoints();
         for (Endpoint endpoint : endpoints)
         {
            System.out.println("EndPoint:" + endpoint.getUrl());
         }
      }
   }

   public void test1()
   {
      //os.identity().domains().list();
      //List<? extends Project> projectList = os.identity().projects().list();
      String id = VMManagerV3.getInstance().createVM("特斯拉控股");
      System.out.println("id=" + id);
      boolean ready = VMManagerV3.getInstance().CheckServerStatusReady(id, Status.ACTIVE);
      if (ready) System.out.println("VM is ready now");
      ;

   }

   public static void main(String[] args) throws IOException
   {

      VMManagerV3.getInstance().getAllServerIDs();
   }

}

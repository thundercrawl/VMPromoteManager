package org.zst.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FilesUtility
{
   public static Log           log = LogFactory.getLog(FilesUtility.class);
   private static FilesUtility ut;

   private FilesUtility()
   {

   }

   public static FilesUtility getInstance()
   {
      if (ut == null) return new FilesUtility();

      return ut;
   }

   public List<String> getAllFilesWithPath(String path)
   {
      File folder = new File(path);
      ArrayList<String> sb = new ArrayList<String>();

      log.info("List files under : " + path);

      Stack<String> dirs = new Stack<String>();
      dirs.add(path);
      String currentPath = path;
      while (!dirs.isEmpty())
      {
         folder = new File(currentPath);
         File[] listOfFiles = folder.listFiles();
         for (int i = 0; i < listOfFiles.length; i++)
         {
            if (listOfFiles[i].isFile())
            {
               sb.add(listOfFiles[i].getAbsolutePath());
               log.debug(listOfFiles[i].getAbsolutePath());
            }
            else
            {
               log.info("Enter folder " + listOfFiles[i]);
               dirs.push(listOfFiles[i].getPath());
            }
         }
         currentPath = dirs.pop();
      }

      return sb;
   }

   public static void main(String[] args)
   {
      List<String> files = FilesUtility.getInstance().getAllFilesWithPath("c:/temp");
      log.info("Total file size=" + files.size());
   }
}

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
package org.zst.RestService.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zst.RestService.actions.TestAction;
import org.zst.RestService.objects.HttpStatus;
import org.zst.engine.zstLogger;

@RestController
public class RestTestController
{
   private static Log log = LogFactory.getLog(RestTestController.class);

   public RestTestController()
   {
      // TODO Auto-generated constructor stub
   }

   @RequestMapping("/test")
   public HttpStatus test(@RequestParam(value = "pass") String pass, @RequestParam(value = "level") int level,
                          HttpServletRequest request, HttpServletResponse response)
   {

      boolean rt = new TestAction(pass).runTestDataInitite(level);
      if (rt) return new HttpStatus(HttpStatus.HTTP_200, "TestProcessing");
      return new HttpStatus(HttpStatus.HTTP_200, "");

   }

   @RequestMapping("/setLog")
   public HttpStatus setLog(@RequestParam(value = "package") String pk, @RequestParam(value = "level") String level)
   {
      zstLogger.getInstance().setLogger(pk, level);
      return new HttpStatus(HttpStatus.HTTP_200, "");
   }

}

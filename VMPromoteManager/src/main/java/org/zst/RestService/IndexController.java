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
package org.zst.RestService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

class ErrorJson
{

   public Integer status;
   public String  error;
   public String  message;
   public String  timeStamp;
   public String  trace;

   public ErrorJson(int status, Map<String, Object> errorAttributes)
   {
      this.status = status;
      this.error = (String) errorAttributes.get("error");
      this.message = (String) errorAttributes.get("message");
      this.timeStamp = errorAttributes.get("timestamp").toString();
      this.trace = (String) errorAttributes.get("trace");
   }

}

@RestController
public class IndexController implements ErrorController
{

   public IndexController()
   {

   }

   private static final String PATH = "/error";

   @Autowired
   private ErrorAttributes     errorAttributes;

   @RequestMapping(value = PATH)
   ErrorJson error(HttpServletRequest request, HttpServletResponse response)
   {

      return new ErrorJson(response.getStatus(), getErrorAttributes(request, false));
   }

   @Override
   public String getErrorPath()
   {
      return PATH;
   }

   private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace)
   {
      RequestAttributes requestAttributes = new ServletRequestAttributes(request);
      return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
   }

}

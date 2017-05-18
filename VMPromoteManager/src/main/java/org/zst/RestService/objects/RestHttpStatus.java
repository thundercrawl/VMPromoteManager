package org.zst.RestService.objects;

public class RestHttpStatus
{
   private String       status;
   public static String HTTP_200           = "200";
   public static String HTTP_400           = "400";
   public static long   HTTP_WAIT_TIMEOUT  = 5L;           //minutes

   public static String MESSAGE_INITIAL    = "Initialized";
   public static String MESSAGE_PROCESSING = "Processing";
   public static String MESSAGE_DONE       = "Done";
   private String       message;

   public RestHttpStatus(String statusCode, String message)
   {
      this.status = statusCode;
      this.message = message;
   }

   public String getStatus()
   {
      return status;
   }

   public String getMessage()
   {
      return message;
   }

   public void setMessage(String s)
   {
      message = s;
   }

   public String payLoad()
   {
      return "";
   }
}

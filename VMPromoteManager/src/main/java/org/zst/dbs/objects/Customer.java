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

public class Customer
{
   private long   id;
   private String firstName, lastName;

   public Customer(long id, String firstName, String lastName)
   {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
   }

   @Override
   public String toString()
   {
      return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id, firstName, lastName);
   }

   // getters & setters omitted for brevity
}
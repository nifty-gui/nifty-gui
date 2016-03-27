/**
 * Copyright 2005 Bushe Enterprises, Inc., Hopkinton, MA, USA, www.bushe.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bushe.swing.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Aids in troubleshooting Swing application exceptions or any exception where the caller's stack may not be the
 * exception stack (such as producer-consumer patterns that cross threads).
 * <p/>
 * Swing exceptions usually occur on the Swing Event Dispatch Thread, and often occur when code puts events on the EDT.
 * This code is often in a non-EDT thread such as a thread that is receiving data from a server.  If the non-EDT threads
 * puts a call on the EDT and that EDT call causes and exception, the stack trace of the exception is lost, and it often
 * difficult or impossible to determine where the non-EDT call came from.
 * <p/>
 * This Exception class is used to handle exceptions that occur when events are posted on the Swing EDT or occur on
 * another thread from the Swing EDT. It includes a "swing" call stack to record from where the event occurred, and
 * overrides so that the exception and the swing calling stack print nicely to logs.
 * <p/>
 * The swing calling stack is different from the cause of the exception since it is gathered before the exception occurs
 * in a different stack from the cause and used after the exception in a new thread occurs.
 *
 * @author Michael Bushe michael@bushe.com
 * @todo in SwingUtils, make an invokeLater() method that saves the calling stack and catches all exceptions from a
 * subsequent call to SwingUtilities.invokeLater(), then throws a Swing Exception so the calling stack is saved.
 */
public class SwingException extends Exception {
   protected StackTraceElement[] callingStackTrace;

   /** Default constructor */
   public SwingException() {
      super();
   }

   /**
    * Constructor for compatibility with Exception. Use ClientException(String, Throwable, StackTraceElement[])
    * instead
    */
   public SwingException(String message) {
      super(message);
   }

   /** Constructor for compatibility with Exception Use ClientException(String, Throwable, StackTraceElement[]) instead */
   public SwingException(Throwable cause) {
      super(cause);
   }

   /** Constructor for compatibility with Exception Use ClientException(String, Throwable, StackTraceElement[]) instead */
   public SwingException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Preferred constructor.
    * <p/>
    *
    * @param message The message of exception
    * @param cause The cause of the exception in the same call stack
    * @param callingStack the stack trace that the client used to call the exception to occur.
    */
   public SwingException(String message, Throwable cause, StackTraceElement[] callingStack) {
      super(message, cause);
      setCallingStack(callingStack);
   }

   /**
    * Swing exceptions often have two stacks - one thread causes the posting of an action on another thread - usually
    * the Swing EDT thread.  The other is the stack of the actual thread the exception occurred on, the exception occurs
    * after the post.
    *
    * @param swingCallingStack the stack trace that the client used to cause the exception to occur.
    */
   public void setCallingStack(StackTraceElement[] swingCallingStack) {
      this.callingStackTrace = swingCallingStack;
   }

   /**
    * Client exceptions often have two stacks - one thread causes the posting of an action on another thread - usually
    * the Swing EDT thread.  The other is the stack of the actual thread the exception occurred on.
    *
    * @return the stack trace that the client used to cause the exception to occur.
    */
   public StackTraceElement[] getCallingStack() {
      return callingStackTrace;
   }

   /**
    * Calls printWriter(ps, true)
    *
    * @param ps the print stream
    */
   public void printStackTrace(PrintStream ps) {
      PrintWriter pw = new PrintWriter(ps, true);
      printStackTrace(pw);
   }

   /**
    * Prints the calling stack and the exception stack trace.
    *
    * @param pw
    */
   public void printStackTrace(PrintWriter pw) {
      pw.println(this);
      if (callingStackTrace != null) {
         pw.println("Calling stack:");
         for (int i = 0; i < callingStackTrace.length; i++) {
            pw.println("\tat " + callingStackTrace[i]);
         }
         pw.println("Stack after call:");
      }
      super.printStackTrace(pw);
   }
}


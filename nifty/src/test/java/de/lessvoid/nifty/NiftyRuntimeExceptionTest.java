package de.lessvoid.nifty;

import static org.junit.Assert.*;

import org.junit.Test;

public class NiftyRuntimeExceptionTest {

  @Test
  public void testWithException() {
    try {
      throw new NiftyRuntimeException(new Exception("Error"));
    } catch (NiftyRuntimeException e) {
      assertEquals("Error", e.getCause().getMessage());
    }
  }

  @Test
  public void testWithMessage() {
    try {
      throw new NiftyRuntimeException("Error");
    } catch (NiftyRuntimeException e) {
      assertEquals("Error", e.getMessage());
    }
  }
}

package de.lessvoid.nifty.controls;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CheckBoxStateChangedEventTest {

  @Test
  public void testChecked() {
    CheckBoxStateChangedEvent event = new CheckBoxStateChangedEvent(null, true);
    assertTrue(event.isChecked());
  }

  @Test
  public void testUnchecked() {
    CheckBoxStateChangedEvent event = new CheckBoxStateChangedEvent(null, false);
    assertFalse(event.isChecked());
  }
}

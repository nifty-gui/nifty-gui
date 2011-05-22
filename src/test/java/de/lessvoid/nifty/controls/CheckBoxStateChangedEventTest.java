package de.lessvoid.nifty.controls;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


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

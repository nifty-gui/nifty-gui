package de.lessvoid.nifty.controls.checkbox;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;

public class CheckBoxImplTest {
  private CheckBoxImpl checkBox = new CheckBoxImpl(null);
  private CheckBoxView view;
  private Capture<CheckBoxStateChangedEvent> capturedEvent = new Capture<CheckBoxStateChangedEvent>();

  @Before
  public void before() {
    view = createMock(CheckBoxView.class);
    checkBox.bindToView(view);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testDefault() {
    replay(view);
    assertFalse(checkBox.isChecked());
  }

  @Test
  public void testCheck() {
    expectViewUpdate(true);
    checkBox.check();
    assertCheckBoxState(true);
  }

  @Test
  public void testUncheck() {
    replay(view);
    checkBox.uncheck();
    assertEquals(false, checkBox.isChecked());
  }

  @Test
  public void testSetToChecked() {
    expectViewUpdate(true);
    checkBox.setChecked(true);
    assertCheckBoxState(true);
  }

  @Test
  public void testSetToUnchecked() {
    replay(view);
    checkBox.setChecked(false);
    assertEquals(false, checkBox.isChecked());
  }

  @Test
  public void testToggle() {
    expectViewUpdate(true);
    checkBox.toggle();
    assertCheckBoxState(true);
  }

  @Test
  public void testToggleToOff() {
    view.update(true);
    view.publish(capture(capturedEvent));
    replay(view);

    checkBox.uncheck();
    checkBox.toggle();
    assertEquals(true, capturedEvent.getValue().isChecked());
    assertEquals(true, checkBox.isChecked());
  }

  private void expectViewUpdate(final boolean expectedCheck) {
    view.update(expectedCheck);
    view.publish(capture(capturedEvent));
    replay(view);
  }

  private void assertCheckBoxState(final boolean expectedState) {
    assertEquals(expectedState, checkBox.isChecked());
    assertEquals(expectedState, capturedEvent.getValue().isChecked());
  }
}

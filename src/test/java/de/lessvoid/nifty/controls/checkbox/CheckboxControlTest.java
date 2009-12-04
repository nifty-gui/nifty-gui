package de.lessvoid.nifty.controls.checkbox;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

public class CheckboxControlTest {
  private CheckboxControl checkBoxControl;
  private Screen screenMock;
  private Element elementMock;
  private Element selectImageMock;
  private FocusHandler focusHandlerMock;

  @Before
  public void setup() {
    selectImageMock = createMock(Element.class);
    selectImageMock.showWithoutEffects();
    screenMock = createMock(Screen.class);
    elementMock = createMock(Element.class);
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    focusHandlerMock = createMock(FocusHandler.class);
    checkBoxControl = new CheckboxControl();
  }

  @After
  public void after() {
    verify(screenMock);
    verify(selectImageMock);
    verify(elementMock);
    verify(focusHandlerMock);
  }

  @Test
  public void testDefaultChecked() {
    replayAllMocks();

    bindCheckBoxControl();
    assertTrue(checkBoxControl.isChecked());
  }

  @Test
  public void testOnStartScreen() {
    expect(screenMock.getFocusHandler()).andReturn(focusHandlerMock);
    replay(screenMock);
    replay(elementMock);
    replay(selectImageMock);
    replay(focusHandlerMock);

    bindCheckBoxControl();
    checkBoxControl.onStartScreen();
  }


  @Test
  public void testInputEventNext() {
    expect(screenMock.getFocusHandler()).andReturn(focusHandlerMock);
    replay(screenMock);

    expect(focusHandlerMock.getNext(elementMock)).andReturn(elementMock);
    replay(focusHandlerMock);

    elementMock.setFocus();
    replay(elementMock);
    replay(selectImageMock);

    bindCheckBoxControl();
    checkBoxControl.onStartScreen();
    checkBoxControl.inputEvent(NiftyInputEvent.NextInputElement);
  }

  @Test
  public void testInputEventPrev() {
    expect(screenMock.getFocusHandler()).andReturn(focusHandlerMock);
    replay(screenMock);

    expect(focusHandlerMock.getPrev(elementMock)).andReturn(elementMock);
    replay(focusHandlerMock);

    elementMock.setFocus();
    replay(elementMock);
    replay(selectImageMock);

    bindCheckBoxControl();
    checkBoxControl.onStartScreen();
    checkBoxControl.inputEvent(NiftyInputEvent.PrevInputElement);
  }

  @Test
  public void testInputEventActivate() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.hide();
    replay(selectImageMock);
    replay(screenMock);
    replay(focusHandlerMock);

    bindCheckBoxControl();
    checkBoxControl.inputEvent(NiftyInputEvent.Activate);
    assertFalse(checkBoxControl.isChecked());
  }

  @Test
  public void testOnClick() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.hide();
    replay(selectImageMock);
    replay(screenMock);
    replay(focusHandlerMock);

    bindCheckBoxControl();
    checkBoxControl.onClick();
    assertFalse(checkBoxControl.isChecked());
  }

  @Test
  public void testOnClickTwice() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock).times(2);
    replay(elementMock);

    selectImageMock.hide();
    selectImageMock.show();
    replay(selectImageMock);
    replay(screenMock);
    replay(focusHandlerMock);

    bindCheckBoxControl();
    checkBoxControl.onClick();
    assertFalse(checkBoxControl.isChecked());

    checkBoxControl.onClick();
    assertTrue(checkBoxControl.isChecked());
  }

  @Test
  public void testCheck() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.show();
    replay(selectImageMock);
    replay(screenMock);
    replay(focusHandlerMock);

    bindCheckBoxControl();
    checkBoxControl.check();
    assertTrue(checkBoxControl.isChecked());
  }

  @Test
  public void testUncheck() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.hide();
    replay(selectImageMock);
    replay(screenMock);
    replay(focusHandlerMock);

    bindCheckBoxControl();
    checkBoxControl.uncheck();
    assertFalse(checkBoxControl.isChecked());
  }

  private void replayAllMocks() {
    replay(elementMock);
    replay(selectImageMock);
    replay(screenMock);
    replay(focusHandlerMock);
  }

  private void bindCheckBoxControl() {
    checkBoxControl.bind(null, screenMock, elementMock, new Properties(), null, null);
  }
}

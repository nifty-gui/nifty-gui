package de.lessvoid.nifty.controls.checkbox;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
  private FocusHandler focusHandler;

  @Before
  public void setup() {
    screenMock = createMock(Screen.class);
    elementMock = createMock(Element.class);
    selectImageMock = createMock(Element.class);
    focusHandler = createMock(FocusHandler.class);
    checkBoxControl = new CheckboxControl();
    checkBoxControl.bind(null, screenMock, elementMock, null, null, null);
  }

  @Test
  public void testDefaultChecked() {
    assertTrue(checkBoxControl.isChecked());
  }

  @Test
  public void testOnStartScreen() {
    expect(screenMock.getFocusHandler()).andReturn(focusHandler);
    replay(screenMock);

    checkBoxControl.onStartScreen();

    verify(screenMock);
  }


  @Test
  public void testInputEventNext() {
    expect(screenMock.getFocusHandler()).andReturn(focusHandler);
    replay(screenMock);

    expect(focusHandler.getNext(elementMock)).andReturn(elementMock);
    replay(focusHandler);

    checkBoxControl.onStartScreen();
    checkBoxControl.inputEvent(NiftyInputEvent.NextInputElement);

    verify(screenMock);
    verify(focusHandler);
  }

  @Test
  public void testInputEventPrev() {
    expect(screenMock.getFocusHandler()).andReturn(focusHandler);
    replay(screenMock);

    expect(focusHandler.getPrev(elementMock)).andReturn(elementMock);
    replay(focusHandler);

    checkBoxControl.onStartScreen();
    checkBoxControl.inputEvent(NiftyInputEvent.PrevInputElement);

    verify(screenMock);
    verify(focusHandler);
  }

  @Test
  public void testInputEventActivate() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.hide();
    replay(selectImageMock);

    checkBoxControl.inputEvent(NiftyInputEvent.Activate);
    assertFalse(checkBoxControl.isChecked());

    verify(elementMock);
    verify(selectImageMock);
  }

  @Test
  public void testOnClick() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.hide();
    replay(selectImageMock);

    checkBoxControl.onClick();
    assertFalse(checkBoxControl.isChecked());

    verify(elementMock);
    verify(selectImageMock);
  }

  @Test
  public void testOnClickTwice() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock).times(2);
    replay(elementMock);

    selectImageMock.hide();
    selectImageMock.show();
    replay(selectImageMock);

    checkBoxControl.onClick();
    assertFalse(checkBoxControl.isChecked());

    checkBoxControl.onClick();
    assertTrue(checkBoxControl.isChecked());

    verify(elementMock);
    verify(selectImageMock);
  }

  @Test
  public void testCheck() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.show();
    replay(selectImageMock);

    checkBoxControl.check();
    assertTrue(checkBoxControl.isChecked());

    verify(elementMock);
    verify(selectImageMock);
  }

  @Test
  public void testUncheck() {
    expect(elementMock.findElementByName("select")).andReturn(selectImageMock);
    replay(elementMock);

    selectImageMock.hide();
    replay(selectImageMock);

    checkBoxControl.uncheck();
    assertFalse(checkBoxControl.isChecked());

    verify(elementMock);
    verify(selectImageMock);
  }

}

package de.lessvoid.nifty.controls.checkbox.controller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.checkbox.CheckboxControl;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

public class CreateCheckBoxControlTest {
  private Nifty niftyMock;
  private Screen screenMock;
  private Element parentMock;
  private Element checkBoxElementMock;
  private CheckboxControl checkBoxControlMock;
  private ControlType controlTypeMock;

  @Before
  public void setup() {
    niftyMock = createMock(Nifty.class);
    screenMock = createMock(Screen.class);
    parentMock = createMock(Element.class);
    checkBoxElementMock = createMock(Element.class);
    checkBoxControlMock = createMock(CheckboxControl.class);
    controlTypeMock = createMock(ControlType.class);
  }

  @Test
  public void testCreateWithId() {
    replay(screenMock, controlTypeMock, checkBoxControlMock);

    expect(niftyMock.createElementFromType(screenMock, parentMock, controlTypeMock)).andReturn(checkBoxElementMock);
    replay(niftyMock);

    expect(parentMock.getNifty()).andReturn(niftyMock);
    expect(parentMock.getScreen()).andReturn(screenMock);
    parentMock.layoutElements();
    expectLastCall();
    replay(parentMock);

    expect(checkBoxElementMock.getNiftyControl(CheckBox.class)).andReturn(checkBoxControlMock);
    replay(checkBoxElementMock);

    CheckboxBuilder checkboxBuilder = new CheckboxBuilder("0815") {
      @Override
      public ElementType buildElementType() {
        return controlTypeMock;
      }
    };

    CheckBox checkBoxControl = checkboxBuilder.build(niftyMock, screenMock, parentMock)
        .getNiftyControl(CheckBox.class);
    assertEquals(checkBoxControlMock, checkBoxControl);

    verify(niftyMock);
    verify(parentMock);
    verify(checkBoxElementMock);
    verify(checkBoxControlMock);
  }
}

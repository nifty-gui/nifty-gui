package de.lessvoid.nifty.controls.checkbox.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.checkbox.CheckboxControl;
import de.lessvoid.nifty.controls.checkbox.builder.CreateCheckBoxControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class CreateCheckBoxControlTest {
  private Nifty niftyMock;
  private Screen screenMock;
  private Element parentMock;
  private StandardControl standardControlMock;
  private CheckboxControl checkBoxControlMock;

  @Before
  public void setup() {
    niftyMock = createMock(Nifty.class);
    screenMock = createMock(Screen.class);
    parentMock = createMock(Element.class);
    standardControlMock = createMock(StandardControl.class);
    checkBoxControlMock = createMock(CheckboxControl.class);
  }

  @Test
  public void testCreateWithId() {
    replay(screenMock);
    replay(checkBoxControlMock);
    replay(standardControlMock);

    niftyMock.addControl(screenMock, parentMock, standardControlMock);
    niftyMock.addControlsWithoutStartScreen();
    replay(niftyMock);

    expect(parentMock.findNiftyControl("0815", CheckBox.class)).andReturn(checkBoxControlMock);
    replay(parentMock);

    CreateCheckBoxControl createCheckBoxControl = new CreateCheckBoxControl("0815") {
      @Override
      protected StandardControl getStandardControl() {
        return standardControlMock;
      }
    };

    CheckBox checkBoxControl = createCheckBoxControl.create(niftyMock, screenMock, parentMock);
    assertEquals(checkBoxControlMock, checkBoxControl);

    verify(niftyMock);
    verify(parentMock);
    verify(checkBoxControlMock);
    verify(standardControlMock);
  }
}

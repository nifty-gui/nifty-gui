package de.lessvoid.nifty.controls.textfield;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Clipboard;

public class TextFieldChangeEventClipboardTest {
  private TextFieldLogic textField;
  private Clipboard clipboard;
  private TextFieldView view;

  @Before
  public void before() {
    clipboard = createMock(Clipboard.class);
    view = createMock(TextFieldView.class);
    textField = new TextFieldLogic("12345", clipboard, view);
  }

  @After
  public void after() {
    verify(clipboard);
    verify(view);
  }

  @Test
  public void testCutWithoutSelection() {
    replay(clipboard);
    replay(view);

    textField.cut();
  }

  @Test
  public void testCutWithSelection() {
    clipboard.put("1");
    replay(clipboard);

    view.textChangeEvent("2345");
    replay(view);

    textField.startSelecting();
    textField.setCursorPosition(1);
    textField.endSelecting();
    textField.cut();
  }
}

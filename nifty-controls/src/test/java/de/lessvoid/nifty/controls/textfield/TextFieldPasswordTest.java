package de.lessvoid.nifty.controls.textfield;

import junit.framework.TestCase;

import de.lessvoid.nifty.Clipboard;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

public class TextFieldPasswordTest extends TestCase {
  private static final int MAX_CURSOR_POSITION = 5;
  private TextFieldLogic textField;
  private Clipboard clipboard;

  @Override
  public void setUp() {
    clipboard = createMock(Clipboard.class);

    textField = new TextFieldLogic("hello", clipboard, new EmptyTextFieldView());
    textField.setCursorPosition(0);
    textField.startSelecting();
    textField.setCursorPosition(MAX_CURSOR_POSITION);
  }

  public void testCutNormal() {
    expectPut("hello");
    textField.setFormat(null);
    textField.cut();
  }

  public void testCutPassword() {
    expectPut("*****");
    textField.setFormat(new FormatPassword('*'));
    textField.cut();
  }

  public void testCopyNormal() {
    expectPut("hello");
    textField.setFormat(null);
    textField.copy();
  }

  public void testCopyPassword() {
    expectPut("*****");
    textField.setFormat(new FormatPassword('*'));
    textField.copy();
  }

  public void testModifyWithPasswordMethodWithNull() {
    textField.setFormat(null);
    assertEquals("hello", textField.getDisplayedText().toString());
  }

  public void testModifyWithPasswordMethodWithChar() {
    textField.setFormat(new FormatPassword('*'));
    assertEquals("*****", textField.getDisplayedText().toString());
  }

  private void expectPut(final String value) {
    clipboard.put(value);
    textField.setFormat(null);
    replay(clipboard);
  }
}

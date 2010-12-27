package de.lessvoid.nifty.controls.textfield.controller;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import junit.framework.TestCase;

public class TextFieldPasswordTest extends TestCase {
  private static final int MAX_CURSOR_POSITION = 5;
  private TextFieldLogic textField;
  private Clipboard clipboard;

  public void setUp() {
    clipboard = createMock(Clipboard.class);

    textField = new TextFieldLogic("hello", clipboard);
    textField.setCursorPosition(0);
    textField.startSelecting();
    textField.setCursorPosition(MAX_CURSOR_POSITION);
  }

  public void testCutNormal() {
    expectPut("hello");
    textField.cut(null);
  }

  public void testCutPassword() {
    expectPut("*****");
    textField.cut('*');
  }

  public void testCopyNormal() {
    expectPut("hello");
    textField.copy(null);
  }

  public void testCopyPassword() {
    expectPut("*****");
    textField.copy('*');
  }

  public void testModifyWithPasswordMethodWithNull() {
    assertEquals("hello", textField.modifyWithPasswordChar("hello", null));
  }

  public void testModifyWithPasswordMethodWithChar() {
    assertEquals("*****", textField.modifyWithPasswordChar("hello", '*'));
  }

  public void testModifyWithPasswordMethodWithCharAndNull() {
    assertNull(textField.modifyWithPasswordChar(null, '*'));
  }

  private void expectPut(final String value) {
    clipboard.put(value);
    replay(clipboard);
  }
}

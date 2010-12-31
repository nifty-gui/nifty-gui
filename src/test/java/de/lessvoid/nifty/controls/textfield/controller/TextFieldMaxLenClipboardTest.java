package de.lessvoid.nifty.controls.textfield.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextFieldMaxLenClipboardTest {

  private static final int MAX_LEN = 5;
  private TextFieldLogic textField;
  private Clipboard clipboard;

  @Before
  public void before() {
    clipboard = createMock(Clipboard.class);
    textField = new TextFieldLogic("12345", clipboard, new EmptyTextFieldView());
    textField.setMaxLength(MAX_LEN);
  }

  @After
  public void after() {
    verify(clipboard);
  }

  @Test
  public void testInsertWithPutMaxLength() {
    expect(clipboard.get()).andReturn("abcdef");
    replay(clipboard);

    textField.put();
    assertEquals("12345", textField.getText());
  }

  @Test
  public void testMaxLengthCopyWithoutMaxLen() {
    expect(clipboard.get()).andReturn("abcdef");
    replay(clipboard);

    textField.setMaxLength(-1);
    textField.put();
    assertEquals("abcdef12345", textField.getText());
  }
}

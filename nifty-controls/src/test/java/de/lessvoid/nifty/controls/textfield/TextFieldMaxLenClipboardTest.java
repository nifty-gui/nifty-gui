package de.lessvoid.nifty.controls.textfield;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Clipboard;
import de.lessvoid.nifty.controls.TextField;

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
    assertEquals("12345", textField.getRealText().toString());
  }

  @Test
  public void testInsertWithPutViolatesMaxLength() {
    expect(clipboard.get()).andReturn("1234567890");
    replay(clipboard);

    textField.setText("");
    textField.put();
    assertEquals("12345", textField.getRealText().toString());
  }

  @Test
  public void testMaxLengthCopyWithoutMaxLen() {
    expect(clipboard.get()).andReturn("abcdef");
    replay(clipboard);

    textField.setMaxLength(TextField.UNLIMITED_LENGTH);
    textField.put();
    assertEquals("abcdef12345", textField.getRealText().toString());
  }

  @Test
  public void testInsertWithNormalNewLine() {
    expect(clipboard.get()).andReturn("abc\ndef");
    replay(clipboard);

    textField = new TextFieldLogic("", clipboard, new EmptyTextFieldView());
    textField.put();
    assertEquals("abcdef", textField.getRealText().toString());
  }

  @Test
  public void testInsertWithWindowsStyleNewLine() {
    expect(clipboard.get()).andReturn("abc\r\ndef");
    replay(clipboard);

    textField = new TextFieldLogic("", clipboard, new EmptyTextFieldView());
    textField.put();
    assertEquals("abcdef", textField.getRealText().toString());
  }
}

package de.lessvoid.nifty.controls.textfield;

import junit.framework.TestCase;

public class TextFieldMaxLenTest extends TestCase {

  private static final int MAX_LEN = 5;
  private TextFieldLogic textField;

  @Override
  public void setUp() {
    textField = new TextFieldLogic("12345", null, new EmptyTextFieldView());
    textField.setMaxLength(MAX_LEN);
  }

  public void testInsertWithMaxLength() {
    textField.insert('A');
    assertEquals("12345", textField.getRealText().toString());
  }

  public void testMaxLengthWithDataEntered() {
    textField.setMaxLength(-1);
    textField.setTextAndNotify("abcdef");
    assertEquals("abcdef", textField.getRealText().toString());

    textField.setMaxLength(1);
    assertEquals("a", textField.getRealText().toString());
  }

  public void testMaxLengthWithSameLength() {
    textField.setMaxLength(-1);
    textField.setTextAndNotify("abcde");
    assertEquals("abcde", textField.getRealText().toString());

    textField.setMaxLength(MAX_LEN);
    assertEquals("abcde", textField.getRealText().toString());
  }

  public void testMaxLengthWithEmptyData() {
    textField.setMaxLength(-1);
    textField.setTextAndNotify("");
    textField.setMaxLength(1);
    assertEquals("", textField.getRealText().toString());
  }

  public void testMakingMaxLengthShorter() {
    textField.setTextAndNotify("123456");
    textField.setMaxLength(2);
    assertEquals("12", textField.getRealText().toString());
  }
}

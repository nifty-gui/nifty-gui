package de.lessvoid.nifty.controls.textfield.controller;

import junit.framework.TestCase;

public class TextFieldMaxLenTest extends TestCase {

  private static final int MAX_LEN = 5;
  private TextFieldLogic textField;

  public void setUp() {
    textField = new TextFieldLogic("12345", null, new EmptyTextFieldView());
    textField.setMaxLength(MAX_LEN);
  }

  public void testInsertWithMaxLength() {
    textField.insert('A');
    assertEquals("12345", textField.getText());
  }

  public void testMaxLengthWithDataEntered() {
    textField.setMaxLength(-1);
    textField.initWithText("abcdef");
    assertEquals("abcdef", textField.getText());

    textField.setMaxLength(1);
    assertEquals("a", textField.getText());
  }

  public void testMaxLengthWithSameLength() {
    textField.setMaxLength(-1);
    textField.initWithText("abcde");
    assertEquals("abcde", textField.getText());

    textField.setMaxLength(MAX_LEN);
    assertEquals("abcde", textField.getText());
  }

  public void testMaxLengthWithEmptyData() {
    textField.setMaxLength(-1);
    textField.initWithText("");
    textField.setMaxLength(1);
    assertEquals("", textField.getText());
  }
}

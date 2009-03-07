package de.lessvoid.nifty.controls.textfield.controller;

import junit.framework.TestCase;



public class TextFieldMaxLenTest extends TestCase {

  private static final int MAX_LEN = 5;
  private TextField textField;
  private ClipboardAWT clipboard;

  public void setUp() {
    clipboard = new ClipboardAWT();
    textField = new TextField("12345", clipboard);
    textField.setMaxLength(MAX_LEN);
  }

  public void testInsertWithMaxLength() {
    textField.insert('A');
    assertEquals("12345", textField.getText());
  }

  public void testInsertWithPutMaxLength() {
    clipboard.put("abcdef");
    textField.put();
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

  public void testMaxLengthCopyWithoutMaxLen() {
    textField.setMaxLength(-1);
    clipboard.put("abcdef");
    textField.put();
    assertEquals("abcdef12345", textField.getText());
  }
}

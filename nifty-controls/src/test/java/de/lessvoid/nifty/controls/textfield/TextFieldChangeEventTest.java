package de.lessvoid.nifty.controls.textfield;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;

public class TextFieldChangeEventTest {
  private TextFieldLogic textField;
  private TextFieldView view;

  @Before
  public void before() {
    view = createMock(TextFieldView.class);
    textField = new TextFieldLogic("12345", null, view);
  }

  @After
  public void after() {
    verify(view);
  }

  @Test
  public void testInitWithTextNull() {
    replay(view);

    textField.setTextAndNotify(null);
  }

  @Test
  public void testInitWithTextEmpty() {
    replay(view);

    textField.setTextAndNotify("");
  }

  @Test
  public void testInitWithText() {
    view.textChangeEvent("new text");
    replay(view);

    textField.setTextAndNotify("new text");
  }

  @Test
  public void testDeleteNotify() {
    view.textChangeEvent("2345");
    replay(view);

    textField.delete();
  }

  @Test
  public void testDeleteNotifyNoChange() {
    replay(view);

    textField.setCursorPosition(5);
    textField.delete();
  }

  @Test
  public void testDeleteNotifyWithSelection() {
    view.textChangeEvent("2345");
    replay(view);

    textField.startSelecting();
    textField.setCursorPosition(1);
    textField.endSelecting();
    textField.delete();
  }

  @Test
  public void testBackspaceNotifyNoChange() {
    replay(view);

    textField.backspace();
  }

  @Test
  public void testBackspaceNotify() {
    view.textChangeEvent("2345");
    replay(view);

    textField.setCursorPosition(1);
    textField.backspace();
  }

  @Test
  public void testInsert() {
    view.textChangeEvent("a12345");
    replay(view);

    textField.insert('a');
  }

  @Test
  public void testInsertWithSelection() {
    view.textChangeEvent("a2345");
    replay(view);

    textField.startSelecting();
    textField.setCursorPosition(1);
    textField.endSelecting();
    textField.insert('a');
  }

  @Test
  public void testInsertNoChange() {
    replay(view);

    textField.setMaxLength(5);
    textField.insert('a');
  }

  @Test
  public void testSetMaxLengthNoChange() {
    replay(view);

    textField.setMaxLength(5);
  }

  @Test
  public void testSetMaxLength() {
    view.textChangeEvent("1");
    replay(view);

    textField.setMaxLength(1);
  }
}

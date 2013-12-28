package de.lessvoid.nifty.loaderv2.types;

import org.junit.Test;

import static de.lessvoid.nifty.loaderv2.types.ControlType.getRealChildrenId;
import static org.junit.Assert.assertEquals;

public class ControlTypeTest {
  @Test
  public void idOverlapTest() {
    assertEquals("#bar", getRealChildrenId("foo", "#bar"));
    assertEquals("#bar", getRealChildrenId("foo#foo", "#bar"));
    assertEquals("#foo#bar", getRealChildrenId("foo", "#foo#bar"));
    assertEquals("#bar", getRealChildrenId("foo#foo", "#foo#bar"));

    assertEquals("#bar", getRealChildrenId("#foo", "#bar"));
    assertEquals("#bar", getRealChildrenId("#foo#foo", "#bar"));
    assertEquals("#bar", getRealChildrenId("#foo", "#foo#bar"));
    assertEquals("#bar", getRealChildrenId("#foo#foo", "#foo#bar"));

    assertEquals("#bar", getRealChildrenId("foo#foo2", "#bar"));
    assertEquals("#foo#bar", getRealChildrenId("foo#foo2", "#foo#bar"));
    assertEquals("#bar", getRealChildrenId("foo#foo2#foo", "#foo#bar"));

    assertEquals("#foo#bar", getRealChildrenId("foo#foo2#foo#bar", "#foo#bar"));
    assertEquals("#foo#bar", getRealChildrenId("foo#foo2#foo#bar", "#foo#bar#foo#bar"));
  }
}

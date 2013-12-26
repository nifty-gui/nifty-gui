package de.lessvoid.xml.tools;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class SplitTest {
  @Test
  public void testNull() {
    List<String> result = Split.split(null);
    assertEquals(0, result.size());
  }

  @Test
  public void testNoSplit() {
    List<String> result = Split.split("hello");
    assertEquals(1, result.size());
    assertEquals("hello", result.get(0));
  }

  @Test
  public void testCompleteSplit() {
    List<String> result = Split.split("${hello}");
    assertEquals(1, result.size());
    assertEquals("${hello}", result.get(0));
  }

  @Test
  public void testDoubleCompleteSplit() {
    List<String> result = Split.split("${hello}${hello}");
    assertEquals(2, result.size());
    assertEquals("${hello}", result.get(0));
    assertEquals("${hello}", result.get(1));
  }

  @Test
  public void testOneSplitAtEnd() {
    List<String> result = Split.split("huhu${hello}");
    assertEquals(2, result.size());
    assertEquals("huhu", result.get(0));
    assertEquals("${hello}", result.get(1));
  }

  @Test
  public void testOneSplitAtStart() {
    List<String> result = Split.split("${hello}huhu");
    assertEquals(2, result.size());
    assertEquals("${hello}", result.get(0));
    assertEquals("huhu", result.get(1));
  }

  @Test
  public void testOneSplitInMiddle() {
    List<String> result = Split.split("abc${hello}def");
    assertEquals(3, result.size());
    assertEquals("abc", result.get(0));
    assertEquals("${hello}", result.get(1));
    assertEquals("def", result.get(2));
  }

  @Test
  public void testMultipleSplits() {
    List<String> result = Split.split("a${hello}${hella}d");
    assertEquals(4, result.size());
    assertEquals("a", result.get(0));
    assertEquals("${hello}", result.get(1));
    assertEquals("${hella}", result.get(2));
    assertEquals("d", result.get(3));
  }

  @Test
  public void testMultipleSplitsWithMiddel() {
    List<String> result = Split.split("a${hello}b${hello}c");
    assertEquals(5, result.size());
    assertEquals("a", result.get(0));
    assertEquals("${hello}", result.get(1));
    assertEquals("b", result.get(2));
    assertEquals("${hello}", result.get(3));
    assertEquals("c", result.get(4));
  }

}

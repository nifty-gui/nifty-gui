/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.internal.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import de.lessvoid.nifty.api.annotation.NiftyCssProperty;

@SuppressWarnings("unused")
public class NiftyCssClassInfoTest {

  @Test
  public void testGetPropertiesWithNiftyCssPropertyAtGetter() throws Exception {
    Object object = new Object() {
      private String value = "42";
      @NiftyCssProperty(name = "the-value") public String getValue() { return value; }
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());

    Map<String, String> map = classCache.getProperties(object);
    assertEquals(1, map.size());
    assertEquals("42", map.get("the-value"));
  }

  @Test
  public void testGetPropertiesWithNiftyCssPropertyAtSetter() throws Exception {
    Object object = new Object() {
      private String value = "42";
      public String getValue() { return value; }
      @NiftyCssProperty(name = "the-value") public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());

    Map<String, String> map = classCache.getProperties(object);
    assertEquals(1, map.size());
    assertEquals("42", map.get("the-value"));
  }

  @Test
  public void testGetPropertiesWithNiftyCssPropertyAtBothMethods() throws Exception {
    Object object = new Object() {
      private String value = "42";
      @NiftyCssProperty(name = "the-value") public String getValue() { return value; }
      @NiftyCssProperty(name = "the-value") public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());

    Map<String, String> map = classCache.getProperties(object);
    assertEquals(1, map.size());
    assertEquals("42", map.get("the-value"));
  }

  @Test
  public void testGetPropertiesNameMismatch() throws Exception {
    Object object = new Object() {
      private String value = "42";
      @NiftyCssProperty(name = "the-value-1") public String getValue() { return value; }
      @NiftyCssProperty(name = "the-value-2") public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertTrue(classCache.getProperties(object).isEmpty());
  }

  @Test
  public void testGetPropertiesNoSetter() throws Exception {
    Object object = new Object() {
      private String value = "42";
      @NiftyCssProperty(name = "the-value") public String getValue() { return value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertTrue(classCache.getProperties(object).isEmpty());
  }

  @Test
  public void testGetPropertiesNoGetter() throws Exception {
    Object object = new Object() {
      private String value = "42";
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertTrue(classCache.getProperties(object).isEmpty());
  }

  @Test
  public void testGetPropertiesNoNiftyCssPropertyAnnotation() throws Exception {
    Object object = new Object() {
      private String value = "42";
      public String getValue() { return value; }
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertTrue(classCache.getProperties(object).isEmpty());
  }

  @Test
  public void testReadValue() throws Exception {
    Object object = new Object() {
      private String value = "42";
      @NiftyCssProperty(name = "the-value") public String getValue() { return value; }
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertEquals("42", classCache.readValue("the-value", object));
  }

  @Test
  public void testReadValueThatIsNull() throws Exception {
    Object object = new Object() {
      private String value = null;
      @NiftyCssProperty(name = "the-value") public String getValue() { return value; }
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertNull(classCache.readValue("the-value", object));
  }

  @Test
  public void testReadValueUnknownName() throws Exception {
    Object object = new Object() {
      private String value = null;
      @NiftyCssProperty(name = "the-value") public String getValue() { return value; }
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    try {
      classCache.readValue("some-value", object);
      fail("expected exception");
    } catch (Exception e) {
      assertEquals("The class {class de.lessvoid.nifty.internal.css.NiftyCssClassInfoTest$10} doesn't seem to have a css property with the given name {some-value}", e.getMessage());
    }
  }

  @Test
  public void testReadValueWithException() throws Exception {
    Object object = new Object() {
      private String value = null;
      @NiftyCssProperty(name = "the-value") public String getValue() { throw new RuntimeException("some error"); }
      public void setValue(final String value) { this.value = value; }
    };

    NiftyCssClassInfo classCache = new NiftyCssClassInfo(object.getClass());
    assertTrue(classCache.getProperties(object).isEmpty());
  }
}

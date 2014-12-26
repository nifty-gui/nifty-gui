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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.annotation.NiftyCssProperty;
import de.lessvoid.nifty.api.annotation.NiftyCssStringConverter;

/**
 * This class stores all properties of a class that are annotated with @NiftyCssProperty for later use. The idea is
 * to let this class process a class once and then reuse that information later.
 *
 * @author void
 */
public class NiftyCssClassInfo {
  private final static Logger log = Logger.getLogger(NiftyCssClassInfo.class.getName());
  private final Class<?> clazz;
  private final Map<String, Accessor> map = new HashMap<String, Accessor>();

  public NiftyCssClassInfo(final Class<?> clazz) throws Exception {
    this.clazz = clazz;

    PropertyDescriptor[] properties = getBeanInfo(clazz).getPropertyDescriptors();
    for (int i=0; i<properties.length; i++) {
      PropertyDescriptor descriptor = properties[i];

      NiftyCssProperty cssProperty = getNiftyCssProperty(descriptor);
      if (cssProperty == null) {
        continue;
      }

      map.put(cssProperty.name(), new Accessor(cssProperty, descriptor.getReadMethod(), descriptor.getWriteMethod()));
    }
  }

  public Map<String, String> getProperties(final Object obj) {
    Map<String, String> result = new HashMap<String, String>();
    for (Map.Entry<String, Accessor> entry : map.entrySet()) {
      try {
        result.put(entry.getKey(), entry.getValue().readValue(obj));
      } catch (Throwable e) {
        log.log(Level.WARNING, "Error accessing {" + entry.getKey() + "} for Object {" + obj + "} value", e);
      }
    }
    return result;
  }

  public String readValue(final String key, final Object o) throws Exception {
    Accessor accessor = map.get(key);
    if (accessor == null) {
      throw new Exception("The class {" + clazz + "} doesn't seem to have a css property with the given name {" + key + "}");
    }
    return accessor.readValue(o);
  }

  public void writeValue(final Object o, final String key, final String value) throws Exception {
    Accessor accessor = map.get(key);
    if (accessor == null) {
      throw new Exception("The class {" + clazz + "} doesn't seem to have a css property with the given name {" + key + "}");
    }
    accessor.writeValue(o, value);
  }

  private NiftyCssProperty getNiftyCssProperty(final PropertyDescriptor property) {
    // we now allow read only properties but this means the read method is mandatory
    Method readMethod = property.getReadMethod();
    if (readMethod == null) {
      return null;
    }

    // since we now allow read only properties the write method is optional
    Method writeMethod = property.getWriteMethod();

    // get the annotations
    NiftyCssProperty readProperty = getNiftyCssProperty(readMethod);
    NiftyCssProperty writeProperty = getNiftyCssProperty(writeMethod);
    if (readProperty == null && writeProperty == null) {
      return null;
    }

    if (readProperty != null && writeProperty != null) {
      // names have to match when they are given at both methods - otherwise this is an error!
      if (!readProperty.name().equals(writeProperty.name())) {
        log.log(Level.WARNING, "CSS property names have to match when @NiftyCssProperty is provided for getter (" + readProperty.name() + ") and setter (" + writeProperty.name() + ") - this entry will be ignored!");
        return null;
      }
    }
    return getNonNull(readProperty, writeProperty);
  }

  private NiftyCssProperty getNiftyCssProperty(final Method method) {
    if (method == null) {
      return null;
    }
    return method.getAnnotation(NiftyCssProperty.class);
  }

  private BeanInfo getBeanInfo(final Class<?> clazz) throws IntrospectionException {
    return Introspector.getBeanInfo(clazz, Object.class);
  }

  private NiftyCssProperty getNonNull(final NiftyCssProperty readProperty, final NiftyCssProperty writeProperty) {
    if (readProperty != null) {
      return readProperty;
    }
    return writeProperty;
  }

  private static class Accessor {
    private final NiftyCssProperty cssProperty;
    private final Method read;
    private final Method write;

    public Accessor(final NiftyCssProperty cssProperty, final Method read, final Method write) {
      this.cssProperty = cssProperty;
      this.read = read;
      this.write = write;
    }

    public String readValue(final Object obj) throws Exception {
      NiftyCssStringConverter converter = cssProperty.converter().newInstance();
      Object value = read.invoke(obj);
      if (value == null) {
        return null;
      }
      return converter.toString(value);
    }

    public void writeValue(final Object obj, final String value) throws Exception {
      if (write == null) {
        throw new Exception("trying to write a read-only property with the name {" + cssProperty.name() + "} and value {" + value + "} on object {" + obj + "} ignored");
      }
      NiftyCssStringConverter<?> converter = cssProperty.converter().newInstance();
      write.invoke(obj, converter.fromString(value));
    }
  }
}

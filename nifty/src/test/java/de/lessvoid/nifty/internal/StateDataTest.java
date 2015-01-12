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
package de.lessvoid.nifty.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StateDataTest {
  private StateData<DemoObject> stateData = new StateData<DemoObject>();
  private DemoObject target;

  @Before
  public void before() {
    target = createMock(DemoObject.class);
  }

  @After
  public void after() {
    verify(target);
  }

  @Test
  public void testApplyWithEmptyData() {
    replay(target);

    stateData.apply(target);
  }

  @Test
  public void testSetWithOneSetter() {
    target.setValue("value-1");
    replay(target);

    stateData.set("value-1", new StateSetter<DemoObject, String>() {
      @Override
      public void set(final DemoObject target, final String value) {
        target.setValue(value);
      }
    });
    stateData.apply(target);
  }

  @Test
  public void testWithTwoSetters() {
    target.setValue("value-1");
    target.setValue("value-2");
    replay(target);

    stateData.set("value-1", new StateSetter<DemoObject, String>() {
      @Override
      public void set(final DemoObject target, final String value) {
        target.setValue(value);
      }
    });
    stateData.set("value-2", new StateSetter<DemoObject, String>() {
      @Override
      public void set(final DemoObject target, final String value) {
        target.setValue(value);
      }
    });

    stateData.apply(target);
  }

  @Test
  public void testWithTwoSettersOverwritting() {
    target.setValue("value-1");
    replay(target);

    StateSetter<DemoObject, String> setter = new StateSetter<DemoObject, String>() {
      @Override
      public void set(final DemoObject target, final String value) {
        target.setValue(value);
      }
    };
    stateData.set("value-1", setter);
    stateData.set("value-1", setter);
    stateData.apply(target);
  }

  @Test
  public void testToStringEmpty() {
    replay(target);

    assertEquals("[]", stateData.toString());
  }

  @Test
  public void testToStringWithData() {
    replay(target);

    stateData.set("value-1", new StateSetter<DemoObject, String>() {
      @Override
      public void set(final DemoObject target, final String value) {
        target.setValue(value);
      }
    });
    stateData.set("value-2", new StateSetter<DemoObject, String>() {
      @Override
      public void set(final DemoObject target, final String value) {
        target.setValue(value);
      }
    });

    assertEquals("[value-1, value-2]", stateData.toString());
  }

  public static class DemoObject {
    public void setValue(final String value) {
    }
  }
}

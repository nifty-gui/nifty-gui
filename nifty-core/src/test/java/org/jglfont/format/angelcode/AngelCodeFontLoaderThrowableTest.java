package org.jglfont.format.angelcode;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import org.jglfont.impl.format.angelcode.AngelCodeJGLFontLoader;
import org.jglfont.impl.format.angelcode.AngelCodeLineProcessors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AngelCodeFontLoaderThrowableTest {
  private AngelCodeJGLFontLoader fontLoader;
  private AngelCodeLineProcessors lineProcessorsMock;
  private boolean closeCalled = false;

  @Before
  public void before() {
    closeCalled = false;
    lineProcessorsMock = createMock(AngelCodeLineProcessors.class);
    replay(lineProcessorsMock);
    fontLoader = new AngelCodeJGLFontLoader(lineProcessorsMock);
  }

  @Test(expected=Throwable.class)
  public void testInputStreamReadRuntimeException() throws Exception {
    fontLoader.load(null, null, createInputStreamWithRuntimeException(), null, 0, 0 ,"");
  }

  @Test(expected=Throwable.class)
  public void testInputStreamReadRuntimeExceptionAndCloseException() throws Exception {
    fontLoader.load(null, null, createInputStreamWithRuntimeExceptionAndCloseException(), null, 0, 0 , "");
  }

  @After
  public void after() {
    verify(lineProcessorsMock);
    assertTrue(closeCalled);
  }

  private InputStream createInputStreamWithRuntimeException() throws Exception {
    InputStream in = new ByteArrayInputStream("stuff\n".getBytes("ISO-8859-1")) {
      @Override
      public synchronized int read(byte[] b, int off, int len) {
        throw new IOError(new NullPointerException());
      }      
      @Override
      public void close() throws IOException {
        super.close();
        closeCalled = true;
      }
    };
    return in;
  }

  private InputStream createInputStreamWithRuntimeExceptionAndCloseException() throws Exception {
    InputStream in = new ByteArrayInputStream("stuff\n".getBytes("ISO-8859-1")) {
      @Override
      public synchronized int read(byte[] b, int off, int len) {
        throw new IOError(new NullPointerException());
      }

      @Override
      public void close() throws IOException {
        closeCalled = true;
        throw new IOException();
      }
    };
    return in;
  }
}

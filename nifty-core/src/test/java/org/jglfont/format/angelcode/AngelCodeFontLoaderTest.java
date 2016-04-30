package org.jglfont.format.angelcode;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.easymock.EasyMock;
import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.angelcode.AngelCodeJGLFontLoader;
import org.jglfont.impl.format.angelcode.AngelCodeLine;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;
import org.jglfont.impl.format.angelcode.AngelCodeLineProcessors;
import org.jglfont.spi.JGLFontRenderer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AngelCodeFontLoaderTest {
  private AngelCodeJGLFontLoader fontLoader;
  private AngelCodeLineProcessors lineProcessorsMock;
  private boolean closeCalled = false;
  private boolean lineMockCalled = false;
  private JGLFontRenderer fontRenderer;

  @Before
  public void before() {
    closeCalled = false;
    lineMockCalled = false;
    lineProcessorsMock = createMock(AngelCodeLineProcessors.class);
    fontLoader = new AngelCodeJGLFontLoader(lineProcessorsMock);
    fontRenderer = EasyMock.createMock(JGLFontRenderer.class);
  }

  @Test
  public void testEmptyException() throws Exception {
    replay(lineProcessorsMock);
    try {
      fontLoader.load(fontRenderer, null, null, "somename.fnt", 0, 0 ,"");
      fail("expected exception");
    } catch (IOException e) {
      assertEquals("InputStream is null", e.getMessage());
    }
  }

  @Test
  public void testEmpty() throws Exception {
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStream(new byte[]{}), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
  }

  @Test
  public void testInputStreamCloseFailed() throws Exception {
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStreamWithCloseError(), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
  }

  @Test
  public void testInputStreamReadFailed() throws Exception {
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStreamWithReadError(), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
  }

  @Test
  public void testInputStreamReadAndCloseFailed() throws Exception {
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStreamWithReadAndCloseError(), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
  }

  @Test
  public void testEmptyLine() throws Exception {
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStream("\n".getBytes("ISO-8859-1")), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
  }

  @Test
  public void testSimpleLineSuccess() throws Exception {
    expect(lineProcessorsMock.get("stuff")).andReturn(createLineMock(true));
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStream("stuff\n".getBytes("ISO-8859-1")), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
    assertTrue(lineMockCalled);
  }

  @Test
  public void testSimpleLineFailed() throws Exception {
    expect(lineProcessorsMock.get("stuff")).andReturn(createLineMock(false));
    replay(lineProcessorsMock);
    fontLoader.load(fontRenderer, null, createInputStream("stuff\n".getBytes("ISO-8859-1")), "somename.fnt", 0, 0 ,"");
    assertTrue(closeCalled);
    assertTrue(lineMockCalled);
  }

  @After
  public void after() {
    verify(lineProcessorsMock);
  }

  private AngelCodeLine createLineMock(final boolean result) {
    return new AngelCodeLine() {
      @Override
      public boolean process(AngelCodeLineData line, JGLAbstractFontData font) {
        lineMockCalled = true;
        return result;
      }};
  }

  private InputStream createInputStreamWithCloseError() {
    InputStream in = new ByteArrayInputStream(new byte[] {}) {

      @Override
      public void close() throws IOException {
        closeCalled = true;
        throw new IOException("this call failed");
      }      
    };
    return in;
  }

  private InputStream createInputStreamWithReadError() throws Exception {
    InputStream in = new ByteArrayInputStream("stuff\n".getBytes("ISO-8859-1")) {
      @Override
      public synchronized int read(byte[] b, int off, int len) {
        throw new NullPointerException();
      }      
      @Override
      public void close() throws IOException {
        super.close();
        closeCalled = true;
      }
    };
    return in;
  }

  private InputStream createInputStreamWithReadAndCloseError() throws Exception {
    InputStream in = new ByteArrayInputStream("stuff\n".getBytes("ISO-8859-1")) {
      @Override
      public synchronized int read(byte[] b, int off, int len) {
        throw new NullPointerException();
      }

      @Override
      public void close() throws IOException {
        closeCalled = true;
        throw new IOException("close failed");
      }
    };
    return in;
  }

  private InputStream createInputStream(final byte[] data) {
    InputStream in = new ByteArrayInputStream(data) {

      @Override
      public void close() throws IOException {
        super.close();
        closeCalled = true;
      }      
    };
    return in;
  }
}

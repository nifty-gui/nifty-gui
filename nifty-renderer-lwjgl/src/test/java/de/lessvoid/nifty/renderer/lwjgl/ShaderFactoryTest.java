package de.lessvoid.nifty.renderer.lwjgl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.coregl.CoreFactory;
import de.lessvoid.coregl.CoreShader;

public class ShaderFactoryTest {
  private CoreFactory factoryMock;
  private CoreShader shaderMock;

  @Before
  public void before() {
    shaderMock = createShaderMock();
  }

  @After
  public void after() {
    verify(factoryMock);
    verify(shaderMock);
  }

  @Test
  public void testNewShader() {
    factoryMock = createFactoryMock();

    ShaderFactory shaderFactory = new ShaderFactory(factoryMock);
    assertEquals(shaderMock, shaderFactory.shaderWithVertexAttributes("basic"));
  }

  @Test
  public void testNewShaderWithAttributes() {
    factoryMock = createFactoryMock("abc", "def");

    ShaderFactory shaderFactory = new ShaderFactory(factoryMock);
    assertEquals(shaderMock, shaderFactory.shaderWithVertexAttributes("basic", "abc", "def"));
  }

  @Test
  public void testNewShaderAndReuse() {
    factoryMock = createFactoryMock();

    ShaderFactory shaderFactory = new ShaderFactory(factoryMock);
    assertEquals(shaderMock, shaderFactory.shaderWithVertexAttributes("basic"));
    assertEquals(shaderMock, shaderFactory.shaderWithVertexAttributes("basic"));
  }

  private CoreShader createShaderMock() {
    CoreShader shaderMock = createMock(CoreShader.class);
    expect(shaderMock.vertexShader("basic.vs")).andReturn(1);
    expect(shaderMock.fragmentShader("basic.fs")).andReturn(2);
    shaderMock.link();
    replay(shaderMock);
    return shaderMock;
  }

  private CoreFactory createFactoryMock(final String ... params) {
    CoreFactory factory = createMock(CoreFactory.class);
    expect(factory.newShaderWithVertexAttributes(params)).andReturn(shaderMock);
    replay(factory);
    return factory;
  }
}

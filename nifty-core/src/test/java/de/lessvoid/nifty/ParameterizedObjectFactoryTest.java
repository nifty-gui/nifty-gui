package de.lessvoid.nifty;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParameterizedObjectFactoryTest {
  ParameterizedObjectFactory<DummyParameterizable> m_parameterizableFactory = null;

  @Before
  public void setUp() {
    Map<String, Class<? extends DummyParameterizable>> parameterizableMapping = new HashMap<String,
        Class<? extends DummyParameterizable>>();
    parameterizableMapping.put("dummy", DummyParameterizable.class);
    m_parameterizableFactory = new ParameterizedObjectFactory<DummyParameterizable>(parameterizableMapping, "dummy");
  }

  @Test
  public void testCreateReturnsExpectedClassInstanceWithoutParameters() {
    Parameterizable parameterizable = m_parameterizableFactory.create("dummy");
    assertEquals(DummyParameterizable.class, parameterizable.getClass());
  }

  @Test
  public void testCreateReturnsExpectedClassInstanceWithParameters() {
    Parameterizable parameterizable = m_parameterizableFactory.create("dummy:blabla,2");
    assertEquals(DummyParameterizable.class, parameterizable.getClass());
  }

  @Test
  public void testCreateFallsBackToDefaultDummyInitializableWithNullParameters() {
    Parameterizable parameterizable = m_parameterizableFactory.create(null);
    assertEquals(DummyParameterizable.class, parameterizable.getClass());
  }

  @Test
  public void testCreatedDummyInitializableIsInitialized() {
    final String parameters = "foobar";

    DummyParameterizable parameterizable = (DummyParameterizable) m_parameterizableFactory.create("dummy:"
        + parameters);
    assertEquals(parameters, parameterizable.getParameters());
  }

  @Test
  public void testCreateFallsBackToDefaultDummyInitializableWithInvalidParameters() {
    Parameterizable parameterizable = m_parameterizableFactory.create("dummy:mustFail");
    assertEquals(DummyParameterizable.class, parameterizable.getClass());
  }

  public static class DummyParameterizable implements Parameterizable {

    private String m_parameters;

    public String getParameters() {
      return m_parameters;
    }

    @Override
    public void setParameters(String parameters) {
      m_parameters = parameters;
      if ("mustFail".equals(parameters)) {
        throw new IllegalArgumentException("Not that fake :( !");
      }
    }
  }
}

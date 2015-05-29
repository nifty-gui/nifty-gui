package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.impl.Nop;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Properties;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

public class HoverEffectTest {

  private static final boolean INHERIT_FALSE = false;
  private static final boolean POST_FALSE = false;
  private static final boolean OVERLAY_TRUE = true;
  private static final String ALTERNATE_ENABLE_NULL = null;
  private static final String ALTERNATE_DISABLE_NULL = null;
  private static final String CUSTOM_KEY_NULL = null;
  private static final boolean NEVER_STOP_RENDERING = false;

  Nifty nifty = EasyMock.createMock(Nifty.class);
  Element element = EasyMock.createMock(Element.class);
  Effect effect;

  @Before
  public void setUp() {
    effect = new Effect(
        nifty,
        INHERIT_FALSE,
        POST_FALSE,
        OVERLAY_TRUE,
        ALTERNATE_ENABLE_NULL,
        ALTERNATE_DISABLE_NULL,
        CUSTOM_KEY_NULL,
        NEVER_STOP_RENDERING,
        EffectEventId.onActive,
        element,
        new Nop(),
        new EffectProperties(new Properties()), new AccurateTimeProvider(),
        new LinkedList<Object>());
  }

  @Test
  public void shouldSetVisibleToMouseEventsOnElementWhenHoverEnabled() {
    element.setVisibleToMouseEvents(true);
    expectLastCall();
    replay(element);

    effect.enableHover(new Falloff(new Properties()));
  }
}

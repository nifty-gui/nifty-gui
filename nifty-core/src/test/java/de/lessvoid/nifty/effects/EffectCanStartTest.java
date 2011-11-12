package de.lessvoid.nifty.effects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Properties;

import org.junit.Test;

import de.lessvoid.nifty.effects.impl.Nop;
import de.lessvoid.nifty.tools.TimeProvider;

public class EffectCanStartTest {
  private static final boolean INHERIT_FALSE = false;
  private static final boolean POST_FALSE = false;
  private static final boolean OVERLAY_TRUE = true;
  private static final String ALTERNATE_ENABLE_NULL = null;
  private static final String ALTERNATE_ENABLE = "alternateEnable";
  private static final String ALTERNATE_DISABLE_NULL = null;
  private static final String ALTERNATE_DISABLE = "alternateDisable";
  private static final String CUSTOM_KEY_NULL = null;
  private static final String CUSTOM_KEY = "customKey";
  private static final boolean NEVER_STOP_RENDERING = false;
  private Effect effect;

  @Test
  public void testAllNull() {
    prepare(ALTERNATE_ENABLE_NULL, ALTERNATE_DISABLE_NULL, CUSTOM_KEY_NULL);
    assertTrue(effect.start(null, null));
    assertTrue(effect.start(ALTERNATE_ENABLE, null));
    assertFalse(effect.start(null, CUSTOM_KEY));
    assertFalse(effect.start(ALTERNATE_ENABLE, CUSTOM_KEY));
  }

  @Test
  public void testWithAlternateEnable() {
    prepare(ALTERNATE_ENABLE, ALTERNATE_DISABLE_NULL, CUSTOM_KEY_NULL);
    assertFalse(effect.start(null, null));
    assertTrue(effect.start(ALTERNATE_ENABLE, null));
    assertFalse(effect.start(null, CUSTOM_KEY));
    assertFalse(effect.start(ALTERNATE_ENABLE, CUSTOM_KEY));
  }

  @Test
  public void testWithAlternateDisable() {
    prepare(ALTERNATE_ENABLE_NULL, ALTERNATE_DISABLE, CUSTOM_KEY_NULL);
    assertTrue(effect.start(null, null));
    assertFalse(effect.start(ALTERNATE_DISABLE, null));
    assertFalse(effect.start(null, CUSTOM_KEY));
    assertFalse(effect.start(ALTERNATE_DISABLE, CUSTOM_KEY));
  }

  @Test
  public void testWithCustomKey() {
    prepare(ALTERNATE_ENABLE_NULL, ALTERNATE_DISABLE_NULL, CUSTOM_KEY);
    assertFalse(effect.start(null, null));
    assertFalse(effect.start(ALTERNATE_DISABLE_NULL, null));
    assertTrue(effect.start(null, CUSTOM_KEY));
    assertTrue(effect.start(ALTERNATE_DISABLE_NULL, CUSTOM_KEY));
  }

  private void prepare(final String alternateEnable, final String alternateDisable, final String customKey) {
    effect = new Effect(null, INHERIT_FALSE, POST_FALSE, OVERLAY_TRUE, alternateEnable, alternateDisable, customKey, NEVER_STOP_RENDERING, EffectEventId.onActive);
    effect.init(null, new Nop(), new EffectProperties(new Properties()), new TimeProvider(), new LinkedList<Object>());
  }
}

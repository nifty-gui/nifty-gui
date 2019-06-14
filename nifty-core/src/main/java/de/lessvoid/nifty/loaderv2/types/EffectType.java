package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.LinearInterpolator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class EffectType extends XmlBaseType implements Cloneable {
  private static final Logger logger = Logger.getLogger(EffectType.class.getName());

  private static final boolean DEFAULT_INHERIT = false;
  private static final boolean DEFAULT_POST = false;
  private static final boolean DEFAULT_OVERLAY = false;

  @Nonnull
  protected List<EffectValueType> effectValues;
  @Nullable
  private String styleId;

  public EffectType() {
    effectValues = new ArrayList<EffectValueType>();
  }

  public EffectType(@Nonnull final Attributes attributes) {
    super(attributes);
    effectValues = new ArrayList<EffectValueType>();
  }

  @Nonnull
  @Override
  public EffectType clone() throws CloneNotSupportedException {
    try {
      final EffectType newObject = (EffectType) super.clone();

      newObject.effectValues = new ArrayList<EffectValueType>(effectValues.size());
      for (EffectValueType effectValue : effectValues) {
        newObject.effectValues.add(effectValue.clone());
      }

      return newObject;
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning failed because the clone method created the wrong object.");
    }
  }

  /**
   * This supports creating CustomControlCreator.
   */
  @Nonnull
  public ControlEffectAttributes convert() {
    return new ControlEffectAttributes(getAttributes(), effectValues);
  }

  public void materialize(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Attributes effectsTypeAttributes,
      @Nonnull final List<Object> controllers) {
    Attributes effectAttributes = new Attributes(getAttributes());
    effectAttributes.merge(effectsTypeAttributes);

    RegisterEffectType registerEffectType = getRegisteredEffectType(nifty, effectAttributes);
    if (registerEffectType == null) {
      return;
    }

    Class<?> effectClass = registerEffectType.getEffectClass();
    if (effectClass == null) {
      return;
    }

    EffectProperties effectProperties = new EffectProperties(effectAttributes.createProperties());
    applyEffectValues(effectProperties);

    EffectImpl effectImpl = createEffectImpl(effectClass);
    if (effectImpl != null) {
      Effect effect = new Effect(
          nifty,
          getInherit(effectAttributes),
          getPost(effectAttributes),
          getOverlay(effectAttributes),
          getAlternateEnable(effectAttributes),
          getAlternateDisable(effectAttributes),
          getCustomKey(effectAttributes),
          getNeverStopRendering(effectAttributes),
          effectEventId,
          element,
          effectImpl,
          effectProperties,
          nifty.getTimeProvider(),
          controllers);
      initializeEffect(effect, effectEventId);

      element.registerEffect(effectEventId, effect);
    }
  }

  @Nullable
  private RegisterEffectType getRegisteredEffectType(@Nonnull final Nifty nifty, @Nonnull final Attributes attributes) {
    String name = getEffectName(attributes);
    RegisterEffectType registerEffectType = nifty.resolveRegisteredEffect(name);
    if (registerEffectType == null) {
      logger.warning("unable to convert effect [" + name + "] because no effect with this name has been registered.");
      return null;
    }
    return registerEffectType;
  }

  private boolean getInherit(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("inherit", DEFAULT_INHERIT);
  }

  private boolean getPost(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("post", DEFAULT_POST);
  }

  private boolean getOverlay(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("overlay", DEFAULT_OVERLAY);
  }

  @Nullable
  private String getAlternateEnable(@Nonnull final Attributes attributes) {
    return attributes.get("alternateEnable");
  }

  @Nullable
  private String getAlternateDisable(@Nonnull final Attributes attributes) {
    return attributes.get("alternateDisable");
  }

  @Nullable
  private String getCustomKey(@Nonnull final Attributes attributes) {
    return attributes.get("customKey");
  }

  private boolean getNeverStopRendering(@Nonnull final Attributes attributes) {
    return attributes.getAsBoolean("neverStopRendering", false);
  }

  protected void initializeEffect(@Nonnull final Effect effect, final EffectEventId effectEventId) {
    if (EffectEventId.onFocus.equals(effectEventId) ||
        EffectEventId.onActive.equals(effectEventId) ||
        EffectEventId.onEnabled.equals(effectEventId) ||
        EffectEventId.onDisabled.equals(effectEventId)) {
      effect.enableInfinite();
    }
    if (EffectEventId.onClick.equals(effectEventId)) {
      effect.setVisibleToMouse(true);
    }
  }

  @Nullable
  private EffectImpl createEffectImpl(@Nonnull final Class<?> effectClass) {
    try {
      if (EffectImpl.class.isAssignableFrom(effectClass)) {
        return (EffectImpl) effectClass.newInstance();
      } else {
        logger.warning("given effect class ["
            + effectClass.getName()
            + "] does not implement ["
            + EffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      logger.warning("class [" + effectClass.getName() + "] could not be instanziated");
    }
    return null;
  }

  @Nullable
  private String getEffectName(@Nonnull final Attributes attributes) {
    return attributes.get("name");
  }

  public void resolveParameters(@Nonnull final Attributes src) {
    getAttributes().resolveParameters(src);

    for (EffectValueType e : effectValues) {
      e.getAttributes().resolveParameters(src);
    }
  }

  public void addValue(@Nonnull final EffectValueType elementValueType) {
    effectValues.add(elementValueType);
  }

  public void addValues(@Nonnull final Collection<EffectValueType> effectValueTypes) {
    effectValues.addAll(effectValueTypes);
  }

  void applyEffectValues(@Nonnull final EffectProperties effectProperties) {
    if (!effectValues.isEmpty()) {
      for (EffectValueType effectValueType : effectValues) {
        effectProperties.addEffectValue(effectValueType.getAttributes());
      }
      if (effectProperties.isTimeInterpolator()) {
        LinearInterpolator interpolator = effectProperties.getInterpolator();
        if (interpolator != null) {
          effectProperties.setProperty("length", String.valueOf((long) interpolator.getMaxX()));
        }
      }
    }
  }

  public void setStyleId(@Nullable final String styleId) {
    this.styleId = styleId;
  }

  @Nullable
  public String getStyleId() {
    return styleId;
  }
}

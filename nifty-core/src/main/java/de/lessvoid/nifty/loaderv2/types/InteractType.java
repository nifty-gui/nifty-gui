package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.ElementInteractionClickHandler;
import de.lessvoid.nifty.loaderv2.types.helper.OnClickType;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class InteractType extends XmlBaseType {
  public InteractType() {
    super();
  }

  public InteractType(final InteractType src) {
    super(src);
  }

  public void mergeFromInteractType(final InteractType interact) {
    mergeFromAttributes(interact.getAttributes());
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<interact> " + super.output(offset);
  }

  public void materialize(
      final Nifty nifty,
      final Element element,
      final Object ... controller) {
    materializeMethods(nifty, element, element.getElementInteraction().getPrimary(),
        "onClick", "onClickRepeat", "onRelease", "onClickMouseMove", controller);
    materializeMethods(nifty, element, element.getElementInteraction().getPrimary(),
        "onPrimaryClick", "onPrimaryClickRepeat", "onPrimaryRelease", "onPrimaryClickMouseMove", controller);
    materializeMethods(nifty, element, element.getElementInteraction().getSecondary(),
        "onSecondaryClick", "onSecondaryClickRepeat", "onSecondaryRelease", "onSecondaryClickMouseMove", controller);
    materializeMethods(nifty, element, element.getElementInteraction().getTertiary(),
        "onTertiaryClick", "onTertiaryClickRepeat", "onTertiaryRelease", "onTertiaryClickMouseMove", controller);

    OnClickType onMouseOver = getOnClickType("onMouseOver");
    if (onMouseOver != null) {
      element.setOnMouseOverMethod(onMouseOver.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onMouseWheel = getOnClickType("onMouseWheel");
    if (onMouseWheel != null) {
      element.getElementInteraction().setOnMouseWheelMethod(onMouseWheel.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
    String onClickAlternateKey = getAttributes().get("onClickAlternateKey");
    if (onClickAlternateKey != null) {
      element.setOnClickAlternateKey(onClickAlternateKey);
    }
  }

  private void materializeMethods(
      final Nifty nifty,
      final Element element,
      final ElementInteractionClickHandler handler,
      final String onClickName,
      final String onClickRepeatName,
      final String onReleaseName,
      final String onClickMouseMoveName,
      final Object... controller) {
    OnClickType onClick = getOnClickType(onClickName);
    if (onClick != null) {
      handler.setOnClickMethod(onClick.getMethod(nifty, controller));
      handler.setOnClickRepeatEnabled(false);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickRepeat = getOnClickType(onClickRepeatName);
    if (onClickRepeat != null) {
      handler.setOnClickMethod(onClickRepeat.getMethod(nifty, controller));
      handler.setOnClickRepeatEnabled(true);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickMouseMove = getOnClickType(onClickMouseMoveName);
    if (onClickMouseMove != null) {
      handler.setOnClickMouseMoveMethod(onClickMouseMove.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onRelease = getOnClickType(onReleaseName);
    if (onRelease != null) {
      handler.setOnReleaseMethod(onRelease.getMethod(nifty, controller));
      element.setVisibleToMouseEvents(true);
    }
  }

  private OnClickType getOnClickType(final String key) {
    String onClick = getAttributes().get(key);
    if (onClick == null) {
      return null;
    }
    return new OnClickType(onClick);
  }

  public void apply(final InteractType interact, final String styleId) {
    interact.getAttributes().mergeAndTag(getAttributes(), styleId);
  }

  public void resolveParameters(final Attributes src) {
    getAttributes().resolveParameters(src);
  }
}

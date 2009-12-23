package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.elements.Element;
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
    OnClickType onClick = getOnClickType("onClick");
    if (onClick != null) {
      NiftyMethodInvoker onClickMethod = onClick.getMethod(nifty, controller);
      element.setOnClickMethod(onClickMethod, false);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onRelease = getOnClickType("onRelease");
    if (onRelease != null) {
      NiftyMethodInvoker onReleaseMethod = onRelease.getMethod(nifty, controller);
      element.setOnReleaseMethod(onReleaseMethod);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onMouseOver = getOnClickType("onMouseOver");
    if (onMouseOver != null) {
      NiftyMethodInvoker onClickMethod = onMouseOver.getMethod(nifty, controller);
      element.setOnMouseOverMethod(onClickMethod);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickRepeat = getOnClickType("onClickRepeat");
    if (onClickRepeat != null) {
      NiftyMethodInvoker onClickRepeatMethod = onClickRepeat.getMethod(nifty, controller);
      element.setOnClickMethod(onClickRepeatMethod, true);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickMouseMove = getOnClickType("onClickMouseMove");
    if (onClickMouseMove != null) {
      NiftyMethodInvoker onClickMouseMoveMethod = onClickMouseMove.getMethod(nifty, controller);
      element.setOnClickMouseMoveMethod(onClickMouseMoveMethod);
      element.setVisibleToMouseEvents(true);
    }
    String onClickAlternateKey = getAttributes().get("onClickAlternateKey");
    if (onClickAlternateKey != null) {
      element.setOnClickAlternateKey(onClickAlternateKey);
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

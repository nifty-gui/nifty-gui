package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.helper.OnClickType;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.tools.MethodInvoker;

public class InteractType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<interact> " + super.output(offset);
  }

  public void materialize(
      final Nifty nifty,
      final Element element,
      final Object ... controller) {
    OnClickType onClick = getOnClickType("onClick");
    if (onClick != null) {
      MethodInvoker onClickMethod = onClick.getMethod(controller);
      element.setOnClickMethod(onClickMethod, false);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onRelease = getOnClickType("onRelease");
    if (onRelease != null) {
      MethodInvoker onReleaseMethod = onRelease.getMethod(controller);
      element.setOnReleaseMethod(onReleaseMethod);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onMouseOver = getOnClickType("onMouseOver");
    if (onMouseOver != null) {
      MethodInvoker onClickMethod = onMouseOver.getMethod(controller);
      element.setOnMouseOverMethod(onClickMethod);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickRepeat = getOnClickType("onClickRepeat");
    if (onClickRepeat != null) {
      MethodInvoker onClickRepeatMethod = onClickRepeat.getMethod(controller);
      element.setOnClickMethod(onClickRepeatMethod, true);
      element.setVisibleToMouseEvents(true);
    }
    OnClickType onClickMouseMove = getOnClickType("onClickMouseMove");
    if (onClickMouseMove != null) {
      MethodInvoker onClickMouseMoveMethod = onClickMouseMove.getMethod(controller);
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
}

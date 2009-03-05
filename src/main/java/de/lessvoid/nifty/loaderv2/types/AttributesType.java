package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class AttributesType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<attributes> (" + getAttributes().toString() + ")";
  }

  public void apply(final Attributes result) {
    setIfSet(getAttributes(), result, "height");
    setIfSet(getAttributes(), result, "width");
    setIfSet(getAttributes(), result, "x");
    setIfSet(getAttributes(), result, "y");
    setIfSet(getAttributes(), result, "align");
    setIfSet(getAttributes(), result, "valign");
    setIfSet(getAttributes(), result, "paddingLeft");
    setIfSet(getAttributes(), result, "paddingRight");
    setIfSet(getAttributes(), result, "paddingTop");
    setIfSet(getAttributes(), result, "paddingBottom");
    setIfSet(getAttributes(), result, "childClip");
    setIfSet(getAttributes(), result, "visible");
    setIfSet(getAttributes(), result, "visibleToMouse");
    setIfSet(getAttributes(), result, "childLayout");
    setIfSet(getAttributes(), result, "focusable");
    setIfSet(getAttributes(), result, "font");
    setIfSet(getAttributes(), result, "textHAlign");
    setIfSet(getAttributes(), result, "textVAlign");
    setIfSet(getAttributes(), result, "color");
    setIfSet(getAttributes(), result, "selectionColor");
    setIfSet(getAttributes(), result, "text");
    setIfSet(getAttributes(), result, "backgroundColor");
    setIfSet(getAttributes(), result, "backgroundImage");
    setIfSet(getAttributes(), result, "imageMode");
    setIfSet(getAttributes(), result, "filename");
    setIfSet(getAttributes(), result, "inset");
    setIfSet(getAttributes(), result, "inputController");
    setIfSet(getAttributes(), result, "inputMapping");
  }

  private void setIfSet(final Attributes source, final Attributes dest, final String key) {
    String value = source.get(key);
    if (value != null) {
      if (!dest.isSet(key)) {
        dest.set(key, value);
      }
    }
  }
}

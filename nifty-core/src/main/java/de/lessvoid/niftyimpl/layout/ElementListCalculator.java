package de.lessvoid.niftyimpl.layout;

import java.util.List;

import de.lessvoid.nifty.layout.SizeValue;

public class ElementListCalculator {

  public SizeValue getMaxChildWidth(final List<Layoutable> children) {
    int newWidth = 0;
    for (int i=0; i<children.size(); i++) {
      Layoutable e = children.get(i);
      int partWidth = e.getBoxConstraints().getWidth().getValueAsInt(0);
      partWidth += e.getBoxConstraints().getMarginLeft().getValueAsInt(0);
      partWidth += e.getBoxConstraints().getMarginRight().getValueAsInt(0);
      if (partWidth > newWidth) {
        newWidth = partWidth;
      }
    }
    return SizeValue.px(newWidth);
  }

  public SizeValue getMaxChildHeight(final List<Layoutable> children) {
    int newHeight = 0;
    for (int i=0; i<children.size(); i++) {
      Layoutable e = children.get(i);
      int partHeight = e.getBoxConstraints().getHeight().getValueAsInt(0);
      partHeight += e.getBoxConstraints().getMarginTop().getValueAsInt(0);
      partHeight += e.getBoxConstraints().getMarginBottom().getValueAsInt(0);
      if (partHeight > newHeight) {
        newHeight = partHeight;
      }
    }
    return SizeValue.px(newHeight);
  }

  public SizeValue getTotalChildrenWidth(final List<Layoutable> children) {
    int newWidth = 0;
    for (int i=0; i<children.size(); i++) {
      Layoutable e = children.get(i);
      newWidth += e.getBoxConstraints().getMarginLeft().getValueAsInt(0);
      newWidth += e.getBoxConstraints().getWidth().getValueAsInt(0);
      newWidth += e.getBoxConstraints().getMarginRight().getValueAsInt(0);
    }
    return SizeValue.px(newWidth);
  }

  public SizeValue getTotalChildrenHeight(final List<Layoutable> children) {
    int newHeight = 0;
    for (int i=0; i<children.size(); i++) {
      Layoutable e = children.get(i);
      newHeight += e.getBoxConstraints().getHeight().getValueAsInt(0);
      newHeight += e.getBoxConstraints().getMarginTop().getValueAsInt(0);
      newHeight += e.getBoxConstraints().getMarginBottom().getValueAsInt(0);
    }
    return SizeValue.px(newHeight);
  }
}

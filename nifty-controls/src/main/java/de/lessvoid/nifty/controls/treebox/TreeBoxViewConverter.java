/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TreeItem;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * This is the default converter for a tree view that takes care for displaying the elements properly inside the GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TreeBoxViewConverter<T> implements ListBox.ListBoxViewConverter<TreeItem<T>> {

  @Override
  public void display(final Element element, final TreeItem<T> item) {
    final Element spacer = element.findElementByName("#tree-item-spacer");
    spacer.setConstraintWidth(SizeValue.px(item.getIndent()));
    spacer.setConstraintHeight(SizeValue.px(1));
    spacer.setVisible(item.getIndent() > 0);

    final Element icon = element.findElementByName("#tree-item-icon");
    if (item.isLeaf()) {
      icon.setStyle(element.getStyle() + "#leaf");
    } else if (item.isExpanded()) {
      icon.setStyle(element.getStyle() + "#opened");
    } else {
      icon.setStyle(element.getStyle() + "#closed");
    }

    final Element text = element.findElementByName("#tree-item-content");
    final Label displayLabel = text.findNiftyControl("#label", Label.class);
    if (displayLabel == null) {
      final LabelBuilder builder =  new LabelBuilder(text.getId() + "#label");
      builder.text(item.getValue().toString());
      builder.textHAlign(ElementBuilder.Align.Left);
      builder.width("*");

      builder.build(text.getNifty(), text.getNifty().getCurrentScreen(), text);
    } else {
      displayLabel.setText(item.getValue().toString());
    }

    element.resetLayout();
    element.layoutElements();
  }

    @Override
    public int getWidth(final Element element, final TreeItem<T> item) {
      int width = item.getIndent();
      final Element icon = element.findElementByName("#tree-item-icon");
      final Element text = element.findElementByName("#tree-item-content");
      final Label displayLabel = text.findNiftyControl("#label", Label.class);
      width += icon.getWidth();
      if (displayLabel != null) {
        final RenderFont font = displayLabel.getElement().getRenderer(TextRenderer.class).getFont();
        width += font.getWidth(item.getValue().toString());
      }

      return width;
    }
}

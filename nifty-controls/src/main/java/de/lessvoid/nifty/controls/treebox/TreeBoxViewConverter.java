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
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * This is the default converter for a tree view that takes care for displaying the elements properly inside the GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TreeBoxViewConverter<T> implements ListBox.ListBoxViewConverter<TreeItem<T>> {
  @Nonnull
  private static final Logger log = Logger.getLogger(TreeBoxViewConverter.class.getName());

  @Override
  public void display(@Nonnull final Element element, @Nonnull final TreeItem<T> item) {
    final Element spacer = element.findElementById("#tree-item-spacer");
    if (spacer == null) {
      log.warning("Spacing area used for indenting of the tree was not found. Looked for: #tree-item-spacer");
    } else {
      spacer.setConstraintWidth(SizeValue.px(item.getIndent()));
      spacer.setConstraintHeight(SizeValue.px(1));
      spacer.setVisible(item.getIndent() > 0);
    }

    final Element icon = element.findElementById("#tree-item-icon");
    if (icon == null) {
      log.warning("Icon area for the tree was not found. Looked for: #tree-item-icon");
    } else {
      if (item.isLeaf()) {
        icon.setStyle(element.getStyle() + "#leaf");
      } else if (item.isExpanded()) {
        icon.setStyle(element.getStyle() + "#opened");
      } else {
        icon.setStyle(element.getStyle() + "#closed");
      }
    }

    final Element text = element.findElementById("#tree-item-content");
    if (text == null) {
      log.warning("Content area of tree node couldn't be found. Looked for: #tree-item-content");
    } else {
      final Label displayLabel = text.findNiftyControl("#label", Label.class);
      if (displayLabel == null) {
        Screen screen = text.getNifty().getCurrentScreen();
        if (screen == null) {
          log.warning("Can't create content label while there is no active screen.");
        } else {
          final LabelBuilder builder = new LabelBuilder(text.getId() + "#label");
          builder.text(String.valueOf(item.getValue()));
          builder.textHAlign(ElementBuilder.Align.Left);
          builder.width(SizeValue.wildcard());
          builder.build(text.getNifty(), screen, text);
        }
      } else {
        displayLabel.setText(String.valueOf(item.getValue()));
      }
    }
    element.layoutElements();
  }

  @Override
  public int getWidth(@Nonnull final Element element, @Nonnull final TreeItem<T> item) {
    int width = item.getIndent();
    final Element icon = element.findElementById("#tree-item-icon");
    if (icon != null) {
      width += icon.getWidth();
    }
    final Element text = element.findElementById("#tree-item-content");
    if (text != null) {
      final Label displayLabel = text.findNiftyControl("#label", Label.class);
      if (displayLabel != null) {
        final Element displayLabelElement = displayLabel.getElement();
        if (displayLabelElement != null) {
          final TextRenderer textRenderer = displayLabelElement.getRenderer(TextRenderer.class);
          if (textRenderer != null) {
            final RenderFont font = textRenderer.getFont();
            if (font != null) {
              width += font.getWidth(String.valueOf(item.getValue()));
            }
          }
        }
      }
    }

    return width;
  }
}

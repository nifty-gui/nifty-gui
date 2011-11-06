/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderFont;

/**
 *
 * @author ractoc
 */
public class TreeBoxViewConverter implements ListBoxViewConverter<TreeEntryModelClass> {

    @Override
    public void display(Element listBoxItem, TreeEntryModelClass item) {
        final Element spacer = listBoxItem.findElementByName("#tree-item-spacer");
        spacer.setWidth(item.getIndent());
        final Element text = listBoxItem.findElementByName("#tree-item-caption");
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        final Element icon = listBoxItem.findElementByName("#tree-item-icon");
        final ImageRenderer iconRenderer = icon.getRenderer(ImageRenderer.class);
        textRenderer.setText(item.getTreeItem().getDisplayCaption());
        if (item.getTreeItem().isExpanded()) {
            iconRenderer.setImage(item.getTreeItem().getDisplayIconExpanded());
        } else {
            iconRenderer.setImage(item.getTreeItem().getDisplayIconCollapsed());
        }
    }

    @Override
    public int getWidth(Element element, TreeEntryModelClass item) {
        final Element text = element.findElementByName("#tree-item-caption");
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        final Element icon = element.findElementByName("#tree-item-icon");
        final ImageRenderer iconRenderer = icon.getRenderer(ImageRenderer.class);
        final RenderFont font = textRenderer.getFont();
        if (font == null) {
          return 0;
        }
        if (iconRenderer == null ||
            iconRenderer.getImage() == null ||
            item == null ||
            item.getTreeItem() == null) {
          return 0;
        }
        NiftyImage image = iconRenderer.getImage();
        return font.getWidth(item.getTreeItem().getDisplayCaption()) + image.getWidth() + item.getIndent();
    }
}

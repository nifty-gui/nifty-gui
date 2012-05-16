/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author ractoc
 */
public class TreeBoxViewConverter implements ListBoxViewConverter<TreeEntryModelClass<?>> {

    @Override
    public void display(Element listBoxItem, TreeEntryModelClass<?> item) {
        final Element spacer = listBoxItem.findElementByName("#tree-item-spacer");
        spacer.setConstraintWidth(new SizeValue(String.valueOf(item.getIndent())));
        spacer.setConstraintHeight(new SizeValue(String.valueOf(item.getTreeItem().getDisplayIconCollapsed().getHeight())));
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
        // set a different style based on active item or not.
        if (item.isActiveItem()) {
        	text.setStyle("nifty-treebox-item-active");
        } else {
        	text.setStyle("nifty-listbox-item");
        }
        icon.setConstraintWidth(new SizeValue(String.valueOf(item.getTreeItem().getDisplayIconCollapsed().getWidth())));
        icon.setConstraintHeight(new SizeValue(String.valueOf(item.getTreeItem().getDisplayIconCollapsed().getHeight())));
    }

    @Override
    public int getWidth(Element element, TreeEntryModelClass<?> item) {
        final Element text = element.findElementByName("#tree-item-caption");
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        return ((textRenderer.getFont() == null) ? 0 : textRenderer.getFont().getWidth(item.getTreeItem().getDisplayCaption())
                + ((item.getTreeItem().getDisplayIconCollapsed() == null) ? 0 : item.getTreeItem().getDisplayIconCollapsed().getWidth() + item.getIndent()));
    }
}

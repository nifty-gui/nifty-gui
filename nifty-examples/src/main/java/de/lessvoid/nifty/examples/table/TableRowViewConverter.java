package de.lessvoid.nifty.examples.table;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.Color;

/**
 * The ViewConverter class that connects the model class (TableRow) to the nifty world.
 */
class TableRowViewConverter implements ListBox.ListBoxViewConverter<TableRow> {
  @Override
  public void display(final Element listBoxItem, final TableRow item) {
    for (int i=0; i<5; i++) {
      Color color = new Color("#ff05");
      if (item.index % 2 == 0) {
        color = new Color("#00f5");
      }
      // get the text element for the row
      Element textElement = listBoxItem.findElementByName("#col-" + String.valueOf(i));
      textElement.getRenderer(TextRenderer.class).setText(item.data[i]);
      listBoxItem.getRenderer(PanelRenderer.class).setBackgroundColor(color);
    }
  }

  @Override
  public int getWidth(final Element listBoxItem, final TableRow item) {
    int width = 0;
    for (int i=0; i<5; i++) {
      TextRenderer renderer = listBoxItem.findElementByName("#col-" + String.valueOf(i)).getRenderer(TextRenderer.class);
      width += renderer.getFont().getWidth(item.data[i]);
    }
    return width;
  }
}

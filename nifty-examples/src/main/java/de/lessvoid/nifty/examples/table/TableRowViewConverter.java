package de.lessvoid.nifty.examples.table;

import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * The ViewConverter class that connects the model class (TableRow) to the nifty world.
 */
class TableRowViewConverter implements ListBox.ListBoxViewConverter<TableRow> {
  private static final Logger log = Logger.getLogger(TableRowViewConverter.class.getName());
  @Override
  public void display(@Nonnull final Element listBoxItem, @Nonnull final TableRow item) {
    for (int i=0; i<5; i++) {
      Color color = new Color("#ff05");
      if (item.index % 2 == 0) {
        color = new Color("#00f5");
      }
      // get the text element for the row
      String textElementId = "#col-" + i;
      Element textElement = listBoxItem.findElementById(textElementId);
      if (textElement == null) {
        log.warning("Failed to locate display area of table. Looking for: " + textElementId);
      } else {
        TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
        if (textRenderer == null) {
          log.warning("Text element does not contain a text renderer.");
        } else {
          textRenderer.setText(item.data[i]);
        }
      }
      PanelRenderer panelRenderer = listBoxItem.getRenderer(PanelRenderer.class);
      if (panelRenderer == null) {
        log.warning("Background element does not contain a panel renderer.");
      } else {
        panelRenderer.setBackgroundColor(color);
      }
    }
  }

  @Override
  public int getWidth(@Nonnull final Element listBoxItem, @Nonnull final TableRow item) {
    int width = 0;
    for (int i=0; i<5; i++) {
      String textElementId = "#col-" + i;
      Element textElement = listBoxItem.findElementById(textElementId);

      if (textElement == null) {
        log.warning("Failed to locate display area of table. Looking for: " + textElementId);
      } else {
        TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
        if (textRenderer == null) {
          log.warning("Text element does not contain a text renderer.");
        } else {
          RenderFont font = textRenderer.getFont();
          if (font == null) {
            log.warning("Text render does not have a font assigned.");
          } else {
            width += font.getWidth(item.data[i]);
          }
        }
      }
    }
    return width;
  }
}

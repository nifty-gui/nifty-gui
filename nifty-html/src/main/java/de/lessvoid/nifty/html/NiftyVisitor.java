package de.lessvoid.nifty.html;

import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * A NodeVisitor for the HTML Parser project that will visit all HTML tags
 * and translate them into Nifty elements using the Nifty Builder pattern.
 * @author void
 */
public class NiftyVisitor extends NodeVisitor {
  private Nifty nifty;
  private PanelBuilder mainPanel;
  private PanelBuilder paragraph;
  private PanelBuilder table;
  private PanelBuilder tableRow;
  private PanelBuilder tableData;

  /**
   * Create the NiftyVisitor.
   * @param nifty the Nifty instance
   */
  public NiftyVisitor(final Nifty nifty) {
    this.nifty = nifty;

    // we'll add a main panel for all HTML content we generate - so that we can decide the childLayout
    // and other properties. this new panel will be the main panel for all HTML elements we generate
    mainPanel = new PanelBuilder();
    mainPanel.width("100%");
    mainPanel.height("100%");
    mainPanel.childLayoutVertical();
  }

  public void visitTag(final Tag tag) {
    if (isParagraph(tag)) {
      paragraph = new PanelBuilder();
      paragraph.width("100%");
      paragraph.childLayoutVertical();
    } else if (isImageTag(tag)) {
      ImageBuilder image = new ImageBuilder();
      image.filename(tag.getAttribute("src"));
      addAttributes(image, tag);
      mainPanel.image(image);
    } else if (isTableTag(tag)) {
      table = new PanelBuilder();
      table.childLayoutVertical();
      addAttributes(table, tag);
    } else if (isTableRowTag(tag)) {
      tableRow = new PanelBuilder();
      tableRow.childLayoutHorizontal();
      addAttributes(tableRow, tag);
    } else if (isTableDataTag(tag)) {
      tableData = new PanelBuilder();
      tableData.childLayoutVertical();
      addAttributes(tableData, tag);
    }
  }

  public void visitEndTag (final Tag tag) {
    if (isParagraph(tag)) {
      if (paragraph.getElementBuilders().isEmpty()) {
        paragraph.height(String.valueOf(nifty.getRenderEngine().createFont("aurulent-sans-16.fnt").getHeight()));
      }
      mainPanel.panel(paragraph);
      paragraph = null;
    } else if (isTableTag(tag)) {
      mainPanel.panel(table);
      table = null;
    } else if (isTableRowTag(tag)) {
      table.panel(tableRow);
      tableRow = null;
    } else if (isTableDataTag(tag)) {
      tableRow.panel(tableData);
      tableData = null;
    }
  }

  public void visitStringNode(final Text textNode) {
    if (paragraph != null) {
      addToPanel(paragraph, textNode);
    } else if (tableData != null) {
      addToPanel(tableData, textNode);
    } else if (table != null) {
      addToPanel(table, textNode);
    } else if (tableRow != null) {
      addToPanel(tableRow, textNode);
    }
  }

  private void addToPanel(final PanelBuilder panelBuilder, final Text textNode) {
    TextBuilder text = new TextBuilder();
    text.text(removeNewLine(textNode.getText()));
    text.wrap(true);
    text.textHAlignLeft();
    text.textVAlignTop();
    text.font("aurulent-sans-16.fnt");
    text.width("100%");
    panelBuilder.text(text);
  }

  private String removeNewLine(final String text) {
    return text.replaceAll("\n", "");
  }

  public Element build(final Nifty nifty, final Screen screen, final Element parent) {
    return mainPanel.build(nifty, screen, parent);
  }

  private boolean isParagraph(final Tag tag) {
    return "P".equals(tag.getTagName());
  }

  private boolean isImageTag(final Tag tag) {
    return "IMG".equals(tag.getTagName());
  }

  private boolean isTableTag(final Tag tag) {
    return "TABLE".equals(tag.getTagName());
  }

  private boolean isTableRowTag(final Tag tag) {
    return "TR".equals(tag.getTagName());
  }

  private boolean isTableDataTag(final Tag tag) {
    return "TD".equals(tag.getTagName());
  }

  private boolean isTableHeaderTag(final Tag tag) {
    return "TH".equals(tag.getTagName());
  }

  private void addAttributes(final PanelBuilder panelBuilder, final Tag tag) {
    if (tag.getAttribute("width") != null) {
      panelBuilder.width(tag.getAttribute("width"));
    }
    if (tag.getAttribute("height") != null) {
      panelBuilder.width(tag.getAttribute("height"));
    }
    if (tag.getAttribute("bgcolor") != null) {
      panelBuilder.backgroundColor(tag.getAttribute("bgcolor"));
    }
  }

  private void addAttributes(final ImageBuilder imageBuilder, final Tag tag) {
    if (tag.getAttribute("width") != null) {
      imageBuilder.width(tag.getAttribute("width"));
    }
    if (tag.getAttribute("height") != null) {
      imageBuilder.width(tag.getAttribute("height"));
    }
    if (tag.getAttribute("bgcolor") != null) {
      imageBuilder.backgroundColor(tag.getAttribute("bgcolor"));
    }
    imageBuilder.padding("2px");
  }
}

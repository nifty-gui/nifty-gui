package de.lessvoid.nifty.html;

import java.util.ArrayList;
import java.util.List;

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
  private List<String> errors = new ArrayList<String>();
  private Nifty nifty;
  private PanelBuilder bodyPanel;
  private PanelBuilder paragraph;
  private PanelBuilder table;
  private PanelBuilder tableRow;
  private PanelBuilder tableData;
  private String currentColor;
  private NiftyBuilderFactory niftyBuilderFactory;

  /**
   * Create the NiftyVisitor.
   * @param nifty the Nifty instance
   * @param niftyBuilderFactory a helper class to create Nifty Builders
   */
  public NiftyVisitor(final Nifty nifty, final NiftyBuilderFactory niftyBuilderFactory) {
    this.nifty = nifty;
    this.niftyBuilderFactory = niftyBuilderFactory;
  }

  /*
   * (non-Javadoc)
   * @see org.htmlparser.visitors.NodeVisitor#beginParsing()
   */
  @Override
  public void beginParsing () {
    errors.clear();
  }

  /*
   * (non-Javadoc)
   * @see org.htmlparser.visitors.NodeVisitor#visitTag(org.htmlparser.Tag)
   */
  @Override
  public void visitTag(final Tag tag) {
    try {
      if (isBody(tag)) {
        // we'll add a main panel for the body which will be the parent element for everything we generate
        // this way we can decide the childLayout and other properties of the body in here.
        bodyPanel = niftyBuilderFactory.createPanelBuilder();
        bodyPanel.width("100%");
        bodyPanel.height("100%");
        bodyPanel.childLayoutVertical();
      } else if (isParagraph(tag)) {
        assertBodyPanelNotNull();
        paragraph = niftyBuilderFactory.createPanelBuilder();
        paragraph.width("100%");
        paragraph.childLayoutVertical();
        /*
      } else if (isImageTag(tag)) {
        ImageBuilder image = niftyBuilderFactory.createImageBuilder();
        image.filename(tag.getAttribute("src"));
        addAttributes(image, tag);
        mainPanel.image(image);
      } else if (isTableTag(tag)) {
        table = niftyBuilderFactory.createPanelBuilder();
        table.childLayoutVertical();
        addAttributes(table, tag);
      } else if (isTableRowTag(tag)) {
        tableRow = niftyBuilderFactory.createPanelBuilder();
        tableRow.childLayoutHorizontal();
        addAttributes(tableRow, tag);
      } else if (isTableDataTag(tag)) {
        tableData = niftyBuilderFactory.createPanelBuilder();
        tableData.childLayoutVertical();
        addAttributes(tableData, tag);
      } else if (isColorTag(tag)) {
        String colorR = toHex(tag.getAttribute("r"));
        String colorG = toHex(tag.getAttribute("g"));
        String colorB = toHex(tag.getAttribute("b"));
        currentColor = "#" + colorR + colorG + colorB + "ff";
        */
      }
    } catch (Exception e) {
      addError(e);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.htmlparser.visitors.NodeVisitor#visitEndTag(org.htmlparser.Tag)
   */
  @Override
  public void visitEndTag(final Tag tag) {
    try {
      if (isBody(tag)) {
        // nothing to do when a body tag ends
      } else if (isParagraph(tag)) {
        assertBodyPanelNotNull();
        if (paragraph.getElementBuilders().isEmpty()) {
          paragraph.height("5"/*String.valueOf(nifty.getRenderEngine().createFont("aurulent-sans-16.fnt").getHeight())*/);
        }
        bodyPanel.panel(paragraph);
        paragraph = null;
        /*
      } else if (isTableTag(tag)) {
        assertMainPanelNotNull();
        mainPanel.panel(table);
        table = null;
      } else if (isTableRowTag(tag)) {
        table.panel(tableRow);
        tableRow = null;
      } else if (isTableDataTag(tag)) {
        tableRow.panel(tableData);
        tableData = null;
      } else if (isColorTag(tag)) {
        currentColor = null;
        */
      }
    } catch (Exception e) {
      addError(e);
    }
  }

  private void addError(final Exception e) {
    if (!errors.contains(e.getMessage())) {
      errors.add(e.getMessage());
    }
  }

  private void assertBodyPanelNotNull() throws Exception {
    if (bodyPanel == null) {
      throw new Exception("This looks like HTML with a missing <body> tag");
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
    TextBuilder text = niftyBuilderFactory.createTextBuilder();
    text.text(removeNewLine(textNode.getText()));
    text.wrap(true);
    text.textHAlignLeft();
    text.textVAlignTop();
    text.font("aurulent-sans-16.fnt");
    text.width("100%");
    if (currentColor != null) {
      text.color(currentColor);
    }
    panelBuilder.text(text);
  }

  private String removeNewLine(final String text) {
    return text.replaceAll("\n", "");
  }

  public Element build(final Nifty nifty, final Screen screen, final Element parent) throws Exception {
    try {
      assertBodyPanelNotNull();
    } catch (Exception e) {
      addError(e);
    }

    assertNoErrors();
    return bodyPanel.build(nifty, screen, parent);
  }

  private void assertNoErrors() throws Exception {
    if (!errors.isEmpty()) {
      StringBuffer message = new StringBuffer();
      for (int i=0; i<errors.size(); i++) {
        message.append(errors.get(i));
        message.append("\n");
      }
      throw new Exception(message.toString());
    }
  }

  private boolean isBody(final Tag tag) {
    return "BODY".equals(tag.getTagName());
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

  private boolean isColorTag(final Tag tag) {
    return "COLOR".equals(tag.getTagName());
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

  private String toHex(final String str) {
    int value = Integer.parseInt(str);
    String hex = Integer.toHexString(value);
    if (value < 16) {
      return "0" + hex;
    }
    return hex;
  }
}

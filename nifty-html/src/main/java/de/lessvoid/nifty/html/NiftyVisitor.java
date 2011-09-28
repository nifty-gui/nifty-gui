package de.lessvoid.nifty.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.spi.render.RenderFont;

/**
 * A NodeVisitor for the HTML Parser project that will visit all HTML tags
 * and translate them into Nifty elements using the Nifty Builder pattern.
 * @author void
 */
public class NiftyVisitor extends NodeVisitor {
  // errors in processing are added to that list
  private List<String> errors = new ArrayList<String>();

  // we keep a Nifty instance around to access some things
  private Nifty nifty;

  // the PanelBuilder for the body tag
  private PanelBuilder bodyPanel;

  // helper class to create new builders
  private NiftyBuilderFactory niftyBuilderFactory;

  // to allow nested block level elements later we must stack them
  private Stack<PanelBuilder> blockElementStack = new Stack<PanelBuilder>();

  // the current block level element
  private PanelBuilder currentBlockElement;

  // default font we use for generatin text elements
  private String defaultFontName;
  private RenderFont defaultFont; 

  // current color
  private String currentColor;

  // table
  private PanelBuilder table;

  // table row
  private PanelBuilder tableRow;

  // table data
  private PanelBuilder tableData;

  /**
   * Create the NiftyVisitor.
   * @param nifty the Nifty instance
   * @param niftyBuilderFactory a helper class to create Nifty Builders
   */
  public NiftyVisitor(final Nifty nifty, final NiftyBuilderFactory niftyBuilderFactory, final String defaultFontName) {
    this.nifty = nifty;
    this.niftyBuilderFactory = niftyBuilderFactory;
    this.defaultFontName = defaultFontName;
    if (defaultFontName != null) {
      this.defaultFont = nifty.createFont(defaultFontName);
    }
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
        // this way we can decide the childLayout and other properties of the body panel.
        bodyPanel = niftyBuilderFactory.createBodyPanelBuilder();
      } else if (isParagraph(tag)) {
        assertBodyPanelNotNull();
        currentBlockElement = niftyBuilderFactory.createParagraphPanelBuilder();
        blockElementStack.push(currentBlockElement);
      } else if (isImageTag(tag)) {
        ImageBuilder image = niftyBuilderFactory.createImageBuilder(
            tag.getAttribute("src"),
            tag.getAttribute("align"),
            tag.getAttribute("width"),
            tag.getAttribute("height"),
            tag.getAttribute("bgcolor"),
            tag.getAttribute("vspace"));
        bodyPanel.image(image);
      } else if (isBreak(tag)) {
        PanelBuilder breakPanelBuilder = niftyBuilderFactory.createBreakPanelBuilder(String.valueOf(defaultFont.getHeight()));
        bodyPanel.panel(breakPanelBuilder);
      } else if (isTableTag(tag)) {
        table = niftyBuilderFactory.createTableTagPanelBuilder();
      } else if (isTableRowTag(tag)) {
        tableRow = niftyBuilderFactory.createTableRowPanelBuilder();
      } else if (isTableDataTag(tag)) {
        tableData = niftyBuilderFactory.createTableDataPanelBuilder();
        /*
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
        // currently there is nothing to do when a body tag ends
      } else if (isParagraph(tag)) {
        assertBodyPanelNotNull();
        assertCurrentBlockElementNotNull();
        if (currentBlockElement.getElementBuilders().isEmpty()) {
          currentBlockElement.height(String.valueOf(defaultFont.getHeight()));
        }
        bodyPanel.panel(currentBlockElement);
        blockElementStack.pop();
        currentBlockElement = null;
      } else if (isImageTag(tag)) {
        // nothing to do
      } else if (isBreak(tag)) {
        // nothing to do
      } else if (isTableTag(tag)) {
        assertBodyPanelNotNull();
        bodyPanel.panel(table);
        table = null;
      } else if (isTableRowTag(tag)) {
        table.panel(tableRow);
        tableRow = null;
      } else if (isTableDataTag(tag)) {
        tableRow.panel(tableData);
        tableData = null;
        /*
      } else if (isColorTag(tag)) {
        currentColor = null;
        */
      }
    } catch (Exception e) {
      addError(e);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.htmlparser.visitors.NodeVisitor#visitStringNode(org.htmlparser.Text)
   */
  @Override
  public void visitStringNode(final Text textNode) {
    if (currentBlockElement != null) {
      addToPanel(currentBlockElement, textNode);
    } else if (tableData != null) {
      addToPanel(tableData, textNode);
    }
  }

  public ElementBuilder builder() throws Exception {
    try {
      assertBodyPanelNotNull();
    } catch (Exception e) {
      addError(e);
    }

    assertNoErrors();
    return bodyPanel;
  }

  // private stuff

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

  private void assertCurrentBlockElementNotNull() throws Exception {
    if (currentBlockElement == null) {
      throw new Exception("This looks like broken HTML. currentBlockElement seems null. Maybe a duplicate close tag?");
    }
  }

  private void addToPanel(final PanelBuilder panelBuilder, final Text textNode) {
    TextBuilder text = niftyBuilderFactory.createTextBuilder(textNode.getText(), defaultFontName, currentColor);
    panelBuilder.text(text);
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

  private boolean isBreak(final Tag tag) {
    return "BR".equals(tag.getTagName());
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

  private String toHex(final String str) {
    int value = Integer.parseInt(str);
    String hex = Integer.toHexString(value);
    if (value < 16) {
      return "0" + hex;
    }
    return hex;
  }
}

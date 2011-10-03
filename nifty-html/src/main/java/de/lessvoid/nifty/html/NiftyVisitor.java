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
import de.lessvoid.nifty.spi.render.RenderFont;

/**
 * A NodeVisitor for the HTML Parser project that will visit all HTML tags
 * and translate them into Nifty elements using the Nifty Builder pattern.
 * @author void
 */
public class NiftyVisitor extends NodeVisitor {
  // errors in processing are added to that list
  private List<String> errors = new ArrayList<String>();

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
  private String defaultFontBoldName;
  private RenderFont defaultFont; 

  // current color
  private String currentColor;

  // this will be set to a different font name when a corresponding tag is being processed
  private String differentFont;

  // we collect all text nodes into this string buffer
  private StringBuffer currentText = new StringBuffer();

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
  public NiftyVisitor(final Nifty nifty, final NiftyBuilderFactory niftyBuilderFactory, final String defaultFontName, final String defaultFontBoldName) {
    this.niftyBuilderFactory = niftyBuilderFactory;
    this.defaultFontName = defaultFontName;
    this.defaultFontBoldName = defaultFontBoldName;
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
        if (currentBlockElement != null) {
          currentText.append("\n");
        } else {
          PanelBuilder breakPanelBuilder = niftyBuilderFactory.createBreakPanelBuilder(String.valueOf(defaultFont.getHeight()));
          bodyPanel.panel(breakPanelBuilder);
        }
      } else if (isTableTag(tag)) {
        assertBodyPanelNotNull();
        table = niftyBuilderFactory.createTableTagPanelBuilder(
            tag.getAttribute("width"),
            tag.getAttribute("bgcolor"),
            tag.getAttribute("border"),
            tag.getAttribute("bordercolor"));
      } else if (isTableRowTag(tag)) {
        assertTableNotNull();
        tableRow = niftyBuilderFactory.createTableRowPanelBuilder(
            tag.getAttribute("width"),
            tag.getAttribute("bgcolor"),
            tag.getAttribute("border"),
            tag.getAttribute("bordercolor"));
      } else if (isTableDataTag(tag)) {
        assertTableRowNotNull();
        tableData = niftyBuilderFactory.createTableDataPanelBuilder(
            tag.getAttribute("width"),
            tag.getAttribute("bgcolor"),
            tag.getAttribute("border"),
            tag.getAttribute("bordercolor"));
      } else if (isFontTag(tag)) {
        String color = tag.getAttribute("color");
        if (color != null) {
          currentColor = color;
        }
      } else if (isStrongTag(tag)) {
        differentFont = defaultFontBoldName;
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

        String textElement = currentText.toString();
        if (textElement.length() > 0) {
          addTextElement(currentBlockElement, textElement);
          currentText.setLength(0);
        }

        if (currentBlockElement.getElementBuilders().isEmpty()) {
          currentBlockElement.height(String.valueOf(defaultFont.getHeight()));
        }
        bodyPanel.panel(currentBlockElement);
        blockElementStack.pop();
        currentBlockElement = null;
        differentFont = null;
      } else if (isImageTag(tag)) {
        // nothing to do
      } else if (isBreak(tag)) {
        // nothing to do
      } else if (isTableTag(tag)) {
        assertBodyPanelNotNull();
        bodyPanel.panel(table);
        table = null;
      } else if (isTableRowTag(tag)) {
        assertTableNotNull();
        table.panel(tableRow);
        tableRow = null;
      } else if (isTableDataTag(tag)) {
        assertTableRowNotNull();

        addTextElement(tableData, currentText.toString());
        currentText.setLength(0);

        tableRow.panel(tableData);
        tableData = null;
        differentFont = null;
      } else if (isFontTag(tag)) {
        currentColor = null;
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
    if (tableData != null) {
      appendText(textNode);
    } else if (currentBlockElement != null) {
      appendText(textNode);
    }
  }

  private void appendText(final Text textNode) {
    if (currentColor != null) {
      currentText.append("\\");
      currentText.append(currentColor);
      currentText.append("#");
    }
    currentText.append(removeNewLine(textNode.getText()));
  }

  public ElementBuilder builder() throws Exception {
    try {
      assertBodyPanelNotNull();
    } catch (Exception e) {
      addAsFirstError(e);
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

  private void addAsFirstError(final Exception e) {
    if (!errors.contains(e.getMessage())) {
      errors.add(0, e.getMessage());
    }
  }

  private void assertBodyPanelNotNull() throws Exception {
    if (bodyPanel == null) {
      throw new Exception("This looks like HTML with a missing <body> tag");
    }
  }

  private void assertTableNotNull() throws Exception {
    if (table == null) {
      throw new Exception("This looks like a <tr> element with a missing <table> tag");
    }
  }

  private void assertTableRowNotNull() throws Exception {
    if (table == null) {
      throw new Exception("This looks like a <td> element with a missing <tr> tag");
    }
  }

  private void assertCurrentBlockElementNotNull() throws Exception {
    if (currentBlockElement == null) {
      throw new Exception("This looks like broken HTML. currentBlockElement seems null. Maybe a duplicate close tag?");
    }
  }

  private void addTextElement(final PanelBuilder panelBuilder, final String text) {
    String font = defaultFontName;
    if (differentFont != null) {
      font = differentFont;
    }
    panelBuilder.text(niftyBuilderFactory.createTextBuilder(text, font, currentColor));
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

  private boolean isFontTag(final Tag tag) {
    return "FONT".equals(tag.getTagName());
  }

  private boolean isStrongTag(final Tag tag) {
    return "STRONG".equals(tag.getTagName());
  }

  private String removeNewLine(final String text) {
    return text.replaceAll("\n", "").replaceAll("\t", "");
  }
}

package de.lessvoid.nifty.html;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.spi.render.RenderFont;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.Translate;
import org.htmlparser.visitors.NodeVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A NodeVisitor for the HTML Parser project that will visit all HTML tags
 * and translate them into Nifty elements using the Nifty Builder pattern.
 * @author void
 */
public class NiftyVisitor extends NodeVisitor {
  // errors in processing are added to that list
  @Nonnull
  private final List<String> errors = new ArrayList<String>();

  // the PanelBuilder for the body tag
  private PanelBuilder bodyPanel;

  // helper class to create new builders
  private final NiftyBuilderFactory niftyBuilderFactory;

  // to allow nested block level elements later we must stack them
  @Nonnull
  private final Stack<PanelBuilder> blockElementStack = new Stack<PanelBuilder>();

  // the current block level element
  @Nullable
  private PanelBuilder currentBlockElement;

  // default font we use for generating text elements
  @Nullable
  private final String defaultFontName;
  private final String defaultFontBoldName;

  // current color
  @Nullable
  private String currentColor;

  // this will be set to a different font name when a corresponding tag is being processed
  @Nullable
  private String differentFont;

  // we collect all text nodes into this string buffer
  @Nonnull
  private final StringBuffer currentText = new StringBuffer();

  // table
  @Nullable
  private PanelBuilder table;

  // table row
  @Nullable
  private PanelBuilder tableRow;

  // table data
  @Nullable
  private PanelBuilder tableData;

  @Nonnull
  private String fontHeight;

  /**
   * Create the NiftyVisitor.
   * @param nifty the Nifty instance
   * @param niftyBuilderFactory a helper class to create Nifty Builders
   */
  public NiftyVisitor(@Nonnull final Nifty nifty, final NiftyBuilderFactory niftyBuilderFactory, @Nullable final String defaultFontName, final String defaultFontBoldName) {
    this.niftyBuilderFactory = niftyBuilderFactory;
    this.defaultFontName = defaultFontName;
    this.defaultFontBoldName = defaultFontBoldName;
    fontHeight = "0";
    if (defaultFontName != null) {
      RenderFont defaultFont = nifty.createFont(defaultFontName);
      if (defaultFont != null) {
        fontHeight = Integer.toString(defaultFont.getHeight());
      }
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
  public void visitTag(@Nonnull final Tag tag) {
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
        if (currentBlockElement != null) {
          currentBlockElement.image(image);
        } else {
          bodyPanel.image(image);
        }
      } else if (isBreak(tag)) {
        if (currentBlockElement != null) {
          currentText.append("\n");
        } else {
          PanelBuilder breakPanelBuilder = niftyBuilderFactory.createBreakPanelBuilder(fontHeight);
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
  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public void visitEndTag(@Nonnull final Tag tag) {
    try {
      if (isBody(tag)) {
        // currently there is nothing to do when a body tag ends
      } else if (isParagraph(tag)) {
        if (bodyPanel == null) {
          throw new Exception("This looks like HTML with a missing <body> tag");
        }
        if (currentBlockElement == null) {
          throw new Exception("This looks like broken HTML. currentBlockElement seems null. Maybe a duplicate close tag?");
        }

        String textElement = currentText.toString();
        if (textElement.length() > 0) {
          addTextElement(currentBlockElement, textElement);
          currentText.setLength(0);
        }

        if (currentBlockElement.getElementBuilders().isEmpty()) {
          currentBlockElement.height(fontHeight);
        }
        bodyPanel.panel(currentBlockElement);
        currentBlockElement = blockElementStack.pop();
        differentFont = null;
      } else if (isImageTag(tag)) {
        // nothing to do
      } else if (isBreak(tag)) {
        // nothing to do
      } else if (isTableTag(tag)) {
        if (bodyPanel == null || table == null) {
          throw new Exception("This looks like HTML with a missing <body> tag");
        }
        bodyPanel.panel(table);
        table = null;
      } else if (isTableRowTag(tag)) {
        if (table == null || tableRow == null) {
          throw new Exception("This looks like a <tr> element with a missing <table> tag");
        }
        table.panel(tableRow);
        tableRow = null;
      } else if (isTableDataTag(tag)) {
        if (tableRow == null) {
          throw new Exception("This looks like a <td> element with a missing <tr> tag");
        }
        if (tableData == null) {
          throw new Exception("This looks like a <td> element with a missing <tr> tag");
        }

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
  public void visitStringNode(@Nonnull final Text textNode) {
    if (tableData != null) {
      appendText(textNode);
    } else if (currentBlockElement != null) {
      appendText(textNode);
    }
  }

  private void appendText(@Nonnull final Text textNode) {
    if (currentColor != null) {
      currentText.append("\\");
      currentText.append(currentColor);
      currentText.append("#");
    }
    currentText.append(removeNewLineAndTabs(translateHTMLEntities(textNode.getText())));
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

  private void addError(@Nonnull final Exception e) {
    if (!errors.contains(e.getMessage())) {
      errors.add(e.getMessage());
    }
  }

  private void addAsFirstError(@Nonnull final Exception e) {
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
    if (tableRow == null) {
      throw new Exception("This looks like a <td> element with a missing <tr> tag");
    }
  }

  private void addTextElement(@Nonnull final PanelBuilder panelBuilder, @Nonnull final String text) {
    String font = defaultFontName;
    if (differentFont != null) {
      font = differentFont;
    }
   if (font == null) {
     return;
   }
    panelBuilder.text(niftyBuilderFactory.createTextBuilder(text, font, currentColor));
  }

  private void assertNoErrors() throws Exception {
    if (!errors.isEmpty()) {
      StringBuilder message = new StringBuilder();
      for (int i=0; i<errors.size(); i++) {
        message.append(errors.get(i));
        message.append("\n");
      }
      throw new Exception(message.toString());
    }
  }

  private boolean isBody(@Nonnull final Tag tag) {
    return "BODY".equals(tag.getTagName());
  }

  private boolean isParagraph(@Nonnull final Tag tag) {
    return "P".equals(tag.getTagName());
  }

  private boolean isBreak(@Nonnull final Tag tag) {
    return "BR".equals(tag.getTagName());
  }

  private boolean isImageTag(@Nonnull final Tag tag) {
    return "IMG".equals(tag.getTagName());
  }

  private boolean isTableTag(@Nonnull final Tag tag) {
    return "TABLE".equals(tag.getTagName());
  }

  private boolean isTableRowTag(@Nonnull final Tag tag) {
    return "TR".equals(tag.getTagName());
  }

  private boolean isTableDataTag(@Nonnull final Tag tag) {
    return "TD".equals(tag.getTagName());
  }

  private boolean isFontTag(@Nonnull final Tag tag) {
    return "FONT".equals(tag.getTagName());
  }

  private boolean isStrongTag(@Nonnull final Tag tag) {
    return "STRONG".equals(tag.getTagName());
  }

  private String removeNewLineAndTabs(@Nonnull final String text) {
    return text.replaceAll("\n", "").replaceAll("\t", "");
  }

  private String translateHTMLEntities(final String text) {
    return Translate.decode(text);
  }
}

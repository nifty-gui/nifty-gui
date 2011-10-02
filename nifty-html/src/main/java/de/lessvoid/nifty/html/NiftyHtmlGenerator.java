package de.lessvoid.nifty.html;

import org.htmlparser.Parser;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * This class will take a HTML String and transforms the HTML into Nifty elements.
 * @author void
 */
public class NiftyHtmlGenerator {
  private Nifty nifty;
  private String defaultFontName = "aurulent-sans-16.fnt";
  private String defaultBoldFontName = "aurulent-sans-16-bold.fnt";

  /**
   * Create the NiftyHtmlGenerator.
   * @param nifty the Nifty instance
   */
  public NiftyHtmlGenerator(final Nifty nifty) {
    this.nifty = nifty;

    // we could set this to true for debug purpose
    this.nifty.setDebugOptionPanelColors(false);
  }

  /**
   * Change the default font to be used.
   * @param name
   */
  public void setDefaultFont(final String name) {
    this.defaultFontName = name;
  }

  /**
   * Change the default bold font to be used.
   * @param name
   */
  public void setDefaultBoldFont(final String name) {
    this.defaultBoldFontName = name;
  }

  /**
   * Parse the given XML and build the corresponding Nifty elements.
   * @param html the actual HTML string to parse and transform
   * @param screen the screen to generate elements for
   * @param parent parent element that all new Nifty elements will be added as child elements
   * @throws Exception in case of any error an Exception is thrown
   */
  public void generate(final String html, final Screen screen, final Element parent) throws Exception {
    removeAllChilds(parent);

    Parser parser = Parser.createParser(html, "ISO-8859-1");

    NiftyVisitor visitor = new NiftyVisitor(nifty, new NiftyBuilderFactory(), defaultFontName, defaultBoldFontName);
    parser.visitAllNodesWith(visitor);

    ElementBuilder builder = visitor.builder();
    builder.build(nifty, screen, parent);
  }

  /**
   * Remove all child elements of the given parent element.
   * @param parent the element we want to remove all childs
   */
  private void removeAllChilds(final Element parent) {
    for (int i=0; i<parent.getElements().size(); i++) {
      parent.getElements().get(i).markForRemoval();
    }
    nifty.executeEndOfFrameElementActions();
  }
}

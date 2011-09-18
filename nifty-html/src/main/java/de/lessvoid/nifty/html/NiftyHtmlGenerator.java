package de.lessvoid.nifty.html;

import org.htmlparser.Parser;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 * This class will take a HTML String and transforms the HTML into Nifty elements.
 * @author void
 */
public class NiftyHtmlGenerator {
  private Nifty nifty;

  /**
   * Create the NiftyHtmlGenerator.
   * @param nifty the Nifty instance
   */
  public NiftyHtmlGenerator(final Nifty nifty) {
    this.nifty = nifty;
  }

  /**
   * Parse the given XML and build the corresponding Nifty elements.
   * @param html the actual HTML string to parse and transform
   * @param screen the screen to generate elements for
   * @param parent parent element that all new Nifty elements will be added as child elements
   * @throws Exception in case of any error an Exception is thrown
   */
  public void generate(final String html, final Screen screen, final Element parent) throws Exception {
    Parser parser = Parser.createParser(html, "ISO-8859-1");

    NiftyVisitor visitor = new NiftyVisitor(nifty);
    parser.visitAllNodesWith(visitor);
    visitor.build(nifty, screen, parent);
  }
}

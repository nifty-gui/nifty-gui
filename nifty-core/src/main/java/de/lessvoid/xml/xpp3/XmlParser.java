package de.lessvoid.xml.xpp3;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * XmlParser is a helper/wrapper around XPP3.
 * @author void
 */
public class XmlParser {

  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(XmlParser.class.getName());

  /**
   * the parser we should use.
   */
  private XmlPullParser xpp;

  /**
   * Create a new XmlParser using the given XmlPullParser.
   * @param xppParam the XmlPullParser to use
   */
  public XmlParser(final XmlPullParser xppParam) {
    this.xpp = xppParam;
  }

  /**
   * Load xml.
   * @param inputStream InputStream to read from
   * @throws Exception exception
   */
  public void read(final InputStream inputStream) throws Exception {
    xpp.setInput(inputStream, null);
  }

  /**
   * Required element.
   * @param tag required tag
   * @param xmlElement handler
   * @throws Exception exception
   */
  public void required(final String tag, final XmlProcessor xmlElement) throws Exception {
    if (isEndTag()) {
      throw new Exception("found end tag but required tag [" + tag + "]");
    }
    if (!matchesTag(tag)) {
      throw new Exception("Expected tag [" + tag + "] but was [" + xpp.getName() + "]");
    }
    processElement(xmlElement);
    nextTag();
  }

  /**
   * Optional element.
   * @param tag required tag
   * @param xmlElement handler
   * @throws Exception exception
   */
  public void optional(final String tag, final XmlProcessor xmlElement) throws Exception {
    if (isEndTag()) {
      return;
    }
    if (!matchesTag(tag)) {
      return;
    }
    processElement(xmlElement);
    nextTag();
  }

  /**
   * Zero or more element.
   * @param tag element tag
   * @param xmlElement handler
   * @throws Exception exception
   */
  public void zeroOrMore(final String tag, final XmlProcessor xmlElement) throws Exception {
    if (isEndTag()) {
      return;
    }
    if (!matchesTag(tag)) {
      return;
    }
    processElement(xmlElement);

    nextTag();
    zeroOrMore(tag, xmlElement);
  }

  /**
   * check if the current tag is an end tag.
   * @return true on end tag reached and false otherwise
   * @throws Exception exception
   */
  private boolean isEndTag() throws Exception {
    return XmlPullParser.END_TAG == xpp.getEventType();
  }

  /**
   * SubstitionGroup Support.
   * @param substGroup SubstitutionGroup
   * @throws Exception exception
   */
  public void zeroOrMore(final SubstitutionGroup substGroup) throws Exception {
    if (isEndTag()) {
      return;
    }
    XmlProcessor element = substGroup.matches(xpp.getName());
    if (element == null) {
      return;
    }
    processElement(element);

    nextTag();
    zeroOrMore(substGroup);
  }

  /**
   * One or more element.
   * @param tag element tag
   * @param xmlElement handler
   * @throws Exception exception
   */
  public void oneOrMore(final String tag, final XmlProcessor xmlElement) throws Exception {
    if (isEndTag()) {
      throw new Exception("End tag reached but was expecting [" + tag + "]");
    }
    if (!matchesTag(tag)) {
      throw new Exception("Expected tag [" + tag + "] but was [" + xpp.getName() + "]");
    }
    processElement(xmlElement);

    nextTag();
    zeroOrMore(tag, xmlElement);
  }

  /**
   * Process the element.
   * @param xmlElement xmlElement
   * @throws Exception exception
   */
  private void processElement(final XmlProcessor xmlElement) throws Exception {
    log.fine("process element: " + xmlElement.getClass().getName());
    try {
     xmlElement.process(this, new Attributes(xpp));
    } catch (Exception ex) {
      if (!(ex instanceof XmlPullParserException)) {
          throw new XmlPullParserException("Error parsing document.", xpp, ex);
      } else {
        throw ex;
      }
    }
  }

  /**
   * Does the current tag matches the given one?
   * @param tag tag to check
   * @return true if tags match and false otherwise
   */
  private boolean matchesTag(final String tag) {
    return tag.equals(xpp.getName());
  }

  /**
   * next start or end tag.
   * @throws Exception exception
   */
  public void nextTag() throws Exception {
    if (xpp.getEventType() == XmlPullParser.END_DOCUMENT) {
      return;
    }

    int eventType = xpp.next();
    while (eventType != XmlPullParser.END_DOCUMENT) {
     if (eventType == XmlPullParser.END_TAG) {
       if (log.isLoggable(Level.FINE)) {
         indent();
         log.fine(indent() + "END <" + xpp.getName() + ">");
       }
       return;
     } else if (eventType == XmlPullParser.START_TAG) {
       if (log.isLoggable(Level.FINE)) {
         log.fine(indent() + "START <" + xpp.getName() + ">");
       }
       return;
     }
     eventType = xpp.next();
    }
  }

  /**
   * indent current xpp depth level.
   * @return string of whitespace of xpp depth level length
   */
  private String indent() {
    StringBuilder b = new StringBuilder();
     for (int i = 0; i < xpp.getDepth(); i++) {
       b.append(" ");
     }
    return b.toString();
  }
}

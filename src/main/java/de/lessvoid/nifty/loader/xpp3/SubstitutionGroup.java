package de.lessvoid.nifty.loader.xpp3;

import java.util.Hashtable;
import java.util.Map;

/**
 * SubstitutionGroup thing.
 * @author void
 */
public class SubstitutionGroup {

  /**
   * tags.
   */
  private Map < String, XmlElementProcessor > registeredTags = new Hashtable < String, XmlElementProcessor >();

  /**
   * Check if the given tag is contained in the list of registered tags.
   * @param tag tag to check
   * @return registered XmlElementProcessor or null
   */
  public XmlElementProcessor matches(final String tag) {
    if (tag == null) {
      return null;
    }

    return registeredTags.get(tag);
  }

  /**
   * Add an XmlElementProcessor for the given tag to this SubstituionGroup.
   * @param tag the tag
   * @param xmlElement the xmlElement processor
   * @return this
   */
  public SubstitutionGroup add(final String tag, final XmlElementProcessor xmlElement) {
    registeredTags.put(tag, xmlElement);
    return this;
  }
}

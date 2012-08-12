package de.lessvoid.xml.xpp3;

import java.util.HashMap;
import java.util.Map;

/**
 * SubstitutionGroup thing.
 * @author void
 */
public class SubstitutionGroup {

  /**
   * tags.
   */
  private Map < String, XmlProcessor > registeredTags = new HashMap < String, XmlProcessor >();

  /**
   * Check if the given tag is contained in the list of registered tags.
   * @param tag tag to check
   * @return registered XmlElementProcessor or null
   */
  public XmlProcessor matches(final String tag) {
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
  public SubstitutionGroup add(final String tag, final XmlProcessor xmlElement) {
    registeredTags.put(tag, xmlElement);
    return this;
  }
}

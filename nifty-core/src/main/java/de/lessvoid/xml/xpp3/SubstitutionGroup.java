package de.lessvoid.xml.xpp3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
  @Nonnull
  private final Map < String, XmlProcessor > registeredTags = new HashMap < String, XmlProcessor >();

  /**
   * Check if the given tag is contained in the list of registered tags.
   * @param tag tag to check
   * @return registered XmlElementProcessor or null
   */
  @Nullable
  public XmlProcessor matches(@Nullable final String tag) {
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
  @Nonnull
  public SubstitutionGroup add(final String tag, final XmlProcessor xmlElement) {
    registeredTags.put(tag, xmlElement);
    return this;
  }
}

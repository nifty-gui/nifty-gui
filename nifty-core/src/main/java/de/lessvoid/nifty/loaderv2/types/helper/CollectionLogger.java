package de.lessvoid.nifty.loaderv2.types.helper;

import java.util.Collection;

import de.lessvoid.nifty.loaderv2.types.XmlBaseType;
import de.lessvoid.nifty.tools.StringHelper;

public final class CollectionLogger {
  private CollectionLogger() {
  }

  public static String out(
      final int offset,
      final Collection < ? extends XmlBaseType > collection,
      final String message) {
    if (collection.isEmpty()) {
      return StringHelper.whitespace(offset) + "no children " + message;
    }
    StringBuilder result = new StringBuilder();
    result.append(StringHelper.whitespace(offset));
    result.append("children ");
    result.append(message);
    result.append(": ");
    result.append(collection.size());
    for (XmlBaseType type : collection) {
      result.append("\n");
      result.append(type.output(offset + 1));
    }
    return result.toString();
  }
}

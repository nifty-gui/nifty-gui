package de.lessvoid.xml.lwxs;

import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public interface XmlType {
  void applyAttributes(@Nonnull Attributes attributes);
}

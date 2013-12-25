package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.Schema;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

public class Type {
  @Nonnull
  private final ArrayList < SubstitutionGroup > substitutionGroups = new ArrayList < SubstitutionGroup >();
  @Nonnull
  private final ArrayList < Element > elements = new ArrayList < Element >();
  @Nonnull
  private final String className;
  @Nullable
  private final String extendsName;

  public Type(@Nonnull final String classNameParam, @Nullable final String extendsNameParam) {
    className = classNameParam;
    extendsName = extendsNameParam;
  }

  public void addElement(final Element child) {
    elements.add(child);
  }

  public void addSubstitutionGroup(final SubstitutionGroup substitutionGroup) {
    substitutionGroups.add(substitutionGroup);
  }

  @Nonnull
  public XmlProcessorType createXmlProcessor(@Nonnull final Schema schema) throws Exception {
    ArrayList < SubstitutionGroup > substitutionGroups = new ArrayList < SubstitutionGroup >();
    ArrayList < Element > elements = new ArrayList < Element >();

    Type typeParent = getTypeParent(schema);

    if (typeParent != null) {
      substitutionGroups.addAll(typeParent.getSubstituitionGroup());
      elements.addAll(typeParent.getElements());
    }

    substitutionGroups.addAll(this.substitutionGroups);
    elements.addAll(this.elements);
    return schema.getInstance(className, elements, substitutionGroups);
  }

  @Nonnull
  private Collection < ? extends Element > getElements() {
    return elements;
  }

  @Nonnull
  private Collection < ? extends SubstitutionGroup > getSubstituitionGroup() {
    return substitutionGroups;
  }

  @Nonnull
  public XmlProcessorType createXmlProcessorFromType(@Nonnull final Schema schema, @Nonnull final Type typeParent) throws Exception {
    return schema.getInstance(typeParent.className, elements, substitutionGroups);
  }

  @Nullable
  public Type getTypeParent(@Nonnull final Schema schema) throws Exception {
    if (extendsName == null) {
      return null;
    }
    if (schema.isTypeAvailable(extendsName)) {
      return schema.getType(extendsName);
    } else {
      return null;
    }
  }

  public void addChildren(
      @Nonnull final Schema schema,
      @Nonnull final XmlProcessorType processor,
      @Nonnull final String tagName,
      final String tagType,
      @Nonnull final OccursEnum occurs) throws Exception {
    childElement(schema, processor, tagName, occurs);
  }

  private void childElement(
      @Nonnull final Schema schema,
      @Nonnull final XmlProcessorType processor,
      @Nonnull final String tagName,
      @Nonnull final OccursEnum occurs) throws Exception {
    XmlProcessorElement child = new XmlProcessorElement(
        createXmlProcessor(schema),
        tagName,
        occurs);
    processor.addElementProcessor(child);
  }

  @Nonnull
  public String getClassName() {
    return className;
  }
}

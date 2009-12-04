package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.xml.lwxs.Schema;

public class Type {
  private ArrayList < SubstitutionGroup > substitutionGroups = new ArrayList < SubstitutionGroup >();
  private ArrayList < Element > elements = new ArrayList < Element >();
  private String className;
  private String extendsName;

  public Type() {
  }

  public Type(final String classNameParam, final String extendsNameParam) throws Exception {
    if (classNameParam == null) {
      throw new Exception("className must not be null");
    }
    className = classNameParam;
    extendsName = extendsNameParam;
  }

  public void addElement(final Element child) {
    elements.add(child);
  }

  public void addSubstitutionGroup(final SubstitutionGroup substitutionGroup) {
    substitutionGroups.add(substitutionGroup);
  }

  public XmlProcessorType createXmlProcessor(final Schema schema) throws Exception {
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

  private Collection < ? extends Element > getElements() {
    return elements;
  }

  private Collection < ? extends SubstitutionGroup > getSubstituitionGroup() {
    return substitutionGroups;
  }

  public XmlProcessorType createXmlProcessorFromType(final Schema schema, final Type typeParent) throws Exception {
    return schema.getInstance(typeParent.className, elements, substitutionGroups);
  }

  public Type getTypeParent(final Schema schema) throws Exception {
    if (schema.isTypeAvailable(extendsName)) {
      return schema.getType(extendsName);
    } else {
      return null;
    }
  }

  public void addChildren(
      final Schema schema,
      final XmlProcessorType processor,
      final String tagName,
      final String tagType,
      final OccursEnum occurs) throws Exception {
    childElement(schema, processor, tagName, tagType, occurs);
  }

  private void childElement(
      final Schema schema,
      final XmlProcessorType processor,
      final String tagName,
      final String tagType,
      final OccursEnum occurs) throws Exception {
    XmlProcessorElement child = new XmlProcessorElement(
        createXmlProcessor(schema),
        tagName,
        tagType,
        occurs);
    processor.addElementProcessor(child);
  }

  public String getClassName() {
    return className;
  }
}

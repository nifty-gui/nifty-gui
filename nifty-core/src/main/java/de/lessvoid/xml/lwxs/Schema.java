package de.lessvoid.xml.lwxs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.lwxs.elements.Element;
import de.lessvoid.xml.lwxs.elements.SubstitutionGroup;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.lwxs.elements.XmlProcessorType;
import de.lessvoid.xml.lwxs.processor.IncludeProcessor;
import de.lessvoid.xml.lwxs.processor.TypeProcessor;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class Schema implements XmlProcessor {
  private static Logger log = Logger.getLogger(Schema.class.getName());
  private Map < String, Type > types = new HashMap < String, Type >();
  private String packageString;
  private String root;
  private String type;
  private NiftyResourceLoader resourceLoader;

  public Schema(final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    packageString = attributes.get("package");
    if (packageString == null) {
      throw new Exception("[package] attribute is a required attribute");
    }
    root = attributes.get("root");
    if (root == null) {
      throw new Exception("[root] attribute is a required attribute");
    }
    type = attributes.get("type");
    if (type == null) {
      throw new Exception("[type] attribute is a required attribute");
    }
    xmlParser.nextTag();
    xmlParser.zeroOrMore(
        new de.lessvoid.xml.xpp3.SubstitutionGroup()
          .add("include", new IncludeProcessor(resourceLoader, types))
          .add("type", new TypeProcessor(this)));
  }

  public void addType(final String name, final Type typeParam) {
    types.put(name, typeParam);
  }

  public Type getType(final String name) throws Exception {
    Type t = types.get(name);
    if (t == null) {
      log.warning("Type [" + name + "] not found. Creating new one on the fly");
      t = new Type(name, null);
      addType(name, t);
    }
    return t;
  }

  public boolean isTypeAvailable(final String name) {
    return types.containsKey(name);
  }

  public XmlType loadXml(final XmlParser parser) throws Exception {
    Type t = getType(type);
    XmlProcessorType xmlType = t.createXmlProcessor(this);
    parser.nextTag();
    parser.required(root, xmlType);
    return xmlType.getXmlType();
  }

  public XmlProcessorType getInstance(
      final String className,
      final Collection < Element > elements,
      final Collection < SubstitutionGroup> substitutionGroups) throws Exception {
    XmlProcessorType processor = new XmlProcessorType(packageString + "." + className);
    for (Element child : elements) {
      child.addToProcessor(this, processor);
    }
    for (SubstitutionGroup subst : substitutionGroups) {
      subst.addToProcessor(this, processor);
    }
    return processor;
  }

  public Map < String, Type > getTypes() {
    return types;
  }
}

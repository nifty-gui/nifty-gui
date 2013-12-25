package de.lessvoid.xml.lwxs;

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
import org.xmlpull.v1.XmlPullParserFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Schema implements XmlProcessor {
  @Nonnull
  private static final Logger log = Logger.getLogger(Schema.class.getName());
  @Nonnull
  private final Map < String, Type > types = new HashMap < String, Type >();
  @Nullable
  private String packageString;
  @Nullable
  private String root;
  @Nullable
  private String type;
  @Nonnull
  private final XmlPullParserFactory parserFactory;
  @Nonnull
  private final NiftyResourceLoader resourceLoader;

  public Schema(@Nonnull final XmlPullParserFactory parserFactory, @Nonnull final NiftyResourceLoader resourceLoader) {
    this.parserFactory = parserFactory;
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
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
          .add("include", new IncludeProcessor(parserFactory, resourceLoader, types))
          .add("type", new TypeProcessor(this)));
  }

  public void addType(@Nonnull final String name, @Nonnull final Type typeParam) {
    types.put(name, typeParam);
  }

  @Nonnull
  public Type getType(@Nonnull final String name) throws Exception {
    Type t = types.get(name);
    if (t == null) {
      log.warning("Type [" + name + "] not found. Creating new one on the fly");
      t = new Type(name, null);
      addType(name, t);
    }
    return t;
  }

  public boolean isTypeAvailable(@Nonnull final String name) {
    return types.containsKey(name);
  }

  @Nonnull
  public XmlType loadXml(@Nonnull final XmlParser parser) throws Exception {
    if (type == null) {
      throw new Exception("The type is null, something is wrong.");
    }
    Type t = getType(type);
    XmlProcessorType xmlType = t.createXmlProcessor(this);
    parser.nextTag();
    if (root == null) {
      throw new Exception("Root element is not set.");
    }
    parser.required(root, xmlType);
    XmlType result = xmlType.getXmlType();
    if (result == null) {
      throw new Exception("Failed to resolve XML data to a proper XML type.");
    }
    return result;
  }

  @Nonnull
  public XmlProcessorType getInstance(
      @Nonnull final String className,
      @Nonnull final Collection < Element > elements,
      @Nonnull final Collection < SubstitutionGroup> substitutionGroups) throws Exception {
    XmlProcessorType processor = new XmlProcessorType(packageString + "." + className);
    for (Element child : elements) {
      child.addToProcessor(this, processor);
    }
    for (SubstitutionGroup subst : substitutionGroups) {
      subst.addToProcessor(this, processor);
    }
    return processor;
  }

  @Nonnull
  public Map < String, Type > getTypes() {
    return types;
  }
}

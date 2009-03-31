package de.lessvoid.xml.lwxs.processor;

import java.util.Hashtable;
import java.util.Map;

import org.xmlpull.mxp1.MXParser;

import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class IncludeProcessor implements XmlProcessor {
  private Map < String, Type > types = new Hashtable < String, Type >();

  public IncludeProcessor(final Map < String, Type > typesParam) {
    types = typesParam;
  }

  public void process(
      final XmlParser xmlParser,
      final Attributes attributes) throws Exception {
    String filename = attributes.get("filename");

    Schema niftyXmlSchema = new Schema();
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(ResourceLoader.getResourceAsStream(filename));
    parser.nextTag();
    parser.required("nxs", niftyXmlSchema);

    types.putAll(niftyXmlSchema.getTypes());
    xmlParser.nextTag();
  }
}

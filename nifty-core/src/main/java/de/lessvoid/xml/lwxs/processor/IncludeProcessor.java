package de.lessvoid.xml.lwxs.processor;

import java.io.InputStream;
import java.util.Map;

import org.xmlpull.mxp1.MXParser;

import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class IncludeProcessor implements XmlProcessor {
  private final Map < String, Type > types;
  private final NiftyResourceLoader resourceLoader;

  public IncludeProcessor(final NiftyResourceLoader resourceLader, final Map < String, Type > typesParam) {
    this.resourceLoader = resourceLader;
    this.types = typesParam;
  }

  public void process(
      final XmlParser xmlParser,
      final Attributes attributes) throws Exception {
    String filename = attributes.get("filename");

    Schema niftyXmlSchema = new Schema(resourceLoader);
    XmlParser parser = new XmlParser(new MXParser());
    InputStream stream = resourceLoader.getResourceAsStream(filename);
    try {
      parser.read(stream);
      parser.nextTag();
      parser.required("nxs", niftyXmlSchema);

      types.putAll(niftyXmlSchema.getTypes());
      xmlParser.nextTag();
    } finally {
      stream.close();
    }
  }
}

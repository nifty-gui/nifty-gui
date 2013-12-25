package de.lessvoid.xml.lwxs.processor;

import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class IncludeProcessor implements XmlProcessor {
  @Nonnull
  private final Map < String, Type > types;
  @Nonnull
  private final XmlPullParserFactory parserFactory;
  @Nonnull
  private final NiftyResourceLoader resourceLoader;

  public IncludeProcessor(
      @Nonnull final XmlPullParserFactory parserFactory,
      @Nonnull final NiftyResourceLoader resourceLoader,
      @Nonnull final Map<String, Type> typesParam) {
    this.resourceLoader = resourceLoader;
    this.types = typesParam;
    this.parserFactory = parserFactory;
  }

  @Override
  public void process(
      @Nonnull final XmlParser xmlParser,
      @Nonnull final Attributes attributes) throws Exception {
    String filename = attributes.get("filename");
    if (filename == null) {
      return;
    }

    Schema niftyXmlSchema = new Schema(parserFactory, resourceLoader);
    XmlParser parser = new XmlParser(parserFactory.newPullParser());
    InputStream stream = resourceLoader.getResourceAsStream(filename);
    if (stream != null) {
      try {
        parser.read(stream);
        parser.nextTag();
        parser.required("nxs", niftyXmlSchema);

        types.putAll(niftyXmlSchema.getTypes());
        xmlParser.nextTag();
      } finally {
        try {
          stream.close();
        } catch (IOException ignored) {}
      }
    }
  }
}

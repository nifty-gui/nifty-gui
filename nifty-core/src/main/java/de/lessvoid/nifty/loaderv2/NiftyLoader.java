package de.lessvoid.nifty.loaderv2;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.types.NiftyControlsType;
import de.lessvoid.nifty.loaderv2.types.NiftyStylesType;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.xpp3.XmlParser;
import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This is the Nifty-Loader that is supposed to take care of loading all the XML definitions used by the Nifty-GUI.
 * Its able to read the definition and style data from XML files and apply it to its parent Nifty-GUI instance.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class NiftyLoader {
  /**
   * The logging instance.
   */
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyLoader.class.getName());

  /**
   * The schema files that are known to the loader.
   */
  @Nonnull
  private final Map<String, Schema> schemes;

  /**
   * The time provider of the loader.
   */
  @Nonnull
  private final TimeProvider timeProvider;

  /**
   * The instance of this Nifty-GUI that is assigned to this loader.
   */
  @Nonnull
  private final Nifty nifty;

  /**
   * The pull parser factory that is used to create the parsers.
   */
  @Nonnull
  private final XmlPullParserFactory parserFactory;

  /**
   * Create a new instance of the Nifty-Loader.
   *
   * @param nifty        the assigned Nifty-GUI instance
   * @param timeProvider the time provider that is used to measure the performance
   */
  public NiftyLoader(@Nonnull final Nifty nifty, @Nonnull final TimeProvider timeProvider) {
    schemes = new HashMap<String, Schema>();
    try {
      parserFactory = XmlPullParserFactory.newInstance();
    } catch (XmlPullParserException e) {
      throw new RuntimeException("Failure to load the XmlPullParser, something is badly wrong!", e);
    }
    parserFactory.setValidating(false);
    parserFactory.setNamespaceAware(true);
    this.nifty = nifty;
    this.timeProvider = timeProvider;
  }

  /**
   * Load a Nifty-GUI XML file and get the type that is defined in the file. The XML file needs to be a valid XML GUI
   * definition.
   *
   * @param schemaId       the name of the schema that should be used to validate the XML file
   * @param inputStreamXml the input stream of the XML file, this stream <b>will be closed</b> by this function
   * @return the NiftyType that was load from the XML file
   * @throws Exception in case the loading fails at any point
   */
  @Nonnull
  public NiftyType loadNiftyXml(
      @Nonnull final String schemaId,
      @Nonnull @WillClose final InputStream inputStreamXml) throws Exception {
    try {
      long start = timeProvider.getMsTime();
      log.fine("loading new nifty xml file with schemaId [" + schemaId + "]");

      XmlParser parser = new XmlParser(parserFactory.newPullParser());
      parser.read(inputStreamXml);

      NiftyType niftyType = (NiftyType) getSchema(schemaId).loadXml(parser);
      niftyType.loadStyles(this, nifty);
      niftyType.loadControls(this);

      long end = timeProvider.getMsTime();
      log.fine("loaded nifty xml file with schemaId [" + schemaId + "] took [" + (end - start) + " ms]");

      return niftyType;
    } finally {
      closeSilently(inputStreamXml);
    }
  }

  @Deprecated
  public boolean validateNiftyXml(@Nonnull @WillClose final InputStream inputStreamXml) throws Exception {
    return validateNiftyXml("nifty.xsd", inputStreamXml);
  }

  public boolean validateNiftyXml(
      @Nonnull final String schemaId,
      @Nonnull @WillClose final InputStream inputStreamXml) throws Exception {
    long start = timeProvider.getMsTime();

    validate(schemaId, inputStreamXml);

    long end = timeProvider.getMsTime();
    log.fine("validating nifty xml took [" + (end - start) + " ms]");

    return true;
  }

  private void validate(
      @Nonnull final String schemaId,
      @Nonnull @WillClose final InputStream inputStreamXml) throws Exception {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder parser = documentBuilderFactory.newDocumentBuilder();
      Document document = parser.parse(inputStreamXml);

      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      InputStream schemaStream = nifty.getResourceAsStream(schemaId);
      try {
        Source schemaFile = new StreamSource(schemaStream);
        javax.xml.validation.Schema schema = factory.newSchema(schemaFile);
        javax.xml.validation.Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
      } finally {
        closeSilently(schemaStream);
      }
    } finally {
      closeSilently(inputStreamXml);
    }
  }

  public void loadStyleFile(
      @Nonnull final String schemaId,
      @Nonnull final String styleFilename,
      @Nonnull final NiftyType niftyType,
      @Nonnull final Nifty nifty) throws Exception {
    log.fine("loading new nifty style xml file [" + styleFilename + "] with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(parserFactory.newPullParser());
    InputStream stream = null;
    try {
      stream = nifty.getResourceAsStream(styleFilename);
      if (stream == null) {
        throw new IOException("Failed to load style. Resource \"" + styleFilename + "\" not found");
      }
      parser.read(stream);
      NiftyStylesType niftyStylesType = (NiftyStylesType) getSchema(schemaId).loadXml(parser);
      niftyStylesType.loadStyles(this, niftyType, nifty, log);
    } finally {
      closeSilently(stream);
    }
  }
  
  public void loadControlFile(
      @Nonnull final String schemaId,
      @Nonnull final String controlFilename,
      @Nonnull final NiftyType niftyType) throws Exception {
    log.fine("loading new nifty controls xml file [" + controlFilename + "] with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(parserFactory.newPullParser());
    InputStream stream = null;
    try {
      stream = nifty.getResourceAsStream(controlFilename);
      if (stream == null) {
        throw new IOException("Failed to load control. Resource \"" + controlFilename + "\" not found");
      }
      parser.read(stream);
      NiftyControlsType niftyControlsType = (NiftyControlsType) getSchema(schemaId).loadXml(parser);
      niftyControlsType.loadControls(this, niftyType);
    } finally {
      closeSilently(stream);
    }
  }

  public void registerSchema(
      @Nonnull final String schemaId,
      @Nonnull @WillClose final InputStream inputStreamSchema) throws Exception {
    try {
      Schema niftyXmlSchema = new Schema(parserFactory, nifty.getResourceLoader());
      XmlParser parser = new XmlParser(parserFactory.newPullParser());
      parser.read(inputStreamSchema);
      parser.nextTag();
      parser.required("nxs", niftyXmlSchema);
      schemes.put(schemaId, niftyXmlSchema);
    } finally {
      closeSilently(inputStreamSchema);
    }
  }

  @Nonnull
  private Schema getSchema(@Nonnull final String schemaId) throws Exception {
    Schema niftyXmlSchema = schemes.get(schemaId);
    if (niftyXmlSchema == null) {
      throw new Exception("unknown schemaId [" + schemaId + "]");
    }
    return niftyXmlSchema;
  }

  private static void closeSilently(@Nullable final Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (@Nonnull final IOException ignored) {
      }
    }
  }
}

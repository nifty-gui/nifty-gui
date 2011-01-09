package de.lessvoid.nifty.loaderv2;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xmlpull.mxp1.MXParser;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.types.NiftyControlsType;
import de.lessvoid.nifty.loaderv2.types.NiftyStylesType;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.xpp3.XmlParser;

public class NiftyLoader {
  private Logger log = Logger.getLogger(NiftyLoader.class.getName());
  private Map < String, Schema > schemes = new Hashtable < String, Schema >();
  private TimeProvider timeProvider;

  public NiftyLoader(final TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  public NiftyType loadNiftyXml(
      final String schemaId,
      final InputStream inputStreamXml,
      final Nifty nifty) throws Exception {
    long start = timeProvider.getMsTime();
    log.info("loading new nifty xml file with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(new MXParser());
    parser.read(inputStreamXml);

    NiftyType niftyType = (NiftyType) getSchema(schemaId).loadXml(parser);
    niftyType.loadStyles(this, nifty);
    niftyType.loadControls(this);

    long end = timeProvider.getMsTime();
    log.info("loaded nifty xml file with schemaId [" + schemaId + "] took [" + (end - start) + " ms]");

    return niftyType;
  }

  public boolean validateNiftyXml(final InputStream inputStreamXml) throws Exception {
    long start = timeProvider.getMsTime();

    validate(inputStreamXml);

    long end = timeProvider.getMsTime();
    log.info("validating nifty xml took [" + (end - start) + " ms]");

    return true;
  }
  
  private void validate(final InputStream inputStreamXml) throws Exception {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(true);
    DocumentBuilder parser = documentBuilderFactory.newDocumentBuilder(); 
    Document document = parser.parse(inputStreamXml);

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Source schemaFile = new StreamSource(ResourceLoader.getResourceAsStream("nifty.xsd"));
    javax.xml.validation.Schema schema = factory.newSchema(schemaFile);
    javax.xml.validation.Validator validator = schema.newValidator();
    validator.validate(new DOMSource(document));
  }

  public void loadStyleFile(
      final String schemaId,
      final String styleFilename,
      final NiftyType niftyType,
      final Nifty nifty) throws Exception {
    log.info("loading new nifty style xml file [" + styleFilename + "] with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(new MXParser());
    parser.read(ResourceLoader.getResourceAsStream(styleFilename));

    NiftyStylesType niftyStylesType = (NiftyStylesType) getSchema(schemaId).loadXml(parser);
    niftyStylesType.loadStyles(this, niftyType, nifty, log);
  }

  public void loadControlFile(
      final String schemaId,
      final String controlFilename,
      final NiftyType niftyType) throws Exception {
    log.info("loading new nifty controls xml file [" + controlFilename + "] with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(new MXParser());
    parser.read(ResourceLoader.getResourceAsStream(controlFilename));

    NiftyControlsType niftyControlsType = (NiftyControlsType) getSchema(schemaId).loadXml(parser);
    niftyControlsType.loadControls(this, niftyType);
  }

  public void registerSchema(final String schemaId, final InputStream inputStreamSchema) throws Exception {
    Schema niftyXmlSchema = new Schema();
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(inputStreamSchema);
    parser.nextTag();
    parser.required("nxs", niftyXmlSchema);
    schemes.put(schemaId, niftyXmlSchema);
  }

  private Schema getSchema(final String schemaId) throws Exception {
    Schema niftyXmlSchema = schemes.get(schemaId);
    if (niftyXmlSchema == null) {
      throw new Exception("unknown schemaId [" + schemaId + "]");
    }
    return niftyXmlSchema;
  }
}

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
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.xpp3.XmlParser;

public class NiftyLoader {
  private static Logger log = Logger.getLogger(NiftyLoader.class.getName());
  private Map < String, Schema > schemes = new Hashtable < String, Schema >();
  private TimeProvider timeProvider;
  private Nifty nifty;

  public NiftyLoader(final Nifty nifty, final TimeProvider timeProvider) {
    this.nifty = nifty;
    this.timeProvider = timeProvider;
  }

  public NiftyType loadNiftyXml(
      final String schemaId,
      final InputStream inputStreamXml,
      final Nifty nifty) throws Exception {
    try {
      long start = timeProvider.getMsTime();
      log.fine("loading new nifty xml file with schemaId [" + schemaId + "]");

      XmlParser parser = new XmlParser(new MXParser());
      parser.read(inputStreamXml);

      NiftyType niftyType = (NiftyType) getSchema(schemaId).loadXml(parser);
      niftyType.loadStyles(this, nifty);
      niftyType.loadControls(this);

      long end = timeProvider.getMsTime();
      log.fine("loaded nifty xml file with schemaId [" + schemaId + "] took [" + (end - start) + " ms]");

      return niftyType;
    } finally {
      inputStreamXml.close();
    }
  }

  public boolean validateNiftyXml(final InputStream inputStreamXml) throws Exception {
    long start = timeProvider.getMsTime();

    validate(inputStreamXml);

    long end = timeProvider.getMsTime();
    log.fine("validating nifty xml took [" + (end - start) + " ms]");

    return true;
  }
  
  private void validate(final InputStream inputStreamXml) throws Exception {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder parser = documentBuilderFactory.newDocumentBuilder(); 
      Document document = parser.parse(inputStreamXml);

      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      InputStream schemaStream = nifty.getResourceAsStream("nifty-1.3.xsd");
      try {
        Source schemaFile = new StreamSource(schemaStream);
        javax.xml.validation.Schema schema = factory.newSchema(schemaFile);
        javax.xml.validation.Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
      } finally {
        schemaStream.close();
      }
    } finally {
      inputStreamXml.close();
    }
  }

  public void loadStyleFile(
      final String schemaId,
      final String styleFilename,
      final NiftyType niftyType,
      final Nifty nifty) throws Exception {
    log.fine("loading new nifty style xml file [" + styleFilename + "] with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(new MXParser());
    InputStream stream = nifty.getResourceAsStream(styleFilename);
    try {
      parser.read(stream);
      NiftyStylesType niftyStylesType = (NiftyStylesType) getSchema(schemaId).loadXml(parser);
      niftyStylesType.loadStyles(this, niftyType, nifty, log);
    } finally {
      stream.close();
    }
  }

  public void loadControlFile(
      final String schemaId,
      final String controlFilename,
      final NiftyType niftyType) throws Exception {
    log.fine("loading new nifty controls xml file [" + controlFilename + "] with schemaId [" + schemaId + "]");

    XmlParser parser = new XmlParser(new MXParser());
    InputStream stream = nifty.getResourceAsStream(controlFilename);
    try {
      parser.read(stream);
      NiftyControlsType niftyControlsType = (NiftyControlsType) getSchema(schemaId).loadXml(parser);
      niftyControlsType.loadControls(this, niftyType);
    } finally {
      stream.close();
    }
  }

  public void registerSchema(final String schemaId, final InputStream inputStreamSchema) throws Exception {
    try {
      Schema niftyXmlSchema = new Schema(nifty.getResourceLoader());
      XmlParser parser = new XmlParser(new MXParser());
      parser.read(inputStreamSchema);
      parser.nextTag();
      parser.required("nxs", niftyXmlSchema);
      schemes.put(schemaId, niftyXmlSchema);
    } finally {
      inputStreamSchema.close();
    }
  }

  private Schema getSchema(final String schemaId) throws Exception {
    Schema niftyXmlSchema = schemes.get(schemaId);
    if (niftyXmlSchema == null) {
      throw new Exception("unknown schemaId [" + schemaId + "]");
    }
    return niftyXmlSchema;
  }
}

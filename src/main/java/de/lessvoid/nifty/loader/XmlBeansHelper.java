package de.lessvoid.nifty.loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

/**
 * XmlBeansHelper provides several helper method for the XmlBeans integration.
 * @author void
 */
public final class XmlBeansHelper {

  /**
   * the logger.
   */
  private static Log log = LogFactory.getLog(XmlBeansHelper.class);

  /**
   * This class is a Helper method that can't be instanziated.
   */
  private XmlBeansHelper() {
  }

  /**
   * Validate the given XmlObject and output any errors to error log.
   * @param xmlObject the XmlObject to check
   * @return true, when xml is valid and false otherwise
   */
  public static boolean validate(final XmlObject xmlObject) {
    ArrayList < String > validationErrors = new ArrayList < String > ();

    XmlOptions validationOptions = new XmlOptions();
    validationOptions.setErrorListener(validationErrors);

    boolean isValid = xmlObject.validate(validationOptions);
    if (!isValid) {
      Iterator < String > iter = validationErrors.iterator();
      while (iter.hasNext()) {
        log.error(iter.next());
      }
    }

    return isValid;
  }

  /**
   * Create Properties instance from all Attributes of the given XmlObject.
   * @param xmlObject the XmlObject to check
   * @return a filled Properties instance
   */
  public static Properties createProperties(final XmlObject xmlObject) {
    Properties props = new Properties();

    // select all attributes
    QNameSet qns = QNameSet.forWildcardNamespaceString("##any", xmlObject.getDomNode().getNamespaceURI());
    XmlObject[] xos = xmlObject.selectAttributes(qns);
    if (xos != null) {
      for (int i = 0; i < xos.length; i++) {
        XmlCursor cursor = xos[i].newCursor();
        props.put(cursor.getName().getLocalPart(), cursor.getTextValue());
      }
    }

    return props;
  }
}

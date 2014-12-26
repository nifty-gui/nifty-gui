package se.fishtank.css.selectors.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.dom.DOMHelper;
import se.fishtank.css.util.Assert;

public class GenericNodeAdapterDOM implements GenericNodeAdapter<Node> {

    @Override
    public Map<String, String> getAttributes(final Node node) {
        Map<String, String> result = new HashMap<String, String>();
    
        NamedNodeMap map = node.getAttributes();
        for (int i=0; i<map.getLength(); i++) {
            Node n = map.item(i);
            result.put(n.getNodeName(), n.getNodeValue());
        }
    
        return result;
    }
  
    @Override
    public List<Node> getChildNodes(final Node e) {
        List<Node> result = new ArrayList<Node>();
    
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            result.add(nl.item(i));
        }
    
        return result;
    }
  
    @Override
    public boolean isEmptyNode(final Node n) {
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            return true;
        }
        if (n.getNodeType() == Node.TEXT_NODE) {
            // TODO: Should we trim the text and see if it's length 0?
            String value = n.getNodeValue();
            if (value.length() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Node getPreviousSiblingElement(final Node e) {
        return DOMHelper.getPreviousSiblingElement(e);
    }

    @Override
    public String getNodeName(final Node e) {
        return e.getNodeName();
    }

    @Override
    public Node getNextSiblingElement(final Node e) {
        return DOMHelper.getNextSiblingElement(e);
    }

    @Override
    public Node getRootNode(final Node root) {
        if (root.getNodeType() == Node.DOCUMENT_NODE) {
            // Get the single element child of the document node.
            // There could be a doctype node and comment nodes that we must skip.
            Element element = DOMHelper.getFirstChildElement(root);
            Assert.notNull(element, "there should be a root element!");
            return element;
        } else {
            Assert.isTrue(root.getNodeType() == Node.ELEMENT_NODE, "root must be a document or element node!");
            return root;
        }
    }

    @Override
    public List<Node> getNodesByTagName(Node node, String tagName) throws NodeSelectorException {
        List<Node> result = new ArrayList<Node>();
        
        NodeList nl;
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            nl = ((Document) node).getElementsByTagName(tagName);
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            nl = ((Element) node).getElementsByTagName(tagName);
        } else {
            throw new NodeSelectorException("Only document and element nodes allowed!");
        }

        for (int i = 0; i < nl.getLength(); i++) {
            result.add(nl.item(i));
        }    
        return result;
    }

    @Override
    public String getTextContent(final Node e) {
        return e.getTextContent();
    }

    @Override
    public boolean isElementNode(Node e) {
        return e.getNodeType() == Node.ELEMENT_NODE;
    }

    @Override
    public boolean isCaseSensitive(Node root) {
      Document doc = (root instanceof Document) ? (Document) root : root.getOwnerDocument();
      return !doc.createElement("a").isEqualNode(doc.createElement("A"));
    }
}

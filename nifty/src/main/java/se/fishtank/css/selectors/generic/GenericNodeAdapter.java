package se.fishtank.css.selectors.generic;

import java.util.List;
import java.util.Map;

import se.fishtank.css.selectors.NodeSelectorException;

/**
 * This connects the GenericNodeSelector to whatever kind of Nodes you're using.
 *
 * @author Jens Hohmuth
 */
public interface GenericNodeAdapter<E> {
    /**
     * Get all attributes of the given Node as a map.
     *
     * @param e the Node to get attributes from
     * @return
     */
    Map<String, String> getAttributes(E e);

    /**
     * Get all child nodes for the given node.
     *
     * @param e the Node to get child nodes for
     * @return the child nodes of the given Node
     */
    List<E> getChildNodes(E e);

    /**
     * Return true if the given Node is considered to be empty. Check the DOM-implementation for an example. For many
     * other node trees this might just always return false.
     *
     * @param e the Node to check
     * @return true if the Node is empty and false if not
     */
    boolean isEmptyNode(E e);

    /**
     * Get the previous sibling of the given Node.
     *
     * @param e the Node to get the sibling for
     * @return the previous sibling for the Node
     */
    E getPreviousSiblingElement(E e);

    /**
     * Get the next sibling of the given Node.
     *
     * @param e the Node to get the sibling for
     * @return the next sibling for the given Node
     */
    E getNextSiblingElement(E e);

    /**
     * Get the name of the given Node.
     *
     * @param e the Node to get the name for
     * @return the name of the Node
     */
    String getNodeName(E e);

    /**
     * Return the root node for the given Node.
     *
     * @param e the node to get the Node for
     * @return the root node for the given Node
     */
    E getRootNode(E e);

    /**
     * Return all descendant nodes with the given tagName.
     *
     * @param e the Node to get the descendant nodes for
     * @param tagName the tag name to return nodes
     * @return the list of descendant Nodes with the given tag name
     * @throws NodeSelectorException
     */
    List<E> getNodesByTagName(E e, String tagName) throws NodeSelectorException;

    /**
     * Get the text content of this node. Can be null.
     * @param e the Node to check
     * @return the text content
     */
    String getTextContent(E e);

    /**
     * This is a somewhat compromise for DOM where getChildNodes() is allowed to return other kind of nodes and not only
     * ELEMENT_NODEs. With this method a Node e can be checked if it's actually a ELEMENT_NODE. For other trees this
     * method might always return true.
     *
     * @param e The Node to check
     * @return true if the Node is a ElementNode and false if not (probably only applicable to DOM-Implementations)
     */
    boolean isElementNode(E e);

    /**
     * Should comparing tags be processed case sensitive or not.
     *
     * @param e the root node
     * @return return true to use case sensitive comparison and false if not.
     */
    boolean isCaseSensitive(E e);
}

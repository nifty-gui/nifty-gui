package se.fishtank.css.selectors;

import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import se.fishtank.css.selectors.generic.GenericNodeAdapterDOM;
import se.fishtank.css.selectors.generic.GenericNodeSelector;

public class GenericNodeSelectorDOMAntonBugTest {
    
    private final NodeSelector<Node> nodeSelector;
    
    public GenericNodeSelectorDOMAntonBugTest() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(getClass().getResourceAsStream("/anton-bug.xml"));
        nodeSelector = new GenericNodeSelector<Node>(new GenericNodeAdapterDOM(), document);
    }
    
    @Test
    public void checkAdjacentSiblings() throws Exception {
        Set<Node> result = nodeSelector.querySelectorAll("token[tag^=l] + token");
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void checkGeneralSiblings() throws Exception {
        Set<Node> result = nodeSelector.querySelectorAll("token[tag^=l] ~ token");
        Assert.assertEquals(6, result.size());
    }
    
}

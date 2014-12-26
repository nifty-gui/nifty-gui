package se.fishtank.css.selectors;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import se.fishtank.css.selectors.dom.DOMNodeSelector;
import se.fishtank.css.selectors.generic.GenericNodeAdapterDOM;
import se.fishtank.css.selectors.generic.GenericNodeSelector;

public class GenericNodeSelectorDOMTest {
    
    private static final Map<String, Integer> testDataMap = new LinkedHashMap<String, Integer>();
    
    static {
        testDataMap.put("h3:contains('palace')", 1);
        testDataMap.put("h3:contains('in the palace')", 1);
        testDataMap.put("*", 251);
        testDataMap.put(":root", 1);
        testDataMap.put(":empty", 2);
        testDataMap.put("div:first-child", 51);
        testDataMap.put("div:nth-child(even)", 106);
        testDataMap.put("div:nth-child(2n)", 106);
        testDataMap.put("div:nth-child(odd)", 137);
        testDataMap.put("div:nth-child(2n+1)", 137);
        testDataMap.put("div:nth-child(n)", 243);
        testDataMap.put("script:first-of-type", 1);
        testDataMap.put("div:last-child", 53);
        testDataMap.put("script:last-of-type", 1);
        testDataMap.put("script:nth-last-child(odd)", 1);
        testDataMap.put("script:nth-last-child(even)", 1);
        testDataMap.put("script:nth-last-child(5)", 0);
        testDataMap.put("script:nth-of-type(2)", 1);
        testDataMap.put("script:nth-last-of-type(n)", 2);
        testDataMap.put("div:only-child", 22);
        testDataMap.put("meta:only-of-type", 1);
        testDataMap.put("div > div", 242);
        testDataMap.put("div + div", 190);
        testDataMap.put("div ~ div", 190);
        testDataMap.put("body", 1);
        testDataMap.put("body div", 243);
        testDataMap.put("div", 243);
        testDataMap.put("div div", 242);
        testDataMap.put("div div div", 241);
        testDataMap.put("div, div, div", 243);
        testDataMap.put("div, a, span", 243);
        testDataMap.put(".dialog", 51);
        testDataMap.put("div.dialog", 51);
        testDataMap.put("div .dialog", 51);
        testDataMap.put("div.character, div.dialog", 99);
        testDataMap.put("#speech5", 1);
        testDataMap.put("div#speech5", 1);
        testDataMap.put("div #speech5", 1);
        testDataMap.put("div.scene div.dialog", 49);
        testDataMap.put("div#scene1 div.dialog div", 142);
        testDataMap.put("#scene1 #speech1", 1);
        testDataMap.put("div[class]", 103);
        testDataMap.put("div[class=dialog]", 50);
        testDataMap.put("div[class^=dia]", 51);
        testDataMap.put("div[class$=log]", 50);
        testDataMap.put("div[class*=sce]", 1);
        testDataMap.put("div[class|=dialog]", 50);
        testDataMap.put("div[class~=dialog]", 51);
        testDataMap.put("head > :not(meta)", 2);
        testDataMap.put("head > :not(:last-child)", 2);
        testDataMap.put("div:not(div.dialog)", 192);
    }
    
    private final NodeSelector<Node> nodeSelector;
    
    public GenericNodeSelectorDOMTest() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(getClass().getResourceAsStream("/test.html"));
        nodeSelector = new GenericNodeSelector<Node>(new GenericNodeAdapterDOM(), document);
    }
    
    @Test
    public void checkSelectors() throws Exception {
        for (Map.Entry<String, Integer> entry : testDataMap.entrySet()) {
            System.out.printf("selector: %s, expected: %d%n", entry.getKey(), entry.getValue());
            Set<Node> result = nodeSelector.querySelectorAll(entry.getKey());
            Assert.assertEquals(entry.getKey(), (int) entry.getValue(), (int) result.size());
        }
    }
    
    @Test
    public void checkRoot() throws NodeSelectorException {
        Node root = nodeSelector.querySelector(":root");
        Assert.assertEquals(Node.ELEMENT_NODE, root.getNodeType());
        Assert.assertEquals("html", root.getNodeName());
        
        DOMNodeSelector subSelector = new DOMNodeSelector(nodeSelector.querySelector("div#scene1"));
        Set<Node> subRoot = subSelector.querySelectorAll(":root");
        Assert.assertEquals(1, subRoot.size());
        Assert.assertEquals("scene1", subRoot.iterator().next().getAttributes().getNamedItem("id").getTextContent());
        Assert.assertEquals((int) testDataMap.get("div#scene1 div.dialog div"), subSelector.querySelectorAll(":root div.dialog div").size());
        
        Node meta = nodeSelector.querySelector(":root > head > meta");
        Assert.assertEquals(meta, new DOMNodeSelector(meta).querySelector(":root"));
    }
    
}

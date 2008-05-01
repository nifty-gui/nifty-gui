package de.lessvoid.nifty.loader.xpp3;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import de.lessvoid.nifty.loader.xpp3.processor.XmlElementProcessor;

import junit.framework.TestCase;

public class SubstitutionGroupTest extends TestCase {

  private SubstitutionGroup subst;
  
  public void setUp() {
    subst = new SubstitutionGroup();
  }

  public void testAdd() {
    XmlElementProcessor tag1 = createMock(XmlElementProcessor.class);
    replay(tag1);

    assertNull(subst.matches("tag1"));

    subst.add("tag1", tag1);
    assertEquals(tag1, subst.matches("tag1"));
    assertNull(subst.matches("tag"));

    verify(tag1);
  }
}

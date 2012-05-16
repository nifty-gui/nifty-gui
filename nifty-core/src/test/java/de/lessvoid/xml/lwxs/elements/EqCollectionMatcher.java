package de.lessvoid.xml.lwxs.elements;

import java.util.Collection;
import java.util.Iterator;

import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;

public class EqCollectionMatcher implements IArgumentMatcher {
  private Collection<Object> expectedCollection;
  private String errorText;

  public EqCollectionMatcher(final Collection<Object> expectedCollectionParam) {
    expectedCollection = expectedCollectionParam;
  }

  public boolean matches(final Object actual) {
    if (!(actual instanceof Collection)) {
      errorText = "[not an Collection instance";
      return false;
    }
    @SuppressWarnings ("unchecked")
    Collection<Object> actualCollection = (Collection<Object>) actual;
    if (expectedCollection.isEmpty()) {
      errorText = "[expected collection is empty, actual is not]";
      return actualCollection.isEmpty();
    }
    Iterator<Object> expectedIt = expectedCollection.iterator();
    Iterator<Object> actualIt = actualCollection.iterator();
    while (expectedIt.hasNext()) {
      Object expectedO = expectedIt.next();
      Object actualO = actualIt.next();
      if (expectedO != actualO) {
        errorText = "[expectedO : [" + expectedO + "] != actualO : [" + actualO + "]";
        return false;
      }
    }
    return true;
  }

  public void appendTo(final StringBuffer buffer) {
    buffer.append("mismatch dude: " + errorText);
  }

  public static Collection <Object> eqCollection(final Collection<Object> in) {
    EasyMock.reportMatcher(new EqCollectionMatcher(in));
    return null;
  }
}

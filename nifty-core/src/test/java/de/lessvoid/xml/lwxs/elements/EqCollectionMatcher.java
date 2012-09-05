package de.lessvoid.xml.lwxs.elements;

import java.util.Collection;
import java.util.Iterator;

import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;

@SuppressWarnings("rawtypes")
public class EqCollectionMatcher implements IArgumentMatcher {
  private Collection expectedCollection;
  private String errorText;

  public EqCollectionMatcher(final Collection expectedCollectionParam) {
    expectedCollection = expectedCollectionParam;
  }

  public boolean matches(final Object actual) {
    if (!(actual instanceof Collection)) {
      errorText = "[not an Collection instance";
      return false;
    }
    Collection actualCollection = ((Collection) actual);
    if (expectedCollection.isEmpty()) {
      errorText = "[expected collection is empty, actual is not]";
      return actualCollection.isEmpty();
    }
    Iterator expectedIt = expectedCollection.iterator();
    Iterator actualIt = actualCollection.iterator();
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

  public static Collection eqCollection(final Collection in) {
    EasyMock.reportMatcher(new EqCollectionMatcher(in));
    return null;
  }
}

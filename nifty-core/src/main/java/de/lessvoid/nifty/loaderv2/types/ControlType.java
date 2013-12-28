package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.types.helper.NullElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ControlType extends ElementType {
  private static final Logger log = Logger.getLogger(ControlType.class.getName());

  public ControlType() {
    super();
  }

  public ControlType(@Nonnull final ControlType src) {
    super(src);
  }

  public ControlType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  @Nonnull
  public ElementType copy() {
    return new ControlType(this);
  }

  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<control>");
    setElementRendererCreator(new NullElementRendererCreator());
  }

  @Nullable
  private String getType() {
    String type = getAttributes().get("type");
    if (type != null) {
      return type;
    }

    return getAttributes().get("name");
  }

  @Override
  void internalApplyControl(@Nonnull final Nifty nifty) {
    ControlDefinitionType controlDefinition = nifty.resolveControlDefinition(getType());
    if (controlDefinition == null) {
      log.warning("controlDefinition [" + getType() + "] missing.");
      return;
    }

    Collection<ElementType> childCopy = new ArrayList<ElementType>();
    childCopy.addAll(elements);

    mergeFromElementType(controlDefinition);

    String childRootId = getAttributes().get("childRootId");
    if (childRootId != null) {
      if (!addChildrenToChildRoot(this, childRootId, childCopy)) {
        log.warning("childRootId [" + childRootId + "] could not be found in any childs of [" + this + "]");
      }
    }
  }

  @Override
  void makeFlatControlsInternal() {
    if (!elements.isEmpty()) {
      mergeFromElementType(elements.get(0));
    }
    String id = getAttributes().get("id");
    resolveIds(this, id, id);
  }

  private void resolveIds(
      @Nonnull final ElementType parent,
      @Nullable final String parentId,
      @Nullable final String grantParentId) {
    String usedParent = parentId == null ? grantParentId : parentId;
    for (ElementType element : parent.elements) {
      String id = element.getAttributes().get("id");
      if (usedParent != null && id != null && id.startsWith("#")) {
        /*
          Simply using the last segment of the ID does not create valid IDs in all cases because the IDs are in some
          cases not resolved in the correct order. Using always the entire ID works,
          but creates IDs that are very long and contain a lot of duplicate parts.

          The trick is to split both the parent and the child id into its parts and check if they overlap at some
          point. If a overlap is detected, this part is removed from the child ID.
         */
        id = usedParent + getRealChildrenId(usedParent, id);
        element.getAttributes().set("id", id);
      }
      resolveIds(element, id, grantParentId);
    }
  }

  @Nonnull
  private static final Pattern ID_SPLIT = Pattern.compile("(?<=.)(?=#)");

  /**
   * Get the element ID without the part that overlaps with the parent ID.
   * <p/>
   * This function is package-local so it can be accessed by the testing class.
   *
   * @param parent the parent id
   * @param child  the child id
   * @return the child id without the overlapping part
   */
  @Nonnull
  static String getRealChildrenId(@Nonnull final CharSequence parent, @Nonnull final String child) {
    int lastSharpIndex = child.lastIndexOf('#');
    if (lastSharpIndex <= 0) {
      /* There is only one # in the ID, marking the final part of the ID, we have to add this, no matter what. */
      return child;
    }

    /* Lets break it up. */
    final String[] elementIdParts = ID_SPLIT.split(child);
    final String[] parentIdParts = ID_SPLIT.split(parent);
    int currentIndex = -1;
    while (currentIndex < parentIdParts.length) {
      currentIndex = findInArray(parentIdParts, currentIndex + 1, elementIdParts[0]);
      if (currentIndex == -1) {
        /* No overlap detected, merge ID parts and be done with it. */
        return child;
      } else if (isOverlapping(parentIdParts, currentIndex, elementIdParts)) {
        /* Check if there really is a overlap and build the remaining parts of the ID to a new ID */
        final int remaining = parentIdParts.length - currentIndex;
        StringBuilder idBuilder = new StringBuilder();
        for (int i = remaining; i < elementIdParts.length; i++) {
          idBuilder.append(elementIdParts[i]);
        }
        return idBuilder.toString();
      }
    }
    return child;
  }

  @SuppressWarnings("MethodCanBeVariableArityMethod")
  private static boolean isOverlapping(
      @Nonnull final String[] parentId,
      final int startIndex,
      @Nonnull final String[] childId) {
    final int remaining = parentId.length - startIndex;
    if (remaining >= childId.length) {
      return false;
    }
    for (int i = startIndex; i < parentId.length; i++) {
      if (!parentId[i].equals(childId[i - startIndex])) {
        return false;
      }
    }
    return true;
  }

  private static int findInArray(
      @Nonnull final String[] haystack,
      final int startIndex,
      @Nonnull final String needle) {
    for (int i = startIndex; i < haystack.length; i++) {
      if (haystack[i].equals(needle)) {
        return i;
      }
    }
    return -1;
  }

  private boolean addChildrenToChildRoot(
      @Nonnull final ElementType elementType,
      @Nonnull final String childRootId,
      @Nonnull final Collection<ElementType> children) {
    if (children.isEmpty()) {
      return true;
    }
    for (ElementType element : elementType.elements) {
      if (childRootId.equals(element.getAttributes().get("id"))) {
        element.elements.clear();
        element.elements.addAll(children);
        return true;
      } else if (addChildrenToChildRoot(element, childRootId, children)) {
        return true;
      }
    }
    return false;
  }
}

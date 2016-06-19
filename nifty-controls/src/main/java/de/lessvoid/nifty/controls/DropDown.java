package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.spi.render.RenderFont;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


/**
 * The DropDown interface is the Nifty control API view of a Nifty DropDown control.
 *
 * @param <T>
 * @author void
 */
public interface DropDown<T> extends NiftyControl {

  /**
   * Change the DropDownViewConverter for this DropDown.
   *
   * @param viewConverter DropDownViewConverter
   */
  void setViewConverter(@Nonnull DropDownViewConverter<T> viewConverter);

  /**
   * Add a item to the DropDown.
   *
   * @param newItem the item to add
   */
  void addItem(@Nonnull final T newItem);

  /**
   * Insert the given item at the given index.
   *
   * @param item  item
   * @param index the index to insert the item.
   */
  void insertItem(@Nonnull T item, int index);

  /**
   * Retrieve the number of items in the DropDown.
   *
   * @return number of items.
   */
  int itemCount();

  /**
   * Clear all items from this DropDown.
   */
  void clear();

  /**
   * Select the item with the given index in the DropDown.
   *
   * @param selectionIndex the item index to select in the DropDown
   */
  void selectItemByIndex(final int selectionIndex);

  /**
   * Select the item in the DropDown.
   *
   * @param item the item to select
   */
  void selectItem(@Nonnull final T item);

  /**
   * Get the current selection.
   *
   * @return the selected item in this DropDown.
   */
  @Nullable
  T getSelection();

  /**
   * Returns the index of the current selected item in the list of all items.
   *
   * @return selected item index
   */
  int getSelectedIndex();

  /**
   * Remove an item from the DropDown by index.
   *
   * @param itemIndex remove the item with the given index from the DropDown
   */
  void removeItemByIndex(int itemIndex);

  /**
   * Remove the given item from the DropDown.
   *
   * @param item the item to remove from the DropDown
   */
  void removeItem(T item);

  /**
   * Get all items of this DropDown.
   *
   * @return list of all items
   */
  @Nonnull
  List<T> getItems();

  /**
   * Add all items to the DropDown.
   *
   * @param itemsToAdd collection of items to add
   */
  void addAllItems(@Nonnull Collection<T> itemsToAdd);

  /**
   * Remove all items given in the List from this DropDown.
   *
   * @param itemsToRemove collection of items to remove
   */
  void removeAllItems(@Nonnull Collection<T> itemsToRemove);

  /**
   * You'll need to implement this interface to change the way your model class T needs
   * to be displayed in the DropDown. If you omit it then Nifty will use its default
   * implementation which simply calls T.toString();
   *
   * @param <T>
   * @author void
   */
  public interface DropDownViewConverter<T> {
    /**
     * Display the given item in the given element.
     *
     * @param itemElement the element to display the item in
     * @param item        the item to display
     */
    void display(@Nonnull Element itemElement, @Nonnull T item);

    /**
     * Return the width in pixel of the given item rendered for the given element.
     *
     * @param itemElement the element to render
     * @param item        the item to render
     * @return the width of the element after the item has been applied to it
     */
    int getWidth(@Nonnull Element itemElement, @Nonnull T item);
  }

  /**
   * A simple implementation of DropDownViewConverter that will just use item.toString().
   * This is the default DropDownViewConverter used when you don't set a different implementation.
   *
   * @param <T>
   * @author void
   */
  public class SimpleDropDownViewConverter<T> implements DropDownViewConverter<T> {
    private final Logger log = Logger.getLogger(SimpleDropDownViewConverter.class.getName());

    @Override
    public void display(@Nonnull final Element element, @Nullable final T item) {
      TextRenderer renderer = element.getRenderer(TextRenderer.class);
      if (renderer == null) {
        log.warning(
            "you're using the SimpleDropDownViewConverter but there is no TextRenderer on the element."
                + "You've probably changed the item template but did not provided your own "
                + "DropDownViewConverter to the DropDown.");
        return;
      }
      if (item != null) {
        renderer.setText(item.toString());
      } else {
        renderer.setText("");
      }
    }

    @Override
    public int getWidth(@Nonnull final Element element, @Nonnull final T item) {
      TextRenderer renderer = element.getRenderer(TextRenderer.class);
      if (renderer == null) {
        log.warning(
            "you're using the SimpleDropDownViewConverter but there is no TextRenderer on the element."
                + "You've probably changed the item template but did not provided your own "
                + "DropDownViewConverter to the DropDown.");
        return 0;
      }
      RenderFont font = renderer.getFont();
      if (font == null) {
        return 0;
      }
      return font.getWidth(item.toString());
    }
  }
}
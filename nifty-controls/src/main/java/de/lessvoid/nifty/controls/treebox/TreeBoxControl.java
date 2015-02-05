/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.listbox.ListBoxItemProcessor;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.logging.Logger;

/**
 * This is the control of the tree box. Its basically a list box that displays a tree.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Use {@link TreeBox}
 */
@Deprecated
public final class TreeBoxControl<T> extends ListBoxControl<TreeItem<T>> implements TreeBox<T> {
  @Nonnull
  private static final Logger log = Logger.getLogger(TreeBoxControl.class.getName());
  /**
   * The default indentation.
   */
  private static final int DEFAULT_INDENT = 15;

  /**
   * Indention per level.
   */
  private int indentWidth;

  /**
   * The root node of the tree that is displayed.
   */
  @Nullable
  private TreeItem<T> treeRoot;

  /**
   * The used instance of the Nifty-GUI.
   */
  @Nullable
  private Nifty nifty;

  public TreeBoxControl() {
    getListBox().addItemProcessor(new ListBoxItemProcessor() {
      @Override
      public void processElement(@Nonnull final Element element) {
        @SuppressWarnings("unchecked")
        final TreeBoxItemController<T> listBoxItemController = element.getControl(TreeBoxItemController.class);
        if (listBoxItemController != null) {
          listBoxItemController.setParentControl(TreeBoxControl.this);
        }
      }
    });
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    super.bind(nifty, screen, element, parameter);
    this.nifty = nifty;

    indentWidth = parameter.getAsInteger("indentWidth", DEFAULT_INDENT);
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    super.inputEvent(inputEvent);
    return true;
  }

    @Override
    public void setTree(@Nonnull final TreeItem<T> treeRoot) {
        clear();
        this.treeRoot = treeRoot;
        treeRoot.setIndent(-indentWidth);
        insertChildren(treeRoot, 0);
    }

  /**
   * Get the {@link ListBox} that is used to display the entries.
   *
   * @return the {@link ListBox}
   */
  @Nonnull
  private ListBox<TreeItem<T>> getListBox() {
    return this;
  }

  /**
   * Update the contents of the {@link ListBox} that is used to display the tree. This should be done every time the
   * tree is changed. So in case the node is expanded or collapsed.
   *
   * @param selectItem the item that is supposed to be selected after the tree is updated
   */
  public void updateList(@Nonnull final TreeItem<T> selectItem) {
    updateList();
    selectItem(selectItem);
  }

  /**
   * Clear and build the tree again into the {@link ListBox}.
   */
  private void updateList() {
    if (treeRoot == null) {
      log.warning("Update of list triggered while root not is not set.");
      return;
    }
    final ListBox<TreeItem<T>> list = getListBox();
    list.clear();

    for (final TreeItem<T> item : treeRoot) {
      addListItem(list, item, 0);
    }
  }

  @Override
  @SuppressWarnings("RefusedBequest")
  public void publish(@Nonnull final ListBoxSelectionChangedEvent<TreeItem<T>> event) {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.publishEvent(id, new TreeItemSelectionChangedEvent<T>(this, event));
      }
    }
  }

  /**
   * Add a tree item to the list box and also insert all its children. This function is made for recursive calls in
   * order to update the current indent value.
   *
   * @param list          the {@link ListBox} that is filled with entries
   * @param currentItem   the current item that is supposed to be added to the list
   * @param currentIndent the indent of the current item
   */
  private void addListItem(
      @Nonnull final ListBox<TreeItem<T>> list,
      @Nonnull final TreeItem<T> currentItem,
      final int currentIndent) {
    list.addItem(currentItem);
    currentItem.setIndent(currentIndent);
    if (currentItem.isExpanded()) {
      for (final TreeItem<T> item : currentItem) {
        addListItem(list, item, currentIndent + indentWidth);
      }
    }
  }
  
    @Override
    public void select(TreeItem<T> item) {
        if (item != null) {
            if (!item.isVisible()) {
                TreeItem<T> parent = item.getParentItem();
                while (parent != null) {
                    if (parent.isVisible()) {
                        expand(parent, true);
                        break;
                    } else parent.setExpanded(true);
                    parent = parent.getParentItem();
                }
            }
            selectItem(item);
        }
    }
    
    public void insert(TreeItem<T> item) {
        if (item == null) item = new TreeItem<T>(null);
        if (item.getParentItem() != null)
            insert(item.getParentItem(), item);
        else
            insert(treeRoot, item);
    }

    @Override
    public void insert(TreeItem<T> parent, TreeItem<T> child) {
        if (parent != null) {
            parent.addTreeItem(child);
            int pos = getItems().indexOf(parent);
            if (pos == -1)
                return;
            if (parent.isVisible() && parent.isExpanded()) {
                child.setIndent(parent.getIndent() + indentWidth);
                insertItem(child, pos++  + parent.getChildCount());
                insertChildren(child, pos + parent.getChildCount());
            }
            refresh();
        } else insert(child);
    }
    
    private int insertChildren(TreeItem<T> parent, int pos) {
        if (parent.isVisible() && parent.isExpanded())
            for (TreeItem<T> child : parent) {
                child.setIndent(parent.getIndent() + indentWidth);
                insertItem(child, pos++);
                pos = insertChildren(child, pos);
            }
        return pos;
    }
  
    @Override
    public void expand(TreeItem<T> item, boolean show) {
        if (show == item.isExpanded()) {
            return;
        }

        item.setExpanded(show);
        int pos = getItems().indexOf(item) + 1;
        if (show)
            insertChildren(item, pos);
        else
            removeChildren(item);
    }
    
    private void removeChildren(TreeItem<T> parent) {
        for (TreeItem<T> item : parent) {
            removeItem(item);
            removeChildren(item);
        }
    }

    @Override
    public void remove(TreeItem<T> item) {
        if (item.getParentItem() != null) {
            item.getParentItem().removeTreeItem(item);
            removeItem(item);
            removeChildren(item);
        }
    }
}

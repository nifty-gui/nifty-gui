/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.listbox.ListBoxItemController;
import de.lessvoid.nifty.controls.listbox.ListBoxItemProcessor;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * This is the control of the tree box. Its basically a list box that displays a tree.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Use {@link TreeBox}
 */
@Deprecated
public final class TreeBoxControl<T> extends ListBoxControl<TreeItem<T>> implements TreeBox<T> {
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
	private TreeItem<T> treeRoot;

  /**
   * The used instance of the Nifty-GUI.
   */
	private Nifty nifty;

  public TreeBoxControl() {
    super();

    getListBox().addItemProcessor(new ListBoxItemProcessor() {
      @Override
      public void processElement(final Element element) {
        final TreeBoxItemController<T> listBoxItemController = element.getControl(TreeBoxItemController.class);
        if (listBoxItemController != null) {
          listBoxItemController.setParentControl(TreeBoxControl.this);
        }
      }
    });
  }

	@Override
	public void bind(final Nifty nifty, final Screen screen, final Element element, final Properties parameter,
                   final Attributes controlDefinitionAttributes) {
		super.bind(nifty, screen, element, parameter, controlDefinitionAttributes);
    this.nifty = nifty;

    indentWidth = controlDefinitionAttributes.getAsInteger("indentWidth", DEFAULT_INDENT);
	}

	@Override
	public boolean inputEvent(final NiftyInputEvent inputEvent) {
    super.inputEvent(inputEvent);
		return true;
	}

	@Override
	public void setTree(final TreeItem<T> treeRoot) {
		this.treeRoot = treeRoot;
    updateList();
	}

	private ListBox<TreeItem<T>> getListBox() {
    return this;
  }

  public void updateList(final TreeItem<T> selectItem) {
    updateList();
    selectItem(selectItem);
  }

  private void updateList() {
    final ListBox<TreeItem<T>> list = getListBox();
    list.clear();

    for (final TreeItem<T> item : treeRoot) {
      addListItem(list, item, 0);
    }
  }

  @Override
  public void publish(final ListBoxSelectionChangedEvent<TreeItem<T>> event) {
    if (getElement().getId() != null) {
      nifty.publishEvent(getElement().getId(), new TreeItemSelectionChangedEvent<T>(this, event));
    }
  }

  private void addListItem(final ListBox<TreeItem<T>> list, final TreeItem<T> currentItem, final int currentIndent) {
    list.addItem(currentItem);
    currentItem.setIndent(currentIndent);
    if (currentItem.isExpanded()) {
      for (final TreeItem<T> item : currentItem) {
        addListItem(list, item, currentIndent + indentWidth);
      }
    }
  }
}

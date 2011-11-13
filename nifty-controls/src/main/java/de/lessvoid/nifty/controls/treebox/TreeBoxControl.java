/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TreeBox;
import de.lessvoid.nifty.controls.TreeItem;
import de.lessvoid.nifty.controls.TreeItemSelectedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * 
 * @author ractoc
 */
public class TreeBoxControl extends AbstractController implements TreeBox {

	private int indentWidth = 15;
	private TreeItem tree;
	private Nifty nifty;
	private Element element;
	private ListBox<TreeEntryModelClass> treeListBox;
	private boolean processingItemSelected;

	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		super.bind(element);
		if (controlDefinitionAttributes.getAsInteger("indentWidth") != null) {
			indentWidth = controlDefinitionAttributes.getAsInteger(
					"indentWidth").intValue();
		}
		this.nifty = nifty;
		this.element = element;
		setListBox("#listbox");
		if (tree != null) {
			addTreeItem(treeListBox, tree, 0, 0);
		}
	}

	@Override
	public void onStartScreen() {
	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		return true;
	}

	@Override
	public void setTree(TreeItem treeRoot) {
		this.tree = treeRoot;
		treeListBox.clear();
		if (treeListBox != null) {
			removeAbsoleteTreeItems(treeListBox, tree);
			addTreeItem(treeListBox, tree, 0, 0);
			getElement().layoutElements();
		}
	}

	/**
	 * When the selection of the ListBox changes this method is called.
	 */
	@NiftyEventSubscriber(id = "tree-box#listbox")
	public void onListBoxSelectionChanged(final String id,
			final ListBoxSelectionChangedEvent<TreeEntryModelClass> event) {
		if (!processingItemSelected) {
			processingItemSelected = true;
			TreeEntryModelClass selectedItem = event.getSelection().get(0);
			Integer selectedIndex = event.getSelectionIndices().get(0);
			System.out.println("setting item " + selectedItem + "-"
					+ selectedItem.getTreeItem().getDisplayCaption()
					+ " to active");
			selectedItem.setActiveItem(true);
			TreeItem item = selectedItem.getTreeItem();
			if (!item.isLeaf()) {
				item.setExpanded(!item.isExpanded());
			}
			nifty.publishEvent(getId(), new TreeItemSelectedEvent(this, item));
			for (int i = 0; i < treeListBox.getItems().size(); i++) {
				TreeEntryModelClass treeListEntry = treeListBox.getItems().get(i);
				if (i == selectedIndex) {
					treeListEntry.setActiveItem(true);
				} else {
					treeListEntry.setActiveItem(false);
				}
				treeListBox.removeItemByIndex(i);
				treeListBox.insertItem(treeListEntry, i);
			}
			setTree(tree);
			processingItemSelected = false;
		}

	}

	@SuppressWarnings("unchecked")
	private void setListBox(final String name) {
		if (treeListBox == null) {
			treeListBox = (ListBox<TreeEntryModelClass>) element
					.findNiftyControl(name, ListBox.class);
		}
	}

	private int addTreeItem(ListBox<TreeEntryModelClass> treeListBox,
			TreeItem treeItem, int currentIndent, int itemIndex) {
		if (currentIndent != 0) {
			treeListBox.insertItem(new TreeEntryModelClass(indentWidth
					* currentIndent, treeItem), itemIndex);
			itemIndex++;
		}
		if (treeItem.isExpanded()) {
			for (Object childItem : treeItem.getTreeItems()) {
				itemIndex = addTreeItem(treeListBox, (TreeItem) childItem,
						currentIndent + 1, itemIndex);
			}
		}
		return itemIndex;
	}

	private void removeAbsoleteTreeItems(
			ListBox<TreeEntryModelClass> treeListBox, TreeItem tree) {
		for (TreeEntryModelClass treeListEntry : treeListBox.getItems()) {
			treeListEntry.setActiveItem(false);
			if (!tree.contains(treeListEntry.getTreeItem())) {
				treeListBox.removeItem(treeListEntry);
			}
		}
	}
}

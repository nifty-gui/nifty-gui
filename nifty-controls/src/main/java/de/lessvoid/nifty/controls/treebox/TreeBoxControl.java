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
    private boolean processingItemSelected;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        super.bind(element);
        this.nifty = nifty;
        if (tree != null) {
            ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
            addTreeItem(treeListBox, tree, 0);
        }
        ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
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
        ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
        treeListBox.clear();
        if (treeListBox != null) {
            addTreeItem(treeListBox, tree, 0);
            getElement().layoutElements();
        }
    }

    /**
     * When the selection of the ListBox changes this method is called.
     */
    @NiftyEventSubscriber(id = "tree-box#listbox")
    public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<TreeEntryModelClass> event) {
        System.out.println("item selected");
        System.out.println("processingItemSelected " + processingItemSelected);
        if (!processingItemSelected) {
            processingItemSelected = true;
            List<TreeEntryModelClass> selection = event.getSelection();

            System.out.println(selection.size() + " items selected");
//            ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
//            List<TreeEntryModelClass> selection = treeListBox.getSelection();

            for (TreeEntryModelClass selectedItem : selection) {
                TreeItem item = selectedItem.getTreeItem();
                System.out.println("found selected item: " + item.getDisplayCaption());
                System.out.println("item has " + item.getTreeItems().size() + " nodes");
                if (!item.isLeaf()) {
                    System.out.println("setting expanded to " + (!item.isExpanded()));
                    item.setExpanded(!item.isExpanded());
                } else {
// TODO: ractoc! fix this! :)                    nifty.publishEvent(getId(), new TreeItemSelectedEvent(this, item));
                }
            }
            System.out.println("setting tree");
//TODO: somehow this next line is where it goes wrong
            // the for loop is never executed for some reason.
            setTree(tree);
            for (TreeEntryModelClass selectedItem : selection) {
            System.out.println("setting selected item " + selectedItem.getTreeItem().getDisplayCaption());
                getListBox("#listbox").selectItem(selectedItem);
            }
            System.out.println("setting processingItemSelected to false");
            processingItemSelected = false;
        }
    }

    @SuppressWarnings("unchecked")
    private ListBox<TreeEntryModelClass> getListBox(final String name) {
        return (ListBox<TreeEntryModelClass>) getElement().findNiftyControl(name, ListBox.class);
    }

    private void addTreeItem(ListBox<TreeEntryModelClass> treeListBox, TreeItem treeItem, int currentIndent) {
        if (currentIndent != 0) {
            treeListBox.addItem(new TreeEntryModelClass(indentWidth * currentIndent, treeItem));
        }
        if (treeItem.isExpanded()) {
            for (Object childItem : treeItem.getTreeItems()) {
                addTreeItem(treeListBox, (TreeItem) childItem, currentIndent + 1);
            }
        }
    }
}

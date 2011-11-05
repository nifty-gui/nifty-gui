/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TreeBox;
import de.lessvoid.nifty.controls.TreeItem;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author ractoc
 */
public class TreeBoxControl extends AbstractController implements TreeBox {

    private int indentWidth = 15;
    private TreeItem tree;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        super.bind(element);
        System.out.println("binding the treebox");
        if (tree != null) {
            System.out.println("tree items found");
            ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
            addTreeItem(treeListBox, tree, 0);
        } else {
            System.out.println("no tree items found");
        }
        ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
        if (treeListBox != null) {
            System.out.println("tree listbox found");
        } else {
            System.out.println("no tree listbox found");
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
        ListBox<TreeEntryModelClass> treeListBox = getListBox("#listbox");
        if (treeListBox != null) {
            System.out.println("tree listbox found");
            addTreeItem(treeListBox, tree, 0);
            getElement().layoutElements();
        } else {
            System.out.println("no tree listbox found");
        }
    }

    @SuppressWarnings("unchecked")
    private ListBox<TreeEntryModelClass> getListBox(final String name) {
        return (ListBox<TreeEntryModelClass>) getElement().findNiftyControl(name, ListBox.class);
    }

    private void addTreeItem(ListBox<TreeEntryModelClass> treeListBox, TreeItem treeItem, int currentIndent) {
        if (treeItem.isExpanded()) {
            for (Object childItem : treeItem.getTreeItems()) {
                addTreeItem(treeListBox, (TreeItem) childItem, currentIndent + 1);
            }
        }
        System.out.println("adding tree item " + treeItem.getDisplayCaption());
        treeListBox.addItem(new TreeEntryModelClass(indentWidth * currentIndent, treeItem));
    }
}

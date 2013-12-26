package de.lessvoid.nifty.examples.defaultcontrols.treebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

/**
 * The TreeboxControlDialogController registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author ractoc
 */
public class TreeboxControlDialogController implements Controller {

  private Nifty nifty;

    @Override
    public void bind(
            @Nonnull final Nifty nifty,
            @Nonnull final Screen screen,
            @Nonnull final Element element,
            @Nonnull final Parameters parameter) {
      TreeBox<String> treebox = screen.findNiftyControl("tree-box", TreeBox.class);
        this.nifty = nifty;
        
        treebox.setTree(setupTree());
    }

    @Override
    public void init(@Nonnull final Parameters parameter) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onFocus(final boolean getFocus) {
    }

    @Override
    public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
        return false;
    }

    @Nonnull
    private TreeItem<String> setupTree() {
        TreeItem<String> treeRoot = new TreeItem<String>();
        TreeItem<String> branch1 = new TreeItem<String>("branch 1");
        branch1.setExpanded(true);
        TreeItem<String> branch11 = new TreeItem<String>("branch 1 1");
        TreeItem<String> branch12 = new TreeItem<String>("branch 1 2");
        branch1.addTreeItem(branch11);
        branch1.addTreeItem(branch12);
        TreeItem<String> branch2 = new TreeItem<String>("branch 2");
        TreeItem<String> branch21 = new TreeItem<String>("branch 2 1");
        TreeItem<String> branch211 = new TreeItem<String>("branch 2 1 1");
        branch2.addTreeItem(branch21);
        branch21.addTreeItem(branch211);
        treeRoot.addTreeItem(branch1);
        treeRoot.addTreeItem(branch2);

        return treeRoot;
    }
    
    @NiftyEventSubscriber(id="tree-box")
    public void treeItemSelected(final String id, @Nonnull final TreeItemSelectionChangedEvent<String> event) {
      final TextField text = nifty.getCurrentScreen().findNiftyControl("selectedItemText", TextField.class);

      if (!event.getSelection().isEmpty()) {
        text.setText(event.getSelection().get(0).getValue());
      }
    }
}

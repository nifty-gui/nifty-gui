/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * This is the controller of the tab controls.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated This class is not meant for direct usage. For accessing the controls provided for this element access the
 * {@link Tab} interface.
 */
@Deprecated
public class TabControl extends AbstractController implements Tab, TabGroupMember {
  @Nonnull
  private static final Logger log = Logger.getLogger(TabControl.class.getName());
  /**
   * The tab group that is the parent of this tab. This might be {@code null} for the time this tab is not a part of tab
   * group.
   */
  @Nullable
  private TabGroup parentGroup;

  /**
   * The caption of this tab.
   */
  @Nullable
  private String tabCaption;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);

    String caption = parameter.get("caption");
    if (caption == null) {
      log.warning("Missing caption for tab. Setting default value. This will not look good.");
      caption = "Tab-" + NiftyIdCreator.generate();
    }
    setCaption(caption);
  }

  @Override
  public void setCaption(@Nonnull final String caption) {
    if (!caption.equals(tabCaption)) {
      tabCaption = caption;
      if (parentGroup != null) {
        parentGroup.setTabCaption(this, caption);
      }
    }
  }

  @Override
  public boolean hasParent() {
    return parentGroup != null;
  }

  @Nonnull
  @Override
  public String getCaption() {
    if (tabCaption == null) {
      log.warning("Tab caption is not set yet.");
      return "";
    }
    return tabCaption;
  }

  @Nullable
  @Override
  public TabGroup getParentGroup() {
    return parentGroup;
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return true;
  }

  @Override
  public boolean isVisible() {
    if (parentGroup != null) {
      Tab selectedTab = parentGroup.getSelectedTab();
      return selectedTab != null && selectedTab.equals(this);
    }
    return false;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void setParentTabGroup(@Nullable final TabGroup tabGroup) {
    parentGroup = tabGroup;
    if (parentGroup != null) {
      parentGroup.setTabCaption(this, getCaption());
    }
  }
}

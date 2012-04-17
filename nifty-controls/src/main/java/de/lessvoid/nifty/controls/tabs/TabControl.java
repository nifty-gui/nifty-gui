/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * This is the controller of the tab controls.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated This class is not meant for direct usage. For accessing the controls provided for this element access the
 *             {@link Tab} interface.
 */
@Deprecated
public class TabControl extends AbstractController implements Tab, TabGroupMember {
  /**
   * The tab group that is the parent of this tab. This might be {@code null} for the time this tab is not a part of tab
   * group.
   */
  private TabGroup parentGroup;

  /**
   * The caption of this tab.
   */
  private String tabCaption;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    bind(element);

    if (controlDefinitionAttributes.isSet("caption")) {
      setCaption(controlDefinitionAttributes.get("caption"));
    } else {
      throw new IllegalStateException("Missing caption tag for the tab.");
    }
  }

  @Override
  public void setCaption(final String caption) {
    if (caption == null) {
      throw new IllegalArgumentException("Caption must not be empty or null");
    }
    if (!caption.equals(tabCaption)) {
      tabCaption = caption;
      if (hasParent()) {
        parentGroup.setTabCaption(this, caption);
      }
    }
  }

  @Override
  public boolean hasParent() {
    return parentGroup != null;
  }

  @Override
  public String getCaption() {
    return tabCaption;
  }

  @Override
  public TabGroup getParentGroup() {
    return parentGroup;
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return true;
  }

  @Override
  public boolean isVisible() {
    if (hasParent()) {
      return parentGroup.getSelectedTab().equals(this);
    }
    return false;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void setParentTabGroup(final TabGroup tabGroup) {
    parentGroup = tabGroup;
    if (parentGroup != null) {
      parentGroup.setTabCaption(this, tabCaption);
    }
  }
}

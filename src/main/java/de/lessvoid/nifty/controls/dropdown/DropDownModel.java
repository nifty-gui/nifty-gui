package de.lessvoid.nifty.controls.dropdown;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

/**
 * The actual Data the DropDownControl handles.
 * @author void
 */
public class DropDownModel
{
  private final List<String> items           = new ArrayList<String>();
  private int                selectedItemIdx = -1;

  public void addItem(final String description)
  {
    items.add(description);
  }

  public void initialize(final Nifty nifty,
                         final Screen currentScreen,
                         final Element popup)
  {
    int count = 0;
    for (String item : items)
    {
      Attributes attr = new Attributes();
      attr.set("text", item);
      nifty.addControl(nifty.getCurrentScreen(), popup, "dropDownControlItem", popup.getId() + "_" + count++, null, null, attr);
    }
  }

  public void setSelectedItemIdx(final int idx)
  {
    selectedItemIdx = idx;
  }

  public void setSelectedItem(final String text)
  {
    selectedItemIdx = items.indexOf(text);
  }

  public String getSelectedItem()
  {
    return items.get(selectedItemIdx);
  }

  public int getSelectedItemIdx()
  {
    return selectedItemIdx;
  }

  public void clear()
  {
    items.clear();
  }
}

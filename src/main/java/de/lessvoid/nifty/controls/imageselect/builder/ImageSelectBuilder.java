package de.lessvoid.nifty.controls.imageselect.builder;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class ImageSelectBuilder extends ControlBuilder {
  private List<String> imageList = new ArrayList<String>();

  public ImageSelectBuilder(final String id) {
    super(id, "imageSelect");
  }

  public void setImageWidth(final SizeValue imageWidth) {
    set("imageWidth", imageWidth.toString());
  }

  public void setImageHeight(final SizeValue imageHeight) {
    set("imageHeight", imageHeight.toString());
  }

  public void addImage(final String filename) {
    imageList.add(filename);
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    if (!imageList.isEmpty()) {
      StringBuffer imageListAttribute = new StringBuffer();
      boolean first = true;
      for (String image : imageList) {
        if (first) {
          first = false;
        } else {
          imageListAttribute.append(",");
        }
        imageListAttribute.append(image);
      }
      set("imageList", imageListAttribute.toString());
    }
    return super.buildInternal(nifty, screen, parent);
  }
}

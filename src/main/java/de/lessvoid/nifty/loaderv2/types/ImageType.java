package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.xml.xpp3.Attributes;

public class ImageType extends ElementType {
  public ImageType() {
    super();
  }

  public ImageType(final ImageType src) {
    super(src);
  }

  public ElementType copy() {
    return new ImageType(this);
  }

  public ImageType(final Attributes attributes) {
    super(attributes);
  }

  protected void makeFlat() {
    super.makeFlat();
    setTagName("<image>");
    setElementRendererCreator(new ElementRendererCreator() {
      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
        ElementRenderer[] renderer = new ElementRenderer[1];
        NiftyImage niftyImage = null;
//        String filename = getFilename();
//        if (filename != null) {
//          niftyImage = nifty.getRenderEngine().createImage(filename, false); // FIXME filter
//        }
//        renderer[0] = new ImageRenderer(niftyImage);
        renderer[0] = new ImageRenderer();
        return renderer;
      }
    });
  }

//  public String output(final int offset) {
//    return StringHelper.whitespace(offset) + "<image> " + super.output(offset);
//  }

//  public ElementRendererCreator getElementRendererBuilder() {
//    return new ElementRendererCreator() {
//      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
//        ElementRenderer[] renderer = new ElementRenderer[1];
//        NiftyImage niftyImage = null;
//        String filename = getFilename();
//        if (filename != null) {
//          niftyImage = nifty.getRenderEngine().createImage(filename, false); // FIXME filter
//        }
//        renderer[0] = new ImageRenderer(niftyImage);
//        return renderer;
//      }
//    };
//  }

  private String getFilename() {
    return getAttributes().get("filename");
  }
}

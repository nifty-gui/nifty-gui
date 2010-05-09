package de.lessvoid.nifty.render;

import java.util.Hashtable;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

public class NiftyImageManager {
  private Logger log = Logger.getLogger("NiftyImageManager");
  private RenderDevice renderDevice;
  private Map <String, ReferencedCountedImage> imageCache = new Hashtable < String, ReferencedCountedImage >();
  private Map <RenderImage, ReferencedCountedImage> backReference = new Hashtable <RenderImage, ReferencedCountedImage>();

  public NiftyImageManager(final RenderDevice renderDevice) {
    this.renderDevice = renderDevice;
  }

  public RenderImage getImage(String filename, boolean filterLinear) {
    String key = buildName(filename, filterLinear);
    if (imageCache.containsKey(key)) {
      RenderImage existingEntry = imageCache.get(key).addReference();
      log.finer(key + " exists [" + imageCache.get(key).references + "]");
      return existingEntry;
    }
    RenderImage createImage = renderDevice.createImage(filename, filterLinear);
    ReferencedCountedImage newEntry = new ReferencedCountedImage(key, createImage);
    backReference.put(createImage, newEntry);

    imageCache.put(key, newEntry);
    log.finer(key + " create [" + imageCache.get(key).references + "]");
    return newEntry.getRenderImage();
  }

  public void dispose(final RenderImage image) {
    if (backReference.containsKey(image)) {
      ReferencedCountedImage reference = backReference.get(image);
      if (reference.removeReference()) {
        imageCache.remove(reference.getName());
        backReference.remove(image);
      }
      log.finer(reference.getName() + " remove [" + reference.getReferences() + "]");
    }
  }

  public String getInfoString() {
    StringBuffer result = new StringBuffer(); 
    result.append("I have " + imageCache.size() + " entries in cache and " + backReference.size() + " backreference entries.\n");
    SortedSet<String> values = new TreeSet<String>();
    for (Map.Entry<String, ReferencedCountedImage> entry : imageCache.entrySet()) {
      values.add(entry.getKey() + " -> " + entry.getValue().getReferences() + "\n");
    }
    for (String s : values) {
      result.append(s);
    }
    return result.toString();
  }

  private String buildName(String filename, boolean filterLinear) {
    return filename + "|" + filterLinear;
  }

  private class ReferencedCountedImage {
    private String name;
    private RenderImage renderImage;
    private int references;

    public ReferencedCountedImage(final String name, final RenderImage renderImage) {
      this.name = name;
      this.renderImage = renderImage;
      this.references = 1;
    }

    public RenderImage addReference() {
      references++;
      return renderImage;
    }

    public boolean removeReference() {
      references--;
      if (references == 0) {
        log.finer(name + " DISPOSE");
        renderImage.dispose();
        return true;
      }
      return false;
    }

    public int getReferences() {
      return references;
    }

    public RenderImage getRenderImage() {
      return renderImage;
    }

    public String getName() {
      return name;
    }
  }
}

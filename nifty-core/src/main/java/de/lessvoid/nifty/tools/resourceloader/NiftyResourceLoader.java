package de.lessvoid.nifty.tools.resourceloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple wrapper around resource loading should anyone decide to change their
 * minds how this is meant to work in the future.
 * 
 * @author Kevin Glass
 * @auther void (made it a none static class)
 */
public class NiftyResourceLoader {
  /** The list of locations to be searched */
  private ArrayList<ResourceLocation> locations = new ArrayList<ResourceLocation>();
  
  public NiftyResourceLoader() {
    locations.add(new ClasspathLocation());
    locations.add(new FileSystemLocation(new File(".")));
  }
  
  /**
   * Add a location that will be searched for resources
   * 
   * @param location
   *          The location that will be searched for resoruces
   */
  public void addResourceLocation(ResourceLocation location) {
    locations.add(location);
  }
  
  /**
   * Remove a location that will be no longer be searched for resources
   * 
   * @param location
   *          The location that will be removed from the search list
   */
  public void removeResourceLocation(ResourceLocation location) {
    locations.remove(location);
  }
  
  /**
   * Remove all the locations, no resources will be found until new locations
   * have been added
   */
  public void removeAllResourceLocations() {
    locations.clear();
  }
  
  /**
   * Get a resource
   * 
   * @param ref
   *          The reference to the resource to retrieve
   * @return A stream from which the resource can be read
   */
  public InputStream getResourceAsStream(String ref) {
    InputStream in = null;
    
    for (int i = 0; i < locations.size(); i++) {
      ResourceLocation location = (ResourceLocation) locations.get(i);
      in = location.getResourceAsStream(ref);
      if (in != null) {
        break;
      }
    }
    
    if (in == null) {
      throw new RuntimeException("Resource not found: " + ref);
    }
    
    return new BufferedInputStream(in);
  }
  
  /**
   * Get a resource as a URL
   * 
   * @param ref
   *          The reference to the resource to retrieve
   * @return A stream from which the resource can be read
   */
  public URL getResource(String ref) {
    
    URL url = null;
    
    for (int i = 0; i < locations.size(); i++) {
      ResourceLocation location = (ResourceLocation) locations.get(i);
      url = location.getResource(ref);
      if (url != null) {
        break;
      }
    }
    
    if (url == null) {
      throw new RuntimeException("Resource not found: " + ref);
    }
    
    return url;
  }
}

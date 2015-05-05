/*
 * Copyright (c) 2015, Nifty GUI Community 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.internal.common.resourceloader.ClasspathLocation;
import de.lessvoid.nifty.internal.common.resourceloader.FileSystemLocation;

/**
 * A simple wrapper around resource loading should anyone decide to change their minds how this is meant to work in
 * the future.
 *
 * @author Kevin Glass
 * @author void (made it a none static class)
 */
public class NiftyResourceLoader {
  /**
   * The list of locations to be searched
   */
  @Nonnull
  private final List<NiftyResourceLocation> locations;

  public NiftyResourceLoader() {
    locations = new ArrayList<NiftyResourceLocation>();
    locations.add(new ClasspathLocation());
    locations.add(new FileSystemLocation(new File(".")));
  }

  /**
   * Add a location that will be searched for resources
   *
   * @param location The location that will be searched for resources
   */
  public void addResourceLocation(@Nonnull final NiftyResourceLocation location) {
    locations.add(location);
  }

  /**
   * Remove a location that will be no longer be searched for resources
   *
   * @param location The location that will be removed from the search list
   */
  public void removeResourceLocation(@Nonnull final NiftyResourceLocation location) {
    locations.remove(location);
  }

  /**
   * Remove all the locations, no resources will be found until new locations have been added
   */
  public void removeAllResourceLocations() {
    locations.clear();
  }

  /**
   * Get a resource
   *
   * @param ref The reference to the resource to retrieve
   * @return A stream from which the resource can be read or {@code null} in case the resource was not found
   */
  @Nullable
  public InputStream getResourceAsStream(@Nonnull final String ref) {
    InputStream in = null;

    for (int i = 0; i < locations.size(); i++) {
      NiftyResourceLocation location = locations.get(i);
      in = location.getResourceAsStream(ref);
      if (in != null) {
        break;
      }
    }

    if (in == null) {
      return null;
    }

    return new BufferedInputStream(in);
  }

  /**
   * Get a resource as a URL
   *
   * @param ref The reference to the resource to retrieve
   * @return A stream from which the resource can be read or {@code null} in case the resource was not found
   */
  @Nullable
  public URL getResource(@Nonnull final String ref) {
    URL url = null;

    for (int i = 0; i < locations.size(); i++) {
      NiftyResourceLocation location = locations.get(i);
      url = location.getResource(ref);
      if (url != null) {
        break;
      }
    }

    return url;
  }
}

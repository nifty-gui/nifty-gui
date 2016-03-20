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
package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyCallback;
import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.NiftyCanvasPainter;
import de.lessvoid.nifty.NiftyConfiguration;
import de.lessvoid.nifty.NiftyImage;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.spi.NiftyRenderDevice.FilterMode;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.niftyinternal.render.io.ImageLoader;
import de.lessvoid.niftyinternal.render.io.ImageLoaderImageIO;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillGradient;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyTransformationNode.transformationNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.types.NiftyLinearGradient.linearGradientFromAngleInDeg;

/**
 * Complex Demo / Example ... DeviantArt Viewer.
 * 
 * @author void
 */
public class UseCase_z00_DeviantArtViewer implements NiftyCanvasPainter {
  // we'll load images in a background thread and we'll use this Executor to run the background thread
  private Executor executor = Executors.newSingleThreadExecutor();

  // this queue is filled in the background thread and is processed in the main thread (where we actually upload the
  // data as a new NiftyImage)
  private Queue<RSSImageData> imageQueue = new LinkedBlockingQueue<>();

  // The current NiftyImage we'll display. This is replaced with a new NiftyImage whenever a new one is available.
  private NiftyImage image;

  // A newly available NiftyImage. This is usually null unless a new one is loaded.
  private NiftyImage pendingImage;

  // We'll set this to true when it's time to toggle the image.
  private boolean toggleImage;

  @Override
  public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
    canvas.drawImage(image, node.getWidth()/2 - image.getWidth()/2, node.getHeight()/2 - image.getHeight()/2);
  }

  public UseCase_z00_DeviantArtViewer(final Nifty nifty) {
    executor.execute(new LoadImagesBackgroundTask());

    image = nifty.createNiftyImage("nifty.png");
    final NiftyTransformationNode transformationNode = transformationNode();
    final NiftyContentNode contentNode = contentNode().setCanvasPainter(this);

    nifty
      .addNode(
        backgroundFillGradient(
          linearGradientFromAngleInDeg(0)
            .addColorStop(0.0, NiftyColor.teal())
            .addColorStop(1.0, NiftyColor.black())))
        .addNode(contentNode())
          .addNode(absoluteLayoutNode())
            .addNode(absoluteLayoutChildNode(nifty.getScreenWidth() / 2 - 400, nifty.getScreenHeight() / 2 - 300))
              .addNode(fixedSizeLayoutNode(800.f, 600.f))
                .addNode(transformationNode)
                  .addNode(backgroundFillColor(NiftyColor.fromString("#eeef")))
                    .addNode(contentNode);
    nifty.startTickAnimator(new NiftyCallback<Float>() {
      @Override public void execute(final Float time) {
        transformationNode.setPosX(Math.sin(time) * 512.);
        double scale = (Math.sin((time/2-Math.PI/4+Math.PI/2)*2.0)+1.0)/4.0+0.5;
        transformationNode.setScaleX(scale);
        transformationNode.setScaleY(scale);
        double angleX = Math.sin(time) * 180.0 + 180.0;
        transformationNode.setAngleX(angleX);

        if (pendingImage == null) {
          RSSImageData rssImageData = imageQueue.poll();
          if (rssImageData != null) {
            pendingImage = rssImageData.createNiftyImage(nifty);
          }
        }

        if (angleX >= 269.0 && angleX <= 271.0) {
          if (!toggleImage) {
            toggleImage = true;
            if (pendingImage != null) {
              image = pendingImage;
              pendingImage = null;
              contentNode.redraw();
            }
          }
        } else {
          toggleImage = false;
        }
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_z00_DeviantArtViewer.class, args,
      new NiftyConfiguration()
        //.showRenderBuckets(true)
        //.showRenderNodes(true)
        .renderBucketWidth(1024)
        .renderBucketHeight(768)
      );
  }

  /**
   * RSSImageData transports image data from the background thread to the main thread so that the data can be loaded.
   */
  private static class RSSImageData {
    private final int width;
    private final int height;
    private final ByteBuffer byteBuffer;

    private RSSImageData(final int width, final int height, final ByteBuffer byteBuffer) {
      this.width = width;
      this.height = height;
      this.byteBuffer = byteBuffer;
    }

    public NiftyImage createNiftyImage(final Nifty nifty) {
      return nifty.createNiftyImage(width, height, byteBuffer, FilterMode.Linear);
    }
  }

  /**
   * This Runnable implementation will parse the RSSFeed, extract and download the images and fill the imageQueue
   * with imageData.
   */
  private class LoadImagesBackgroundTask implements Runnable {
    @Override
    public void run() {
      while (true) {
        while (!imageQueue.isEmpty()) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
          }
        }
        RSSFeedParser parser = new RSSFeedParser("http://backend.deviantart.com/rss.xml?q=special%3Add&type=deviation");
        for (RSSFeedEntry f : parser.readFeed()) {
          try {
            imageQueue.add(f.loadImage());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * A RSSFeedEntry.
   */
  private static class RSSFeedEntry {
    private String title;
    private String url;

    RSSFeedEntry(final String title, final String url) {
      this.title = title;
      this.url = url;
    }

    @Override
    public String toString() {
      return title + ":" + url;
    }

    public RSSImageData loadImage() throws Exception {
      URL imageURL = new URL(url);
      ImageLoader imageLoader = new ImageLoaderImageIO();
      ByteBuffer byteBuffer = imageLoader.loadAsByteBufferRGBA(imageURL.openStream());
      return new RSSImageData(imageLoader.getTextureWidth(), imageLoader.getTextureHeight(), byteBuffer);
    }
  }

  /**
   * A brute-force RSSFeedParser.
   */
  private static class RSSFeedParser {
    private final URL url;

    RSSFeedParser(final String feedUrl) {
      try {
        this.url = new URL(feedUrl);
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }

    List<RSSFeedEntry> readFeed() {
      List<RSSFeedEntry> result = new ArrayList<>();
      try (InputStream in = read()) {
        XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(in);
        while (eventReader.hasNext()) {
          XMLEvent event = eventReader.nextEvent();
          if (event.isStartElement()) {
            if ("item".equals(event.asStartElement().getName().getLocalPart())) {
              eventReader.nextEvent();
              result.add(readItem(eventReader));
            }
          }
        }
        return result;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    private RSSFeedEntry readItem(final XMLEventReader eventReader) throws XMLStreamException {
      String title = "";
      String url = "";

      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        if (event.isStartElement()) {
          String namespace = event.asStartElement().getName().getNamespaceURI();
          if ("http://search.yahoo.com/mrss/".equals(namespace)) {
            String localPart = event.asStartElement().getName().getLocalPart();
            if ("title".equals(localPart)) {
              title = getCharacterData(eventReader);
            } else if ("content".equals(localPart)) {
              url = event.asStartElement().getAttributeByName(new QName("", "url")).getValue();
            }
          }
        } else if (event.isEndElement()) {
          if ("item".equals(event.asEndElement().getName().getLocalPart())) {
            return new RSSFeedEntry(title, url);
          }
        }
      }
      throw new XMLStreamException("missing item end event");
    }

    private String getCharacterData(final XMLEventReader eventReader) throws XMLStreamException {
      XMLEvent event = eventReader.nextEvent();
      if (event instanceof Characters) {
        return event.asCharacters().getData();
      }
      return "";
    }

    private InputStream read() {
      try {
        return url.openStream();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

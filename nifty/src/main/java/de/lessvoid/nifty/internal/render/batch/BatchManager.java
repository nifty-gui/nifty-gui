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
package de.lessvoid.nifty.internal.render.batch;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.types.NiftyColor;
import de.lessvoid.nifty.api.types.NiftyCompositeOperation;
import de.lessvoid.nifty.api.types.NiftyLinearGradient;
import de.lessvoid.nifty.internal.canvas.LineParameters;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class BatchManager {
  private List<Batch<?>> activeBatches = new ArrayList<Batch<?>>();

  public void begin() {
    activeBatches.clear();
  }

  public void end(final NiftyRenderDevice renderDevice) {
    for (int i=0; i<activeBatches.size(); i++) {
      activeBatches.get(i).render(renderDevice);
    }
  }

  public void addChangeCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    requestBatch(
        ChangeCompositeOperationBatch.class,
        compositeOperation,
        new BatchFactory<ChangeCompositeOperationBatch>() {
          @Override
          public ChangeCompositeOperationBatch createBatch() {
            return new ChangeCompositeOperationBatch(compositeOperation);
          }
        });
  }

  public void addBeginPath() {
    requestBatch(
        BeginPathBatch.class,
        null,
        new BatchFactory<BeginPathBatch>() {
          @Override
          public BeginPathBatch createBatch() {
            return new BeginPathBatch();
          }
        });
  }

  public void addEndPath(final NiftyColor lineColor) {
    requestBatch(
        EndPathBatch.class,
        null,
        new BatchFactory<EndPathBatch>() {
          @Override
          public EndPathBatch createBatch() {
            return new EndPathBatch(lineColor);
          }
        });
  }

  public void addTextureQuad(final NiftyTexture niftyTexture, final Mat4 mat, final NiftyColor color) {
    TextureBatch batch = requestBatch(TextureBatch.class, niftyTexture, new BatchFactory<TextureBatch>() {
      @Override
      public TextureBatch createBatch() {
        return new TextureBatch(niftyTexture);
      }
    });
    batch.add(
        0.0, 0.0,
        niftyTexture.getWidth(), niftyTexture.getHeight(),
        niftyTexture.getU0(), niftyTexture.getV0(),
        niftyTexture.getU1(), niftyTexture.getV1(),
        mat,
        color);
  }

  public void addTextureQuad(
      final NiftyTexture niftyTexture,
      final Mat4 mat,
      final double x,
      final double y,
      final int width,
      final int height,
      final double u0,
      final double v0,
      final double u1,
      final double v1,
      final NiftyColor color) {
    TextureBatch batch = requestBatch(TextureBatch.class, niftyTexture, new BatchFactory<TextureBatch>() {
      @Override
      public TextureBatch createBatch() {
        return new TextureBatch(niftyTexture);
      }
    });
    batch.add(x, y, width, height, u0, v0, u1, v1, mat, color);
  }

  public void addLinearGradientQuad(
      final double x0,
      final double y0,
      final double x1,
      final double y1,
      final Mat4 mat,
      final NiftyLinearGradient fillLinearGradient) {
    final LinearGradient gradient = new LinearGradient(x0, y0, x1, y1, fillLinearGradient);

    LinearGradientQuadBatch batch = requestBatch(LinearGradientQuadBatch.class, gradient, new BatchFactory<LinearGradientQuadBatch>() {
      @Override
      public LinearGradientQuadBatch createBatch() {
        return new LinearGradientQuadBatch(gradient);
      }
    });
    batch.add(x0, y0, x1, y1, mat);
  }

  public void addColorQuad(
      final double x0,
      final double y0,
      final double x1,
      final double y1,
      final NiftyColor c,
      final Mat4 mat) {
    ColorQuadBatch batch = requestBatch(ColorQuadBatch.class, null, new BatchFactory<ColorQuadBatch>() {
      @Override
      public ColorQuadBatch createBatch() {
        return new ColorQuadBatch();
      }
    });
    batch.add(x0, y0, x1, y1, c, c, c, c, mat);
  }

  public void addCustomShader(final String shaderId) {
    requestBatch(CustomShaderBatch.class, shaderId, new BatchFactory<CustomShaderBatch>() {
      @Override
      public CustomShaderBatch createBatch() {
        return new CustomShaderBatch(shaderId);
      }
    });
  }

  public void addLineVertex(
      final float x,
      final float y,
      final Mat4 mat,
      final LineParameters lineParameters) {
    LineBatch batch = requestBatch(LineBatch.class, lineParameters, createLineBatchFactory(lineParameters));
    batch.add(x, y, mat);
  }

  public void addFirstLineVertex(
      final float x,
      final float y,
      final Mat4 mat,
      final LineParameters lineParameters) {
    LineBatch batch = addBatch(createLineBatchFactory(lineParameters));
    batch.add(x, y, mat);
  }

  public void addArc(
      final double x,
      final double y,
      final Mat4 mat,
      final ArcParameters arcParameters) {
    BatchFactory<ArcBatch> batchFactory = new BatchFactory<ArcBatch>() {
      @Override
      public ArcBatch createBatch() {
        return new ArcBatch(arcParameters);
      }
    };
    ArcBatch batch = requestBatch(ArcBatch.class, arcParameters, batchFactory);
    batch.add(x, y, arcParameters.getRadius(), mat);
  }

  public void addTriangleFanVertex(final float x, final float y, final Mat4 mat, final boolean forceNewBatch, final boolean last) {
    BatchFactory<TriangleFanBatch> batchFactory = new BatchFactory<TriangleFanBatch>() {
      @Override
      public TriangleFanBatch createBatch() {
        return new TriangleFanBatch();
      }
    };
    TriangleFanBatch batch;
    if (forceNewBatch) {
      batch = addBatch(batchFactory);
    } else {
      batch = requestBatch(TriangleFanBatch.class, (Void) null, batchFactory);
    }
    if (forceNewBatch) {
      batch.enableStartPathBatch();
    }
    if (last) {
      batch.enableEndPathBatch();
    }
    batch.add(x, y, mat);
  }

  private <T extends Batch<P>, P> T requestBatch(
      final Class<T> clazz,
      final P param,
      final BatchFactory<T> batchFactory) {
    if (activeBatches.isEmpty()) {
      return addBatch(batchFactory);
    }
    Batch<P> lastBatch = (Batch<P>) activeBatches.get(activeBatches.size() - 1);
    if (!clazz.isInstance(lastBatch)) {
      return addBatch(batchFactory);
    }
    if (!lastBatch.requiresNewBatch(param)) {
      return (T) lastBatch;
    }
    return addBatch(batchFactory);
  }

  private <T extends Batch<P>, P> T addBatch(final BatchFactory<T> batchFactory) {
    T batch = batchFactory.createBatch();
    activeBatches.add(batch);
    return batch;
  }

  private BatchFactory<LineBatch> createLineBatchFactory(final LineParameters lineParameters) {
    BatchFactory<LineBatch> batchFactory = new BatchFactory<LineBatch>() {
      @Override
      public LineBatch createBatch() {
        return new LineBatch(lineParameters);
      }
    };
    return batchFactory;
  }

  private interface BatchFactory<T> {
    T createBatch();
  }
}

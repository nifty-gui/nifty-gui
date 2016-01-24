/*
 * Copyright (c) 2016, Nifty GUI Community
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
package de.lessvoid.niftyinternal.render.batch;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyLinearGradient;
import de.lessvoid.niftyinternal.canvas.LineParameters;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

import javax.annotation.Nonnull;

public class BatchManager {
  private List<Batch<?>> activeBatches = new ArrayList<Batch<?>>();

  public void begin() {
    activeBatches.clear();
  }

  public int end(final NiftyRenderDevice renderDevice) {
    for (int i=0; i<activeBatches.size(); i++) {
      activeBatches.get(i).render(renderDevice);
    }
    return activeBatches.size();
  }

  public void addChangeCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    requestBatch(
        ChangeCompositeOperationBatch.class,
        compositeOperation,
        ChangeCompositeOperationBatchFactory.Instance);
  }

  public void addBeginPath() {
    requestBatch(BeginPathBatch.class, null, BeginPathBatchFactory.Instance);
  }

  public void addEndPath(final NiftyColor lineColor) {
    requestBatch(EndPathBatch.class, lineColor, EndPathBatchFactory.Instance);
  }

  public void addTextureQuad(final NiftyTexture niftyTexture, final Mat4 mat, final NiftyColor color) {
    TextureBatch batch = requestBatch(TextureBatch.class, niftyTexture, TextureBatchFactory.Instance);
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
    TextureBatch batch = requestBatch(TextureBatch.class, niftyTexture, TextureBatchFactory.Instance);
    batch.add(x, y, width, height, u0, v0, u1, v1, mat, color);
  }

  public void addLinearGradientQuad(
      final double x0,
      final double y0,
      final double x1,
      final double y1,
      final Mat4 mat,
      final NiftyLinearGradient fillLinearGradient) {
    LinearGradient gradient = new LinearGradient(x0, y0, x1, y1, fillLinearGradient);

    LinearGradientQuadBatch batch = requestBatch(LinearGradientQuadBatch.class, gradient,
        LinearGradientQuadBatchFactory.Instance);
    batch.add(x0, y0, x1, y1, mat);
  }

  public void addColorQuad(
      final double x0,
      final double y0,
      final double x1,
      final double y1,
      final NiftyColor c,
      final Mat4 mat) {
    ColorQuadBatch batch = requestBatch(ColorQuadBatch.class, null, ColorQuadBatchFactory.Instance);
    batch.add(x0, y0, x1, y1, c, c, c, c, mat);
  }

  public void addCustomShader(final String shaderId) {
    requestBatch(CustomShaderBatch.class, shaderId, CustomShaderBatchFactory.Instance);
  }

  public void addLineVertex(
      final float x,
      final float y,
      final Mat4 mat,
      final LineParameters lineParameters) {
    LineBatch batch = requestBatch(LineBatch.class, lineParameters, LineBatchFactory.Instance);
    batch.add(x, y, mat);
  }

  public void addFirstLineVertex(
      final float x,
      final float y,
      final Mat4 mat,
      final LineParameters lineParameters) {
    LineBatch batch = addBatch(LineBatchFactory.Instance, lineParameters);
    batch.add(x, y, mat);
  }

  public void addArc(
      final double x,
      final double y,
      final Mat4 mat,
      final ArcParameters arcParameters) {
    ArcBatch batch = requestBatch(ArcBatch.class, arcParameters, ArcBatchFactory.Instance);
    batch.add(x, y, arcParameters.getRadius(), mat);
  }

  public void addTriangleFanVertex(final float x, final float y, final Mat4 mat, final boolean forceNewBatch, final boolean last) {
    TriangleFanBatch batch;
    if (forceNewBatch) {
      batch = addBatch(TriangleFanBatchFactory.Instance, null);
    } else {
      batch = requestBatch(TriangleFanBatch.class, null, TriangleFanBatchFactory.Instance);
    }
    if (forceNewBatch) {
      batch.enableStartPathBatch();
    }
    if (last) {
      batch.enableEndPathBatch();
    }
    batch.add(x, y, mat);
  }

  @SuppressWarnings("unchecked")
  private <T extends Batch<P>, P> T requestBatch(
      final Class<T> clazz,
      final P param,
      final BatchFactory<T, P> batchFactory) {
    if (activeBatches.isEmpty()) {
      return addBatch(batchFactory, param);
    }
    Batch<P> lastBatch = (Batch<P>) activeBatches.get(activeBatches.size() - 1);
    if (!clazz.isInstance(lastBatch)) {
      return addBatch(batchFactory, param);
    }
    if (!lastBatch.requiresNewBatch(param)) {
      return (T) lastBatch;
    }
    return addBatch(batchFactory, param);
  }

  private <T extends Batch<P>, P> T addBatch(final BatchFactory<T, P> batchFactory, final P param) {
    T batch = batchFactory.createBatch(param);
    activeBatches.add(batch);
    return batch;
  }

  private interface BatchFactory<T extends Batch<P>, P> {
    @Nonnull
    T createBatch(P param);
  }

  private enum ArcBatchFactory implements BatchFactory<ArcBatch, ArcParameters> {
    Instance;
    @Nonnull
    @Override
    public ArcBatch createBatch(final ArcParameters param) {
      return new ArcBatch(param);
    }
  }

  private enum BeginPathBatchFactory implements BatchFactory<BeginPathBatch, Void> {
    Instance;
    @Nonnull
    @Override
    public BeginPathBatch createBatch(final Void param) {
      return new BeginPathBatch();
    }
  }

  private enum ChangeCompositeOperationBatchFactory
      implements BatchFactory<ChangeCompositeOperationBatch, NiftyCompositeOperation> {
    Instance;
    @Nonnull
    @Override
    public ChangeCompositeOperationBatch createBatch(final NiftyCompositeOperation param) {
      return new ChangeCompositeOperationBatch(param);
    }
  }

  private enum ColorQuadBatchFactory implements BatchFactory<ColorQuadBatch, Void> {
    Instance;
    @Nonnull
    @Override
    public ColorQuadBatch createBatch(final Void param) {
      return new ColorQuadBatch();
    }
  }

  private enum CustomShaderBatchFactory implements BatchFactory<CustomShaderBatch, String> {
    Instance;
    @Nonnull
    @Override
    public CustomShaderBatch createBatch(final String param) {
      return new CustomShaderBatch(param);
    }
  }

  private enum EndPathBatchFactory implements BatchFactory<EndPathBatch, NiftyColor> {
    Instance;
    @Nonnull
    @Override
    public EndPathBatch createBatch(final NiftyColor param) {
      return new EndPathBatch(param);
    }
  }

  private enum LinearGradientQuadBatchFactory implements BatchFactory<LinearGradientQuadBatch, LinearGradient> {
    Instance;
    @Nonnull
    @Override
    public LinearGradientQuadBatch createBatch(final LinearGradient param) {
      return new LinearGradientQuadBatch(param);
    }
  }

  private enum LineBatchFactory implements BatchFactory<LineBatch, LineParameters> {
    Instance;
    @Nonnull
    @Override
    public LineBatch createBatch(final LineParameters param) {
      return new LineBatch(param);
    }
  }

  private enum TextureBatchFactory implements BatchFactory<TextureBatch, NiftyTexture> {
    Instance;
    @Nonnull
    @Override
    public TextureBatch createBatch(final NiftyTexture param) {
      return new TextureBatch(param);
    }
  }

  private enum TriangleFanBatchFactory implements BatchManager.BatchFactory<TriangleFanBatch, Void> {
    Instance;
    @Nonnull
    @Override
    public TriangleFanBatch createBatch(final Void param) {
      return new TriangleFanBatch();
    }
  }
}

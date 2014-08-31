package de.lessvoid.nifty.internal.render.batch;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class BatchManager {
  private List<Batch<?>> activeBatches = new ArrayList<Batch<?>>();

  public void begin() {
    activeBatches.clear();

    // we always start with blend mode enabled
    changeBlendMode(BlendMode.BLEND);
  }

  public void end(final NiftyRenderDevice renderDevice) {
    renderDevice.beginRender();
    for (int i=0; i<activeBatches.size(); i++) {
      activeBatches.get(i).render(renderDevice);
    }
    renderDevice.endRender();
  }

  public void changeBlendMode(final BlendMode blendMode) {
    requestBatch(ChangeBlendModeBatch.class, blendMode, new BatchFactory<ChangeBlendModeBatch>() {
      @Override
      public ChangeBlendModeBatch createBatch() {
        return new ChangeBlendModeBatch(blendMode);
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
    float dx = (float) x1 - (float) x0;
    float dy = (float) y1 - (float) y0;
    final NiftyLinearGradient gradient = new NiftyLinearGradient(
        x0 + fillLinearGradient.getX0() * dx,
        y0 + fillLinearGradient.getY0() * dy,
        x0 + fillLinearGradient.getX1() * dx,
        y0 + fillLinearGradient.getY1() * dy);
    gradient.addColorSteps(fillLinearGradient.getColorStops());

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

  public void addLine(final float x0, final float y0, final float x1, final float y1, final Mat4 mat) {
    LineBatch batch = requestBatch(LineBatch.class, null, new BatchFactory<LineBatch>() {
      @Override
      public LineBatch createBatch() {
        return new LineBatch();
      }
    });
    batch.add(x0, y0, x1, y1, mat);
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

  private interface BatchFactory<T> {
    T createBatch();
  }
}

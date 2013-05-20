package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyMutableColor;
import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

public class RenderNodeContentChild {
  private final int nodeId;
  private final List<Command> commands;
  private final List<RenderNodeContentChild> children = new ArrayList<RenderNodeContentChild>();
  private final Mat4 local;
  private int width;
  private int height;
  private final Box oldAABB = new Box();
  private final Box currentAABB = new Box();
  private boolean changed = true;
  private boolean rerender = true;

  public RenderNodeContentChild(final int nodeId, final Mat4 local, final int w, final int h, final List<Command> commands) {
    this.nodeId = nodeId;
    this.local = new Mat4(local);
    this.commands = commands;
    this.width = w;
    this.height = h;

    currentAABB.setX(0);
    currentAABB.setY(0);
    currentAABB.setWidth(width);
    currentAABB.setHeight(height);
  }

  public void prepareRender(final NiftyRenderTarget renderTarget) {
    if (changed) {
      renderTarget.setMatrix(new Mat4());
      renderTarget.markStencil(
        oldAABB.getX(),
        oldAABB.getY(),
        oldAABB.getWidth(),
        oldAABB.getHeight());
      renderTarget.markStencil(
        currentAABB.getX(),
        currentAABB.getY(),
        currentAABB.getWidth(),
        currentAABB.getHeight());
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).prepareRender(renderTarget);
    }
  }

  public void updateContent(final NiftyRenderTarget renderTarget, final Context context, final Mat4 mat, final boolean forceRender) {
    Mat4 my = Mat4.mul(mat, local);

    if (rerender || forceRender) {
      renderTarget.setMatrix(my);
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(renderTarget, context);
      }
    }

    if (changed) {
      oldAABB.from(currentAABB);
      updateAABB(currentAABB, my);
      changed = false;
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).updateContent(renderTarget, context, my, rerender);
    }
    rerender = false;
  }

  public void addChildNode(final RenderNodeContentChild childNode) {
    children.add(childNode);
  }

  public int getNodeId() {
    return nodeId;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Mat4 getLocal() {
    return new Mat4(local);
  }

  public void setLocal(final Mat4 src) {
    this.local.load(src);
  }

  public void setWidth(final int width) {
    this.width = width;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public void setCommands(final List<Command> commands) {
    this.commands.clear();
    this.commands.addAll(commands);
  }

  public RenderNodeContentChild findChildWithId(final int id) {
    for (int i=0; i<children.size(); i++) {
      RenderNodeContentChild node = children.get(i);
      if (node.getNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  public void markChanged() {
    changed = true;
  }

  public void markNeedsReRender() {
    rerender = true;
  }

  public void getStateInfo(final StringBuilder result, final String offset) {
    result.append(offset).append("- ").append("[").append(nodeId).append("]\n");
    outputDimension(attributesOffset(result, offset));
    outputChanged(result);
    outputRender(result);
    result.append("\n");
    outputLocal(result, offset);
    outputAABB(attributesOffset(result, offset), oldAABB, "AABB old");
    outputAABB(attributesOffset(result, offset), currentAABB, "AABB cur");
    outputCommands(attributesOffset(result, offset));

    for (int i=0; i<children.size(); i++) {
      children.get(i).getStateInfo(result, offset + "  ");
    }
  }

  private StringBuilder attributesOffset(final StringBuilder result, final String offset) {
    return result.append(offset + "  ");
  }

  private void outputCommands(final StringBuilder result) {
    result.append("command count [").append(commands.size()).append("]\n");
  }

  private void outputLocal(final StringBuilder result, final String offset) {
    result.append(offset + "  ");
    result.append("local [").append(local.m00).append(' ').append(local.m10).append(' ').append(local.m20).append(' ').append(local.m30).append("]\n");
    result.append(offset + "  ");
    result.append("local [").append(local.m01).append(' ').append(local.m11).append(' ').append(local.m21).append(' ').append(local.m31).append("]\n");
    result.append(offset + "  ");
    result.append("local [").append(local.m02).append(' ').append(local.m12).append(' ').append(local.m22).append(' ').append(local.m32).append("]\n");
    result.append(offset + "  ");
    result.append("local [").append(local.m03).append(' ').append(local.m13).append(' ').append(local.m23).append(' ').append(local.m33).append("]\n");
  }

  private void outputDimension(final StringBuilder result) {
    result.append("width [").append(width).append("] height [").append(height).append("]");
  }

  private void outputAABB(final StringBuilder result, final Box box, final String name) {
    result.append(name).append(" ");
    box.toString(result);
    result.append("\n");
  }

  private void outputChanged(final StringBuilder result) {
    result.append(" changed [").append(changed).append("]");
  }

  private void outputRender(final StringBuilder result) {
    result.append(" rerender [").append(rerender).append("]");
  }

  private void updateAABB(final Box box, final Mat4 local) {
    Vec4 topLeft = new Vec4(0.f, 0.f, 0.f, 1.f);
    Vec4 topRight = new Vec4(width, 0.f, 0.f, 1.f);
    Vec4 bottomRight = new Vec4(width, height, 0.f, 1.f);
    Vec4 bottomLeft = new Vec4(0, height, 0.f, 1.f);
    Vec4 topLeftT = Mat4.transform(local, topLeft);
    Vec4 topRightT = Mat4.transform(local, topRight);
    Vec4 bottomRightT = Mat4.transform(local, bottomRight);
    Vec4 bottomLeftT = Mat4.transform(local, bottomLeft);
    float minX = Math.min(topLeftT.x, Math.min(topRightT.x, Math.min(bottomRightT.x, bottomLeftT.x)));
    float maxX = Math.max(topLeftT.x, Math.max(topRightT.x, Math.max(bottomRightT.x, bottomLeftT.x)));
    float minY = Math.min(topLeftT.y, Math.min(topRightT.y, Math.min(bottomRightT.y, bottomLeftT.y)));
    float maxY = Math.max(topLeftT.y, Math.max(topRightT.y, Math.max(bottomRightT.y, bottomLeftT.y)));
    box.setX((int) minX);
    box.setY((int) minY);
    box.setWidth((int) (maxX - minX));
    box.setHeight((int) (maxY - minY));
  }
}

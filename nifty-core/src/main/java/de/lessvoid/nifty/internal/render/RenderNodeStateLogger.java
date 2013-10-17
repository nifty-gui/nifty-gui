package de.lessvoid.nifty.internal.render;

import java.util.List;

import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.math.Mat4;

/**
 * This just logs a RenderNode to a StringBuilder.
 * @author void
 */
class RenderNodeStateLogger {

  static void stateInfo(
      final RenderNode renderNode,
      final AABB aabb,
      final List<Command> commands,
      final boolean changed,
      final boolean rerender,
      final StringBuilder result,
      final String offset) {
    result.append(offset).append("- ").append("[").append(renderNode.getNodeId()).append("]\n");
    outputDimension(renderNode.getWidth(), renderNode.getHeight(), attributesOffset(result, offset));
    outputChanged(changed, result);
    outputRender(rerender, result);
    result.append("\n");
    outputLocal(renderNode.getLocal(), result, offset);
    aabb.output(result, offset);
    outputCommands(commands, attributesOffset(result, offset));
  }

  private static StringBuilder attributesOffset(final StringBuilder result, final String offset) {
    return result.append(offset + "  ");
  }

  private static void outputCommands(final List<Command> commands, final StringBuilder result) {
    result.append("command count [").append(commands.size()).append("]\n");
  }

  private static void outputLocal(final Mat4 local, final StringBuilder result, final String offset) {
    result.append(offset + "  ");
    result.append("local [").append(local.m00).append(' ').append(local.m10).append(' ').append(local.m20).append(' ').append(local.m30).append("]\n");
    result.append(offset + "  ");
    result.append("local [").append(local.m01).append(' ').append(local.m11).append(' ').append(local.m21).append(' ').append(local.m31).append("]\n");
    result.append(offset + "  ");
    result.append("local [").append(local.m02).append(' ').append(local.m12).append(' ').append(local.m22).append(' ').append(local.m32).append("]\n");
    result.append(offset + "  ");
    result.append("local [").append(local.m03).append(' ').append(local.m13).append(' ').append(local.m23).append(' ').append(local.m33).append("]\n");
  }

  private static void outputDimension(final int width, final int height, final StringBuilder result) {
    result.append("width [").append(width).append("] height [").append(height).append("]");
  }

  private static void outputChanged(final boolean changed, final StringBuilder result) {
    result.append(" changed [").append(changed).append("]");
  }

  private static void outputRender(final boolean rerender, final StringBuilder result) {
    result.append(" rerender [").append(rerender).append("]");
  }
}

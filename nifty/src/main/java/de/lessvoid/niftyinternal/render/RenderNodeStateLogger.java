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
package de.lessvoid.niftyinternal.render;

import java.util.List;

import de.lessvoid.niftyinternal.canvas.Command;
import de.lessvoid.niftyinternal.math.Mat4;

/**
 * This just logs a RenderNode to a StringBuilder.
 * @author void
 */
class RenderNodeStateLogger {

  static void stateInfo(
      final RenderNode renderNode,
      final AABB aabb,
      final List<Command> commands,
      final boolean needsContentChange,
      final boolean needsRender,
      final StringBuilder result,
      final String offset) {
    result.append(offset).append("- ").append("[").append(renderNode.getNodeId()).append("] ").append("indexInParent [").append(renderNode.getIndexInParent()).append("]\n");
    outputDimension(renderNode.getWidth(), renderNode.getHeight(), attributesOffset(result, offset));
    outputChanged(needsContentChange, result);
    outputRender(needsRender, result);
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
    result.append(" content [").append(changed).append("]");
  }

  private static void outputRender(final boolean rerender, final StringBuilder result) {
    result.append(" render [").append(rerender).append("]");
  }
}

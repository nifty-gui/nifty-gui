package de.lessvoid.nifty.controls.textfield.filter.delete;

import javax.annotation.Nonnull;

/**
 * This interface can be used to block delete operations from a text field.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TextFieldDeleteFilter {
  /**
   * Check if the deletion of a part of the text that was put in is acceptable.
   *
   * @param oldSequence the character sequence before the delete is issued
   * @param deleteStart the index of the first character that is deleted
   * @param deleteEnd   the index of the first character after the deletion range
   * @return {@code true} in case its okay to delete the character
   */
  boolean acceptDelete(@Nonnull CharSequence oldSequence, int deleteStart, int deleteEnd);
}

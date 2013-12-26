package de.lessvoid.nifty.examples.table;

import javax.annotation.Nonnull;

/**
 * The "model" class. This should really be your own class.
 */
class TableRow {
  public final int index;
  @Nonnull
  public final String[] data = new String[5];

  public TableRow(final int index, @Nonnull final String... param) {
    this.index = index;
    System.arraycopy(param, 0, data, 0, param.length);
  }
}

package de.lessvoid.nifty.examples.table;

/**
 * The "model" class. This should really be your own class.
 */
class TableRow {
  public int index;
  public String[] data = new String[5];

  public TableRow(final int index, final String... param) {
    this.index = index;
    for (int i=0; i<param.length; i++) {
      data[i] = param[i];
    }
  }
}

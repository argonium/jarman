package io.miti.jarman.gui.table;

import javax.swing.table.DefaultTableModel;

/**
 * Table model for showing FMI data.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FMITableModel extends DefaultTableModel
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Constructor.
   */
  public FMITableModel()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param data the data set
   * @param columnNames the column names
   */
  public FMITableModel(final Object[][] data, final Object[] columnNames)
  {
    super(data, columnNames);
  }
}

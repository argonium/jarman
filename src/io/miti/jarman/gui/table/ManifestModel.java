package io.miti.jarman.gui.table;

import io.miti.jarman.data.JarData;

import javax.swing.table.DefaultTableModel;

/**
 * The table model for the Manifest table.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ManifestModel extends DefaultTableModel
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;


  /**
   * Default constructor.
   */
  public ManifestModel()
  {
    super();
  }
  
  
  /**
   * Return the class for the column.
   * 
   * @param columnIndex the index of the column
   * @return the class for the column
   */
  @Override
  public Class<?> getColumnClass(final int columnIndex)
  {
    return String.class;
  }
  
  
  /**
   * Return the number of columns in the table.
   * 
   * @return the number of columns in the table
   */
  @Override
  public int getColumnCount()
  {
    return 2;
  }
  
  
  /**
   * Return the column name.
   * 
   * @param columnIndex the index of the column
   * @return the column name
   */
  @Override
  public String getColumnName(final int columnIndex)
  {
    switch (columnIndex)
    {
      case 0: return "Key"; 
      case 1: return "Value"; 
      default: return "XXX";
    }
  }
  
  
  /**
   * Return the number of rows.
   * 
   * @return the number of rows
   */
  @Override
  public int getRowCount()
  {
    if (!JarData.hasData())
    {
      return 0;
    }
    
    return JarData.getInstance().getManifestCount();
  }
  
  
  /**
   * Return the value at a cell.
   *
   * @param rowIndex the row index
   * @param columnIndex the column index
   * @return the value at the cell
   */
  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex)
  {
    return JarData.getInstance().getManifestValue(rowIndex, columnIndex);
  }
  
  
  /**
   * Return whether the cell is editable at the given location.
   * 
   * @param rowIndex the row index
   * @param columnIndex the column index
   * @return whether the cell is editable
   */
  @Override
  public boolean isCellEditable(final int rowIndex, final int columnIndex)
  {
    return false;
  }
}

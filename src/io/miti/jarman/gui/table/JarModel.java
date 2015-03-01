package io.miti.jarman.gui.table;

import io.miti.jarman.data.JarData;

import javax.swing.table.DefaultTableModel;

/**
 * The table model for the Jars page.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class JarModel extends DefaultTableModel
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  public JarModel()
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
    switch (columnIndex)
    {
      case 0: return Boolean.class;
      case 1: return String.class;
      case 2: return java.util.Date.class;
      case 3: return Float.class;
      case 4: return Float.class;
      default: return String.class;
    }
  }
  
  
  /**
   * Return the number of columns in the table.
   * 
   * @return the number of columns in the table
   */
  @Override
  public int getColumnCount()
  {
    return 5;
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
      case 0: return "Found?"; 
      case 1: return "Jar Name"; 
      case 2: return "Date";
      case 3: return "# Files";
      case 4: return "File Size";
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
    
    return JarData.getInstance().getJarFileCount();
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
    return JarData.getInstance().getJarFileValue(rowIndex, columnIndex);
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

package io.miti.jarman.gui.table;

import io.miti.jarman.data.FileData;
import io.miti.jarman.data.JarData;

import javax.swing.table.DefaultTableModel;

/**
 * The table model for the Listings page.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ListingsModel extends DefaultTableModel
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  public ListingsModel()
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
      case 2: return java.util.Date.class;
      case 3: return Long.class;
      case 4: return Double.class;
      
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
    return 6;
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
      case 0: return "Name"; 
      case 1: return "Path"; 
      case 2: return "Date"; 
      case 3: return "Checksum"; 
      case 4: return "Size";
      case 5: return "Jar";
      
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
    
    return JarData.getInstance().getJarDataCount();
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
    FileData fd = JarData.getInstance().getJarFileData(rowIndex);
    switch (columnIndex)
    {
      case 0: return fd.getFileName();
      case 1: return fd.getFileDir();
      case 2: return Long.valueOf(fd.getDate());
      case 3: return Long.valueOf(fd.getCrc());
      case 4: return Long.valueOf(fd.getSize());
      case 5: return fd.getJar();
      default: return "XXX";
    }
  }
  
  
  /**
   * Return the FileData object for the specified row.
   * 
   * @param rowIndex the row
   * @return the FileData object for the row
   */
  public FileData getFileData(final int rowIndex)
  {
    // Convert the view index to the model index
    return JarData.getInstance().getJarFileData(rowIndex);
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

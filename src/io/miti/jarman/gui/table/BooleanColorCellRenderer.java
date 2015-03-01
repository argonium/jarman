package io.miti.jarman.gui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for a column that stores a boolean, and renders it
 * with the appropriate background (red=false, green=true).
 * 
 * @author mwallace
 * @version 1.0
 */
public final class BooleanColorCellRenderer extends DefaultTableCellRenderer
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  public BooleanColorCellRenderer()
  {
    super();
  }
  
  
  /**
   * Return the renderer component for this column.
   * 
   * @param table the table
   * @param value the value for the cell
   * @param isSelected whether the row is selected
   * @param hasFocus whether the cell has the focus
   * @param row the row of the cell to render
   * @param column the column of the cell to render
   * @return the component to render
   */
  @Override
  public Component getTableCellRendererComponent(final JTable table,
      final Object value, final boolean isSelected,
      final boolean hasFocus, final int row, final int column)
  {
    boolean val = ((Boolean) value).booleanValue();
    setForeground(table.getForeground());
    setBackground(val ? Color.GREEN : Color.RED);
    setText(val ? "Yes" : "No");
    
    return this;
  }
}

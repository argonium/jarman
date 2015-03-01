package io.miti.jarman.gui.table;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for a date column.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class DateCellRenderer extends DefaultTableCellRenderer
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The date formatter.
   */
  private SimpleDateFormat formatter = null;
  
  
  /**
   * Default constructor.
   */
  public DateCellRenderer()
  {
    super();
    
    // Define the output format of the date field
    formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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
    // Set the appropriate background color when selected, has the focus
    if (isSelected)
    {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    }
    else if (hasFocus)
    {
      setForeground(UIManager.getDefaults().getColor("Table.focusCellForeground"));
      setBackground(UIManager.getDefaults().getColor("Table.focusCellBackground"));
    }
    else
    {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    
    // Set the date as text in the control
    final long val = ((Long) value).longValue();
    if (val > 0L)
    {
      final Date date = new Date(val);
      setText(formatter.format(date));
    }
    else
    {
      setText("N/A");
    }
    
    return this;
  }
}

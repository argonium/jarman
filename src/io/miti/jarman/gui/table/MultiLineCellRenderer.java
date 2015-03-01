package io.miti.jarman.gui.table;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * A renderer for multiple-line cells.
 */
public final class MultiLineCellRenderer
  extends JTextArea implements TableCellRenderer
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  public MultiLineCellRenderer()
  {
    setLineWrap(true);
    setWrapStyleWord(true);
    setOpaque(true);
  }
  
  
  /**
   * Return the component to render.
   * 
   * @param table the table
   * @param value the value to render
   * @param isSelected whether the row is selected
   * @param hasFocus whether the row has focus
   * @param row the row of the cell to render
   * @param column the column of the cell to render
   * @return the component to render
   */
  public Component getTableCellRendererComponent(final JTable table,
                     final Object value, final boolean isSelected,
                     final boolean hasFocus, final int row,
                     final int column)
  {
    if (isSelected)
    {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    }
    else
    {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setFont(table.getFont());
    if (hasFocus)
    {
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      if (table.isCellEditable(row, column))
      {
        setForeground(UIManager.getColor("Table.focusCellForeground"));
        setBackground(UIManager.getColor("Table.focusCellBackground"));
      }
    }
    else
    {
      setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
    }
    
    setText((value == null) ? "" : value.toString());
    return this;
  }
}

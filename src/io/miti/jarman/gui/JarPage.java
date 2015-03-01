package io.miti.jarman.gui;

import io.miti.jarman.gui.table.BooleanColorCellRenderer;
import io.miti.jarman.gui.table.DateCellRenderer;
import io.miti.jarman.gui.table.JarModel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The page showing the Jar info.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class JarPage extends JPanel
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The one instance of this class.
   */
  private static JarPage page = null;
  
  /**
   * The table.
   */
  private JTable table = null;
  
  
  /**
   * Default constructor.
   */
  public JarPage()
  {
    super();
    setLayout(new BorderLayout());
    buildGUI();
  }
  
  
  /**
   * Return whether this page is null.
   * 
   * @return whether this page is null
   */
  public static boolean isNull()
  {
    return (page == null);
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static JarPage getInstance()
  {
    if (page == null)
    {
      page = new JarPage();
    }
    
    return page;
  }
  
  
  /**
   * Build the GUI.
   */
  private void buildGUI()
  {
    addTable();
  }
  
  
  /**
   * Add the table to the center of the page.
   */
  private void addTable()
  {
    // Create the table
    table = new JTable();
    table.setAutoCreateRowSorter(true);
    table.setColumnSelectionAllowed(false);
    table.getTableHeader().setReorderingAllowed(false);
    table.setModel(new JarModel());
    
    // Center the column headings
    ((DefaultTableCellRenderer) table.getTableHeader().
        getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Set the column widths
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    table.getColumnModel().getColumn(0).setPreferredWidth(100);
    table.getColumnModel().getColumn(0).setMaxWidth(250);
    table.getColumnModel().getColumn(2).setPreferredWidth(140);
    table.getColumnModel().getColumn(2).setMaxWidth(250);
    table.getColumnModel().getColumn(3).setPreferredWidth(100);
    table.getColumnModel().getColumn(3).setMaxWidth(250);
    table.getColumnModel().getColumn(4).setPreferredWidth(100);
    table.getColumnModel().getColumn(4).setMaxWidth(250);
    
    // Sort on the first column
    table.getRowSorter().toggleSortOrder(0);
    
    table.getColumnModel().getColumn(0).setCellRenderer(new BooleanColorCellRenderer());
    table.getColumnModel().getColumn(2).setCellRenderer(new DateCellRenderer());
    
    // Add the table to the panel
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  
  
  /**
   * Remove all data from the table.
   */
  public void emptyTable()
  {
    final int lastRow = table.getRowCount();
    if (lastRow != 0)
    {
      ((JarModel) table.getModel()).fireTableRowsDeleted(0, lastRow - 1);
    }
  }
  
  
  /**
   * Force an update of the table.
   */
  public void updateTable()
  {
    ((JarModel) table.getModel()).fireTableDataChanged();
  }
}

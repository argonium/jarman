package io.miti.jarman.gui;

import io.miti.jarman.gui.table.ManifestModel;
import io.miti.jarman.gui.table.MultiLineCellRenderer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The page showing the manifest data.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ManifestPage extends JPanel
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The one instance of this class.
   */
  private static ManifestPage page = null;
  
  /**
   * The table.
   */
  private JTable table = null;
  
  
  /**
   * Default constructor.
   */
  public ManifestPage()
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
  public static ManifestPage getInstance()
  {
    if (page == null)
    {
      page = new ManifestPage();
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
    table.setModel(new ManifestModel());
    
    // Center the column headings
    ((DefaultTableCellRenderer) table.getTableHeader().
        getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Set the column widths
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    table.getColumnModel().getColumn(0).setPreferredWidth(200);
    table.getColumnModel().getColumn(0).setMaxWidth(400);
    
    table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
    
    // Sort on the first column
    table.getRowSorter().toggleSortOrder(0);
    
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
      ((ManifestModel) table.getModel()).fireTableRowsDeleted(0, lastRow - 1);
    }
  }
  
  
  /**
   * Force an update of the table.
   */
  public void updateTable()
  {
    ((ManifestModel) table.getModel()).fireTableDataChanged();
  }
}

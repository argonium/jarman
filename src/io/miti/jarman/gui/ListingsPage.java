package io.miti.jarman.gui;

import io.miti.jarman.actions.SingleFileList;
import io.miti.jarman.actions.SingleFileVerify;
import io.miti.jarman.actions.ViewAsTextListener;
import io.miti.jarman.gui.table.DateCellRenderer;
import io.miti.jarman.gui.table.ListingsModel;
import io.miti.jarman.gui.table.ListingsTableListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * The page showing the list of all data in all jar files.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ListingsPage extends JPanel
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The one instance of this class.
   */
  private static ListingsPage page = null;
  
  /**
   * The table.
   */
  private JTable table = null;
  
  /**
   * The text from the Filter text field.
   */
  private StringBuilder filterText = new StringBuilder();
  
  /**
   * The filter for the table.
   */
  private transient RowFilter<TableModel, Integer> filter = null;
  
  /**
   * The index into the View combo box's selected item.
   */
  private int displayIndex = 0;
  
  /**
   * The Filter text field.
   */
  private JTextField tfFilter = null;
  
  /**
   * The combo box for the View options.
   */
  private JComboBox<String> cbView = null;
  
  
  /**
   * Default constructor.
   */
  private ListingsPage()
  {
    super();
    setLayout(new BorderLayout());
    buildGUI();
  }
  
  
  /**
   * Return whether this has been created.
   * 
   * @return whether this has been created
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
  public static ListingsPage getInstance()
  {
    if (page == null)
    {
      page = new ListingsPage();
    }
    
    return page;
  }
  
  
  /**
   * Build the GUI.
   */
  private void buildGUI()
  {
    addTopPanel();
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
    table.setModel(new ListingsModel());
    
    // Center the column headings
    ((DefaultTableCellRenderer) table.getTableHeader().
        getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Sort on the first column
    table.getRowSorter().toggleSortOrder(0);
    
    // Build the filter for the table
    filter = new RowFilter<TableModel, Integer>()
    {
      @Override
      public boolean include(final Entry<? extends TableModel,
                                         ? extends Integer> entry)
      {
        // Get the filter text
        String text = filterText.toString();
        if (text.length() < 1)
        {
          return true;
        }
        
        // Check for a match on columns 0, 1 and 5 (name, path, jar)
        boolean match = (entry.getStringValue(0).contains(text)) ||
                        (entry.getStringValue(1).contains(text)) ||
                        (entry.getStringValue(5).contains(text));
        return match;
      }
    };
    
    table.getColumnModel().getColumn(2).setCellRenderer(new DateCellRenderer());
    
    ((TableRowSorter<? extends TableModel>) table.getRowSorter()).setRowFilter(filter);
    
    // Set the column widths
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    setColumnWidths();
    
    // Add a popup menu
    JPopupMenu popup = new JPopupMenu();
    JMenuItem listView = new JMenuItem("View as Text");
    popup.add(listView);
    listView.addActionListener(new ViewAsTextListener());
    JMenuItem listVerify = new JMenuItem("Verify");
    popup.add(listVerify);
    listVerify.addActionListener(new SingleFileVerify());
    JMenuItem listIncluded = new JMenuItem("Included");
    popup.add(listIncluded);
    listIncluded.addActionListener(new SingleFileList());
    JMenuItem listExplore = new JMenuItem("Explore");
    popup.add(listExplore);
    listExplore.addActionListener(new io.miti.jarman.actions.ExploreFile());
    table.addMouseListener(new ListingsMenu(popup));
    
    // Add the table to the panel
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  
  
  /**
   * Set the table column widths.
   */
  private void setColumnWidths()
  {
    TableColumnModel model = table.getColumnModel();
    model.getColumn(0).setPreferredWidth(200);
    model.getColumn(0).setMaxWidth(500);
    model.getColumn(1).setPreferredWidth(230);
    model.getColumn(1).setMaxWidth(500);
    model.getColumn(2).setPreferredWidth(200);
    model.getColumn(2).setMaxWidth(300);
    model.getColumn(3).setPreferredWidth(100);
    model.getColumn(3).setMaxWidth(200);
    model.getColumn(4).setPreferredWidth(100);
    model.getColumn(4).setMaxWidth(200);
  }
  
  
  /**
   * Create the top panel of the Listings page.  This
   * panel has the options for the table.
   */
  private void addTopPanel()
  {
    JPanel top = new JPanel();
    
    // Add the View combo box, with options controlling which files
    // to show in the table
    JLabel lblView = new JLabel("View: ");
    lblView.setDisplayedMnemonic(KeyEvent.VK_V);
    top.add(lblView);
    final String[] opts = new String[]
       {"Show All", "Show Name Duplicates", "Show File Duplicates"};
    cbView = new JComboBox<String>(opts);
    cbView.addItemListener(new ListingsTableListener());
    cbView.setEditable(false);
    lblView.setLabelFor(cbView);
    top.add(cbView);
    
    top.add(Box.createHorizontalStrut(20));
    
    // Add the filter text field and label
    JLabel lblFilter = new JLabel("Filter:");
    lblFilter.setDisplayedMnemonic(KeyEvent.VK_I);
    top.add(lblFilter);
    tfFilter = new JTextField(20);
    lblFilter.setLabelFor(tfFilter);
    tfFilter.setToolTipText(getFilterTipText());
    top.add(tfFilter);
    
    // Add a button to clear the filter text field
    JButton btnClear = new JButton("X");
    btnClear.setToolTipText("Clear the filter text");
    btnClear.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
        tfFilter.setText("");
      }
    });
    btnClear.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
    btnClear.setMnemonic(KeyEvent.VK_X);
    top.add(btnClear);
    
    // Add a listener for changes to the text field
    tfFilter.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void changedUpdate(final DocumentEvent e)
      {
        filterChanged(e);
      }
      
      @Override
      public void insertUpdate(final DocumentEvent e)
      {
        filterChanged(e);
      }
      
      @Override
      public void removeUpdate(final DocumentEvent e)
      {
        filterChanged(e);
      }
    });
    
    add(top, BorderLayout.NORTH);
  }
  
  
  /**
   * Return the tooltip text for the filter text field.
   * 
   * @return the tooltip text for the filter text field
   */
  private String getFilterTipText()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<html><body>The table will only show rows that match on text<br>")
      .append("entered here with the row's name, path or jar file</body></html>");
    
    return sb.toString();
  }
  
  
  /**
   * Handle a change to the filter text.
   * 
   * @param e the event
   */
  private void filterChanged(final DocumentEvent e)
  {
    Document doc = e.getDocument();
    try
    {
      filterText.setLength(0);
      filterText.append(doc.getText(0, doc.getLength()));
      
      ((TableRowSorter<? extends TableModel>) table.getRowSorter()).setRowFilter(filter);
      
      // The number of visible rows on the Listings page may have changed,
      // so update the status bar label
      Jarman.getApp().updateVisibleRowCount();
    }
    catch (BadLocationException ble)
    {
      ble.printStackTrace();
    }
  }
  
  
  /**
   * Remove all records from the table.
   */
  public void emptyTable()
  {
    final int lastRow = table.getRowCount();
    if (lastRow != 0)
    {
      ((ListingsModel) table.getModel()).fireTableRowsDeleted(0, lastRow - 1);
    }
  }
  
  
  /**
   * Fire a table update.
   */
  public void updateTable()
  {
    ((ListingsModel) table.getModel()).fireTableDataChanged();
  }
  
  
  /**
   * Set the type of data to show (the selected index from the View combo).
   * 
   * @param sel the selected index from the View combo box
   */
  public void setDisplayIndex(final int sel)
  {
    displayIndex = sel;
  }
  
  
  /**
   * Return the type of data to show (the selected index from the View combo).
   * 
   * @return the type of data to show
   */
  public int getDisplayIndex()
  {
    return displayIndex;
  }
  
  
  /**
   * Return the page's table.
   * 
   * @return the table
   */
  public JTable getTable()
  {
    return table;
  }
  
  
  /**
   * Reset the display options to the default.
   */
  public void resetOptions()
  {
    if (tfFilter.getText().length() > 0)
    {
      tfFilter.setText("");
    }
    
    if (cbView.getSelectedIndex() > 0)
    {
      cbView.setSelectedIndex(0);
    }
  }
}

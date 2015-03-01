package io.miti.jarman.dialog;

import io.miti.jarman.classref.ClassInfo;
import io.miti.jarman.classref.FieldMethodInfo;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.table.FMITableModel;
import io.miti.jarman.util.Utility;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Dialog box to show the class file data.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ExploreFileDlg extends JDialog
{
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  public ExploreFileDlg()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param owner the parent frame
   * @param ci the class info object
   */
  public ExploreFileDlg(final Frame owner, final ClassInfo ci)
  {
    super(owner, "Class Information", true);
    fillDialog(ci);
  }
  
  
  /**
   * Put the data in the dialog box.
   * 
   * @param ci the class info object
   */
  private void fillDialog(final ClassInfo ci)
  {
    setPreferredSize(new java.awt.Dimension(450, 420));
    
    JTabbedPane tp = new JTabbedPane();
    tp.addTab("General", getMainPanel(ci));
    tp.addTab("Fields", getFieldsPanel(ci));
    tp.addTab("Methods", getMethodsPanel(ci));
    tp.addTab("Interfaces", getInterfacesPanel(ci));
    tp.addTab("Strings", getStringsPanel(ci));
    
    tp.setMnemonicAt(0, KeyEvent.VK_G);
    tp.setMnemonicAt(1, KeyEvent.VK_F);
    tp.setMnemonicAt(2, KeyEvent.VK_M);
    tp.setMnemonicAt(3, KeyEvent.VK_I);
    tp.setMnemonicAt(4, KeyEvent.VK_S);
    
    getContentPane().add(tp);
    pack();
    setResizable(true);
    setLocationRelativeTo(Jarman.getApp().getFrame());
    setVisible(true);
  }
  
  
  /**
   * Return the main panel for the dialog box.
   * 
   * @param ci the class info object
   * @return the panel
   */
  private JPanel getMainPanel(final ClassInfo ci)
  {
    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Set up the text pane
    JTextPane pane = new javax.swing.JTextPane();
    pane.setEditable(false);
    pane.setOpaque(false);
    pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    StyledDocument doc = pane.getStyledDocument();
    addStylesToDocument(doc);
    
    try
    {
      addToDoc(doc, "Class: ", "bold");
      addToDoc(doc, ci.getClassName(), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
      addToDoc(doc, "Super: ", "bold");
      addToDoc(doc, ci.getParentName(), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
      addToDoc(doc, "Version: ", "bold");
      addToDoc(doc, ci.getVersionString(), "regular");
      
      addToDoc(doc, "Access Flags: ", "bold");
      StringBuilder sb = new StringBuilder(100);
      ci.addAccessFlagDesc(sb);
      sb.append(" (").append(Integer.toString(ci.getAccessFlags())).append(")");
      addToDoc(doc, sb.toString(), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
      
      addToDoc(doc, "Fields: ", "bold");
      addToDoc(doc, Integer.toString(ci.getClassFieldsCount()), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
      
      addToDoc(doc, "Methods: ", "bold");
      addToDoc(doc, Integer.toString(ci.getClassMethodsCount()), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
      
      addToDoc(doc, "Interfaces: ", "bold");
      addToDoc(doc, Integer.toString(ci.getInterfacesCount()), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
      
      addToDoc(doc, "Strings: ", "bold");
      addToDoc(doc, Integer.toString(ci.getStringRefCount()), "regular");
      addToDoc(doc, Utility.getLineSeparator(), "regular");
    }
    catch (BadLocationException e)
    {
      e.printStackTrace();
    }

    // Put the text pane in a scroll pane
    JScrollPane scrollPane = new JScrollPane(pane);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(getButtons(), BorderLayout.SOUTH);
    
    return panel;
  }
  
  
  /**
   * Get any buttons for the bottom of the dialog box.
   * 
   * @return the button panel
   */
  private JPanel getButtons()
  {
    // Add a button to close the dialog
    JPanel panel = new JPanel(new FlowLayout());
    final JButton btnOk = new JButton("OK");
    btnOk.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
        dispose();
      }
    });
    
    panel.add(btnOk);
    return panel;
  }
  
  
  /**
   * Add text to a style document.
   * 
   * @param doc the document
   * @param text the text
   * @param style the style
   * @throws BadLocationException exception with the document location
   */
  private void addToDoc(final StyledDocument doc, final String text, final String style)
    throws BadLocationException
  {
    doc.insertString(doc.getLength(), text, doc.getStyle(style));
  }
  
  
  /**
   * Add styles to the document.
   * 
   * @param doc the StyledDocument to add styles to
   */
  protected void addStylesToDocument(final StyledDocument doc)
  {
    //Initialize some styles.
    Style def = StyleContext.getDefaultStyleContext().
                    getStyle(StyleContext.DEFAULT_STYLE);

    Style regular = doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");
    StyleConstants.setFontSize(def, 12);
    StyleConstants.setLineSpacing(def, 2.0f);

    Style s = doc.addStyle("italic", regular);
    StyleConstants.setItalic(s, true);

    s = doc.addStyle("bold", regular);
    StyleConstants.setBold(s, true);

    s = doc.addStyle("small", regular);
    StyleConstants.setFontSize(s, 10);

    s = doc.addStyle("large", regular);
    StyleConstants.setFontSize(s, 14);
  }
  
  
  /**
   * Return the interfaces panel for the dialog box.
   * 
   * @param ci the class info object
   * @return the panel
   */
  private JPanel getInterfacesPanel(final ClassInfo ci)
  {
    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    DefaultListModel model = new DefaultListModel();
    JList lbNames = new JList(model);
    Iterator<String> names = ci.getInterfaces();
    if (names != null)
    {
      while (names.hasNext())
      {
        model.addElement(names.next());
      }
    }
    
    panel.add(new JScrollPane(lbNames), BorderLayout.CENTER);
    panel.add(getButtons(), BorderLayout.SOUTH);
    
    return panel;
  }
  
  
  /**
   * Return the methods panel for the dialog box.
   * 
   * @param ci the class info object
   * @return the panel
   */
  private JPanel getMethodsPanel(final ClassInfo ci)
  {
    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JTable table = getFMITable(ci.getClassMethods(), ci.getClassMethodsCount(), ci);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    panel.add(getButtons(), BorderLayout.SOUTH);
    
    return panel;
  }
  
  
  /**
   * Create a table showing FMI data.
   * 
   * @param iter the FMI data iterator
   * @param iterCount the number of rows in the data
   * @param ci the class info object
   * @return the new table
   */
  private JTable getFMITable(final Iterator<FieldMethodInfo> iter,
                              final int iterCount,
                              final ClassInfo ci)
  {
    // Create the column names
    final String[] colnames = new String[] {"Name", "Description", "Attributes"};
    
    // Build the data set
    final String[][] data = new String[iterCount][3];
    int row = 0;
    if (iter != null)
    {
      while (iter.hasNext())
      {
        FieldMethodInfo fmi = iter.next();
        data[row][0] = ci.getCpString(fmi.getNameIndex());
        data[row][1] = ci.getCpString(fmi.getDescIndex());
        data[row][2] = fmi.getAFDesc();
        
        ++row;
      }
    }
    
    // Create the table and model
    FMITableModel model = new FMITableModel(data, colnames);
    JTable table = new JTable(model);
    table.setAutoCreateRowSorter(true);
    table.setColumnSelectionAllowed(false);
    table.getTableHeader().setReorderingAllowed(false);
    
    // Center the column headings
    ((DefaultTableCellRenderer) table.getTableHeader().
        getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Sort on the first column
    table.getRowSorter().toggleSortOrder(0);
    
    return table;
  }
  
  
  /**
   * Return the fields panel for the dialog box.
   * 
   * @param ci the class info object
   * @return the panel
   */
  private JPanel getFieldsPanel(final ClassInfo ci)
  {
    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JTable table = getFMITable(ci.getClassFields(), ci.getClassFieldsCount(), ci);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    panel.add(getButtons(), BorderLayout.SOUTH);
    
    return panel;
  }
  
  
  /**
   * Return the Strings panel for the dialog box.
   * 
   * @param ci the class info object
   * @return the panel
   */
  private JPanel getStringsPanel(final ClassInfo ci)
  {
    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    DefaultListModel model = new DefaultListModel();
    JList lbNames = new JList(model);
    Iterator<String> names = ci.getStringRefIterator();
    if (names != null)
    {
      while (names.hasNext())
      {
        String name = names.next();
        if (name == null)
        {
          model.addElement("<<Null>>");
        }
        else if (name.length() < 1)
        {
          model.addElement("<<Empty>>");
        }
        else
        {
          model.addElement(name);
        }
      }
    }
    
    panel.add(new JScrollPane(lbNames), BorderLayout.CENTER);
    panel.add(getButtons(), BorderLayout.SOUTH);
    
    return panel;
  }
}

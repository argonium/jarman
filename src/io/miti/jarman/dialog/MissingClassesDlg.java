package io.miti.jarman.dialog;

import io.miti.jarman.gui.Jarman;
import io.miti.jarman.util.Utility;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

/**
 * Dialog box to show the list of missing referenced classes.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class MissingClassesDlg extends JDialog
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Default constructor.
   */
  public MissingClassesDlg()
  {
    super(Jarman.getApp().getFrame(), "Missing Classes", true);
  }
  
  
  /**
   * Constructor taking the data to show.
   * 
   * @param names the list of referenced unfound class names
   * @param showMissing whether to show missing classes
   */
  public MissingClassesDlg(final Set<String> names, final boolean showMissing)
  {
    // Set up the basic parameters - title, modal, size
    super(Jarman.getApp().getFrame(), true);
    setTitle(showMissing ? "Missing Classes" : "Included Classes");
    setPreferredSize(new java.awt.Dimension(340, 420));
    
    // Create the listbox and populate it with the strings
    final JPanel panel = new JPanel(new BorderLayout(0, 15));
    final JList lbNames = new JList();
    final DefaultListModel model = new DefaultListModel();
    final int count = fillModel(model, names);
    lbNames.setModel(model);
    panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    panel.add(new JScrollPane(lbNames), BorderLayout.CENTER);
    
    // Add the label to the top of the panel
    JLabel topLabel = new JLabel(getTopLabel(count, showMissing));
    JPanel topPanel = new JPanel();
    topPanel.add(topLabel);
    panel.add(topPanel, BorderLayout.NORTH);
    
    // Add the buttons
    panel.add(getPanelButtons(lbNames), BorderLayout.SOUTH);
    
    // Finish setting up the dialog box
    getContentPane().add(panel);
    pack();
    setResizable(true);
    setLocationRelativeTo(Jarman.getApp().getFrame());
    setVisible(true);
  }
  
  
  /**
   * Return the panel with the buttons.
   * 
   * @param lbNames the listbox
   * @return the panel for the buttons
   */
  private JPanel getPanelButtons(final JList lbNames)
  {
    // The panel that we fill with buttons
    JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    
    // Copy all classes to the clipboard
    JButton btnCA = new JButton("Copy All");
    btnCA.setMnemonic(KeyEvent.VK_A);
    btnCA.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
        copyAll(lbNames);
      }
    });
    panelButton.add(btnCA);
    
    // Copy selected classes to the clipboard
    JButton btnCS = new JButton("Copy Selected");
    btnCS.setMnemonic(KeyEvent.VK_S);
    btnCS.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
        copySelected(lbNames);
      }
    });
    panelButton.add(btnCS);
    
    // Close the window
    JButton btnOK = new JButton("Close");
    btnOK.setMnemonic(KeyEvent.VK_C);
    btnOK.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent e)
      {
        dispose();
      }
    });
    panelButton.add(btnOK);
    
    return panelButton;
  }
  
  
  /**
   * Copy selected rows to the clipboard.
   * 
   * @param lbNames the listbox
   */
  private void copySelected(final JList lbNames)
  {
    // Check the selected list size
    final int[] sel = lbNames.getSelectedIndices();
    final int size = sel.length;
    if (size < 1)
    {
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(), "No data selected",
                      "Status", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    // Build the string containing all classes in the list
    StringBuilder sb = new StringBuilder(500);
    String lineEnd = Utility.getLineSeparator();
    final DefaultListModel model = (DefaultListModel) lbNames.getModel();
    for (int i = 0; i < size; ++i)
    {
      String name = (String) model.elementAt(sel[i]);
      sb.append(name).append(lineEnd);
    }
    
    // Copy the list to the clipboard
    Utility.saveToClipboard(sb.toString());
  }
  
  
  /**
   * Copy all rows to the clipboard.
   * 
   * @param lbNames the listbox
   */
  private void copyAll(final JList lbNames)
  {
    // Check the list size
    final DefaultListModel model = (DefaultListModel) lbNames.getModel();
    final int size = model.size();
    if (size < 1)
    {
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(), "No data to copy",
                      "Status", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    // Build the string containing all classes in the list
    StringBuilder sb = new StringBuilder(500);
    String lineEnd = Utility.getLineSeparator();
    for (int i = 0; i < size; ++i)
    {
      String name = (String) model.elementAt(i);
      sb.append(name).append(lineEnd);
    }
    
    // Copy the list to the clipboard
    Utility.saveToClipboard(sb.toString());
  }
  
  
  /**
   * Fill the listbox model with the data.
   * 
   * @param model the listbox model
   * @param names the set of strings
   * @return the number of elements in the listbox
   */
  private int fillModel(final DefaultListModel model, final Set<String> names)
  {
    // Check for an empty set
    int count = 0;
    if (names.isEmpty())
    {
      return count;
    }
    
    // Add the strings from the set to a temporary list
    List<String> list = new ArrayList<String>(100);
    for (String name : names)
    {
      if (name == null)
      {
        list.add("<null>");
      }
      else if (name.length() < 1)
      {
        list.add("<empty>");
      }
      else
      {
        list.add(name);
      }
    }
    
    // Save the size, sort the list and add the strings to the listbox model
    count = list.size();
    Collections.sort(list);
    for (String item : list)
    {
      model.addElement(item);
    }
    
    return count;
  }
  
  
  /**
   * Return the top label for the dialog box, showing the number of missing classes.
   * 
   * @param count the number of missing classes
   * @param showMissing whether to show missing classes
   * @return the string to display
   */
  private String getTopLabel(final int count, final boolean showMissing)
  {
    StringBuilder sb = new StringBuilder(30);
    sb.append(showMissing ? "Missing " : "Includes ")
      .append(Integer.toString(count)).append(" class");
    if (count > 1)
    {
      sb.append("es");
    }
    
    return sb.toString();
  }
}

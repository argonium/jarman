package io.miti.jarman.actions;

import io.miti.jarman.data.FileData;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.ListingsPage;
import io.miti.jarman.gui.TextPopup;
import io.miti.jarman.gui.table.ListingsModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * Action listener for the user selecting to view a file from
 * the Listings page.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ViewAsTextListener implements ActionListener
{
  /**
   * Default constructor.
   */
  public ViewAsTextListener()
  {
    super();
  }
  
  
  /**
   * Handle the event.
   * 
   * @param event the event
   */
  @Override
  public void actionPerformed(final ActionEvent event)
  {
    // Get the selected row from the table
    final JTable table = ListingsPage.getInstance().getTable();
    final int index = table.getSelectedRow();
    if (index >= 0)
    {
      // Find the matching jar entry
      final int row = table.convertRowIndexToModel(index);
      FileData fd = ((ListingsModel) table.getModel()).getFileData(row);
      if (fd != null)
      {
        // Check the file size
        if (fd.getSize() > 10000000L)
        {
          JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
              "The selected file is too large (larger than 10 MB)",
              "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          // Have FileData return the text
          String s = fd.getFileText();
          new TextPopup(s);
          s = null;
        }
      }
    }
  }
}

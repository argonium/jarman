package io.miti.jarman.actions;

import io.miti.jarman.data.FileData;
import io.miti.jarman.dialog.MissingClassesDlg;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.ListingsPage;
import io.miti.jarman.gui.table.ListingsModel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * The action listener for the Included pop-up menu, to list all of the
 * external classes that a .class file refers to.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SingleFileList implements ActionListener
{
  /**
   * The default constructor.
   */
  public SingleFileList()
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
        verifyFile(fd);
      }
    }
  }
  
  
  /**
   * Verify the file.
   * 
   * @param fd the FileData object
   */
  private void verifyFile(final FileData fd)
  {
    // Get the list of missing classes
    Cursor cursor = Jarman.getApp().getFrame().getCursor();
    Jarman.getApp().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    Set<String> linked = fd.getIncludedClasses();
    Jarman.getApp().getFrame().setCursor(cursor);
    
    // Check if any are missing
    if (linked.isEmpty())
    {
      // The set is empty, so all referenced classes were found
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
                  "No referenced classes");
      return;
    }
    
    // Show the missing classes
    new MissingClassesDlg(linked, false);
  }
}

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
 * The action listener for the Verify pop-up menu, to let the user verify
 * a single class in the open jar file has access to all of the other
 * classes it relies on. 
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SingleFileVerify implements ActionListener
{
  /**
   * The default constructor.
   */
  public SingleFileVerify()
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
        // Get the list of missing classes
        Cursor cursor = Jarman.getApp().getFrame().getCursor();
        Jarman.getApp().getFrame().setCursor(
            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final Set<String> missingClasses = fd.getMissingClasses();
        Jarman.getApp().getFrame().setCursor(cursor);
        
        if (missingClasses == null)
        {
          JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
              "The selected file is not a valid compiled Java class file",
              "File Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
          verifyFile(missingClasses);
          missingClasses.clear();
        }
      }
    }
  }
  
  
  /**
   * Verify the file.
   * 
   * @param missing the missing classes
   */
  private void verifyFile(final Set<String> missing)
  {
    // Check if any are missing
    if (missing.isEmpty())
    {
      // The set is empty, so all referenced classes were found
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
                  "No missing classes");
      return;
    }
    
    // Show the missing classes
    new MissingClassesDlg(missing, true);
  }
}

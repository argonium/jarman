package io.miti.jarman.actions;

import io.miti.jarman.classref.ClassInfo;
import io.miti.jarman.data.FileData;
import io.miti.jarman.dialog.ExploreFileDlg;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.ListingsPage;
import io.miti.jarman.gui.table.ListingsModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

/**
 * The action listener for the Explore pop-up menu, to list info about
 * the selected .class file.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ExploreFile implements ActionListener
{
  /**
   * The default constructor.
   */
  public ExploreFile()
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
        ClassInfo ci = fd.getClassInfo();
        if (ci != null)
        {
          showClassData(ci);
        }
      }
    }
  }
  
  
  /**
   * Show the class file data.
   * 
   * @param ci the class information structure
   */
  private void showClassData(final ClassInfo ci)
  {
    // Show the info in a dialog
    new ExploreFileDlg(Jarman.getApp().getFrame(), ci);
  }
}

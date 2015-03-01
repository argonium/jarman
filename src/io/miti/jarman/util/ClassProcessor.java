package io.miti.jarman.util;

import io.miti.jarman.data.FileList;
import io.miti.jarman.data.JarData;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.ListingsPage;

import java.awt.Cursor;
import java.io.File;

/**
 * Process a class file.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ClassProcessor
{
  /**
   * Default constructor.
   */
  public ClassProcessor()
  {
    super();
  }
  
  
  /**
   * Process the file.
   * 
   * @param file the file to process
   */
  public void processFile(final File file)
  {
    // Show busy cursor
    final Cursor cursor = Jarman.getApp().getFrame().getCursor();
    Jarman.getApp().getFrame().setCursor(
        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    
    // Reset the data cache and save the jar file
    JarData.reset();
    ListingsPage.getInstance().resetOptions();
    JarData.getInstance().setJarFile(file);
    
    // Save manifest entries
    getJarAttributes(file);
    
    // Save the class file
    JarData.getInstance().addJarEntry(file);
    
    // Fire a table data change
    JarData.getInstance().resetTables(true);
    
    Jarman.getApp().updateFileInfo();
    
    // Update the tabs in the frame
    Jarman.getApp().updateTabsForClass();
    
    // Show the previous cursor
    Jarman.getApp().getFrame().setCursor(cursor);
  }
  
  
  /**
   * Verify the file is valid and save the contents of the manifest.
   * 
   * @param file the file to open
   * @return whether the file is valid
   */
  private boolean getJarAttributes(final File file)
  {
    // Save the file to the quick-file list on the menu bar
    FileList.getInstance().addFile(file);
    return true;
  }
}

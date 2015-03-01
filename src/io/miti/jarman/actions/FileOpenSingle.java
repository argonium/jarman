package io.miti.jarman.actions;

import io.miti.jarman.data.FileList;
import io.miti.jarman.util.JarProcessor;
import io.miti.jarman.util.Utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import io.miti.jarman.actions.FileOpenSingle;
import io.miti.jarman.actions.ListingsFilter;

/**
 * The action listener for File | Open Single, to let the user open a
 * single Java archive file (don't include references in the manifest).
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileOpenSingle implements ActionListener
{
  /**
   * The one instance of this class.
   */
  private static FileOpenSingle handler = null;
  
  
  /**
   * The default constructor.
   */
  private FileOpenSingle()
  {
    super();
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static FileOpenSingle getInstance()
  {
    if (handler == null)
    {
      handler = new FileOpenSingle();
    }
    
    return handler;
  }
  
  
  /**
   * Perform the action.
   * 
   * @param e the event
   */
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    // Let the user select a java archive (jar/war/ear)
    JFileChooser fc = new JFileChooser(Utility.getCurrentDir());
    fc.setDialogTitle("Open Single Jar");
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.addChoosableFileFilter(new ListingsFilter());
    int rc = fc.showOpenDialog(null);
    if (rc == JFileChooser.APPROVE_OPTION)
    {
      // Save the current directory
      Utility.setCurrentDir(fc.getCurrentDirectory());
      
      // Open and process the selected file
      new JarProcessor().processFile(fc.getSelectedFile(), false);
      
      // Update the menu
      FileList.getInstance().updateMenu();
    }
  }
}

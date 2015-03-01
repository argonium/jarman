package io.miti.jarman.actions;

import io.miti.jarman.data.FileList;
import io.miti.jarman.util.JarProcessor;
import io.miti.jarman.util.Utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import io.miti.jarman.actions.FileOpen;
import io.miti.jarman.actions.ListingsFilter;

/**
 * The action listener for File | Open, to let the user open
 * a java archive file.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileOpen implements ActionListener
{
  /**
   * The one instance of this class.
   */
  private static FileOpen handler = null;
  
  
  /**
   * The default constructor.
   */
  private FileOpen()
  {
    super();
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static FileOpen getInstance()
  {
    if (handler == null)
    {
      handler = new FileOpen();
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
    fc.setDialogTitle("Open Jar with Dependencies");
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.addChoosableFileFilter(new ListingsFilter());
    int rc = fc.showOpenDialog(null);
    if (rc == JFileChooser.APPROVE_OPTION)
    {
      // Save the current directory
      Utility.setCurrentDir(fc.getCurrentDirectory());
      
      // Open and process the selected file
      new JarProcessor().processFile(fc.getSelectedFile(), true);
      
      // Update the menu
      FileList.getInstance().updateMenu();
    }
  }
}

package io.miti.jarman.actions;

import io.miti.jarman.data.FileList;
import io.miti.jarman.util.ClassProcessor;
import io.miti.jarman.util.Utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import io.miti.jarman.actions.ClassFilter;
import io.miti.jarman.actions.FileOpenClass;

/**
 * The action listener for File | Open Class, to let the user open
 * a Java class.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileOpenClass implements ActionListener
{
  /**
   * The one instance of this class.
   */
  private static FileOpenClass handler = null;
  
  
  /**
   * The default constructor.
   */
  private FileOpenClass()
  {
    super();
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static FileOpenClass getInstance()
  {
    if (handler == null)
    {
      handler = new FileOpenClass();
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
    // Let the user select a Java class
    JFileChooser fc = new JFileChooser(Utility.getCurrentDir());
    fc.setDialogTitle("Open Class");
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.addChoosableFileFilter(new ClassFilter());
    int rc = fc.showOpenDialog(null);
    if (rc == JFileChooser.APPROVE_OPTION)
    {
      // Save the current directory
      Utility.setCurrentDir(fc.getCurrentDirectory());
      
      // Open and process the selected file
      new ClassProcessor().processFile(fc.getSelectedFile());
      
      // Update the menu
      FileList.getInstance().updateMenu();
    }
  }
}

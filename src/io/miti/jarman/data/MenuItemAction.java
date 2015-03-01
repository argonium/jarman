package io.miti.jarman.data;

import io.miti.jarman.gui.Jarman;
import io.miti.jarman.util.ClassProcessor;
import io.miti.jarman.util.JarProcessor;
import io.miti.jarman.util.Utility;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import io.miti.jarman.data.FileList;

/**
 * Action handler for menu items.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class MenuItemAction extends AbstractAction
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The file to open.
   */
  private String target = null;
  
  /**
   * The index of the file.
   */
  private int fileIndex = 0;
  
  
  /**
   * Default constructor.
   */
  public MenuItemAction()
  {
    super();
  }
  
  
  /**
   * Constructor taking the menu item text.
   * 
   * @param name the menu item text
   * @param tempIndex the index of the file
   * @param fileName the name of the file to open
   */
  public MenuItemAction(final String name, final int tempIndex,
                         final String fileName)
  {
    super(name);
    fileIndex = tempIndex;
    target = fileName;
  }
  
  
  /**
   * Handle the menu item action.
   * 
   * @param e the event
   */
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    // Try to open the file
    final File file = new File(target);
    if (!file.exists() || !file.isFile())
    {
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
          "The file was not found", "Error: File Not Found",
          JOptionPane.ERROR_MESSAGE);
      FileList.getInstance().removeFile(fileIndex);
      FileList.getInstance().updateMenu();
      
      return;
    }
    
    // Check that the file has an extension
    final String ext = Utility.getExtension(file);
    if (ext == null)
    {
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
          "The file extension is invalid", "Error: Invalid File",
          JOptionPane.ERROR_MESSAGE);
      FileList.getInstance().removeFile(fileIndex);
      FileList.getInstance().updateMenu();
      
      return;
    }
    
    // Check the extension is valid
    final boolean isJar = (ext.equals("jar"));
    final boolean isClass = (ext.equals("class"));
    if (!isJar && !isClass)
    {
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
          "The file extension is invalid", "Error: Invalid File",
          JOptionPane.ERROR_MESSAGE);
      FileList.getInstance().removeFile(fileIndex);
      FileList.getInstance().updateMenu();
      
      return;
    }
    
    // Update the current directory in FileOpen
    final File parent = file.getParentFile();
    Utility.setCurrentDir(parent);
    
    // Open and process the selected file
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        // Use the appropriate processor (Jar v. Class)
        if (isJar)
        {
          new JarProcessor().processFile(file, true);
        }
        else
        {
          new ClassProcessor().processFile(file);
        }
        
        FileList.getInstance().updateMenu();
      }
    });
  }
}

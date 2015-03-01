package io.miti.jarman.data;

import io.miti.jarman.gui.Jarman;
import io.miti.jarman.util.WindowState;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import io.miti.jarman.data.FileList;
import io.miti.jarman.data.JarData;
import io.miti.jarman.data.MenuItemAction;

/**
 * This class manages the most-recently opened files listed in the
 * File menu item.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileList
{
  /**
   * The one instance of this class.
   */
  private static FileList data = null;
  
  /**
   * The list of files.
   */
  private List<String> files = null;
  
  /**
   * The number of files to save.
   */
  private static final int MAX_FILES = 4;
  
  /**
   * The property string for the number of files.
   */
  private static final String numFilesProp = "num.files";
  
  
  /**
   * Default constructor.
   */
  private FileList()
  {
    super();
    files = new ArrayList<String>(MAX_FILES);
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static FileList getInstance()
  {
    if (data == null)
    {
      data = new FileList();
    }
    
    return data;
  }
  
  
  /**
   * Load the data from the properties file.
   * 
   * @param props the properties file instance
   */
  public void loadData(final Properties props)
  {
    // Get the number of files listed in the properties file
    final int count = Math.min(MAX_FILES, WindowState.parseInteger(props, numFilesProp));
    if (count < 1)
    {
      return;
    }
    
    // Get each file
    for (int i = 0; i < count; ++i)
    {
      // Get the next file in the list
      String key = "file." + Integer.toString(i);
      String val = (String) props.get(key);
      
      // If there's no entry or it's blank, skip it
      if ((val == null) || (val.trim().length() < 1))
      {
        continue;
      }
      
      // Add the file
      files.add(val.trim());
    }
  }
  
  
  /**
   * Add a file to the list.
   * 
   * @param file the file to add
   */
  public void addFile(final File file)
  {
    // Look for a match on the filename
    final String name = file.getAbsolutePath();
    int match = -1;
    final int size = files.size();
    for (int i = 0; i < size; ++i)
    {
      if (name.equals(files.get(i)))
      {
        match = i;
        break;
      }
    }
    
    // Check if/where the match was found
    if (match == 0)
    {
      // The file is already at the top of the list, so do nothing
      return;
    }
    else if (match > 0)
    {
      // We found the file, and now we need to move it to the top
      files.remove(match);
      files.add(0, name);
    }
    else
    {
      // Add to the top of the list
      files.add(0, name);
      
      // If the list is too big, remove the last item
      if (files.size() > MAX_FILES)
      {
        files.remove(MAX_FILES);
      }
    }
  }
  
  
  /**
   * Remove the entry with the specified index.
   * 
   * @param index the index of the file to remove
   */
  public void removeFile(final int index)
  {
    files.remove(index);
  }
  
  
  /**
   * Update the menu item.
   */
  public void updateMenu()
  {
    // Get the File menu and remove any extra menu items
    JMenu menu = Jarman.getApp().getFrame().getJMenuBar().getMenu(0);
    final int count = menu.getItemCount();
    int numLeft = count - 8;
    
    // Remove any files and separator after the first separator
    while (numLeft > 0)
    {
      menu.remove(7);
      --numLeft;
    }
    
    // If no files to list, return
    final int size = files.size();
    if (size < 1)
    {
      return;
    }
    
    // Add a separator after File | List All
    int menuIndex = 6;
    menu.insertSeparator(menuIndex++);
    
    // Add the files
    int fileIndex = 0;
    for (int i = 0; i < size; ++i)
    {
      String name = parseFilename(files.get(i));
      if (name == null)
      {
        continue;
      }
      
      // Create the menu item
      String text = Integer.toString(fileIndex + 1) + " " + name;
      MenuItemAction action = new MenuItemAction(text, i, files.get(i));
      JMenuItem item = new JMenuItem(action);
      item.setMnemonic(KeyEvent.VK_1 + fileIndex);
      menu.insert(item, menuIndex++);
      ++fileIndex;
    }
  }
  
  
  /**
   * Parse the name of the file from the full path.
   * 
   * @param fullPath the full name of the file
   * @return the filename
   */
  private static String parseFilename(final String fullPath)
  {
    final int slashIndex = JarData.getLastSlashIndex(fullPath);
    final String fname = ((slashIndex >= 0) ? fullPath.substring(
                             slashIndex + 1) : fullPath);
    return fname;
  }
  
  
  /**
   * Save the data to the properties file.
   * 
   * @param props the properties file instance
   */
  public void saveData(final Properties props)
  {
    // Save the number of files, and if any, save each filename
    final int size = files.size();
    props.put(numFilesProp, Integer.toString(size));
    for (int i = 0; i < size; ++i)
    {
      String key = "file." + Integer.toString(i);
      props.put(key, files.get(i));
    }
  }
}

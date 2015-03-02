package io.miti.jarman.util;

import io.miti.jarman.data.FileList;
import io.miti.jarman.data.JarData;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.ListingsPage;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JOptionPane;

/**
 * Process a jar file.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class JarProcessor
{
  /**
   * Default constructor.
   */
  public JarProcessor()
  {
    super();
  }
  
  
  /**
   * Process the file.
   * 
   * @param file the file to process
   * @param recurse whether to save the external jars referenced by the manifest
   */
  public void processFile(final File file, final boolean recurse)
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
    boolean result = getJarAttributes(file);
    if (!result)
    {
      // Show the previous cursor
      Jarman.getApp().getFrame().setCursor(cursor);
      
      // This is a bad file
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
            "The file appears to be invalid",
            "Error Opening File", JOptionPane.ERROR_MESSAGE);
      JarData.getInstance().setJarFile(null);
      Jarman.getApp().updateFileInfo();
      return;
    }
    
    // Save the files used by the jar
    getJarDataFiles(file, recurse);
    
    // Fire a table data change
    JarData.getInstance().resetTables(false);
    
    Jarman.getApp().updateFileInfo();
    
    // Update the tabs in the frame
    Jarman.getApp().updateTabsForJar();
    
    // Show the previous cursor
    Jarman.getApp().getFrame().setCursor(cursor);
  }
  
  
  /**
   * Get the jar files in the jar's class path (from the manifest).
   * 
   * @param file the parent jar file
   * @param recurse whether to save the external jars referenced by the manifest
   */
  private void getJarDataFiles(final File file, final boolean recurse)
  {
    // Save the file that was opened
    saveJarData(file, true);
    
    // Iterate over the referenced jar files
    Iterator<File> paths = JarData.getInstance().getPath();
    if (paths != null)
    {
      while (paths.hasNext())
      {
        File path = paths.next();
        saveJarData(path, recurse);
      }
    }
    
    JarData.getInstance().checkForDuplicates();
  }
  
  
  /**
   * Save the data on all files in this jar file.
   * 
   * @param file the jar file
   * @param recurse whether to save the external jars referenced by the manifest
   */
  private void saveJarData(final File file, final boolean recurse)
  {
    // Verify the file exists
    if (!file.exists())
    {
      JarData.getInstance().addJarFileEntry(file.getAbsolutePath(), false, 0, 0L, 0L);
      return;
    }
    
    try
    {
      // Open the Jar file and iterate over its contents
      final JarFile jar = new JarFile(file, true);
      
      // Check if we want to look at all of the files in this jar file
      int count = 0;
      if (recurse)
      {
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements())
        {
          // Get the next zip entry and save it; if added, increment the count
          JarEntry entry = entries.nextElement();
          if (JarData.getInstance().addJarEntry(file, entry))
          {
            ++count;
          }
        }
      }
      else
      {
        // This count includes directories.  If recurse=true, the count
        // does not include directories.
        count = jar.size();
      }
      
      // Save the jar file and include the number of files it contains
      JarData.getInstance().addJarFileEntry(file.getAbsolutePath(), true, count,
                                            file.length(), file.lastModified());
      
      jar.close();
    }
    catch (IOException e)
    {
      JarData.getInstance().addJarFileEntry(file.getAbsolutePath(), false, 0, 0L, 0L);
      e.printStackTrace();
    }
  }
  
  
  /**
   * Verify the file is valid and save the contents of the manifest.
   * 
   * @param file the file to open
   * @return whether the file is valid
   */
  private boolean getJarAttributes(final File file)
  {
    boolean result = false;
    try
    {
      // Open the jar file; if it's invalid, it will throw an exception
      JarFile jar = new JarFile(file, false);
      
      // If we reach this point, the file was opened successfully
      result = true;
      FileList.getInstance().addFile(file);
      
      // Get the manifest
      Manifest man = jar.getManifest();
      if (man == null)
      {
    	jar.close();
        return true;
      }
      
      // Get the main attributes of the jar file's manifest;
      // this includes Main-Class and Class-Path
      Attributes attrs = man.getMainAttributes();
      if (attrs != null)
      {
        for (Entry<Object, Object> attr : attrs.entrySet())
        {
          String key = attr.getKey().toString();
          String val = attr.getValue().toString();
          JarData.getInstance().addManifestEntry(key, val);
        }
      }
      
      jar.close();
    }
    catch (IOException ioe)
    {
      result = false;
    }
    
    return result;
  }
}

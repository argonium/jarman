package io.miti.jarman.data;

import io.miti.jarman.gui.JarPage;
import io.miti.jarman.gui.ListingsPage;
import io.miti.jarman.gui.ManifestPage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import io.miti.jarman.data.FileData;
import io.miti.jarman.data.JarData;
import io.miti.jarman.data.JarStatus;

/**
 * The data used by the app.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class JarData
{
  /**
   * The one instance of this class.
   */
  private static JarData data = null;
  
  /**
   * The parent jar file.
   */
  private File jar = null;
  
  /**
   * The list of manifest data.
   */
  private Map<String, String> manifest = new HashMap<String, String>(20);
  
  /**
   * The list of data on each file in each jar.
   */
  private List<FileData> filedata = new ArrayList<FileData>(20);
  
  /**
   * The list of indexes to matches on file (CRC).
   */
  private List<Integer> fileMatchIndexes = new ArrayList<Integer>(20);
  
  /**
   * The list of indexes to matches on file path/name.
   */
  private List<Integer> nameMatchIndexes = new ArrayList<Integer>(20);
  
  /**
   * The list of jars in the class path, and the status (found or not).
   */
  private List<JarStatus> jarlist = new ArrayList<JarStatus>(20);
  
  
  /**
   * Default constructor.
   */
  private JarData()
  {
    super();
  }
  
  
  /**
   * Get the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static JarData getInstance()
  {
    if (data == null)
    {
      data = new JarData();
    }
    
    return data;
  }
  
  
  /**
   * Return whether there is data in the cache.
   * 
   * @return whether there is data in the cache
   */
  public static boolean hasData()
  {
    return ((data != null) && (data.jar != null));
  }
  
  
  /**
   * Reset the data and tables.
   */
  public static void reset()
  {
    data = null;
    ListingsPage.getInstance().emptyTable();
    ManifestPage.getInstance().emptyTable();
    JarPage.getInstance().emptyTable();
  }
  
  
  /**
   * Set the parent jar file.
   * 
   * @param pJar the parent jar file
   */
  public void setJarFile(final File pJar)
  {
    jar = pJar;
  }
  
  
  /**
   * Return the name of the parent jar file.
   * 
   * @return the name of the parent jar file
   */
  public String getJarFileName()
  {
    if (jar == null)
    {
      return null;
    }
    
    return jar.getAbsolutePath();
  }
  
  
  /**
   * Add a manifest entry.
   * 
   * @param key the key
   * @param value the value
   */
  public void addManifestEntry(final String key, final String value)
  {
    manifest.put(key, value);
  }
  
  
  /**
   * Return the name of the main class.  Can be null.
   * 
   * @return the name of the main class
   */
  public String getMainClass()
  {
    return manifest.get("Main-Class");
  }
  
  
  /**
   * Return a file iterator over the jars in the class path.
   * 
   * @return a file iterator over the jars in the class path
   */
  public Iterator<File> getPath()
  {
    String path = manifest.get("Class-Path");
    if (path == null)
    {
      return null;
    }
    
    StringTokenizer st = new StringTokenizer(path, " ");
    List<File> dirs = new ArrayList<File>(20);
    while (st.hasMoreTokens())
    {
      String token = st.nextToken().trim();
      File child = new File(jar.getParentFile(), token);
      dirs.add(child);
    }
    
    return dirs.iterator();
  }
  
  
  /**
   * Add the jar entry.  Directories are not saved.
   * 
   * @param file the parent jar file
   * @param entry the jar entry
   * @return whether the entry was saved (directories are skipped)
   */
  public boolean addJarEntry(final File file, final JarEntry entry)
  {
    // Skip directories
    if (entry.isDirectory())
    {
      return false;
    }
    
    // Make sure the name doesn't end with a slash
    final String name = entry.getName();
    final int slashIndex = getLastSlashIndex(name);
    if (slashIndex == (name.length() - 1))
    {
      return false;
    }
    
    // Parse the file name and full path from the full name
    final String fname = ((slashIndex >= 0) ? name.substring(slashIndex + 1) : name);
    final String fpath = (slashIndex <= 0) ? "" : name.substring(0, slashIndex);
    
    // Add the entry to the list
    filedata.add(new FileData(file, entry, fname, fpath, false, false));
    
    return true;
  }
  
  
  /**
   * Add the jar entry.  Directories are not saved.
   * 
   * @param file the file to add
   * @return whether the entry was saved (directories are skipped)
   */
  public boolean addJarEntry(final File file)
  {
    // Make sure the name doesn't end with a slash
    final String name = file.getAbsolutePath();
    final int slashIndex = getLastSlashIndex(name);
    if (slashIndex == (name.length() - 1))
    {
      return false;
    }
    
    // Parse the file name and full path from the full name
    final String fname = ((slashIndex >= 0) ? name.substring(slashIndex + 1) : name);
    final String fpath = (slashIndex <= 0) ? "" : name.substring(0, slashIndex);
    
    // Add the entry to the list
    filedata.add(new FileData(file, fname, fpath, false, false));
    
    return true;
  }
  
  
  /**
   * Return the index of the last slash (forward or backward) in the filename.
   * 
   * @param name the name of the file (full path)
   * @return the index of the last slash, or -1 if not found
   */
  public static int getLastSlashIndex(final String name)
  {
    int forwardIndex = name.lastIndexOf('/');
    int backIndex = name.lastIndexOf('\\');
    int index = -1;
    if ((forwardIndex < 0) && (backIndex < 0))
    {
      index = -1;
    }
    else if (forwardIndex > 0)
    {
      index = forwardIndex;
    }
    else if (backIndex > 0)
    {
      index = backIndex;
    }
    else
    {
      index = Math.max(backIndex, forwardIndex);
    }
    
    return index;
  }
  
  
  /**
   * Return the number of records to show in the jar contents table.
   * 
   * @return the number of records to show in the jar contents table
   */
  public int getJarDataCount()
  {
    if (ListingsPage.isNull())
    {
      return 0;
    }
    
    switch (ListingsPage.getInstance().getDisplayIndex())
    {
      case 0: return filedata.size();
      case 1: return nameMatchIndexes.size();
      case 2: return fileMatchIndexes.size();
      default: break;
    }
    
    return 0;
  }
  
  
  /**
   * Return an iterator to all the file data.
   * 
   * @return an iterator to all the file data
   */
  public Iterator<FileData> getAllFileData()
  {
    if (ListingsPage.isNull())
    {
      return null;
    }
    
    return filedata.iterator();
  }
  
  
  /**
   * Get the data for the specified row in the Listings table.
   * 
   * @param rowIndex the row we need data for
   * @return the data for that row
   */
  public FileData getJarFileData(final int rowIndex)
  {
    if (ListingsPage.isNull())
    {
      return null;
    }
    
    int index = rowIndex;
    switch (ListingsPage.getInstance().getDisplayIndex())
    {
      case 1: index = nameMatchIndexes.get(rowIndex).intValue();
              break;
      case 2: index = fileMatchIndexes.get(rowIndex).intValue();
              break;
      default: break;
    }
    
    return filedata.get(index);
  }
  
  
  /**
   * Return the number of entries in the manifest.
   * 
   * @return the number of entries in the manifest
   */
  public int getManifestCount()
  {
    return manifest.size();
  }
  
  
  /**
   * For an update of the JTables in the application.
   * 
   * @param updateListingOnly whether to only update the listing table
   */
  public void resetTables(final boolean updateListingOnly)
  {
    ListingsPage.getInstance().updateTable();
    
    if (!updateListingOnly)
    {
      ManifestPage.getInstance().updateTable();
      JarPage.getInstance().updateTable();
    }
  }
  
  
  /**
   * Return the manifest value for the JTable.  The column index determines
   * if the returned string is the key or the value.
   * 
   * @param rowIndex the index of the table row
   * @param columnIndex the index of the table column
   * @return the string for the specified row and column
   */
  public String getManifestValue(final int rowIndex, final int columnIndex)
  {
    int index = 0;
    String value = null;
    for (Entry<String, String> entry : manifest.entrySet())
    {
      if (index == rowIndex)
      {
        value = (columnIndex == 0) ? entry.getKey() : entry.getValue();
        break;
      }
      
      ++index;
    }
    
    return value;
  }
  
  
  /**
   * Add a jar file entry to the data cache.
   * 
   * @param jarName the name of the jar
   * @param jarFound whether the jar is in the manifest's classpath
   * @param fileCount the number of files in the jar
   * @param size the size of the jar file
   * @param date the last-modified date of the jar file
   */
  public void addJarFileEntry(final String jarName, final boolean jarFound,
                               final int fileCount, final long size,
                               final long date)
  {
    jarlist.add(new JarStatus(jarName, jarFound, fileCount, size, date));
  }
  
  
  /**
   * Return the number of jars in the jar cache.
   * 
   * @return the number of jars in the jar cache
   */
  public int getJarFileCount()
  {
    return jarlist.size();
  }
  
  
  /**
   * Return the string for the Jar table, based on the row and column.
   * 
   * @param rowIndex the index of the table row
   * @param columnIndex the index of the table column
   * @return the string for the table at row and column
   */
  public Object getJarFileValue(final int rowIndex, final int columnIndex)
  {
    final JarStatus jarStatus = jarlist.get(rowIndex);
    switch (columnIndex)
    {
      case 0: return Boolean.valueOf(jarStatus.isFound());
      case 1: return jarStatus.getName();
      case 2: return Long.valueOf(jarStatus.getDate());
      case 3: return Integer.valueOf(jarStatus.getFileCount());
      case 4: return Long.valueOf(jarStatus.getSize());
      default: return null;
    }
  }
  
  
  /**
   * Check the FileData list for duplicate file entries.
   */
  public void checkForDuplicates()
  {
    // Get the size and check if the list is empty
    final int size = filedata.size();
    if (size == 0)
    {
      return;
    }
    
    // Iterate over each item in the list, except for the last item
    for (int i = 0; i < (size - 1); ++i)
    {
      // Get the current item and initialize the two match variables
      FileData fdSrc = filedata.get(i);
      boolean nameMatch = false;
      boolean crcMatch = false;
      
      // Iterate over each item in the list after fdSrc
      for (int j = i + 1; j < size; ++j)
      {
        // Get the current target
        FileData fdTarget = filedata.get(j);
        
        // Check for a CRC match and name match
        final boolean sameName = fdTarget.getFullPath().equals(fdSrc.getFullPath());
        if (sameName && (fdTarget.getCrc() == fdSrc.getCrc()))
        {
          // We have a match.  If this was not already tagged as a match, then
          // set it now and save its index.
          if (!fdTarget.isCRCMatch())
          {
            fdTarget.setCRCMatch(true);
            fileMatchIndexes.add(Integer.valueOf(j));
            crcMatch = true;
          }
        }
        
        // Check for a match on full file name (including path)
        if (sameName)
        {
          // We have a match.  If this was not already tagged as a match, then
          // set it now and save its index.
          if (!fdTarget.isNameMatch())
          {
            fdTarget.setNameMatch(true);
            nameMatchIndexes.add(Integer.valueOf(j));
            nameMatch = true;
          }
        }
      }
      
      // If we found a match on CRC, then update the src object and save its index
      if (crcMatch && !fdSrc.isCRCMatch())
      {
        fdSrc.setCRCMatch(true);
        fileMatchIndexes.add(Integer.valueOf(i));
      }
      
      // If we found a match on path/name, then update the src object and save its index
      if (nameMatch && !fdSrc.isNameMatch())
      {
        fdSrc.setNameMatch(true);
        nameMatchIndexes.add(Integer.valueOf(i));
      }
    }
  }
  
  
  /**
   * Return the jar entry from a jar file matching on target.
   * 
   * @param target the entry to match
   * @return the JarEntry object for target
   */
  public JarEntry getJarEntry(final FileData target)
  {
    JarEntry targetEntry = null;
    
    // Create the jar file object
    JarFile jarfile = null;
    try
    {
      // Create the jar file object and iterate over the entries
      jarfile = new JarFile(target.getJar());
      Enumeration<JarEntry> entries = jarfile.entries();
      while (entries.hasMoreElements())
      {
        // If this element matches on name and CRC, we have a match
        JarEntry entry = entries.nextElement();
        if ((entry.getName().equals(target.getFullPath())) &&
            (entry.getCrc() == target.getCrc()))
        {
          // Save the entry and break out of the loop
          targetEntry = entry;
          break;
        }
      }
      
      // Close the jar file
      jarfile.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (jarfile != null)
      {
        try
        {
          jarfile.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        
        jarfile = null;
      }
    }
    
    return targetEntry;
  }
}

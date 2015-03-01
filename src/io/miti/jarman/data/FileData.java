package io.miti.jarman.data;

import io.miti.jarman.classref.ClassFileParser;
import io.miti.jarman.classref.ClassInfo;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.util.ClassParser;
import io.miti.jarman.util.SystemPathList;
import io.miti.jarman.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import io.miti.jarman.data.FileData;
import io.miti.jarman.data.JarData;

/**
 * The data structure of information stored about files to display
 * in the Listings page (the data on each file in each jar file).
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileData
{
  /**
   * The file name (no path).
   */
  private String filename;
  
  /**
   * The directory of the file entry.
   */
  private String filedir;
  
  /**
   * The file name with path.
   */
  private String fullpath;
  
  /**
   * The CRC value.
   */
  private long crc;
  
  /**
   * The date the file was last modified.
   */
  private long date;
  
  /**
   * The size of the file, in bytes.
   */
  private long size;
  
  /**
   * The name of the parent jar.
   */
  private String jar;
  
  /**
   * Whether this file is a CRC match on another file in the list.
   */
  private boolean crcMatch;
  
  /**
   * Whether this file is a name match on another file in the list.
   */
  private boolean nameMatch;
  
  
  /**
   * Default constructor.
   */
  public FileData()
  {
    super();
  }
  
  
  /**
   * Constructor for all values.
   * 
   * @param file the file
   * @param entry the jar entry
   * @param name the file name (no path)
   * @param dir the directory of the file entry
   * @param pCRCMatch whether this is a CRC match
   * @param pNameMatch whether this is a name match
   */
  public FileData(final File file, final JarEntry entry,
                  final String name, final String dir,
                  final boolean pCRCMatch, final boolean pNameMatch)
  {
    filename = name;
    filedir = dir;
    fullpath = entry.getName();
    crc = entry.getCrc();
    date = entry.getTime();
    size = entry.getSize();
    jar = file.getAbsolutePath();
    crcMatch = pCRCMatch;
    nameMatch = pNameMatch;
  }
  
  
  /**
   * Constructor for all values.
   * 
   * @param file the file
   * @param name the file name (no path)
   * @param dir the directory of the file entry
   * @param pCRCMatch whether this is a CRC match
   * @param pNameMatch whether this is a name match
   */
  public FileData(final File file,
                   final String name,
                   final String dir,
                   final boolean pCRCMatch,
                   final boolean pNameMatch)
  {
    filename = name;
    filedir = dir;
    fullpath = file.getAbsolutePath();
    crc = 0L; // TODO Compute the CRC for a single file
    date = file.lastModified();
    size = file.length();
    jar = "";
    crcMatch = pCRCMatch;
    nameMatch = pNameMatch;
  }
  
  
  /**
   * Return just the name of the entry file, no path.
   * 
   * @return the filename
   */
  public String getFileName()
  {
    return filename;
  }
  
  
  /**
   * Return the full path/name of the entry.
   * 
   * @return the full path and name
   */
  public String getFullPath()
  {
    return fullpath;
  }
  
  
  /**
   * @return the crc
   */
  public long getCrc()
  {
    return crc;
  }
  
  
  /**
   * @return the date
   */
  public long getDate()
  {
    return date;
  }
  
  
  /**
   * @return the size
   */
  public long getSize()
  {
    return size;
  }
  
  
  /**
   * @return the jar
   */
  public String getJar()
  {
    return jar;
  }
  
  
  /**
   * Return the directory of the file entry.
   * 
   * @return the directory of the file entry
   */
  public String getFileDir()
  {
    return filedir;
  }
  
  
  /**
   * @return the crcmatch
   */
  public boolean isCRCMatch()
  {
    return crcMatch;
  }
  
  
  /**
   * Set whether this file is a file match (based on CRC).
   * 
   * @param match whether this file is a CRC match
   */
  public void setCRCMatch(final boolean match)
  {
    crcMatch = match;
  }
  
  
  /**
   * @return the namematch
   */
  public boolean isNameMatch()
  {
    return nameMatch;
  }
  
  
  /**
   * Set whether this file is a file name match.
   * 
   * @param match whether this file is a name match
   */
  public void setNameMatch(final boolean match)
  {
    nameMatch = match;
  }
  
  
  /**
   * Return the text from the file.
   * 
   * @return the text from the file
   */
  public String getFileText()
  {
    String s = null;
    if (inJar())
    {
      JarEntry entry = JarData.getInstance().getJarEntry(this);
      JarFile jarFile = null;
      try
      {
        jarFile = new JarFile(jar);
        s = Utility.getFileAsText(jarFile, entry);
        
        jarFile.close();
        jarFile = null;
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (jarFile != null)
        {
          try
          {
            jarFile.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
          jarFile = null;
        }
      }
    }
    else
    {
      s = Utility.getFileAsText(new File(fullpath));
    }
    
    return s;
  }
  
  
  /**
   * Return whether the file is in a jar.
   * 
   * @return whether the file is in a jar
   */
  private boolean inJar()
  {
    return (jar.length() > 0);
  }
  
  
  /**
   * Return a string representing this object.
   * 
   * @return a string representing this object
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(100);
    sb.append(fullpath).append(" (");
    if (jar.length() > 0)
    {
      sb.append(jar);
    }
    else
    {
      sb.append("no-jar");
    }
    sb.append(")");
    
    return sb.toString();
  }
  
  
  /**
   * Return the list of missing classes.
   * 
   * @return the list of missing classes
   */
  public Set<String> getMissingClasses()
  {
    Set<String> missing = null;
    if (inJar())
    {
      missing = readMissingFromJar();
    }
    else
    {
      missing = readMissingFromClass();
    }
    
    return missing;
  }
  
  
  /**
   * Read the list of missing classes from a class file.
   * 
   * @return the list of missing classes from a class file
   */
  private Set<String> readMissingFromClass()
  {
    Set<String> missingClasses = new HashSet<String>(10);
    
    // Get a list of all system classes
    Set<String> sysset = new SystemPathList().getList();
    
    // Get the name of this class
    ClassInfo ci = new ClassFileParser().parseClassFile(new File(fullpath));
    if (ci == null)
    {
      return null;
    }
    
    final String ciName = ci.getClassName();
    
    // Verify that the classes referenced by the jar entry are found,
    // either in the included jars or in the system classpath
    List<String> refs = new ClassParser().getReferences(fullpath);
    if (refs != null)
    {
      // Check that each entry is in the classpath
      final int num = refs.size();
      for (int j = 0; j < num; ++j)
      {
        String ref = refs.get(j);
        if (!sysset.contains(ref) && !ciName.equals(ref))
        {
          missingClasses.add(ref);
        }
      }
    }
    
    return missingClasses;
  }
  
  
  /**
   * Read the list of missing classes from the jar entry.
   * 
   * @return the list of missing classes from the jar entry
   */
  private Set<String> readMissingFromJar()
  {
    Set<String> missing = null;
    
    // Read the file
    JarEntry entry = JarData.getInstance().getJarEntry(this);
    JarFile jarFile = null;
    try
    {
      jarFile = new JarFile(jar);
      missing = getMissingClasses(jarFile, entry);
      jarFile.close();
      jarFile = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (jarFile != null)
      {
        try
        {
          jarFile.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    return missing;
  }
  
  
  /**
   * Return the list of missing classes.
   * 
   * @param jarFile the jar file
   * @param entry the jar file entry
   * @return the list of missing classes
   */
  private Set<String> getMissingClasses(final JarFile jarFile, final JarEntry entry)
  {
    Set<String> missingClasses = new HashSet<String>(10);
    
    // Get a list of all system classes
    Set<String> sysset = new SystemPathList().getList();
    
    // Get a list of all classes that we're showing
    Set<String> jarset = new HashSet<String>(20);
    Iterator<FileData> allData = JarData.getInstance().getAllFileData();
    if (allData == null)
    {
      return missingClasses;
    }
    
    // Iterate over all the file data
    while (allData.hasNext())
    {
      // Get the next element and check if the name ends with .class
      FileData fd = allData.next();
      String name = fd.getFullPath();
      if (name.endsWith(".class"))
      {
        // It does, so save it without the .class
        jarset.add(name.substring(0, name.length() - 6));
      }
    }
    
    // Verify that the classes referenced by the jar entry are found,
    // either in the included jars or in the system classpath
    List<String> refs = new ClassParser().getReferences(jarFile, entry);
    if (refs != null)
    {
      // Check that each entry is in the classpath
      final int num = refs.size();
      for (int j = 0; j < num; ++j)
      {
        String ref = refs.get(j);
        if (!jarset.contains(ref) &&
            !sysset.contains(ref))
        {
          missingClasses.add(ref);
        }
      }
    }
    
    return missingClasses;
  }
  
  
  /**
   * Return the list of included classes.
   * 
   * @return the list of included classes
   */
  public Set<String> getIncludedClasses()
  {
    Set<String> incl = null;
    if (inJar())
    {
      incl = readIncludedFromJar();
    }
    else
    {
      incl = readIncludedFromClass();
    }
    
    return incl;
  }
  
  
  /**
   * Get the list of included classes for a class file.
   * 
   * @return the list of included classes for a class file
   */
  private Set<String> readIncludedFromClass()
  {
    Set<String> linkedClasses = new HashSet<String>(10);
    
    // Get all classes referenced by the selected jar file entry
    List<String> refs = new ClassParser().getReferences(new File(fullpath));
    if (refs != null)
    {
      // Add each item in the list to the set
      for (String ref : refs)
      {
        linkedClasses.add(ref);
      }
    }
    
    return linkedClasses;
  }
  
  
  /**
   * Get the list of included classes for a class file in a jar.
   * 
   * @return the list of included classes for a class file in a jar
   */
  private Set<String> readIncludedFromJar()
  {
    Set<String> incl = null;
    
    // Read the file
    JarEntry entry = JarData.getInstance().getJarEntry(this);
    JarFile jarFile = null;
    try
    {
      jarFile = new JarFile(jar);
      incl = getIncludedJarClasses(jarFile, entry);
      jarFile.close();
      jarFile = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (jarFile != null)
      {
        try
        {
          jarFile.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    return incl;
  }
  
  
  /**
   * Return the list of included classes.
   * 
   * @param jarFile the jar file
   * @param entry the jar file entry
   * @return the list of missing classes
   */
  private Set<String> getIncludedJarClasses(final JarFile jarFile, final JarEntry entry)
  {
    Set<String> linkedClasses = new HashSet<String>(10);
    
    // Get all classes referenced by the selected jar file entry
    List<String> refs = new ClassParser().getReferences(jarFile, entry);
    if (refs != null)
    {
      // Add each item in the list to the set
      for (String ref : refs)
      {
        linkedClasses.add(ref);
      }
    }
    
    return linkedClasses;
  }
  
  
  /**
   * Return the info on the class.
   * 
   * @return the info on the class
   */
  public ClassInfo getClassInfo()
  {
    ClassInfo ci = null;
    if (inJar())
    {
      ci = readInfoFromJar();
    }
    else
    {
      ci = readInfoFromClass();
    }
    
    return ci;
  }
  
  
  /**
   * Return the info on the class.
   * 
   * @return the info on the class
   */
  private ClassInfo readInfoFromJar()
  {
    ClassInfo ci = null;
    
    // Read the file
    JarEntry entry = JarData.getInstance().getJarEntry(this);
    JarFile jarFile = null;
    try
    {
      jarFile = new JarFile(jar);
      ci = new ClassFileParser().parseClassFile(jarFile, entry);
      if ((ci == null) || (!ci.isValid()))
      {
        JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
            "The selected file is not a valid compiled Java class file",
            "File Error", JOptionPane.ERROR_MESSAGE);
        ci = null;
      }
      
      jarFile.close();
      jarFile = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (jarFile != null)
      {
        try
        {
          jarFile.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    return ci;
  }
  
  
  /**
   * Return the info on the class.
   * 
   * @return the info on the class
   */
  private ClassInfo readInfoFromClass()
  {
    ClassInfo ci = new ClassFileParser().parseClassFile(new File(fullpath));
    if ((ci == null) || (!ci.isValid()))
    {
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
          "The selected file is not a valid compiled Java class file",
          "File Error", JOptionPane.ERROR_MESSAGE);
      ci = null;
    }
    
    return ci;
  }
}

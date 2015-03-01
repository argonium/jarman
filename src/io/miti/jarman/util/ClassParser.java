package io.miti.jarman.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import io.miti.jarman.util.ClassStreamParser2;

/**
 * Parse a .class file and return the list of referenced Java classes.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ClassParser
{
  /**
   * Default constructor.
   */
  public ClassParser()
  {
    super();
  }
  
  
  /**
   * Generate the list of referenced class names.
   * 
   * @param path the path to the input file
   * @return the list of referenced class names
   */
  public List<String> getReferences(final String path)
  {
    return getReferences(new File(path));
  }
  
  
  /**
   * Generate the list of referenced class names.
   * 
   * @param file the input file
   * @return the list of referenced class names
   */
  public List<String> getReferences(final File file)
  {
    // Check the input
    if (file == null)
    {
      return null;
    }
    else if (file.isDirectory())
    {
      return null;
    }
    
    // Verify the name ends with .class.
    final String name = file.getAbsolutePath();
    if (name == null)
    {
      return null;
    }
    else if (!name.endsWith(".class"))
    {
      return null;
    }
    
    // Get the list of referenced classes
    List<String> names = new ArrayList<String>(20);
    try
    {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      DataInputStream stream = new DataInputStream(bis);
      new ClassStreamParser2().parseStream(stream, names);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    // Return the referenced class names
    return names;
  }
  
  
  /**
   * Generate the list of referenced class names.
   * 
   * @param file the jar file
   * @param entry the jar file entry
   * @return the list of referenced class names
   */
  public List<String> getReferences(final JarFile file, final JarEntry entry)
  {
    // Check the input
    if (entry == null)
    {
      return null;
    }
    else if (entry.isDirectory())
    {
      return null;
    }
    
    // Verify the name ends with .class.
    String name = entry.getName();
    if (name == null)
    {
      return null;
    }
    else if (!name.endsWith(".class"))
    {
      return null;
    }
    
    List<String> names = new ArrayList<String>(20);
    try
    {
      BufferedInputStream bis = new BufferedInputStream(file.getInputStream(entry));
      DataInputStream stream = new DataInputStream(bis);
      new ClassStreamParser2().parseStream(stream, names);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    // Return the referenced class names
    return names;
  }
}

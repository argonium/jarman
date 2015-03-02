package io.miti.jarman.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class to generate the list of Java classes in the classpath.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SystemPathList
{
  /**
   * Default constructor.
   */
  public SystemPathList()
  {
    super();
  }
  
  
  /**
   * Return the set of Java system classes.
   * 
   * @return the set of Java system classes
   */
  public Set<String> getList()
  {
    Set<String> set = new HashSet<String>(20);
    
    String pathSep = System.getProperty("path.separator");
    String path = System.getProperty("sun.boot.class.path");
    if ((pathSep == null) || (pathSep.length() < 1))
    {
      return set;
    }
    else if ((path == null) || (path.length() < 1))
    {
      return set;
    }
    
    StringTokenizer st = new StringTokenizer(path, pathSep);
    while (st.hasMoreTokens())
    {
      String token = st.nextToken();
      File file = new File(token);
      if ((file.exists()) && (file.isFile()) && (token.endsWith(".jar")))
      {
        // Process this jar
        processJar(set, file);
      }
    }
    
    // Return the list
    return set;
  }
  
  
  /**
   * Save the classes that are in this jar.
   * 
   * @param set the set to save the class name to
   * @param file the input jar file
   */
  private void processJar(final Set<String> set, final File file)
  {
    try
    {
      JarFile jar = new JarFile(file);
      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements())
      {
        JarEntry entry = entries.nextElement();
        if (!entry.isDirectory())
        {
          String name = entry.getName();
          if (name.endsWith(".class"))
          {
            set.add(name.substring(0, name.length() - 6));
          }
        }
      }
      
      jar.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

package io.miti.jarman.actions;

import io.miti.jarman.util.Utility;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filter the listed files when opening a class file
 * via File | Open Class.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ClassFilter extends FileFilter
{
  /**
   * Default constructor.
   */
  public ClassFilter()
  {
    super();
  }
  
  
  /**
   * Return whether to accept this file.
   * 
   * @param f the file
   * @return whether to accept
   */
  @Override
  public boolean accept(final File f)
  {
    // Check for some non-file cases
    if (f == null)
    {
      return false;
    }
    else if (f.isDirectory())
    {
      return true;
    }
    
    // Get the file's extension (might be null)
    String ext = Utility.getExtension(f);
    if (ext == null)
    {
      return false;
    }
    
    // Check the extension
    boolean match = (ext.equals("class"));
    return match;
  }


  /**
   * Return the description for this filter.
   * 
   * @return the description for this filter
   */
  @Override
  public String getDescription()
  {
    return "Java Class (*.class)";
  }
}

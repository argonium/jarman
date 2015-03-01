package io.miti.jarman.actions;

import io.miti.jarman.data.FileData;
import io.miti.jarman.data.JarData;
import io.miti.jarman.dialog.MissingClassesDlg;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.util.ClassParser;
import io.miti.jarman.util.SystemPathList;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import io.miti.jarman.actions.FileVerify;

/**
 * The action listener for File | Verify, to let the user verify
 * that each class in the open jar file has access to all of the
 * other classes it relies on. 
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileVerify implements ActionListener
{
  /**
   * The one instance of this class.
   */
  private static FileVerify handler = null;
  
  
  /**
   * The default constructor.
   */
  private FileVerify()
  {
    super();
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static FileVerify getInstance()
  {
    if (handler == null)
    {
      handler = new FileVerify();
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
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        processFile();
      }
    });
  }
  
  
  /**
   * Process the selected file.
   */
  private void processFile()
  {
    // Get the list of missing classes
    Cursor cursor = Jarman.getApp().getFrame().getCursor();
    Jarman.getApp().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    Set<String> missing = getMissingClasses();
    Jarman.getApp().getFrame().setCursor(cursor);
    
    // Check if any are missing
    if (missing.isEmpty())
    {
      // The set is empty, so all referenced classes were found
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
                  "No missing classes");
      return;
    }
    
    // Show the missing classes
    new MissingClassesDlg(missing, true);
  }
  
  
  /**
   * Return the list of missing classes.
   * 
   * @return the list of missing classes
   */
  private Set<String> getMissingClasses()
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
    
    // Iterate over all jars and get the classes referenced by each class
    int jarFileCount = JarData.getInstance().getJarFileCount();
    for (int i = 0; i < jarFileCount; ++i)
    {
      // Verify the jar was found
      Boolean found = (Boolean) JarData.getInstance().getJarFileValue(i, 0);
      if (!found.booleanValue())
      {
        continue;
      }
      
      // Get all classes in the jar
      String jarName = (String) JarData.getInstance().getJarFileValue(i, 1);
      try
      {
        JarFile jar = new JarFile(jarName);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements())
        {
          JarEntry entry = entries.nextElement();
          List<String> refs = new ClassParser().getReferences(jar, entry);
          if (refs != null)
          {
            // Check that each entry is in the classpath
            int size = refs.size();
            for (int j = 0; j < size; ++j)
            {
              String ref = refs.get(j);
              if (!jarset.contains(ref) &&
                  !sysset.contains(ref))
              {
                missingClasses.add(ref);
              }
            }
          }
        }
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
      }
    }
    
    return missingClasses;
  }
}

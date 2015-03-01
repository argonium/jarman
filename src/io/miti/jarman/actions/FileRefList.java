package io.miti.jarman.actions;

import io.miti.jarman.data.JarData;
import io.miti.jarman.dialog.MissingClassesDlg;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.util.ClassParser;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import io.miti.jarman.actions.FileRefList;
import io.miti.jarman.classref.ClassFileParser;
import io.miti.jarman.classref.ClassInfo;

/**
 * The action listener for File | List All, to let the user list
 * all referenced classes in the open jar file. 
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FileRefList implements ActionListener
{
  /**
   * The one instance of this class.
   */
  private static FileRefList handler = null;
  
  
  /**
   * The default constructor.
   */
  private FileRefList()
  {
    super();
  }
  
  
  /**
   * Return the one instance of this class.
   * 
   * @return the one instance of this class
   */
  public static FileRefList getInstance()
  {
    if (handler == null)
    {
      handler = new FileRefList();
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
    // Get the list of referenced classes
    Cursor cursor = Jarman.getApp().getFrame().getCursor();
    Jarman.getApp().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    Set<String> refs = getReferencedClasses();  // TODO Use the new parser
    Jarman.getApp().getFrame().setCursor(cursor);
    
    // Check if any are found
    if (refs.isEmpty())
    {
      // The set is empty, so no referenced classes were found
      JOptionPane.showMessageDialog(Jarman.getApp().getFrame(),
                  "No referenced classes");
      return;
    }
    
    // Show the referenced classes
    new MissingClassesDlg(refs, false);
  }
  
  
  /**
   * Return the list of referenced classes.
   * 
   * @return the list of referenced classes
   */
  private Set<String> getReferencedClasses()
  {
    Set<String> refClasses = new HashSet<String>(10);
    
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
            // Add each item in the list to the set
            for (String ref : refs)
            {
              refClasses.add(ref);
            }
          }
        }
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
      }
    }
    
    return refClasses;
  }
  
  
  /**
   * Return the list of referenced classes using the newer class file parser.
   * 
   * @return the list of referenced classes
   */
  @SuppressWarnings("unused")
  private Set<String> getReferencedClasses2()
  {
    Set<String> refClasses = new HashSet<String>(10);
    
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
          ClassInfo ci = new ClassFileParser().parseClassFile(jar, entry);
          if ((ci != null) && (ci.isValid()))
          {
            final Set<String> refs = ci.getClassNamesSet();
            if (refs != null)
            {
              // Add each item in the list to the set
              for (String ref : refs)
              {
                refClasses.add(ref);
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
    
    return refClasses;
  }
}

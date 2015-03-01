package io.miti.jarman.data;

/**
 * The structure used to store data on the entries on the Jars page.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class JarStatus
{
  /**
   * The full name of the jar file.
   */
  private String name;
  
  /**
   * Whether the jar file was found in the main jar's class path.
   */
  private boolean found;
  
  /**
   * The number of files in the jar.
   */
  private int fileCount = 0;
  
  /**
   * The size of the file, in bytes.
   */
  private long size = 0L;
  
  /**
   * The date of the file.
   */
  private long modDate = 0L;
  
  
  /**
   * Default constructor.
   */
  public JarStatus()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param sName the name of the jar file
   * @param bFound whether the jar file was found
   * @param nFileCount the number of files in the jar
   * @param lSize the size of the file
   * @param date the modification date of the jar file
   */
  public JarStatus(final String sName, final boolean bFound,
                    final int nFileCount, final long lSize,
                    final long date)
  {
    name = sName;
    found = bFound;
    fileCount = nFileCount;
    size = lSize;
    modDate = date;
  }
  
  
  /**
   * Return the name of the jar file.
   * 
   * @return the name of the jar file
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Whether the jar file was found.
   * 
   * @return the jar file was found
   */
  public boolean isFound()
  {
    return found;
  }
  
  
  /**
   * Return the number of files in the jar.
   * 
   * @return the number of files in the jar
   */
  public int getFileCount()
  {
    return fileCount;
  }
  
  
  /**
   * Return the size of the file.
   * 
   * @return the size of the file
   */
  public long getSize()
  {
    return size;
  }
  
  
  /**
   * Return the last-modified date of the file.
   * 
   * @return the last-modified date of the file
   */
  public long getDate()
  {
    return modDate;
  }
}

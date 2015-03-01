package io.miti.jarman.util;

/**
 * Determine information about the virtual machine.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SystemInfo
{
  /**
   * Whether the member fields have been initialized.
   */
  private static boolean initialized = false;
  
  /**
   * Whether this JVM is version 5.0 or higher.
   */
  private static boolean isJava5orHigher = false;
  
  /**
   * Whether this JVM is version 6.0 or higher.
   */
  private static boolean isJava6orHigher = false;
  
  /**
   * Whether we're running under Windows.
   */
  private static boolean isWindows = false;
  
  /**
   * The major Java version number.
   */
  private static int majorVersion = 0;
  
  /**
   * The minor Java version number.
   */
  private static int minorVersion = 0;
  
  
  /**
   * Default constructor.
   */
  private SystemInfo()
  {
    super();
  }
  
  
  /**
   * Initialize the member fields.
   */
  public static void initialize()
  {
    // Whether we've already initialized the data
    if (initialized)
    {
      // We have, so return
      return;
    }
    
    // We're about to initialize
    initialized = true;
    
    // Get the OS name and check if this is Windows
    String osName = System.getProperty("os.name");
    if ((osName != null) && (osName.startsWith("Windows")))
    {
      isWindows = true;
    }
    
    // Get the version number
    parseVersionInformation();
    
    // Save whether it matches some version numbers
    isJava5orHigher = meetsOrExceedsVersionNumber(1, 5);
    isJava6orHigher = meetsOrExceedsVersionNumber(1, 6);
  }
  
  
  /**
   * Get the JVM version number and store its values.
   */
  private static void parseVersionInformation()
  {
    // Get the version string
    final String verStr = System.getProperty("java.version");
    if ((verStr == null) || (verStr.length() < 3))
    {
      // The string is null or empty
      return;
    }
    else
    {
      // Get the first period, comes after the major version number
      final int firstPeriodIndex = verStr.indexOf('.');
      if (firstPeriodIndex < 0)
      {
        // No period found at all
        return;
      }
      else
      {
        // Get the second period
        final int secondPeriodIndex = verStr.indexOf('.', firstPeriodIndex + 1);
        if (secondPeriodIndex < 0)
        {
          // No second period found
          return;
        }
        else
        {
          // Parse the major version number (everything before the first period)
          majorVersion =
            Integer.parseInt(verStr.substring(0, firstPeriodIndex));
          
          // Parse the minor version number (everything between the first 2 periods)
          minorVersion = Integer.parseInt(
              verStr.substring(firstPeriodIndex + 1, secondPeriodIndex));
        }
      }
    }
  }
  
  
  /**
   * Returns whether the JVM version meets or exceeds the specified values.
   * 
   * @param nMajor the major version number to check for
   * @param nMinor the minor version number to check for
   * @return whether the JVM version meets or exceeds the specified values
   */
  public static boolean meetsOrExceedsVersionNumber(final int nMajor, final int nMinor)
  {
    // Check the version number
    if (majorVersion > nMajor)
    {
      // We're at a higher major version number; this is ok
      return true;
    }
    else if (majorVersion < nMajor)
    {
      // We're at a lower major version number; this is bad
      return false;
    }
    
    // We're at the same major version number, so return true only
    // if we're at least at the requested minor version number
    return (minorVersion >= nMinor);
  }
  
  
  /**
   * Return whether this is Java 5 or higher.
   * 
   * @return whether this is Java 5 or higher
   */
  public static boolean isJava5orHigher()
  {
    // Check if this is initialized
    if (!initialized)
    {
      initialize();
    }
    
    return isJava5orHigher;
  }
  
  
  /**
   * Return whether this is Java 6 or higher.
   * 
   * @return whether this is Java 6 or higher
   */
  public static boolean isJava6orHigher()
  {
    // Check if this is initialized
    if (!initialized)
    {
      initialize();
    }
    
    return isJava6orHigher;
  }
  
  
  /**
   * Return whether this is Windows.
   * 
   * @return whether this is Windows
   */
  public static boolean isWindows()
  {
    // Check if this is initialized
    if (!initialized)
    {
      initialize();
    }
    
    return isWindows;
  }
  
  
  /**
   * Return the current version number as a string.
   * 
   * @return the current version number as a string
   */
  public static String getCurrentJavaVersionAsString()
  {
    // Check if this is initialized
    if (!initialized)
    {
      initialize();
    }
    
    // Build the string
    StringBuilder sb = new StringBuilder(20);
    sb.append(Integer.toString(majorVersion)).append('.')
      .append(Integer.toString(minorVersion));
    
    // Return the string
    return sb.toString();
  }
}

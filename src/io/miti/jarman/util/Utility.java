package io.miti.jarman.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * Utility methods.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Utility
{
  /**
   * Whether to read input files as a stream.
   */
  private static boolean readAsStream = false;
  
  /**
   * The current directory (that a file was opened in).
   */
  private static File currentDir = new File(".");
  
  /**
   * The line separator for this OS.
   */
  private static String lineSep = null;
  
  /**
   * The characters for the hex conversion.
   */
  private static final char[] hexChar = {'0', '1', '2', '3',
                                         '4', '5', '6', '7',
                                         '8', '9', 'A', 'B',
                                         'C', 'D', 'E', 'F'};
  
  /**
   * Default constructor.
   */
  private Utility()
  {
    super();
  }
  
  
  /**
   * Return the application name.
   * 
   * @return the application name
   */
  public static String getAppName()
  {
    return "JarMan";
  }
  
  
  /**
   * Get the extension of a file.
   * 
   * @param f the file
   * @return the extension of the file
   */
  public static String getExtension(final File f)
  {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');
    
    if (i > 0 &&  i < s.length() - 1)
    {
      ext = s.substring(i+1).toLowerCase();
    }
    
    return ext;
  }
  
  
  /**
   * Return the line separator for this OS.
   * 
   * @return the line separator for this OS
   */
  public static String getLineSeparator()
  {
    // See if it's been initialized
    if (lineSep == null)
    {
      lineSep = System.getProperty("line.separator");
    }
    
    return lineSep;
  }
  
  
  /**
   * Whether to read content files as a stream.  This
   * is used when running the program as a standalone
   * jar file.
   * 
   * @param useStream whether to read files via a stream
   */
  public static void readFilesAsStream(final boolean useStream)
  {
    readAsStream = useStream;
  }
  
  
  /**
   * Whether to read content files as a stream.
   * 
   * @return whether to read content files as a stream
   */
  public static boolean readFilesAsStream()
  {
    return readAsStream;
  }
  
  
  /**
   * Sleep for the specified number of milliseconds.
   * 
   * @param time the number of milliseconds to sleep
   */
  public static void sleep(final long time)
  {
    try
    {
      Thread.sleep(time);
    }
    catch (InterruptedException e)
    {
      Logger.error(e);
    }
  }
  
  
  /**
   * Return whether this number is effectively zero.
   * 
   * @param value the input value
   * @return whether this is effectively zero
   */
  public static boolean isZero(final double value)
  {
    return ((Math.abs(value) < 0.00000001));
  }
  
  
  /**
   * Return a colon-separated string as two integers, in a Point.
   * 
   * @param sInput the input string
   * @param defaultValue the default value
   * @return a point containing the two integers
   */
  public static Point getStringAsPoint(final String sInput,
                                       final int defaultValue)
  {
    // Declare the return value
    Point pt = new Point(defaultValue, defaultValue);
    
    // Check the input
    if (sInput == null)
    {
      return pt;
    }
    
    // Find the first colon
    final int colonIndex = sInput.indexOf(':');
    if (colonIndex < 0)
    {
      // There is no colon, so save the whole string
      // as the x value and return
      pt.x = Utility.getStringAsInteger(sInput, defaultValue, defaultValue);
      return pt;
    }
    
    // Get the two values.  Everything before the colon
    // is returned in x, and everything after the colon
    // is returned in y.
    pt.x = Utility.getStringAsInteger(sInput.substring(0, colonIndex),
                                      defaultValue, defaultValue);
    pt.y = Utility.getStringAsInteger(sInput.substring(colonIndex + 1),
                                      defaultValue, defaultValue);
    
    // Return the value
    return pt;
  }
  
  
  /**
   * Convert a string into an integer.
   * 
   * @param sInput the input string
   * @param defaultValue the default value
   * @param emptyValue the value to return for an empty string
   * @return the value as an integer
   */
  public static int getStringAsInteger(final String sInput,
                                       final int defaultValue,
                                       final int emptyValue)
  {
    // This is the variable that gets returned
    int value = defaultValue;
    
    // Check the input
    if (sInput == null)
    {
      return emptyValue;
    }
    
    // Trim the string
    final String inStr = sInput.trim();
    if (inStr.length() < 1)
    {
      // The string is empty
      return emptyValue;
    }
    
    // Convert the number
    try
    {
      value = Integer.parseInt(inStr);
    }
    catch (NumberFormatException nfe)
    {
      value = defaultValue;
    }
    
    // Return the value
    return value;
  }
  
  
  /**
   * Convert a string into a floating point number.
   * 
   * @param sInput the input string
   * @param defaultValue the default value
   * @param emptyValue the value to return for an empty string
   * @return the value as a float
   */
  public static float getStringAsFloat(final String sInput,
                                       final float defaultValue,
                                       final float emptyValue)
  {
    // This is the variable that gets returned
    float fValue = defaultValue;
    
    // Check the input
    if (sInput == null)
    {
      return emptyValue;
    }
    
    // Trim the string
    final String inStr = sInput.trim();
    if (inStr.length() < 1)
    {
      // The string is empty
      return emptyValue;
    }
    
    // Convert the number
    try
    {
      fValue = Float.parseFloat(inStr);
    }
    catch (NumberFormatException nfe)
    {
      fValue = defaultValue;
    }
    
    // Return the value
    return fValue;
  }
  
  
  /**
   * Convert a string into a double.
   * 
   * @param sInput the input string
   * @param defaultValue the default value
   * @param emptyValue the value to return for an empty string
   * @return the value as a double
   */
  public static double getStringAsDouble(final String sInput,
                                         final double defaultValue,
                                         final double emptyValue)
  {
    // This is the variable that gets returned
    double value = defaultValue;
    
    // Check the input
    if (sInput == null)
    {
      return emptyValue;
    }
    
    // Trim the string
    final String inStr = sInput.trim();
    if (inStr.length() < 1)
    {
      // The string is empty
      return emptyValue;
    }
    
    // Convert the number
    try
    {
      value = Double.parseDouble(inStr);
    }
    catch (NumberFormatException nfe)
    {
      value = defaultValue;
    }
    
    // Return the value
    return value;
  }
  
  
  /**
   * Returns the input value as a string with the default number of
   * digits in the mantissa.
   * 
   * @param value the input value
   * @return the score as a String
   */
  public static String toString(final double value)
  {
    return Utility.toString(value, 5);
  }
  
  
  /**
   * Returns the input value as a string with a specified number of
   * digits in the mantissa.
   * 
   * @param value the input value
   * @param nMantissaDigits the number of digits to the right of the decimal
   * @return the score as a String
   */
  public static String toString(final double value, final int nMantissaDigits)
  {
    // Set reasonable bounds for the precision
    final int nPrecision = Math.min(10, Math.max(0, nMantissaDigits));
    
    // Construct the StringBuffer to hold the formatting string.
    // This is necessary since the format string is variable due
    // to the nMantissaDigits value.  The "-" means the string
    // is left-justified.
    StringBuilder buf = new StringBuilder(10);
    buf.append("%-5.").append(Integer.toString(nPrecision))
       .append("f");
    
    // Construct the string and return it to the caller.
    // Trim it since the output string may have trailing
    // spaces.
    final String result = String.format(buf.toString(), value).trim();
    
    // Save the length
    final int len = result.length();
    if (len < 3)
    {
      return result;
    }
    
    // Check the position of any decimals
    final int decPos = result.lastIndexOf('.');
    if ((decPos < 0) || (decPos >= (len - 2)))
    {
      return result;
    }
    
    // Trim any trailing zeros, except one just after the decimal
    int index = len - 1;
    buf = new StringBuilder(result);
    while ((index > (decPos + 1)) && (result.charAt(index) == '0'))
    {
      // Delete the character
      buf.deleteCharAt(index);
      
      // Decrement the index
      --index;
    }
    
    return buf.toString();
  }
  
  
  /**
   * Return whether the string is null or has no length.
   * 
   * @param msg the input string
   * @return whether the string is null or has no length
   */
  public static boolean isStringEmpty(final String msg)
  {
    return ((msg == null) || (msg.length() == 0));
  }
  
  
  /**
   * Make the application compatible with Apple Macs.
   */
  public static void makeMacCompatible()
  {
    // Set the system properties that a Mac uses
    System.setProperty("apple.awt.brushMetalLook", "true");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.showGrowBox", "true");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                       getAppName());
  }
  
  
  /**
   * Initialize the application's Look And Feel with the default
   * for this OS.
   */
  public static void initLookAndFeel()
  {
    // Use the default look and feel
    try
    {
      javax.swing.UIManager.setLookAndFeel(
        javax.swing.UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      Logger.error("Exception: " + e.getMessage());
    }
  }
  
  
  /**
   * Verify the user has the minimum version of the JVM
   * needed to run the application.
   * 
   * @return whether the JVM is the minimum supported version
   */
  public static boolean hasRequiredJVMVersion()
  {
    // The value that gets returned
    boolean status = true;
    
    // Check the version number
    status = SystemInfo.isJava6orHigher();
    if (!status)
    {
      // This will hold the error string
      StringBuilder sb = new StringBuilder(100);
      
      sb.append("This application requires Java 1.6 (6.0) or later")
        .append(".\nYour installed version of Java is ")
        .append(SystemInfo.getCurrentJavaVersionAsString())
        .append('.');
      
      // Show an error message to the user
      JOptionPane.showMessageDialog(null, sb.toString(),
                         "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Return the status code
    return status;
  }
  
  
  /**
   * Center the application on the screen.
   * 
   * @param comp the component to center on the screen
   */
  public static void centerOnScreen(final java.awt.Component comp)
  {
    // Get the size of the screen
    Dimension screenDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    // Determine the new location of the window
    int x = (screenDim.width - comp.getSize().width) / 2;
    int y = (screenDim.height - comp.getSize().height) / 2;
    
    // Move the window
    comp.setLocation(x, y);
  }
  
  
  /**
   * Return an AWT image based on the filename.
   * 
   * @param fileName the image file name
   * @return the AWT image
   */
  public static java.awt.Image loadImageByName(final String fileName)
  {
    // The icon that gets returned
    Image icon = null;
    
    // Load the resource
    URL url = Utility.class.getResource(fileName);
    if (url != null)
    {
      icon = Toolkit.getDefaultToolkit().getImage(url);
    }
    else
    {
      Logger.error("Error: Unable to open the file " + fileName);
      return null;
    }
    
    // Return the AWT image
    return icon;
  }
  
  
  /**
   * Load the buffered image by name.
   * 
   * @param loc the name of the image file
   * @return the image
   */
  public static BufferedImage loadBufferedImageByName(final String loc)
  {
    // The icon that gets returned
    BufferedImage image = null;
    
    // Load the resource
    URL url = Utility.class.getResource(loc);
    if (url != null)
    {
      try
      {
        image = ImageIO.read(url);
      }
      catch (IOException e)
      {
        Logger.error("Exception opening " + loc + ": " + e.getMessage());
        image = null;
      }
    }
    else
    {
      Logger.error("Error: Unable to open the file " + loc);
      return null;
    }
    
    return image;
  }
  
  
  /**
   * Load the icon by name.
   * 
   * @param loc the name of the icon file
   * @return the image
   */
  public static ImageIcon loadIconByName(final String loc)
  {
    // The icon that gets returned
    ImageIcon icon = null;
    
    // Load the resource
    URL url = Utility.class.getResource(loc);
    if (url != null)
    {
      icon = new ImageIcon(url);
    }
    else
    {
      Logger.error("Error: Unable to open the file " + loc);
      return null;
    }
    
    return icon;
  }
  
  
  /**
   * Store the properties object to the filename.
   * 
   * @param filename name of the output file
   * @param props the properties to store
   */
  public static void storeProperties(final String filename,
                                     final Properties props)
  {
    // Write the properties to a file
    FileOutputStream outStream = null;
    try
    {
      // Open the output stream
      outStream = new FileOutputStream(filename);
      
      // Save the properties
      props.store(outStream, "Properties file");
      
      // Close the stream
      outStream.close();
      outStream = null;
    }
    catch (FileNotFoundException fnfe)
    {
      Logger.error("File not found: " + fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      Logger.error("IOException: " + ioe.getMessage());
    }
    finally
    {
      if (outStream != null)
      {
        try
        {
          outStream.close();
        }
        catch (IOException ioe)
        {
          Logger.error("IOException: " + ioe.getMessage());
        }
        
        outStream = null;
      }
    }
  }
  
  
  /**
   * Load the properties object.
   * 
   * @param filename the input file name
   * @return the loaded properties
   */
  public static Properties getProperties(final String filename)
  {
    // The object that gets returned
    Properties props = null;
    
    InputStream propStream = null;
    try
    {
      // Open the input stream as a file
      propStream = new FileInputStream(filename);
      
      // Check for an error
      if (propStream != null)
      {
        // Load the input stream
        props = new Properties();
        props.load(propStream);
        
        // Close the stream
        propStream.close();
        propStream = null;
      }
    }
    catch (IOException ioe)
    {
      props = null;
    }
    finally
    {
      // Make sure we close the stream
      if (propStream != null)
      {
        try
        {
          propStream.close();
        }
        catch (IOException e)
        {
          Logger.error(e.getMessage());
        }
        
        propStream = null;
      }
    }
    
    // Return the properties
    return props;
  }
  
  
  /**
   * Ensure that a particular table cell is visible.
   * 
   * @param table the table
   * @param rowIndex the row index
   * @param vColIndex the column index
   */
  public static void scrollToVisible(final JTable table,
                                     final int rowIndex,
                                     final int vColIndex)
  {
    // Check the parent
    if (!(table.getParent() instanceof JViewport))
    {
      return;
    }
    
    // Get the parent viewport
    JViewport viewport = (JViewport) table.getParent();
    
    // This rectangle is relative to the table where the
    // northwest corner of cell (0,0) is always (0,0).
    final Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
    
    // The location of the viewport relative to the table
    final Point pt = viewport.getViewPosition();
    
    // Translate the cell location so that it is relative
    // to the view, assuming the northwest corner of the
    // view is (0,0)
    rect.setLocation(rect.x - pt.x, rect.y - pt.y);
    
    // Scroll the area into view
    viewport.scrollRectToVisible(rect);
  }
  
  
  /**
   * Format the date as a string, using a standard format.
   * 
   * @param date the date to format
   * @return the date as a string
   */
  public static String getDateString(final Date date)
  {
    // Declare our formatter
    java.text.SimpleDateFormat formatter = new
    java.text.SimpleDateFormat("MMMM d, yyyy");
    
    // Return the date/time as a string
    return formatter.format(date);
  }
  
  
  /**
   * Format the date and time as a string, using a standard format.
   * 
   * @return the date as a string
   */
  public static String getDateTimeString()
  {
    // Declare our formatter
    java.text.SimpleDateFormat formatter = new
      java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
    // Return the date/time as a string
    return formatter.format(new Date());
  }
  
  
  /**
   * Convert a boolean array into a string.  Used for database
   * access and the listRoles and listPrimaries arrays.
   * 
   * @param data the boolean array
   * @return the array as a string
   */
  public static String listToString(final boolean[] data)
  {
    StringBuilder sb = new StringBuilder(data.length);
    for (int i = 0; i < data.length; ++i)
    {
      sb.append((data[i]) ? '1' : '0');
    }
    return sb.toString();
  }
  
  
  /**
   * Convert a string into an array of booleans.
   * 
   * @param str the input string
   * @param listData the output array
   */
  public static void stringToBooleanList(final String str,
                                         final boolean[] listData)
  {
    // Check the input for a null or empty string, or no 1's
    if ((str == null) || (str.length() < 1) || (str.indexOf('1') < 0))
    {
      // Fill the array with false
      java.util.Arrays.fill(listData, false);
      return;
    }
    
    // Iterate over the string
    final int len = str.length();
    for (int i = 0; i < 6; ++i)
    {
      // Check if we're before the end of the string
      if (i < len)
      {
        // We are, so convert the character into a boolean
        listData[i] = (str.charAt(i) == '1');
      }
      else
      {
        // We're past the end of the array, so assume false
        listData[i] = false;
      }
    }
  }
  
  
  /**
   * Parse a string of time (in minutes and seconds) and return
   * the time in seconds.  Acceptable input formats are SS, :SS
   * and MM:SS.
   * 
   * @param timeStr the input time string
   * @return the number of seconds in the time
   */
  public static int getMSTimeInSeconds(final String timeStr)
  {
    // Check the input
    if ((timeStr == null) || (timeStr.trim().length() < 1))
    {
      // The input string is invalid
      return 0;
    }
    
    // Trim the string
    final String time = timeStr.trim();
    
    // Check for a colon
    final int colonIndex = time.indexOf(':');
    if (colonIndex < 0)
    {
      // There is no colon, so just parse the string as the
      // number of seconds
      return getStringAsInteger(time, 0, 0);
    }
    else if (colonIndex == 0)
    {
      // There is a colon at the start, so just parse the rest
      // of the string as the number of seconds
      return getStringAsInteger(time.substring(1), 0, 0);
    }
    
    // There is a colon inside the string, so parse the
    // minutes (before) and seconds (after), and then add
    int mins = 60 * (getStringAsInteger(time.substring(0, colonIndex), 0, 0));
    int secs = getStringAsInteger(time.substring(colonIndex + 1), 0, 0);
    return (mins + secs);
  }
  
  
  /**
   * Parse a string of time (in hours and minutes) and return
   * the time in minutes.  Acceptable input formats are MM, :MM
   * and HH:MM.
   * 
   * @param timeStr the input time string
   * @return the number of minutes in the time
   */
  public static int getHMTimeInMinutes(final String timeStr)
  {
    // Check the input
    if ((timeStr == null) || (timeStr.trim().length() < 1))
    {
      // The input string is invalid
      return 0;
    }
    
    // Trim the string
    final String time = timeStr.trim();
    
    // Check for a colon
    final int colonIndex = time.indexOf(':');
    if (colonIndex < 0)
    {
      // There is no colon, so just parse the string as the
      // number of minutes
      return getStringAsInteger(time, 0, 0);
    }
    else if (colonIndex == 0)
    {
      // There is a colon at the start, so just parse the rest
      // of the string as the number of minutes
      return getStringAsInteger(time.substring(1).trim(), 0, 0);
    }
    
    // There is a colon inside the string, so parse the
    // hours (before) and minutes (after), and then add
    // together after multiplying the hours by 60 (to put
    // in minutes)
    int hrs = 60 * (getStringAsInteger(time.substring(0, colonIndex), 0, 0));
    int mins = getStringAsInteger(time.substring(colonIndex + 1), 0, 0);
    return (hrs + mins);
  }
  
  
  /**
   * Return the contents of a jar file and entry as a string.
   * 
   * @param file the input file
   * @param entry the jar entry
   * @return the contents of the file
   */
  public static String getFileAsText(final JarFile file,
                                      final JarEntry entry)
  {
    // Get the text of the file
    StringBuilder sb = new StringBuilder(1000);
    
    // Read the file
    BufferedReader in = null;
    try
    {
      InputStream input = file.getInputStream(entry);
      InputStreamReader isr = new InputStreamReader(input);
      in = new BufferedReader(isr);
      String str;
      while ((str = in.readLine()) != null)
      {
        sb.append(str).append('\n');
      }
      
      in.close();
      in = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        
        in = null;
      }
    }
    
    // Return the builder
    return sb.toString();
  }
  
  
  /**
   * Return the contents of a file as a string.
   * 
   * @param file the input file
   * @return the contents of the file
   */
  public static String getFileAsText(final File file)
  {
    // Check the input parameter
    if ((file == null) || (!file.exists()) || (file.isDirectory()))
    {
      return "";
    }
    
    // Get the text of the file
    StringBuilder sb = new StringBuilder(1000);
    
    // Read the file
    BufferedReader in = null;
    try
    {
      in = new BufferedReader(new FileReader(file));
      String str;
      while ((str = in.readLine()) != null)
      {
        sb.append(str).append('\n');
      }
      
      in.close();
      in = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        
        in = null;
      }
    }
    
    // Return the builder
    return sb.toString();
  }
  
  
  /**
   * Return the contents of a file as a hex dump.
   * 
   * @param file the input file
   * @return the contents of the file
   */
  public static String getFileAsHex(final File file)
  {
    // Check the input parameter
    if ((file == null) || (!file.exists()) || (file.isDirectory()))
    {
      return "";
    }
    
    // Save the size
    final int size = (int) file.length();
    
    // Get the text of the file
    StringBuilder sb = new StringBuilder(1000);
    
    // Read the input file
    FileInputStream fis = null;
    try
    {
      // Create a memory-map for the file
      fis = new FileInputStream(file);
      FileChannel fc = fis.getChannel();
      MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);
      
      // Declare two string builders, one for the middle section of
      // the output (hex), and one for the end section (chars)
      StringBuilder hexOutput = new StringBuilder(48);
      StringBuilder charOutput = new StringBuilder(16);
      
      // The offset for this row
      int offset = 0;
      
      // Iterate over the file, processing each character
      for (int index = 0; index < size;)
      {
        // Get the character
        byte b = mbb.get(index);
        
        // Build the middle section (each byte in hex)
        hexOutput.append(byteToHex(b)).append(' ');
        
        // Build the end section - the char, or a period
        char ch = (char) b;
        if ((ch < 32) || (ch > 126))
        {
          charOutput.append('.');
        }
        else
        {
          charOutput.append(ch);
        }
        
        // Increment the index and check if we've finished a row
        ++index;
        if (0 == (index % 16))
        {
          // Add the current row to the builder
          addHexRow(sb, offset, hexOutput, charOutput);
          
          // Reset the variables
          offset = index;
          hexOutput.setLength(0);
          charOutput.setLength(0);
        }
      }
      
      // Add the last row, if necessary
      if (hexOutput.length() > 0)
      {
        addHexRow(sb, offset, hexOutput, charOutput);
      }
      
      // Close the map
      fis.close();
      fis = null;
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      System.err.println("IOException: " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      if (fis != null)
      {
        try
        {
          fis.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        fis = null;
      }
    }
    
    // Return the builder
    return sb.toString();
  }
  
  
  /**
   * Convert a byte into a hex string.
   * 
   * @param b the byte
   * @return the byte as a hex string
   */
  private static String byteToHex(final byte b)
  {
    // Turn the byte into a two character hex string
    StringBuilder sb = new StringBuilder(2);
    sb.append(hexChar[(b & 0xF0) >>> 4]);
    sb.append(hexChar[b & 0x0F]);
    return sb.toString();
  }
  
  
  /**
   * Add a row to the output.
   * 
   * @param output the output string builder to append to
   * @param offset the offset for the row
   * @param hexOutput the middle section
   * @param charOutput the end section
   */
  private static void addHexRow(final StringBuilder output,
                                final int offset,
                                final StringBuilder hexOutput,
                                final StringBuilder charOutput)
  {
    // Build the output row
    output.append(getOffsetColumn(offset)).append("  ")
          .append(hexOutput.toString());
    
    // Check the length of hexOutput (the last row is usually
    // too short, if the file length is not a multiple of 16)
    if (hexOutput.length() < 48)
    {
      for (int i = hexOutput.length(); i < 48; ++i)
      {
        output.append(' ');
      }
    }
    
    // Add the final section
    output.append(' ').append(charOutput.toString()).append('\n');
  }
  
  
  /**
   * Build the string for the offset column.
   * 
   * @param offset the file offset
   * @return a string of the file offset
   */
  private static String getOffsetColumn(final int offset)
  {
    // This is the length of the output column
    final int outputLen = 10;
    
    // Check if we're already at the destination length
    String sOffset = Integer.toString(offset);
    if (sOffset.length() >= outputLen)
    {
      // We are, so return
      return sOffset;
    }
    
    // We need to prepend zeroes to make the output string long enough
    StringBuilder sb = new StringBuilder(outputLen);
    final int diff = outputLen - sOffset.length();
    for (int i = 0; i < diff; ++i)
    {
      sb.append('0');
    }
    
    // Now add the offset value to the end
    sb.append(sOffset);
    
    // Return the string
    return sb.toString();
  }
  
  
  /**
   * Copy a string to the clipboard.
   * 
   * @param str the string to copy to the clipboard
   */
  public static void saveToClipboard(final String str)
  {
    // Check the parameter
    if (str == null)
    {
      return;
    }
    
    // Save the value to the clipboard
    StringSelection ss = new StringSelection(str);
    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
        .setContents(ss, null);
  }
  
  
  /**
   * Set the current file directory.
   * 
   * @param parent the new file directory
   */
  public static void setCurrentDir(final File parent)
  {
    currentDir = parent;
  }
  
  
  /**
   * Return the current file directory.
   * 
   * @return the current file directory
   */
  public static File getCurrentDir()
  {
    return currentDir;
  }
}

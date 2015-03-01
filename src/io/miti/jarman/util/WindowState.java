package io.miti.jarman.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Properties;

import io.miti.jarman.data.FileList;

/**
 * Store information about the main window.
 */
public final class WindowState
{
  /**
   * The one instance of this class.
   */
  private static final WindowState ws = new WindowState();
  
  /**
   * X coordinate of top-left corner.
   */
  private int x = 0;
  
  /**
   * Y coordinate of top-left corner.
   */
  private int y = 0;
  
  /**
   * Window height.
   */
  private int height = 0;
  
  /**
   * Window width.
   */
  private int width = 0;
  
  /**
   * The log level.  Zero = no logging.  Possible values are 0-5.
   */
  // private int logLevel = 0;
  
  /**
   * The name of the log output file.  The names "stdout" and
   * "stderr" are not treated as file names.
   */
  // private String logFile = "stderr";
  
  /**
   * Whether to overwrite the log file, or append.
   */
  // private boolean logOverwrite = true;
  
  
  /**
   * Default constructor.
   */
  private WindowState()
  {
    super();
  }
  
  
  /**
   * Get the current window state object.
   * 
   * @return the window state object
   */
  public static WindowState getInstance()
  {
    return ws;
  }
  
  
  /**
   * Update the member data with the rectangle size and position,
   * and the location of the divider bar.
   * 
   * @param rect the bounding rectangle
   */
  public void update(final Rectangle rect)
  {
    x = rect.x;
    y = rect.y;
    width = rect.width;
    height = rect.height;
  }
  
  
  /**
   * Return the height.
   * 
   * @return the height
   */
  public int getHeight()
  {
    if (height <= 0)
    {
      return 600;
    }
    
    return height;
  }
  
  
  /**
   * Set the height.
   * 
   * @param heightValue the height to set
   */
  public void setHeight(final int heightValue)
  {
    height = heightValue;
  }
  
  
  /**
   * Return the width.
   * 
   * @return the width
   */
  public int getWidth()
  {
    if (width <= 0)
    {
      return 800;
    }
    
    return width;
  }
  
  
  /**
   * Set the width.
   * 
   * @param widthValue the width to set
   */
  public void setWidth(final int widthValue)
  {
    width = widthValue;
  }
  
  
  /**
   * Return the x coordinate of the top left corner.
   * 
   * @return the x coordinate
   */
  public int getX()
  {
    return x;
  }
  
  
  /**
   * Set the x coordinate of the top left corner.
   * 
   * @param xValue the x coordinate
   */
  public void setX(final int xValue)
  {
    x = xValue;
  }
  
  
  /**
   * Return the y coordinate of the top left corner.
   * 
   * @return the y coordinate
   */
  public int getY()
  {
    return y;
  }
  
  
  /**
   * Set the y coordinate of the top left corner.
   * 
   * @param yValue the y coordinate
   */
  public void setY(final int yValue)
  {
    y = yValue;
  }
  
  
  /**
   * Return the height and width as a dimension.
   * 
   * @return the height and width as a dimension
   */
  public Dimension getSize()
  {
    return new Dimension(getWidth(), getHeight());
  }
  
  
  /**
   * Whether the window should be centered.  This
   * returns true if any value in the state is zero
   * or negative.
   * 
   * @return whether the window should be centered
   */
  public boolean shouldCenter()
  {
    return ((x <= 0) || (y <= 0) || (width <= 0) || (height <= 0));
  }
  
  
  /**
   * Get the position of the top-left point.
   * 
   * @return the position
   */
  public Point getPosition()
  {
    return new Point(x, y);
  }
  
  
  /**
   * Return this object as a string.
   * 
   * @return this object as a string
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    // Declare the string builder
    StringBuilder sb = new StringBuilder(200);
    
    // Build the string
    sb.append("X: ").append(x).append("  Y: ").append(y)
      .append("  Height: ").append(height).append("  Width: ")
      .append(width);
    
    // Return the string
    return sb.toString();
  }
  
  
  /**
   * Save this object to a file.
   * 
   * @param filename the name of the output file
   */
  public void saveToFile(final String filename)
  {
    // This will hold the data
    Properties prop = new Properties();
    
    // Save the data
    prop.put("window.pos.x", Integer.toString(x));
    prop.put("window.pos.y", Integer.toString(y));
    prop.put("window.size.width", Integer.toString(width));
    prop.put("window.size.height", Integer.toString(height));
    // prop.put("log.level", Integer.toString(logLevel));
    // prop.put("log.file", logFile);
    // prop.put("log.overwrite", logOverwrite ? "1" : "0");
    FileList.getInstance().saveData(prop);
    
    // Save the properties to a file
    Utility.storeProperties(filename, prop);
  }
  
  
  /**
   * Load the window state.
   * 
   * @param filename the input filename
   */
  public static void load(final String filename)
  {
    // Get the properties object
    Properties props = Utility.getProperties(filename);
    
    // Check the properties
    if (props == null)
    {
      return;
    }
    
    // Save the values
    ws.x = parseInteger(props, "window.pos.x");
    ws.y = parseInteger(props, "window.pos.y");
    ws.width = parseInteger(props, "window.size.width");
    ws.height = parseInteger(props, "window.size.height");
    
    // Get data about files to load
    FileList.getInstance().loadData(props);
    
    // Get the logging preferences.  Start with the log level.
//    String val = props.getProperty("log.level");
//    if ((val != null) && (val.length() > 0))
//    {
//      ws.logLevel = Utility.getStringAsInteger(val, 0, 0);
//    }
    
    // Read the name of the log output file, stdout or stderr
//    val = props.getProperty("log.file");
//    if ((val != null) && (val.length() > 0))
//    {
//      ws.logFile = val.trim();
//    }
    
    // Read whether to overwrite the log file at startup
//    val = props.getProperty("log.overwrite");
//    if ((val != null) && (val.length() > 0))
//    {
//      ws.logOverwrite = (val.equals("1"));
//    }
    
    // Instantiate a logging class
//    Logger.initialize(ws.logLevel, ws.logFile, ws.logOverwrite);
    
    // Get the size of the screen
    Dimension screenDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    // Check if the x or y go off-screen in the current resolution
    if (ws.x >= screenDim.width)
    {
      ws.x = 100;
    }
    if (ws.y >= screenDim.height)
    {
      ws.y = 100;
    }
  }
  
  
  /**
   * Cast the string to an integer and return it.
   * 
   * @param props the properties object
   * @param key the key
   * @return the number
   */
  public static int parseInteger(final Properties props, final String key)
  {
    // Get the string
    String val = (String) (props.get(key));
    
    // Check for null
    if (key == null)
    {
      return 0;
    }
    
    // Cast the string to an integer
    int num = 0;
    try
    {
      num = Integer.parseInt(val);
    }
    catch (NumberFormatException nfe)
    {
      num = 0;
    }
    
    // Check for reasonable value
    num = Math.max(Math.min(num, 2000), 0);
    
    // Return the value
    return num;
  }
}

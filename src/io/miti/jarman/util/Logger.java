package io.miti.jarman.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Encapsulate the logging functionality.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Logger
{
  /**
   * The log level.
   */
  private int logLevel = 0;
  
  /**
   * The name of the log file.
   */
  private String logFile = "stdout";
  
  /**
   * Whether to overwrite the log file, or append.
   */
  private boolean logOverwrite = true;
  
  /**
   * Whether we've written any output yet.
   */
  private boolean dataWritten = false;
  
  /**
   * The output mode (-1=no output, 0=stdout, 1=stderr, 2=file).
   */
  private int logFileMode = 0;
  
  /**
   * The one instance of this class.
   */
  private static final Logger logger = new Logger();
  
  /**
   * Default constructor.
   */
  private Logger()
  {
    super();
  }
  
  
  /**
   * Initialize the logging mechanism.  The log level must be
   * between 0 and 5.  Zero turns off logging, and 1-5 are more
   * progressive logging levels.  1=info, 2=debug, 3=warn,
   * 4=error and 5=fatal.  The logFile is the name of the
   * output file.  File names of stdout and stderr redirect
   * output to the standard output stream and the standard
   * error stream, respectively.  Log overwrite will overwrite
   * the log file at startup if true; otherwise, it will append.
   * 
   * @param nLogLevel the requested log level
   * @param sLogFile the log file name
   * @param bLogOverwrite whether to overwrite the contents of the file
   */
  public static void initialize(final int nLogLevel,
                                final String sLogFile,
                                final boolean bLogOverwrite)
  {
    // Bound the log level to 0-5
    logger.logLevel = Math.max(0, Math.min(5, nLogLevel));
    logger.logFile = sLogFile;
    logger.logOverwrite = bLogOverwrite;
    
    // Determine the validity of the filename
    boolean bValidFileName = checkFileName(sLogFile);
    
    // Set the mode, based on the filename
    if (!bValidFileName)
    {
      // Don't write out anything
      logger.logFileMode = -1;
    }
    else if (sLogFile.equals("stdout"))
    {
      // Send messages to stdout
      logger.logFileMode = 0;
    }
    else if (sLogFile.equals("stderr"))
    {
      // Send messages to stderr
      logger.logFileMode = 1;
    }
    else
    {
      // Send messages to the specified filename
      logger.logFileMode = 2;
    }
  }
  
  
  /**
   * Return whether this is a valid file name.
   * 
   * @param name the file name to check
   * @return if the name is valid for a file
   */
  private static boolean checkFileName(final String name)
  {
    // Check the variable
    if ((name == null) || (name.length() < 1))
    {
      return false;
    }
    
    // Create a File variable
    File file = new File(name);
    if (file.exists())
    {
      // Something exists with this name; return whether this is a file
      return (file.isFile());
    }
    
    // The file doesn't exist, so we can create a file with its name
    return true;
  }
  
  
  /**
   * Write a message to the log, if we're in info level (1) or higher.
   * 
   * @param msg the output string
   */
  public static void info(final String msg)
  {
    writeMsg(msg, 1);
  }
  
  
  /**
   * Write an exception to the log, if we're in info level (1) or higher.
   * 
   * @param ex the exception
   */
  public static void info(final Exception ex)
  {
    writeMsg(ex, 1);
  }
  
  
  /**
   * Write a message to the log, if we're in debug level (2) or higher.
   * 
   * @param msg the output string
   */
  public static void debug(final String msg)
  {
    writeMsg(msg, 2);
  }
  
  
  /**
   * Write an exception to the log, if we're in debug level (2) or higher.
   * 
   * @param ex the exception
   */
  public static void debug(final Exception ex)
  {
    writeMsg(ex, 2);
  }
  
  
  /**
   * Write a message to the log, if we're in warn level (3) or higher.
   * 
   * @param msg the output string
   */
  public static void warn(final String msg)
  {
    writeMsg(msg, 3);
  }
  
  
  /**
   * Write an exception to the log, if we're in warn level (3) or higher.
   * 
   * @param ex the exception
   */
  public static void warn(final Exception ex)
  {
    writeMsg(ex, 3);
  }
  
  
  /**
   * Write a message to the log, if we're in error level (4) or higher.
   * 
   * @param msg the output string
   */
  public static void error(final String msg)
  {
    writeMsg(msg, 4);
  }
  
  
  /**
   * Write an exception to the log, if we're in error level (4) or higher.
   * 
   * @param ex the exception
   */
  public static void error(final Exception ex)
  {
    writeMsg(ex, 4);
  }
  
  
  /**
   * Write a message to the log, if we're in fatal level (5).
   * 
   * @param msg the output string
   */
  public static void fatal(final String msg)
  {
    writeMsg(msg, 5);
  }
  
  
  /**
   * Write an exception to the log, if we're in fatal level (5) or higher.
   * 
   * @param ex the exception
   */
  public static void fatal(final Exception ex)
  {
    writeMsg(ex, 5);
  }
  
  
  /**
   * Write out the specified exception.
   * 
   * @param ex the exception to write
   * @param levelBound the requested level to write out the exception
   */
  private static void writeMsg(final Exception ex,
                               final int levelBound)
  {
    // Check if any messages should be written
    if ((logger.logLevel < 1) || (logger.logFileMode < 0) ||
        (logger.logLevel > levelBound) || (ex == null))
    {
      // Don't write out the message; just return
      return;
    }
    
    // This will hold the output string
    StringBuilder sb = new StringBuilder(200);
    
    // Build the string, starting with the class name and the message
    sb.append(ex.getClass().getName()).append(": ")
      .append(ex.getMessage());
    
    // Add the stack trace elements
    StackTraceElement[] elements = ex.getStackTrace();
    if (elements.length > 0)
    {
      // Add the first element
      sb.append(Utility.getLineSeparator()).append("  ").append(elements[0]);
      
      // Don't write out more than the first few elements of the stack trace
      int lastElement = Math.min(elements.length, 10);
      for (int i = 1; i < lastElement; ++i)
      {
        sb.append(Utility.getLineSeparator()).append("    ").append(elements[i]);
      }
    }
    
    // Write out the output string
    writeMsg(sb.toString(), levelBound);
  }
  
  
  /**
   * Write out the specified message.
   * 
   * @param msg the message to write
   * @param levelBound the requested level to write out the message
   */
  private static void writeMsg(final String msg,
                               final int levelBound)
  {
    // Check if any messages should be written
    if ((logger.logLevel < 1) || (logger.logFileMode < 0) ||
        (logger.logLevel > levelBound))
    {
      // Don't write out the message; just return
      return;
    }
    
    // Prepend the date/time
    String s = Utility.getDateTimeString() + ": " + msg;
    
    // Write out the string, based on the file mode
    if (logger.logFileMode == 0)
    {
      // Send to standard output
      System.out.println(s);
    }
    else if (logger.logFileMode == 1)
    {
      // Send to standard error
      System.err.println(s);
    }
    else if (logger.logFileMode == 2)
    {
      // Determine whether to append to the ouput file.  We only
      // overwrite if logOverwrite is true and no data has been
      // written yet to the output stream (for this instance).
      // So, we append if the negation of that conditional is true.
      boolean bAppend = ((!logger.logOverwrite) || (logger.dataWritten));
      
      // Open the output file for writing
      BufferedWriter out = null;
      try
      {
        // Open it for appending/writing (the second argument to BufferedWriter
        // tells it whether to open for appending)
        out = new BufferedWriter(new FileWriter(logger.logFile, bAppend));
        out.write(s);
        out.write(Utility.getLineSeparator());
        out.close();
        out = null;
      }
      catch (IOException e)
      {
        // Write out the original error message
        System.err.println(s);
        
        // Write out the new exception
        System.err.println(e.getMessage());
        
        // Change the log file mode
        logger.logFileMode = 1;
      }
      finally
      {
        // Make sure we close the writer
        if (out != null)
        {
          try
          {
            out.close();
            out = null;
          }
          catch (IOException e)
          {
            System.err.println(e.getMessage());
          }
        }
      }
    }
    
    // We've written data to the output file, so no need to overwrite
    // the output stream with future data (everything else gets appended)
    logger.dataWritten = true;
  }
}

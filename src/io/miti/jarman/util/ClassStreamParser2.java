package io.miti.jarman.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parse a .class file and return the list of referenced Java classes.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ClassStreamParser2
{
  /**
   * Whether the class file is Java 6 or later.
   */
  private boolean isJava6 = false;
  
  /**
   * The loop index while reading the constant pool data.
   */
  private int index = 0;
  
  /**
   * List of all strings in the constant pool.
   */
  private Map<Integer, String> strings = new HashMap<Integer, String>();
  
  /**
   * The set of indexes into strings holding class names.
   */
  private Set<Integer> classes = new HashSet<Integer>();
  
  /**
   * The set of indexes into strings holding variable types.
   */
  private Set<Integer> vtypes = new HashSet<Integer>();
  
  /**
   * Define the set of characters allowed just before a 'L'.
   */
  private static final Set<Character> pres = new HashSet<Character>(20);
  
  /**
   * Fill in pres.
   */
  static
  {
    pres.add('(');
    pres.add(')');
    pres.add('[');
    pres.add(';');
    pres.add('B');
    pres.add('C');
    pres.add('D');
    pres.add('F');
    pres.add('I');
    pres.add('J');
    pres.add('S');
    pres.add('Z');
  }
  
  
  /**
   * Default constructor.
   */
  public ClassStreamParser2()
  {
    super();
  }
  
  
  /**
   * Parse the stream.
   * 
   * @param stream the input data stream
   * @param names the array to fill with class names
   * @throws IOException an error occurred while reading
   */
  public void parseStream(final DataInputStream stream,
                           final List<String> names)
    throws IOException
  {
    // Verify this is a valid class file (starts with 0xCAFEBABE)
    if (readHeader(stream))
    {
      final int poolSize = readU2(stream);
      for (index = 1; index < poolSize; index++)
      {
        savePoolEntryIfIsClassReference(stream);
      }
      
      // Save the strings to our output array
      for (Integer i : classes)
      {
        final String name = strings.get(i.intValue());
        cleanClassName(name, names);
      }
      
      // Parse variable types for Java class names (for Java 5 or earlier classes)
      parseVariableTypes(names);
    }
  }
  
  
  /**
   * Clean up class names.
   * 
   * @param name the raw class name
   * @param names the array to fill with class names
   */
  private void cleanClassName(final String name,
                               final List<String> names)
  {
    if ((name == null) || (name.length() < 1))
    {
      names.add(name);
      return;
    }
    
    // Check for a string between [L and ; - that would be a class name
    final int len = name.length();
    int i = name.lastIndexOf('[');
    if (i < 0)
    {
      // No class reference so just add the string and return
      names.add(name);
      return;
    }
    else
    {
      if (i == (len - 1))
      {
        // The string ends with a [.  This should never happen.
        return;
      }
      else
      {
        // Verify the next character is an L
        final char next = name.charAt(i + 1);
        if (next != 'L')
        {
          // It's some other type (intrinsic)
          return;
        }
        else
        {
          // Get the closing semi-colon
          final int ei = name.indexOf(';', i + 2);
          if (ei < 0)
          {
            // No semi-colon.  This should never happen.
            return;
          }
          else
          {
            // Save the string between the [L and the ; tokens
            names.add(name.substring(i + 2, ei));
          }
        }
      }
    }
  }
  
  
  /**
   * If this is a Java 5 class or earlier, parse variable types.
   * 
   * @param names the array to fill with class names
   */
  private void parseVariableTypes(final List<String> names)
  {
    // Check if the list is empty (normally this happens with Java 6 classes)
    if (vtypes.isEmpty())
    {
      return;
    }
    
    // Parse each string and add classes to the names set
    for (Integer p : vtypes)
    {
      String item = strings.get(p.intValue());
      parseClassType(item, names);
    }
  }
  
  
  /**
   * Parse a class type.  We look for strings starting with L and ending
   * with a semi-colon.
   * 
   * @param item the string to parse for one or more class names
   * @param names the array to fill with class names
   */
  private void parseClassType(final String item, final List<String> names)
  {
    // Check for any strings
    if ((item == null) || (item.length() < 1) || (item.indexOf('L') < 0))
    {
      // Nothing to do here
      return;
    }
    
    // Iterate over the list to find class names
    int i = item.indexOf('L');
    while (i >= 0)
    {
      // Check for known cases
      int j = item.indexOf(';', i + 1);
      if (j < 0)
      {
        break;
      }
      
      if (i == 0)
      {
        // This is the start of the string, so add everything after the L and before the ;
        String name = item.substring(1, j);
        names.add(name);
      }
      else
      {
        // Check if the previous character is valid
        final char pre = item.charAt(i - 1);
        if (pres.contains(Character.valueOf(pre)))
        {
          String name = item.substring(i + 1, j);
          names.add(name);
        }
        else
        {
          System.out.println(">> Found a L just after a " + Character.toString(pre));
          System.out.println(item);
        }
      }
      
      i = item.indexOf('L', j + 1);
    }
  }
  
  
  /**
   * Read the header and return whether this is a valid class file.
   * 
   * @param stream the input stream
   * @return whether this is a valid class file
   * @throws IOException error during reading
   */
  private boolean readHeader(final DataInputStream stream) throws IOException
  {
    int result = readU4(stream); // magic byte
    if (result != 0xCAFEBABE)
    {
      return false;
    }
    
    // Read the rest of the header (before the constant pool)
    readU2(stream); // minor version
    int majorVer = readU2(stream); // major version
    isJava6 = (majorVer >= 50);
    
    return true;
  }
  
  
  /**
   * Return whether this class file is marked for Java 6 or later.
   * 
   * @return whether this class file is marked for Java 6 or later
   */
  public boolean isJavaVer6orLater()
  {
    return isJava6;
  }
  
  
  /**
   * Check the pool entry and save the class names.
   * 
   * @param stream the input data stream
   * @throws IOException an error occurred during reading
   */
  private void savePoolEntryIfIsClassReference(final DataInputStream stream)
    throws IOException
  {
    final int tag = readU1(stream);
    switch (tag)
    {
      case 1: // Utf8
        saveStringFromUtf8Entry(stream);
        break;
        
      case 7: // Class
        saveClassEntry(stream);
        break;
        
      case 8: // String
        readU2(stream);
        break;
        
      case 3: // Integer
      case 4: // Float
        readU4(stream);
        break;
        
      case 5: // Long
      case 6: // Double
        readU4(stream);
        readU4(stream);
        index++;
        break;
        
      case 9: // Fieldref
      case 10: // Methodref
      case 11: // InterfaceMethodref
        readU2(stream);
        readU2(stream);
        break;
      
      case 12: // NameAndType
        processNAT(stream);
        break;
        
      default:
        break;
    }
  }
  
  
  /**
   * Process name and type entries from the input stream.
   * 
   * @param stream the input data stream
   * @throws IOException error during reading
   */
  private void processNAT(final DataInputStream stream)
    throws IOException
  {
    // Skip the index to the variable name
    readU2(stream);
    
    // Get the index to the variable type, and save if we're in Java 5 or
    // earlier (the format changed with Java 6)
    int val2 = readU2(stream);
    vtypes.add(val2);
  }
  
  
  /**
   * Read a string from the stream.
   * 
   * @param stream the input data stream
   * @throws IOException error during reading
   */
  private void saveStringFromUtf8Entry(final DataInputStream stream)
    throws IOException
  {
    String content = readString(stream);
    strings.put(index, content);
  }
  
  
  /**
   * Save a string as a class name.
   * 
   * @param stream the input data stream
   * @throws IOException error during reading
   */
  private void saveClassEntry(final DataInputStream stream) throws IOException
  {
    int nameIndex = readU2(stream);
    classes.add(nameIndex);
  }
  
  
  /**
   * Read a string.
   * 
   * @param stream the input data stream
   * @return the read data
   * @throws IOException error during reading
   */
  private String readString(final DataInputStream stream) throws IOException
  {
    String name = null;
    try
    {
      name = stream.readUTF();
    }
    catch (java.io.EOFException e)
    {
      logError("EOFException in readString: ", e);
    }
    catch (java.io.UTFDataFormatException e)
    {
      logError("UTFDataFormatException in readString: ", e);
    }
    
    return name;
  }
  
  
  /**
   * Read an unsigned byte.
   * 
   * @param stream the input data stream
   * @return the read data
   * @throws IOException error during reading
   */
  private int readU1(final DataInputStream stream) throws IOException
  {
    int result = 0;
    try
    {
      result = stream.readUnsignedByte();
    }
    catch (java.io.EOFException e)
    {
      logError("EOFException in readU1: ", e);
    }
    catch (java.io.UTFDataFormatException e)
    {
      logError("UTFDataFormatException in readU1: ", e);
    }
    
    return result;
  }
  
  
  /**
   * Read an unsigned short.
   * 
   * @param stream the input data stream
   * @return the read data
   * @throws IOException error during reading
   */
  private int readU2(final DataInputStream stream) throws IOException
  {
    return stream.readUnsignedShort();
  }
  
  
  /**
   * Read an integer.
   * 
   * @param stream the input data stream
   * @return the read data
   * @throws IOException error during reading
   */
  private int readU4(final DataInputStream stream) throws IOException
  {
    return stream.readInt();
  }
  
  
  /**
   * Log an exception.
   * 
   * @param msg the text message
   * @param e the exception
   */
  private void logError(final String msg, final Exception e)
  {
    System.err.println(msg + e.getMessage());
  }
}

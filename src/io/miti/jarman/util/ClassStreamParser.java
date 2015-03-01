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
public final class ClassStreamParser
{
  /**
   * For correcting indexes to constant pools with long and double entries.
   */
  private int indexCorrection = 0;
  
  /**
   * List of all strings in the constant pool.
   */
  private Map<Integer, String> strings = new HashMap<Integer, String>();
  
  /**
   * The set of indexes into strings holding class names.
   */
  private Set<Integer> classes = new HashSet<Integer>();
  
  
  /**
   * Default constructor.
   */
  public ClassStreamParser()
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
      for (int n = 1; n < poolSize; n++)
      {
        savePoolEntryIfIsClassReference(n, stream);
      }
      
      // Save the strings to our output array
      for (Integer index : classes)
      {
        names.add(strings.get(index));
      }
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
    readU2(stream); // major version
    
    return true;
  }
  
  
  /**
   * Check the pool entry and save the class names.
   * 
   * @param index the pool entry index
   * @param stream the input data stream
   * @throws IOException an error occurred during reading
   */
  private void savePoolEntryIfIsClassReference(final int index,
                                                final DataInputStream stream)
    throws IOException
  {
    final int tag = readU1(stream);
    switch (tag)
    {
      case 1: // Utf8
        saveStringFromUtf8Entry(index, stream);
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
        indexCorrection++;
        break;
        
      case 9: // Fieldref
      case 10: // Methodref
      case 11: // InterfaceMethodref
      case 12: // NameAndType
        readU2(stream);
        readU2(stream);
        break;
        
      default:
        break;
    }
  }
  
  
  /**
   * Read a string from the stream.
   * 
   * @param index the index to store the string in
   * @param stream the input data stream
   * @throws IOException error during reading
   */
  private void saveStringFromUtf8Entry(final int index, final DataInputStream stream)
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
    classes.add(nameIndex - indexCorrection);
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

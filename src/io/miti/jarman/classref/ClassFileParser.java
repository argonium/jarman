package io.miti.jarman.classref;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import io.miti.jarman.classref.ClassInfo;
import io.miti.jarman.classref.FieldMethodInfo;

/**
 * Parse a .class file and return the class info.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ClassFileParser
{
  /**
   * The class file object that gets populated during parsing.
   */
  private ClassInfo ci = null;
  
  /**
   * The index into the constant pool.
   */
  private int cpIndex = 0;
  
  
  /**
   * Default constructor.
   */
  public ClassFileParser()
  {
    super();
  }
  
  
  /**
   * Parse a compiled Java class file and return a ClassInfo object
   * populated with data about the class.
   * 
   * @param file the input file
   * @return data about the java class
   */
  public ClassInfo parseClassFile(final File file)
  {
    // Check the input
    if (file == null)
    {
      return null;
    }
    else if (file.isDirectory())
    {
      return null;
    }
    
    // Verify the name ends with .class.
    final String name = file.getAbsolutePath();
    if (name == null)
    {
      return null;
    }
    else if (!name.endsWith(".class"))
    {
      return null;
    }
    
    // Parse the file
    ci = new ClassInfo();
    try
    {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      DataInputStream stream = new DataInputStream(bis);
      parseStream(stream);
      
      stream.close();
      bis.close();
      stream = null;
      bis = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return ci;
  }
  
  
  /**
   * Parse a jar file entry of a class file and return a ClassInfo
   * object populated with data about the class.
   * 
   * @param file the jar file
   * @param entry the jar file entry
   * @return data about the java class
   */
  public ClassInfo parseClassFile(final JarFile file, final JarEntry entry)
  {
    // Check the input
    if (entry == null)
    {
      return null;
    }
    else if (entry.isDirectory())
    {
      return null;
    }
    
    // Verify the name ends with .class.
    String name = entry.getName();
    if (name == null)
    {
      return null;
    }
    else if (!name.endsWith(".class"))
    {
      return null;
    }
    
    // Parse the file
    ci = new ClassInfo();
    try
    {
      BufferedInputStream bis = new BufferedInputStream(file.getInputStream(entry));
      DataInputStream stream = new DataInputStream(bis);
      parseStream(stream);
      
      stream.close();
      bis.close();
      stream = null;
      bis = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return ci;
  }
  
  
  /**
   * Parse the stream and populate a ClassInfo object.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void parseStream(final DataInputStream stream) throws IOException
  {
    // Verify this is a valid class file (starts with 0xCAFEBABE)
    if (readHeader(stream))
    {
      // Read the constant pool
      final int poolSize = readU2(stream);
      for (cpIndex = 1; cpIndex < poolSize; cpIndex++)
      {
        getConstantPoolEntry(stream);
      }
      
      // Get the access flags
      int af = readU2(stream);
      ci.setAccessFlags(af);
      
      // Get the name of the class
      int nameIndex = readU2(stream);
      ci.setClassName(nameIndex);
      
      // Get the class's parent's name
      int parentIndex = readU2(stream);
      ci.setParentName(parentIndex);
      
      // Save the interface names
      final int interCount = readU2(stream);
      for (int index = 0; index < interCount; ++index)
      {
        int ifIndex = readU2(stream);
        ci.addInterface(ifIndex);
      }
      
      // Read the fields
      readClassFields(stream);
      
      // Read the methods
      readClassMethods(stream);
      
      // Read the attributes
      readClassAttributes(stream);
    }
  }
  
  
  /**
   * Read the fields in the .class file.
   * 
   * @param stream the input data stream
   * @throws IOException an error occurred during reading
   */
  private void readClassFields(final DataInputStream stream)
    throws IOException
  {
    final int fieCount = readU2(stream);
    for (int i = 0; i < fieCount; ++i)
    {
      final int accFields = readU2(stream);
      final int nameIndex = readU2(stream);
      final int descIndex = readU2(stream);
      ci.addClassField(new FieldMethodInfo(accFields, nameIndex, descIndex));
      readClassAttributes(stream);
    }
  }
  
  
  /**
   * Read the methods in the .class file.
   * 
   * @param stream the input data stream
   * @throws IOException an error occurred during reading
   */
  private void readClassMethods(final DataInputStream stream)
    throws IOException
  {
    final int metCount = readU2(stream);
    for (int i = 0; i < metCount; ++i)
    {
      final int accFields = readU2(stream);
      final int nameIndex = readU2(stream);
      final int descIndex = readU2(stream);
      ci.addClassMethod(new FieldMethodInfo(accFields, nameIndex, descIndex));
      readClassAttributes(stream);
    }
  }
  
  
  /**
   * Read the attributes in the .class file.
   * 
   * @param stream the input data stream
   * @throws IOException an error occurred during reading
   */
  private void readClassAttributes(final DataInputStream stream)
    throws IOException
  {
    final int attCount = readU2(stream);
    for (int i = 0; i < attCount; ++i)
    {
      readU2(stream); // attribute name index
      final int len = readU4(stream);
      for (int j = 0; j < len; ++j)
      {
        readU1(stream); // read the info attribute
      }
    }
  }
  
  
  /**
   * Check the pool entry and save the class names.
   * 
   * @param stream the input data stream
   * @throws IOException an error occurred during reading
   */
  private void getConstantPoolEntry(final DataInputStream stream)
    throws IOException
  {
    // Get the field tag type and save the data
    final int tag = readU1(stream);
    switch (tag)
    {
      case 1: // Utf8
        saveString(stream);
        break;
        
      case 3: // Integer
        saveInt(stream);
        break;
        
      case 4: // Float
        saveFloat(stream);
        break;
        
      case 5: // Long
        saveLong(stream);
        break;
        
      case 6: // Double
        saveDouble(stream);
        break;
        
      case 7: // Class
        saveClassEntry(stream);
        break;
        
      case 8: // String
        saveStringRef(stream);
        break;
        
      case 9: // Fieldref
        saveFieldRef(stream);
        break;
        
      case 10: // Methodref
        saveMethodRef(stream);
        break;
        
      case 11: // InterfaceMethodref
        saveInterfMethodRef(stream);
        break;
        
      case 12: // NameAndType
        saveNameAndTypeRefs(stream);
        break;
        
      default:
        break;
    }
  }
  
  
  /**
   * Save a name and type description reference.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveNameAndTypeRefs(final DataInputStream stream) throws IOException
  {
    final int nameRef = readU2(stream);
    final int typeRef = readU2(stream);
    ci.addNATRef(cpIndex, nameRef, typeRef);
  }
  
  
  /**
   * Get a reference in the constant pool to a class name.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveClassEntry(final DataInputStream stream) throws IOException
  {
    final int nameIndex = readU2(stream);
    ci.addCpClassIndex(cpIndex, nameIndex);
  }
  
  
  /**
   * Read a string reference.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveStringRef(final DataInputStream stream) throws IOException
  {
    final int val = readU2(stream);
    ci.addStringRef(val);
  }
  
  
  /**
   * Read a field reference.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveFieldRef(final DataInputStream stream) throws IOException
  {
    final int classRef = readU2(stream);
    final int natRef = readU2(stream);
    ci.addFieldRef(classRef, natRef);
  }
  
  
  /**
   * Read a method reference.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveMethodRef(final DataInputStream stream) throws IOException
  {
    final int classRef = readU2(stream);
    final int natRef = readU2(stream);
    ci.addMethodRef(classRef, natRef);
  }
  
  
  /**
   * Read an interface method reference.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveInterfMethodRef(final DataInputStream stream) throws IOException
  {
    final int classRef = readU2(stream);
    final int natRef = readU2(stream);
    ci.addInterfMethodRef(classRef, natRef);
  }
  
  
  /**
   * Read an integer.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveInt(final DataInputStream stream) throws IOException
  {
    final int val = readU4(stream);
    ci.addCPInt(val);
  }
  
  
  /**
   * Read an integer and turn it into a float.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveFloat(final DataInputStream stream) throws IOException
  {
    final int nVal = readU4(stream);
    final float fVal = Float.intBitsToFloat(nVal);
    ci.addCPFloat(fVal);
  }
  
  
  /**
   * Read a long.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveLong(final DataInputStream stream) throws IOException
  {
    // Turn two ints into a long
    int p1 = readU4(stream);
    long val = ((long) p1) << 32;
    int p2 = readU4(stream);
    val += (p2 & 0xFFFFFFFFL);
    ci.addCPLong(val);
    cpIndex++;
  }
  
  
  /**
   * Read a double.
   * 
   * @param stream the input stream
   * @throws IOException error during reading
   */
  private void saveDouble(final DataInputStream stream) throws IOException
  {
    // Turn two ints into a long
    int p1 = readU4(stream);
    long val = ((long) p1) << 32;
    int p2 = readU4(stream);
    val += (p2 & 0xFFFFFFFFL);
    
    // Now turn the long into a double and save it
    double dVal = Double.longBitsToDouble(val);
    ci.addCPDouble(dVal);
    cpIndex++;
  }
  
  
  /**
   * Read the string name from the input stream and save it and
   * its index.
   * 
   * @param stream the input stream
   * @throws IOException an error while reading
   */
  private void saveString(final DataInputStream stream) throws IOException
  {
    // Read the string and save it and the current index
    String name = stream.readUTF();
    ci.addCPString(name, cpIndex);
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
    // Read the magic number and verify it has the right value
    final int result = readU4(stream);
    if (result != 0xCAFEBABE)
    {
      ci.setValid(false);
      return false;
    }
    
    // This is a valid stream
    ci.setValid(true);
    
    // Read the rest of the header (before the constant pool)
    int mi = readU2(stream); // minor version
    ci.setMinorVersion(mi);
    int ma = readU2(stream); // major version
    ci.setMajorVersion(ma);
    
    return true;
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
    return stream.readUnsignedByte();
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
}

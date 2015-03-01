package io.miti.jarman.classref;

import io.miti.jarman.util.Utility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import io.miti.jarman.classref.FieldMethodInfo;

/**
 * Class to hold information about a parsed class file.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ClassInfo
{
  /**
   * Whether this is a valid Java class file.
   */
  private boolean isValid = false;
  
  /**
   * The major version number.
   */
  private int majorVersion = 0;
  
  /**
   * The minor version number.
   */
  private int minorVersion = 0;
  
  /**
   * The Java version number.
   */
  private int javaVersion = 0;
  
  /**
   * List of all strings in the constant pool.
   */
  private Map<Integer, String> cpStrings = null;
  
  /**
   * The class access flags.
   */
  private int accessFlags = 0;
  
  /**
   * The class name.
   */
  private String className = null;
  
  /**
   * The parent class name.
   */
  private String parentName = null;
  
  /**
   * The list of implemented interfaces.
   */
  private List<String> interfaces = null;
  
  /**
   * The integer constants in the pool.
   */
  private List<Integer> arrayInt = null;
  
  /**
   * The float constants in the pool.
   */
  private List<Float> arrayFloat = null;
  
  /**
   * The long constants in the pool.
   */
  private List<Long> arrayLong = null;
  
  /**
   * The double constants in the pool.
   */
  private List<Double> arrayDouble = null;
  
  /**
   * The class references in the pool.
   */
  private Map<Integer, Integer> refClass = null;
  
  /**
   * The string references in the pool.
   */
  private List<Integer> refString = null;
  
  /**
   * The field references in the pool.
   */
  private List<Point> refField = null;
  
  /**
   * The method references in the pool.
   */
  private List<Point> refMethod = null;
  
  /**
   * The interface method references in the pool.
   */
  private List<Point> refInterfMethod = null;
  
  /**
   * The name and type references in the pool.
   */
  private Map<Integer, Point> refNAT = null;
  
  /**
   * The class fields.
   */
  private List<FieldMethodInfo> classFields = null;
  
  /**
   * The class methods.
   */
  private List<FieldMethodInfo> classMethods = null;
  
  
  /**
   * Default constructor.
   */
  public ClassInfo()
  {
    super();
  }
  
  
  /**
   * @return the isValid
   */
  public boolean isValid()
  {
    return isValid;
  }
  
  
  /**
   * Set whether this is a valid class file.
   * 
   * @param bIsValid whether this is a valid class file
   */
  public void setValid(final boolean bIsValid)
  {
    isValid = bIsValid;
  }
  
  
  /**
   * Return the major version number.
   * 
   * @return the major version
   */
  public int getMajorVersion()
  {
    return majorVersion;
  }
  
  
  /**
   * Set the major version number.
   * 
   * @param num the major version number
   */
  public void setMajorVersion(final int num)
  {
    majorVersion = num;
    javaVersion = num - 44;
  }
  
  
  /**
   * @return the minorVersion
   */
  public int getMinorVersion()
  {
    return minorVersion;
  }
  
  
  /**
   * Set the minor version number.
   * 
   * @param num the minor version number
   */
  public void setMinorVersion(final int num)
  {
    minorVersion = num;
  }
  
  
  /**
   * @return the javaVersion
   */
  public int getJavaVersion()
  {
    return javaVersion;
  }
  
  
  /**
   * Return a string containing the version information.
   * 
   * @return a version string
   */
  public String getVersionString()
  {
    StringBuilder sb = new StringBuilder(100);
    sb.append(Integer.toString(majorVersion))
      .append(".").append(Integer.toString(minorVersion))
      .append("  (Java ").append(Integer.toString(javaVersion)).append(")")
      .append(Utility.getLineSeparator());
    return sb.toString();
  }
  
  
  /**
   * Return the constant pool data.
   * 
   * @return the cpStrings
   */
  public Set<Map.Entry<Integer, String>> getCpStrings()
  {
    return cpStrings.entrySet();
  }
  
  
  /**
   * Return the constant pool string with the specified index.
   * 
   * @param index the CP index for the string
   * @return the string with a key of index
   */
  public String getCpString(final int index)
  {
    return cpStrings.get(Integer.valueOf(index));
  }
  
  
  /**
   * Add a constant pool ID and string.
   * 
   * @param cpString the constant pool string
   * @param cpID the ID of the string
   */
  public void addCPString(final String cpString,
                           final int cpID)
  {
    if (cpStrings == null)
    {
      cpStrings = new HashMap<Integer, String>(20);
    }
    
    cpStrings.put(cpID, cpString);
  }
  
  
  /**
   * Add an integer from the constant pool.
   * 
   * @param val the value to add
   */
  public void addCPInt(final int val)
  {
    if (arrayInt == null)
    {
      arrayInt = new ArrayList<Integer>(10);
    }
    
    arrayInt.add(Integer.valueOf(val));
  }
  
  
  /**
   * Add a float from the constant pool.
   * 
   * @param val the value to add
   */
  public void addCPFloat(final float val)
  {
    if (arrayFloat == null)
    {
      arrayFloat = new ArrayList<Float>(10);
    }
    
    arrayFloat.add(Float.valueOf(val));
  }
  
  
  /**
   * Add a long from the constant pool.
   * 
   * @param val the value to add
   */
  public void addCPLong(final long val)
  {
    if (arrayLong == null)
    {
      arrayLong = new ArrayList<Long>(10);
    }
    
    arrayLong.add(Long.valueOf(val));
  }
  
  
  /**
   * Add a double from the constant pool.
   * 
   * @param val the value to add
   */
  public void addCPDouble(final double val)
  {
    if (arrayDouble == null)
    {
      arrayDouble = new ArrayList<Double>(10);
    }
    
    arrayDouble.add(Double.valueOf(val));
  }
  
  
  /**
   * Add a string reference from the constant pool.
   * 
   * @param val the value to add
   */
  public void addStringRef(final int val)
  {
    if (refString == null)
    {
      refString = new ArrayList<Integer>(10);
    }
    
    refString.add(Integer.valueOf(val));
  }
  
  
  /**
   * Get the string reference count from the constant pool.
   * 
   * @return the number of objects in the data
   */
  public int getStringRefCount()
  {
    return ((refString == null) ? 0 : refString.size());
  }
  
  
  /**
   * Get the string reference iterator from the constant pool.
   * 
   * @return an iterator over the data
   */
  public Iterator<String> getStringRefIterator()
  {
    if (refString == null)
    {
      return null;
    }
    
    List<String> list = new ArrayList<String>(refString.size());
    for (Integer index : refString)
    {
      list.add(cpStrings.get(index));
    }
    
    return list.iterator();
  }
  
  
  /**
   * Add a field reference from the constant pool.
   * 
   * @param classRef the class reference in the constant pool
   * @param natRef the name and type descriptor reference in the constant pool
   */
  public void addFieldRef(final int classRef, final int natRef)
  {
    if (refField == null)
    {
      refField = new ArrayList<Point>(10);
    }
    
    refField.add(new Point(classRef, natRef));
  }
  
  
  /**
   * Get the field reference count from the constant pool.
   * 
   * @return the number of objects in the data
   */
  public int getFieldRefCount()
  {
    return ((refField == null) ? 0 : refField.size());
  }
  
  
  /**
   * Add a method reference from the constant pool.
   * 
   * @param classRef the class reference in the constant pool
   * @param natRef the name and type descriptor reference in the constant pool
   */
  public void addMethodRef(final int classRef, final int natRef)
  {
    if (refMethod == null)
    {
      refMethod = new ArrayList<Point>(10);
    }
    
    refMethod.add(new Point(classRef, natRef));
  }
  
  
  /**
   * Get the method reference count from the constant pool.
   * 
   * @return the number of objects in the data
   */
  public int getMethodRefCount()
  {
    return ((refMethod == null) ? 0 : refMethod.size());
  }
  
  
  /**
   * Add an interface method reference from the constant pool.
   * 
   * @param classRef the class reference in the constant pool
   * @param natRef the name and type descriptor reference in the constant pool
   */
  public void addInterfMethodRef(final int classRef, final int natRef)
  {
    if (refInterfMethod == null)
    {
      refInterfMethod = new ArrayList<Point>(10);
    }
    
    refInterfMethod.add(new Point(classRef, natRef));
  }
  
  
  /**
   * Get the Interface Method reference count from the constant pool.
   * 
   * @return the number of objects in the data
   */
  public int getInterfMethodRefCount()
  {
    return ((refInterfMethod == null) ? 0 : refInterfMethod.size());
  }
  
  
  /**
   * Add an interface method reference from the constant pool.
   * 
   * @param cpIndex the index into the constant pool
   * @param nameRef the name descriptor reference in the constant pool
   * @param typeRef the type descriptor reference in the constant pool
   */
  public void addNATRef(final int cpIndex, final int nameRef, final int typeRef)
  {
    if (refNAT == null)
    {
      refNAT = new HashMap<Integer, Point>(10);
    }
    
    refNAT.put(Integer.valueOf(cpIndex), new Point(nameRef, typeRef));
  }
  
  
  /**
   * Get the name and type description reference count from the constant pool.
   * 
   * @return the number of objects in the data
   */
  public int getNATRefCount()
  {
    return ((refNAT == null) ? 0 : refNAT.size());
  }
  
  
  /**
   * Return the access flags.
   * 
   * @return the access flags
   */
  public int getAccessFlags()
  {
    return accessFlags;
  }
  
  
  /**
   * Set the access flags.
   * 
   * @param nFlags the access flags
   */
  public void setAccessFlags(final int nFlags)
  {
    accessFlags = nFlags;
  }
  
  
  /**
   * Return the name of the class.
   * 
   * @return the name of the class
   */
  public String getClassName()
  {
    return className;
  }
  
  
  /**
   * Set the class name.
   * 
   * @param nameIndex the index into the CP for the class name
   */
  public void setClassName(final int nameIndex)
  {
    className = cpStrings.get(refClass.get(Integer.valueOf(nameIndex)));
  }
  
  
  /**
   * Return the name of the parent class.
   * 
   * @return the name of the parent class
   */
  public String getParentName()
  {
    return parentName;
  }
  
  
  /**
   * Set the name of the parent class.
   * 
   * @param parentIndex the parent class name to set
   */
  public void setParentName(final int parentIndex)
  {
    if (parentIndex <= 0)
    {
      parentName = "java.lang.object";
    }
    else
    {
      parentName = cpStrings.get(refClass.get(Integer.valueOf(parentIndex)));
    }
  }
  
  
  /**
   * Return a set of the referenced class names.
   * 
   * @return a set of the referenced class names
   */
  public Set<String> getClassNamesSet()
  {
    Set<String> set = new HashSet<String>();
    if (refClass == null)
    {
      return set;
    }
    
    // Add the referenced class names to the set
    for (Entry<Integer, Integer> entrySet : refClass.entrySet())
    {
      set.add(cpStrings.get(entrySet.getValue().intValue()));
    }
    
    return set;
  }
  
  
  /**
   * Return an iterator for the interfaces list.
   * 
   * @return the interfaces
   */
  public Iterator<String> getInterfaces()
  {
    if (interfaces == null)
    {
      return null;
    }
    
    return interfaces.iterator();
  }
  
  
  /**
   * Add to the list of implemented interfaces for the class.
   * 
   * @param ifIndex the index into the CP of the interface name
   */
  public void addInterface(final int ifIndex)
  {
    if (interfaces == null)
    {
      interfaces = new ArrayList<String>(10);
    }
    
    interfaces.add(cpStrings.get(refClass.get(Integer.valueOf(ifIndex))));
  }
  
  
  /**
   * Return the number of interfaces.
   * 
   * @return the number of interfaces
   */
  public int getInterfacesCount()
  {
    if (interfaces == null)
    {
      return 0;
    }
    
    return interfaces.size();
  }
  
  
  /**
   * Add an index to the CP of a class name.
   * 
   * @param key the CP index
   * @param value the corresponding index into the CP of the string
   */
  public void addCpClassIndex(final int key, final int value)
  {
    if (refClass == null)
    {
      refClass = new HashMap<Integer, Integer>(10);
    }
    
    refClass.put(Integer.valueOf(key), Integer.valueOf(value));
  }
  
  
  /**
   * Add a class field instance.
   * 
   * @param fmi the object description
   */
  public void addClassField(final FieldMethodInfo fmi)
  {
    if (classFields == null)
    {
      classFields = new ArrayList<FieldMethodInfo>(10);
    }
    
    classFields.add(fmi);
  }
  
  
  /**
   * Return an iterator over the class fields.
   * 
   * @return an iterator over the class fields
   */
  public Iterator<FieldMethodInfo> getClassFields()
  {
    if (classFields == null)
    {
      return null;
    }
    
    return classFields.iterator();
  }
  
  
  /**
   * Return the number of class fields.
   * 
   * @return the number of class fields
   */
  public int getClassFieldsCount()
  {
    if (classFields == null)
    {
      return 0;
    }
    
    return classFields.size();
  }
  
  
  /**
   * Add a class method instance.
   * 
   * @param fmi the object description
   */
  public void addClassMethod(final FieldMethodInfo fmi)
  {
    if (classMethods == null)
    {
      classMethods = new ArrayList<FieldMethodInfo>(10);
    }
    
    classMethods.add(fmi);
  }
  
  
  /**
   * Return an iterator over the class methods.
   * 
   * @return an iterator over the class methods
   */
  public Iterator<FieldMethodInfo> getClassMethods()
  {
    if (classMethods == null)
    {
      return null;
    }
    
    return classMethods.iterator();
  }
  
  
  /**
   * Return the number of class methods.
   * 
   * @return the number of methods
   */
  public int getClassMethodsCount()
  {
    if (classMethods == null)
    {
      return 0;
    }
    
    return classMethods.size();
  }
  
  
  /**
   * Return a string representation of this object.
   * 
   * @return this object as a string
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    // Check if this is a valid class
    if (!isValid)
    {
      return "Invalid class";
    }
    
    // Build the string, starting with the class name
    StringBuilder sb = new StringBuilder(50);
    sb.append("Class ").append(className)
      .append(" extending ")
      .append((parentName == null) ? "java/lang/Object" : parentName)
      .append(Utility.getLineSeparator());
    
    // Generate a string explaining the flags
    sb.append("Access Flag (").append(Integer.toString(accessFlags))
      .append("): ");
    addAccessFlagDesc(sb);
    sb.append(Utility.getLineSeparator());
    
    // Add the version numbers
    sb.append("Version ").append(majorVersion)
      .append(".").append(minorVersion)
      .append("  (Java ").append(javaVersion).append(")")
      .append(Utility.getLineSeparator());
    
    // Print the CP strings
//    if (cpStrings != null)
//    {
//      sb.append("Constant Pool:").append(Utility.getLineSeparator());
//      for (java.util.Map.Entry<Integer, String> entrySet : cpStrings.entrySet())
//      {
//        Integer key = entrySet.getKey();
//        String val = entrySet.getValue();
//        sb.append("  ").append(key.toString()).append(" : ")
//          .append(val).append(Utility.getLineSeparator());
//      }
//    }
    
    // Print the list of integers
    if (arrayInt == null)
    {
      sb.append("No integers were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Integers:").append(Utility.getLineSeparator());
      for (Integer val : arrayInt)
      {
        sb.append("  ").append(val.toString()).append(Utility.getLineSeparator());
      }
    }
    
    // Print the list of floats
    if (arrayFloat == null)
    {
      sb.append("No floats were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Floats:").append(Utility.getLineSeparator());
      for (Float val : arrayFloat)
      {
        sb.append("  ").append(val.toString()).append(Utility.getLineSeparator());
      }
    }
    
    // Print the list of longs
    if (arrayLong == null)
    {
      sb.append("No longs were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Longs:").append(Utility.getLineSeparator());
      for (Long val : arrayLong)
      {
        sb.append("  ").append(val.toString()).append(Utility.getLineSeparator());
      }
    }
    
    // Print the list of doubles
    if (arrayDouble == null)
    {
      sb.append("No doubles were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Doubles:").append(Utility.getLineSeparator());
      for (Double val : arrayDouble)
      {
        sb.append("  ").append(val.toString()).append(Utility.getLineSeparator());
      }
    }
    
    // Print the list of class references
    if (refClass == null)
    {
      sb.append("No class references were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Classes:").append(Utility.getLineSeparator());
      for (Entry<Integer, Integer> entrySet : refClass.entrySet())
      {
        sb.append("  ").append(cpStrings.get(entrySet.getValue().intValue()))
          .append(Utility.getLineSeparator());
      }
    }
    
    // Write out the unique list of referenced class names
//    Set<String> classSet = getClassNamesSet();
//    if (classSet != null)
//    {
//      sb.append("Set of Classes:").append(Utility.getLineSeparator());
//      for (String cn : classSet)
//      {
//        sb.append("  ").append(cn).append(Utility.getLineSeparator());
//      }
//    }
    
    // Print the list of string references
    if (refString == null)
    {
      sb.append("No string references were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Strings:").append(Utility.getLineSeparator());
      for (Integer val : refString)
      {
        sb.append("  ").append(cpStrings.get(val.intValue()))
          .append(Utility.getLineSeparator());
      }
    }
    
    // Print the list of field references
    appendRefPointData(sb, refField, "Field");
    
    // Print the list of method references
    appendRefPointData(sb, refMethod, "Method");
    
    // Print the list of interface method references
    appendRefPointData(sb, refInterfMethod, "Interface Method");
    
    // Print the list of NAT descriptor references
//    if (refNAT == null)
//    {
//      sb.append("No name and type descriptors were found")
//        .append(Utility.getLineSeparator());
//    }
//    else
//    {
//      sb.append("Name and Type Descriptors:").append(Utility.getLineSeparator());
//      for (Entry<Integer, Point> entry : refNAT.entrySet())
//      {
//        Point pt = entry.getValue();
//        sb.append("  ").append(cpStrings.get(pt.x)).append(", ")
//          .append(cpStrings.get(pt.y)).append(Utility.getLineSeparator());
//      }
//    }
    
    // Print the interfaces
    if (interfaces == null)
    {
      sb.append("No interfaces were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Interfaces:").append(Utility.getLineSeparator());
      for (String name : interfaces)
      {
        sb.append("  ").append(name).append(Utility.getLineSeparator());
      }
    }
    
    // Print the class fields
    if (classFields == null)
    {
      sb.append("No fields were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Fields:").append(Utility.getLineSeparator());
      for (FieldMethodInfo fmi : classFields)
      {
        String name = cpStrings.get(Integer.valueOf(fmi.getNameIndex()));
        sb.append("  ").append(name).append(" (").append(fmi.getAFDesc())
          .append(")").append(Utility.getLineSeparator());
      }
    }
    
    // Print the class methods
    if (classMethods == null)
    {
      sb.append("No methods were found").append(Utility.getLineSeparator());
    }
    else
    {
      sb.append("Methods:").append(Utility.getLineSeparator());
      for (FieldMethodInfo fmi : classMethods)
      {
        String name = cpStrings.get(Integer.valueOf(fmi.getNameIndex()));
        sb.append("  ").append(name).append(" (").append(fmi.getAFDesc())
          .append(")").append(Utility.getLineSeparator());
      }
    }
    
    // Return the generated string
    return sb.toString();
  }
  
  
  /**
   * Append a description of the access flags.
   * 
   * @param sb the string builder
   * @param data the list of Point data
   * @param desc the string description
   */
  private void appendRefPointData(final StringBuilder sb,
                                   final List<Point> data,
                                   final String desc)
  {
    // Check for no data
    if ((data == null) || (data.size() == 0))
    {
      sb.append(desc).append(" data not found").append(Utility.getLineSeparator());
      return;
    }
    
    sb.append(desc).append(" data:").append(Utility.getLineSeparator());
    for (Point pt : data)
    {
      String cname = cpStrings.get(refClass.get(Integer.valueOf(pt.x)));
      Point nat = refNAT.get(Integer.valueOf(pt.y));
      String name = cpStrings.get(nat.x);
      String typeStr = cpStrings.get(nat.y);
      sb.append("  ").append(cname).append(".").append(name)
        .append(": ").append(typeStr)
        .append(Utility.getLineSeparator());
    }
  }
  
  
  /**
   * Append a description of the access flags.
   * 
   * @param sb the string builder
   */
  public void addAccessFlagDesc(final StringBuilder sb)
  {
    if (accessFlags == 0)
    {
      sb.append("(None)").append(Utility.getLineSeparator());
      return;
    }
    
    boolean added = false;
    if ((accessFlags & 0x0001) != 0)
    {
      sb.append("Public");
      added = true;
    }
    
    if ((accessFlags & 0x0010) != 0)
    {
      if (added)
      {
        sb.append(", ");
      }
      sb.append("Final");
      added = true;
    }
    
    if ((accessFlags & 0x0020) != 0)
    {
      if (added)
      {
        sb.append(", ");
      }
      sb.append("Super");
      added = true;
    }
    
    if ((accessFlags & 0x0200) != 0)
    {
      if (added)
      {
        sb.append(", ");
      }
      sb.append("Interface");
      added = true;
    }
    
    if ((accessFlags & 0x0400) != 0)
    {
      if (added)
      {
        sb.append(", ");
      }
      sb.append("Abstract");
      added = true;
    }
    
    if (!added)
    {
      // No flags detected.  Data may be bad.
      sb.append("(Unknown)");
    }
  }
}

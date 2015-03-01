package io.miti.jarman.classref;

/**
 * Structure to hold data about class fields and methods.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FieldMethodInfo
{
  /**
   * The access flag for this object.
   */
  private int accessFlag = 0;
  
  /**
   * The index to the class name in the constant pool.
   */
  private int nameIndex = 0;
  
  /**
   * The index to the description in the constant pool.
   */
  private int descIndex = 0;
  
  /**
   * The list of access flag codes.
   */
  private static final int[] afCodes;
  
  /**
   * The corresponding list of access flag description strings.
   */
  private static final String[] afNames;
  
  /**
   * Fill in the access flags codes and descriptions.
   */
  static
  {
    afCodes = new int[] {0x0001, 0x0002, 0x0004, 0x0008, 0x0010,
        0x0020, 0x0040, 0x0080, 0x0100, 0x0200, 0x0400, 0x0800};
    
    afNames = new String[] {"public", "private", "protected", "static",
                    "final", "synchronized", "volatile", "transient",
                    "native", "interface", "abstract", "strict"};
  }
  
  
  /**
   * Default constructor.
   */
  public FieldMethodInfo()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param nAccess the access flags
   * @param nName the name index
   * @param nDesc the description index
   */
  public FieldMethodInfo(final int nAccess,
                          final int nName,
                          final int nDesc)
  {
    accessFlag = nAccess;
    nameIndex = nName;
    descIndex = nDesc;
  }


  /**
   * Return the access flags.
   * 
   * @return the accessFlag
   */
  public int getAccessFlag()
  {
    return accessFlag;
  }


  /**
   * Return the name index in the constant pool.
   * 
   * @return the nameIndex
   */
  public int getNameIndex()
  {
    return nameIndex;
  }


  /**
   * Return the description index in the constant pool.
   * 
   * @return the descIndex
   */
  public int getDescIndex()
  {
    return descIndex;
  }
  
  
  /**
   * Return a string describing this object's access flags.
   * 
   * @return a string describing the access flags
   */
  public String getAFDesc()
  {
    StringBuilder sb = new StringBuilder(100);
    if (accessFlag == 0)
    {
      return sb.toString();
    }
    
    final int max = afCodes.length;
    boolean added = false;
    for (int i = 0; i < max; ++i)
    {
      if ((accessFlag & afCodes[i]) != 0)
      {
        if (added)
        {
          sb.append(" ");
        }
        sb.append(afNames[i]);
        added = true;
      }
    }
    
    return sb.toString();
  }
}

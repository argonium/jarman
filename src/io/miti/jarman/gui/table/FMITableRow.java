package io.miti.jarman.gui.table;

/**
 * Represents each row in an FMI table.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class FMITableRow
{
  /**
   * Name.
   */
  private String name = null;
  
  /**
   * Description.
   */
  private String desc = null;
  
  /**
   * Attributes.
   */
  private String attr = null;
  
  
  /**
   * Default constructor.
   */
  public FMITableRow()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param sName the name
   * @param sDesc the description
   * @param sAttr the attribute string
   */
  public FMITableRow(final String sName, final String sDesc, final String sAttr)
  {
    name = sName;
    desc = sDesc;
    attr = sAttr;
  }
  
  
  /**
   * Return the name.
   * 
   * @return name
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Return the description.
   * 
   * @return the description
   */
  public String getDescription()
  {
    return desc;
  }
  
  
  /**
   * Return the attribute string.
   * 
   * @return the attribute string
   */
  public String getAttribute()
  {
    return attr;
  }
}

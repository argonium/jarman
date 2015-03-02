package io.miti.jarman.gui.table;

import io.miti.jarman.data.JarData;
import io.miti.jarman.gui.Jarman;
import io.miti.jarman.gui.ListingsPage;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

/**
 * Listen for changes to the View combo box.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ListingsTableListener implements ItemListener
{
  /**
   * Default constructor.
   */
  public ListingsTableListener()
  {
    super();
  }
  
  
  /**
   * Notification that the selection changed.
   * 
   * @param event the event
   */
  @Override
  public void itemStateChanged(final ItemEvent event)
  {
    JComboBox<String> cb = (JComboBox<String>) event.getSource();
    
    // Get the affected item
    if (event.getStateChange() == ItemEvent.SELECTED)
    {
      // Item was just selected
      int sel = cb.getSelectedIndex();
      ListingsPage.getInstance().setDisplayIndex(sel);
      JarData.getInstance().resetTables(true);
      
      // The number of visible rows on the Listings page may have changed,
      // so update the status bar label
      Jarman.getApp().updateVisibleRowCount();
    }
  }
}

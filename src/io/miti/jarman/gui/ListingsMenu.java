package io.miti.jarman.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

import io.miti.jarman.gui.ListingsPage;

/**
 * Popup menu for the Listings table.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ListingsMenu extends MouseAdapter
{
  /**
   * The popup menu for the Listings table.
   */
  private JPopupMenu popup = null;
  
  
  /**
   * Default constructor.
   */
  public ListingsMenu()
  {
  }
  
  
  /**
   * Constructor.
   * 
   * @param pPopup the popup menu
   */
  public ListingsMenu(final JPopupMenu pPopup)
  {
    popup = pPopup;
  }
  
  
  /**
   * Handle a mouse click.
   * 
   * @param e the event
   */
  @Override
  public void mouseClicked(final MouseEvent e)
  {
    // Check for a right-click on a row
    if (e.getButton() == MouseEvent.BUTTON3)
    {
      // Determine the row
      JTable table = ListingsPage.getInstance().getTable();
      int row = table.rowAtPoint(new Point(e.getX(), e.getY()));
      if (row >= 0)
      {
        // Select the row and show the popup menu
        table.setRowSelectionInterval(row, row);
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}

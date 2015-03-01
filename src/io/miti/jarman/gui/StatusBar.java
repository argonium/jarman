package io.miti.jarman.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * Status bar for the main frame.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class StatusBar extends JPanel
{
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The left-side label.
   */
  private JLabel label1 = null;
  
  /**
   * The right-side label.
   */
  private JLabel label2 = null;
  
  
  /**
   * Default constructor.
   */
  public StatusBar()
  {
    // Set the GUI elements of the panel
    setLayout(new BorderLayout(20, 0));
    setForeground(Color.black);
    setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
                                 new SoftBevelBorder(SoftBevelBorder.LOWERED)));
    
    // Create and add the two label fields
    label1 = new JLabel("Ready", SwingConstants.LEFT);
    label2 = new JLabel("", SwingConstants.RIGHT);
    add(label1, BorderLayout.CENTER);
    add(label2, BorderLayout.EAST);
  }
  
  
  /**
   * Set the text in the left-side label.
   * 
   * @param text the text to display
   */
  public void setText(final String text)
  {
    label1.setText(text);
  }
  
  
  /**
   * Set the text in the right-side label.
   * 
   * @param text the text to display
   */
  public void setText2(final String text)
  {
    label2.setText(text);
  }
}

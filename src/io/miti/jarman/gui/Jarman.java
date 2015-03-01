package io.miti.jarman.gui;

import io.miti.jarman.actions.FileOpen;
import io.miti.jarman.actions.FileOpenClass;
import io.miti.jarman.actions.FileOpenSingle;
import io.miti.jarman.actions.FileRefList;
import io.miti.jarman.actions.FileVerify;
import io.miti.jarman.data.FileList;
import io.miti.jarman.data.JarData;
import io.miti.jarman.util.Utility;
import io.miti.jarman.util.WindowState;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This is the main class for the application.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Jarman
{
  /**
   * The one instance of this class.
   */
  private static Jarman app = null;
  
  /**
   * The name of the properties file.
   */
  private static final String PROPS_FILE_NAME = "jm.prop";
  
  /**
   * The application frame.
   */
  private JFrame frame = null;
  
  /**
   * The status bar.
   */
  private StatusBar statusBar = null;
  
  /**
   * The window state (position and size).
   */
  private WindowState windowState = null;
  
  /**
   * The middle panel.
   */
  private JPanel appPanel = null;
  
  /**
   * The tabbed pane.
   */
  private JTabbedPane tp = null;
  
  
  /**
   * Default constructor.
   */
  private Jarman()
  {
    super();
  }
  
  
  /**
   * Return the one instance of this app.
   * 
   * @return the one instance of this app
   */
  public static Jarman getApp()
  {
    if (app == null)
    {
      app = new Jarman();
    }
    
    return app;
  }
  
  
  /**
   * Create the application's GUI.
   */
  private void createGUI()
  {
    // Load the properties file
    windowState = WindowState.getInstance();
    // checkInputFileSource();
    
    // Set up the frame
    setupFrame();
    
    // Initialize the menu bar
    initMenuBar();
    
    // Create the empty middle window
    initScreen();
    
    // Set up the status bar
    initStatusBar();
    
    // Update the menu to show recently-opened files
    FileList.getInstance().updateMenu();
    
    // Display the window.
    frame.pack();
    frame.setVisible(true);
    
    updateFileInfo();
  }
  
  
  /**
   * Set up the application frame.
   */
  private void setupFrame()
  {
    // Create and set up the window.
    frame = new JFrame(Utility.getAppName());
    
    // Have the frame call exitApp() whenever it closes
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter()
    {
      /**
       * Close the windows.
       * 
       * @param e the event
       */
      @Override
      public void windowClosing(final WindowEvent e)
      {
        exitApp();
      }
    });
    
    // Set up the size of the frame
    frame.setPreferredSize(windowState.getSize());
    frame.setSize(windowState.getSize());
    
    // Set the position
    if (windowState.shouldCenter())
    {
      frame.setLocationRelativeTo(null);
    }
    else
    {
      frame.setLocation(windowState.getPosition());
    }
  }
  
  
  /**
   * Initialize the main screen (middle window).
   */
  private void initScreen()
  {
    // Build the middle panel
    appPanel = new JPanel(new BorderLayout());
    appPanel.setBackground(Color.WHITE);
    
    // Set up the tabbed pane
    tp = new JTabbedPane();
    tp.add("Listing", ListingsPage.getInstance());
    tp.add("Jars", JarPage.getInstance());
    tp.add("Manifest", ManifestPage.getInstance());
    appPanel.add(tp, BorderLayout.CENTER);
    
    tp.setMnemonicAt(0, KeyEvent.VK_L);
    tp.setMnemonicAt(1, KeyEvent.VK_J);
    tp.setMnemonicAt(2, KeyEvent.VK_M);
    
    frame.getContentPane().add(appPanel, BorderLayout.CENTER);
  }
  
  
  /**
   * Update the tabbed pane for an open class.
   */
  public void updateTabsForClass()
  {
    // Make sure the Listings tab is selected since we're about to
    // disable the other two tabs
    tp.setSelectedIndex(0);
    enableGuiForJar(false);
  }
  
  
  /**
   * Update the tabbed pane for an open jar.
   */
  public void updateTabsForJar()
  {
    enableGuiForJar(true);
  }
  
  
  /**
   * Set the enabled state for some menu items and tabs,
   * based on whether a jar or class file is being opened.
   * 
   * @param isJar if we're opening a jar
   */
  private void enableGuiForJar(final boolean isJar)
  {
    tp.setEnabledAt(1, isJar);
    tp.setEnabledAt(2, isJar);
    
    frame.getJMenuBar().getMenu(0).getItem(4).setEnabled(isJar);
    frame.getJMenuBar().getMenu(0).getItem(5).setEnabled(isJar);
  }
  
  
  /**
   * Initialize the status bar.
   */
  private void initStatusBar()
  {
    // Instantiate the status bar
    statusBar = new StatusBar();
    
    // Add to the content pane
    frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
  }
  
  
  /**
   * Initialize the Menu Bar and associate actions.
   */
  private void initMenuBar()
  {
    // Create the menu bar
    JMenuBar menuBar = new JMenuBar();
    
    // Build the File menu
    buildFileMenu(menuBar);
    
    /*
     * Help menu item
     */
    JMenu menuHelp = new JMenu("Help");
    menuHelp.setMnemonic(KeyEvent.VK_H);
    menuBar.add(menuHelp);
    
    // Add the About menu item
    JMenuItem itemAbout = new JMenuItem("About");
    itemAbout.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        showAbout();
      }
    });
    itemAbout.setMnemonic(KeyEvent.VK_A);
    menuHelp.add(itemAbout);
    
    // Add the menu bar on the frame
    frame.setJMenuBar(menuBar);
  }
  
  
  /**
   * Show the About dialog box.
   */
  private void showAbout()
  {
    StringBuilder sb = new StringBuilder(100);
    sb.append("JarMan: Java Archive Manager\n")
      .append("Version 1.5, 15 Feb 2015");
    JOptionPane.showMessageDialog(frame, sb.toString(),
                  "About JarMan", JOptionPane.INFORMATION_MESSAGE);
  }
  
  
  /**
   * Build the File menu bar item.
   * 
   * @param menuBar the application menu bar
   */
  private void buildFileMenu(final JMenuBar menuBar)
  {
    /*
     * File Menu
     */
    JMenu menuFile = new JMenu("File");
    menuFile.setMnemonic(KeyEvent.VK_F);
    menuBar.add(menuFile);
    
    // Open a jar with its dependencies (from the manifest)
    JMenuItem itemOpen = new JMenuItem("Open Jar");
    itemOpen.setMnemonic(KeyEvent.VK_O);
    menuFile.add(itemOpen);
    itemOpen.addActionListener(FileOpen.getInstance());
    
    // Open a jar with its dependencies (from the manifest)
    JMenuItem itemOpenSingle = new JMenuItem("Open Single Jar");
    itemOpenSingle.setMnemonic(KeyEvent.VK_S);
    itemOpenSingle.addActionListener(FileOpenSingle.getInstance());
    menuFile.add(itemOpenSingle);
    
    // Open a jar with its dependencies (from the manifest)
    JMenuItem itemOpenClass = new JMenuItem("Open Class");
    itemOpenClass.setMnemonic(KeyEvent.VK_P);
    itemOpenClass.addActionListener(FileOpenClass.getInstance());
    menuFile.add(itemOpenClass);
    
    // Close
    JMenuItem itemClose = new JMenuItem("Close");
    itemClose.setMnemonic(KeyEvent.VK_C);
    itemClose.addActionListener(new ActionListener()
    {
      /**
       * Handle this menu item.
       * 
       * @param e the event
       */
      public void actionPerformed(final ActionEvent e)
      {
        closeFile();
      }
    });
    menuFile.add(itemClose);
    
    // Verify all
    JMenuItem itemVerify = new JMenuItem("Verify All");
    itemVerify.setMnemonic(KeyEvent.VK_V);
    menuFile.add(itemVerify);
    itemVerify.addActionListener(FileVerify.getInstance());
    
    // List all
    JMenuItem itemList = new JMenuItem("List All");
    itemList.setMnemonic(KeyEvent.VK_L);
    menuFile.add(itemList);
    itemList.addActionListener(FileRefList.getInstance());
    
    menuFile.addSeparator();
    
    // Close the application
    JMenuItem itemExit = new JMenuItem("Exit");
    itemExit.addActionListener(new ActionListener()
    {
      /**
       * Handle this menu item.
       * 
       * @param e the event
       */
      public void actionPerformed(final ActionEvent e)
      {
        exitApp();
      }
    });
    itemExit.setMnemonic(KeyEvent.VK_X);
    menuFile.add(itemExit);
  }
  
  
  /**
   * Close the open file and delete the data.
   */
  private void closeFile()
  {
    JarData.reset();
    updateFileInfo();
    ListingsPage.getInstance().resetOptions();
  }
  
  
  /**
   * Update the status bar to show the open/closed file.
   */
  public void updateFileInfo()
  {
    // Check if there is data to show
    final boolean hasData = JarData.hasData();
    
    // Update the file name in the status bar
    boolean showingFile = false;
    if (hasData)
    {
      String name = JarData.getInstance().getJarFileName();
      if (name != null)
      {
        statusBar.setText("Showing file " + name);
        showingFile = true;
      }
    }
    
    // Check if an error occurred
    if (!showingFile)
    {
      statusBar.setText("Ready");
    }
    
    // Update the menu items
    frame.getJMenuBar().getMenu(0).getItem(3).setEnabled(hasData); // close
    frame.getJMenuBar().getMenu(0).getItem(4).setEnabled(hasData); // verify
    frame.getJMenuBar().getMenu(0).getItem(5).setEnabled(hasData); // list
    
    // The number of visible rows on the Listings page changed, so
    // update the status bar label
    updateVisibleRowCount();
  }
  
  
  /**
   * Update the displayed count of the number of visible rows in the table.
   */
  public void updateVisibleRowCount()
  {
    // Check if there is data to show
    final boolean hasData = JarData.hasData();
    
    // Update the right-side label in the status bar
    if (!hasData)
    {
      statusBar.setText2("");
    }
    else
    {
      // Show the number of rows visible in the table
      final int rowCount = ListingsPage.getInstance().getTable().getRowCount();
      String data = NumberFormat.getIntegerInstance().format(rowCount);
      
      // Build the output string.  Append an 's' if the count is not one.
      StringBuilder sb = new StringBuilder(50);
      sb.append("Showing ").append(data).append(" file");
      if (rowCount != 1)
      {
        sb.append("s");
      }
      
      // Set the label text
      statusBar.setText2(sb.toString());
    }
  }
  
  
  /**
   * Return the parent frame.
   * 
   * @return the parent frame
   */
  public JFrame getFrame()
  {
    return frame;
  }
  
  
  /**
   * Exit the application.
   */
  private void exitApp()
  {
    // Store the window state in the properties file
    windowState.update(frame.getBounds());
    windowState.saveToFile(PROPS_FILE_NAME);
    
    // Close the application by disposing of the frame
    frame.dispose();
  }
  
  
  /**
   * Entry point to the application.
   * 
   * @param args arguments passed to the application
   */
  public static void main(final String[] args)
  {
    // Make the application Mac-compatible
    Utility.makeMacCompatible();
    
    // Load the properties file data
    WindowState.load(PROPS_FILE_NAME);
    
    // Initialize the look and feel to the default for this OS
    Utility.initLookAndFeel();
    
    // Check the version number
    if (!Utility.hasRequiredJVMVersion())
    {
      System.exit(0);
    }
    
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // Run the application
        Jarman.getApp().createGUI();
      }
    });
  }
}

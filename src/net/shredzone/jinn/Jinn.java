/*
 * jinn -- A tool for easier translation of properties files
 *
 * Copyright (c) 2005 Richard "Shred" Körber
 *   http://www.shredzone.net/go/jinn
 *
 *-----------------------------------------------------------------------
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is JINN.
 *
 * The Initial Developer of the Original Code is
 * Richard "Shred" Körber.
 * Portions created by the Initial Developer are Copyright (C) 2005
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK *****
 */
 
package net.shredzone.jinn;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.shredzone.jinn.action.AboutAction;
import net.shredzone.jinn.action.GotoAction;
import net.shredzone.jinn.action.MergeAction;
import net.shredzone.jinn.action.NewAction;
import net.shredzone.jinn.action.NextAction;
import net.shredzone.jinn.action.OpenAction;
import net.shredzone.jinn.action.QuitAction;
import net.shredzone.jinn.action.SaveAction;
import net.shredzone.jinn.action.SaveAsAction;
import net.shredzone.jinn.action.SearchAction;
import net.shredzone.jinn.action.SearchNextAction;
import net.shredzone.jinn.gui.JinnPane;
import net.shredzone.jinn.i18n.L;

/**
 * Jinn is a tool for easier translation of properties files.
 * <p>
 * It was designed with KISS (Keep It Simple Stupid) in mind. Most translators
 * are living all over the world, most probably living in a country where
 * their native language is spoken. Also most translators are not software
 * developers, but normal users who don't know too much about computers and
 * especially about software localizion.
 * <p>
 * This means that a translation tool must be:
 * <ul>
 *   <li>easy to install
 *   <li>easy to use
 *   <li>hiding the complexity of properties files (and believe me, they ARE
 *       complex for common computer users)
 * </ul>
 * <p>
 * The name jinn consists of 'j' for Java, 'in' for i18n (the abbreviation of
 * 'internationalization') without the 18, and 'n' because 'jin' was already
 * taken by another project. ;-) 
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: Jinn.java 84 2006-05-17 12:46:49Z shred $
 */
public class Jinn {
  private final static Preferences prefs = java.util.prefs.Preferences.userNodeForPackage( Jinn.class );

  private final Registry registry = new Registry();
  private JFrame frame;
  
  public Jinn() {
    frame = new JFrame();
    frame.setTitle( L.tr("generic.title") + Style.VERSION );
    
    registry.put( JinnRegistryKeys.FRAME_MAIN, frame );
    
    createActions();
    
    frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
    frame.addWindowListener( new WindowAdapter() {
      public void windowClosing ( WindowEvent e ) {
        storeFrame();
        QuitAction aQuit = (QuitAction) registry.get( JinnRegistryKeys.ACTION_QUIT );
        aQuit.perform();
      }
    });

    JinnPane jinnpane = new JinnPane( registry );
    frame.getContentPane().add( jinnpane );
    recallFrame();
    
    // Why using invokeLater here? See:
    // http://java.sun.com/developer/JDCTechTips/2003/tt1208.html#1
    SwingUtilities.invokeLater( new Runnable() {
      public void run() {
        frame.setVisible( true );
      }
    } );
  }

  /**
   * Store the frame position and size.
   */
  protected void storeFrame() {
    final Point pnt = frame.getLocation();
    final Dimension dim = frame.getSize();
    prefs.putInt( "win.pos.x" , pnt.x );
    prefs.putInt( "win.pos.y" , pnt.y );
    prefs.putInt( "win.size.w", dim.width  );
    prefs.putInt( "win.size.h", dim.height );
    
  }
  
  /**
   * Recall the frame position and size.
   */
  protected void recallFrame() {
    final int pw = prefs.getInt( "win.size.w", -1 );
    final int ph = prefs.getInt( "win.size.h", -1 );
    if( pw >= 0 && ph >= 0 ) {
      frame.setSize( pw, ph );
    }else {
      frame.pack();
    }

    final int px = prefs.getInt( "win.pos.x", -9999 );
    final int py = prefs.getInt( "win.pos.y", -9999 );
    if( px != -9999 && py != -9999 ) {
      frame.setLocation( px, py );
    }else {
      frame.setLocationRelativeTo( null );
    }
  }
  
  /**
   * Create all Actions and put them into the registry.
   */
  protected void createActions() {
    registry.put( JinnRegistryKeys.ACTION_QUIT,   new QuitAction( frame ) );
    registry.put( JinnRegistryKeys.ACTION_NEW,    new NewAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_OPEN,   new OpenAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_MERGE,  new MergeAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_SAVE,   new SaveAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_SAVEAS, new SaveAsAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_NEXT,   new NextAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_SEARCH, new SearchAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_SEARCH_NEXT, new SearchNextAction( registry, SearchNextAction.NEXT ) );
    registry.put( JinnRegistryKeys.ACTION_SEARCH_PREV, new SearchNextAction( registry, SearchNextAction.PREVIOUS ) );
    registry.put( JinnRegistryKeys.ACTION_GOTO,   new GotoAction( registry ) );
    registry.put( JinnRegistryKeys.ACTION_ABOUT,  new AboutAction( registry ) );
  }

  /**
   * @param args
   */
  public static void main( String[] args ) {
    //--- Sprache setzen ---
    Preferences prefs  = Preferences.userNodeForPackage( Jinn.class );
    String lname = prefs.get( "lang", "" );
    if( lname.length()!=0 ) {
      Locale.setDefault( new Locale( lname ) );
    }

    //--- Do some MacX patches ---
    final boolean isMacOS = ( System.getProperty("mrj.version") != null );
    if( isMacOS ) {
      System.setProperty( "apple.laf.useScreenMenuBar", "true" );
      System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "jinn" );
    }

    new Jinn();
  }


}

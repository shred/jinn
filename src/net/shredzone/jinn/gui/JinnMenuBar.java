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
 
package net.shredzone.jinn.gui;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jshred.swing.MenuActionProxy;
import net.shredzone.jshred.swing.SwingUtils;


/**
 * This is the MenuBar of Jinn.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: JinnMenuBar.java,v 1.5 2005/11/14 12:14:35 shred Exp $
 */
public class JinnMenuBar extends JMenuBar {
  private static final long serialVersionUID = 302069297128892725L;
  private final Registry registry;
  
  public JinnMenuBar( Registry registry ) {
    this.registry = registry;
    
    final JMenu jmFile = SwingUtils.createJMenu( L.tr("menu.file") );
    {
      jmFile.add( getItem( JinnRegistryKeys.ACTION_NEW ) );
      jmFile.add( getItem( JinnRegistryKeys.ACTION_OPEN ) );
      jmFile.add( getItem( JinnRegistryKeys.ACTION_SAVE ) );
      jmFile.add( getItem( JinnRegistryKeys.ACTION_SAVEAS ) );
      jmFile.addSeparator();
      jmFile.add( getItem( JinnRegistryKeys.ACTION_MERGE ) );
      jmFile.addSeparator();
      jmFile.add( getItem( JinnRegistryKeys.ACTION_QUIT ) );
    }
    add( jmFile );
    
    final JMenu jmEdit = SwingUtils.createJMenu( L.tr("menu.edit") );
    {
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_UNDO ) );
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_REDO ) );
      jmEdit.addSeparator();
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_CUT ) );
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_COPY ) );
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_PASTE ) );
      jmEdit.addSeparator();
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_NEXT ) );
      jmEdit.add( getItem( JinnRegistryKeys.ACTION_RESET ) );
    }
    add( jmEdit );
    
    add( Box.createGlue() );
    
    final JMenu jmHelp = SwingUtils.createJMenu( L.tr("menu.help") );
    {
      jmHelp.add( getItem( JinnRegistryKeys.ACTION_ABOUT ) );
    }
    add( jmHelp );
  }
    
  /**
   * Create a menu Action for a registry Action key.
   * 
   * @param key   ACTION key to be used
   * @return  Created MenuActionProxy
   */
  protected MenuActionProxy getItem( String key ) {
    return new MenuActionProxy( (Action) registry.get( key ) );
  }
  
  
}

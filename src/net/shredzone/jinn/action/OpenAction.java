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
 
package net.shredzone.jinn.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.ExceptionDialog;
import net.shredzone.jinn.gui.PropertiesFileFilter;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Open a properties file
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: OpenAction.java 68 2006-02-02 12:51:43Z shred $
 */
public class OpenAction extends AsyncBaseAction {
  private static final long serialVersionUID = -1701159023065148732L;
  protected final Registry registry;
  
  /**
   * Create a new OpenAction.
   *
   * @param   registry    The application's Registry
   */
  public OpenAction( Registry registry ) {
    super (
      L.tr( "action.open" ),
      ImgPool.get( "open.png" ),
      L.tr( "action.open.tt" ),
      KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.CTRL_MASK )
    );

    this.registry = registry;
  }
  
  /**
   * The action implementation itself.
   * 
   * @param  e      ActionEvent, may be null if directly invoked
   */
  public void perform( ActionEvent e ) {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory( (File) registry.get( JinnRegistryKeys.FILE_LASTDIR ) );
    fc.setFileFilter( PropertiesFileFilter.DEFAULT );
    int result = fc.showOpenDialog( getFrame(e) );
    if (result == JFileChooser.APPROVE_OPTION) {
      final File file = fc.getSelectedFile();
      
      InputStream in = null;
      
      try {
        PropertyModel src = new PropertyModel();
        in = new BufferedInputStream( new FileInputStream( file ) );
        src.read( in );
        registry.put( JinnRegistryKeys.FILE_LASTDIR, file.getParentFile() );
        registry.put( JinnRegistryKeys.FILE_TRANSLATION, file );
        registry.put( JinnRegistryKeys.MODEL_TRANSLATION, src );

      }catch( Exception ex ) {
        ExceptionDialog.show( getFrame(e), L.tr("a.open.ex.reading"), ex );
      }finally {
        if (in != null) {
          try {
            in.close();
          }catch( IOException ex2 ) {
            ExceptionDialog.show( getFrame(e), L.tr("a.open.ex.closing"), ex2 );
          }
        }
      }
      
    }
  }
  
}

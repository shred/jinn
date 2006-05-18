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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;
import net.shredzone.jinn.property.PropertyModel;

/**
 * Quit the application.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: QuitAction.java 85 2006-05-18 07:00:11Z shred $
 */
public class QuitAction extends BaseAction {
  private static final long serialVersionUID = -2916484363510929249L;
  private Window window;
  protected final Registry registry;

  /**
   * Create a new QuitAction.
   *
   * @param   registry    The application's Registry
   */
  public QuitAction( Registry registry ) {
    super (
      L.tr( "action.quit" ),
      ImgPool.get( "quit.png" ),
      L.tr( "action.quit.tt" ),
      KeyStroke.getKeyStroke( KeyEvent.VK_Q, ActionEvent.CTRL_MASK )
    );

    this.registry = registry;
    this.window = (Window) registry.get( JinnRegistryKeys.FRAME_MAIN );
  }
  
  /**
   * The action implementation itself.
   * 
   * @param  e      ActionEvent, may be null if directly invoked
   */
  public void perform( ActionEvent e ) {
    final File target = (File) registry.get( JinnRegistryKeys.FILE_TRANSLATION );
    final PropertyModel model = (PropertyModel) registry.get( JinnRegistryKeys.MODEL_TRANSLATION );

    if (target != null && model != null && registry.is(  JinnRegistryKeys.FLAG_CHANGED )) {
      int result = JOptionPane.showConfirmDialog(
          window,
          L.tr("save.confirm"),
          L.tr("save.confirm.quit"),
          JOptionPane.YES_NO_CANCEL_OPTION
      );
      
      if( result == JOptionPane.CANCEL_OPTION ) {
        return;
      }
      
      if( result == JOptionPane.YES_OPTION ) {
        SaveAction save = (SaveAction) registry.get( JinnRegistryKeys.ACTION_SAVE );
        if(! save.doSave( getFrame(e) ) ) {
          return;     // Save failed: do not quit!
        }
      }
    }
    
    if (window != null) {
      window.setVisible( false );
      window.dispose();
    }else {
      System.exit(0);   // nothing else would take care for quitting
    }
  }
  
}

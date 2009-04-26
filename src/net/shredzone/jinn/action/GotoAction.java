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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.PropertyKeyModel;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * Go to a key.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: GotoAction.java 285 2009-04-26 22:42:14Z shred $
 */
public class GotoAction extends BaseAction {
  private static final long serialVersionUID = -6185052702783447904L;
  private final Registry registry;
  
  /**
   * Create a new GotoAction.
   */
  public GotoAction( Registry registry ) {
    super (
      L.tr( "action.goto" ),
      ImgPool.get( "goto.png" ),
      L.tr( "action.goto.tt" ),
      KeyStroke.getKeyStroke( KeyEvent.VK_G, ActionEvent.CTRL_MASK )
    );
    this.registry = registry;

    setEnabled( registry.get( JinnRegistryKeys.MODEL_TRANSLATION ) != null );
    
    registry.addPropertyChangeListener( JinnRegistryKeys.MODEL_TRANSLATION, new PropertyChangeListener() {
      public void propertyChange( PropertyChangeEvent evt ) {
        setEnabled( evt.getNewValue() != null );
      }
    });
  }
  
  /**
   * The action implementation itself.
   * 
   * @param  e      ActionEvent, may be null if directly invoked
   */
  @Override
  public void perform( ActionEvent e ) {
    String input = JOptionPane.showInputDialog(
        getFrame( e ),
        L.tr( "goto.msg" ),
        L.tr( "goto.title" ),
        JOptionPane.QUESTION_MESSAGE
    );

    if( input!=null ) {
      final PropertyKeyModel keyModel = (PropertyKeyModel) registry.get( JinnRegistryKeys.MODEL_REFERENCE_KEY );
      if( keyModel==null ) return;
      
      if( keyModel.hasKey( input ) ) {
        registry.put( JinnRegistryKeys.CURRENT_KEY, input );
      }else {
        JOptionPane.showMessageDialog(
            getFrame( e ),
            L.tr( "goto.notfound.msg" ),
            L.tr( "goto.notfound.title" ),
            JOptionPane.INFORMATION_MESSAGE
        );
      }
    }
  }
  
}

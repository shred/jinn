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

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import net.shredzone.jshred.swing.SwingUtils;


/**
 * The basic class for all Jinn actions. The action is executed
 * synchronously with in the GUI thread, so it is rather meant to be
 * used for actions that either take very little time to execute, or
 * for actions which open a modal dialog.
 * <p>
 * The <code>perform()</code> method is usually executed in the AWT
 * dispatch thread. You may manipulate Swing objects immediately, but
 * changes are reflected only after the <code>perform()</code> method
 * exits.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: BaseAction.java 285 2009-04-26 22:42:14Z shred $
 */
public abstract class BaseAction extends AbstractAction {
  private static final long serialVersionUID = -8894889050966762864L;

  /**
   * Create a new, synchronous Action. Event processing will be stopped
   * during execution, which also means that there is no GUI update.
   *
   * @param   name        Action Name
   * @param   icon        Action Icon or null
   * @param   tip         Action Tooltip or null
   * @param   accel       Accelerator Key or null
   */
  public BaseAction( String name, Icon icon, String tip, KeyStroke accel ) {
    SwingUtils.setMenuKey( this, name );
    if( icon!=null )
      putValue( Action.SMALL_ICON,        icon );
    if( tip!=null )
      putValue( Action.SHORT_DESCRIPTION, tip );
    if( accel!=null )
      putValue( Action.ACCELERATOR_KEY,   accel );
  }

  /**
   * The action has been invoked from a button, menu item etc.
   *
   * @param   e       ActionEvent
   */
  public void actionPerformed( ActionEvent e ) {
    perform( e );
  }
  
  /**
   * The action implementation itself. Overwrite this method with your
   * desired action.
   * <p>
   * This method is usually performed within the AWT dispatch thread,
   * so you can manipulate Swing objects directly, but changes won't
   * be painted while this method executes.
   * 
   * @param  e      ActionEvent, may be null if directly invoked
   */
  public abstract void perform( ActionEvent e );

  /**
   * A convenience call to invoke <code>perform()</code> directly, e.g.
   * from a batch process.
   */
  public void perform() {
    perform( null );
  }

  /**
   * Get the owner Frame of the ActionEvent's source object. If there
   * is no ActionEvent, or if the source was not a Component, null will
   * be returned.
   * 
   * @param   e     ActionEvent, may be null
   * @return  Frame or null if no Frame could be evaluated
   */
  protected Frame getFrame( ActionEvent e ) {
    if (e == null) return null;
    Object src = e.getSource();
    if (src == null || !(src instanceof Component)) return null;
    return SwingUtils.getComponentFrame( (Component) src );
  }
  
}

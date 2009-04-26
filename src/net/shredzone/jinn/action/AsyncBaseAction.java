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

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * The base class for asynchronous actions. The action itself
 * will be executed in a separate thread, keeping the GUI thread
 * running. The GUI will be blocked meanwhile, though. This kind is
 * meant for actions which take a rather long time to execute, but
 * do not open a modal dialog.
 * <p>
 * The <code>perform()</code> method is always executed in an own thread,
 * not in the AWT dispatch thread. If you manipulate Swing objects, remember
 * to use <code>EventQueue.invokeLater()</code>.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: AsyncBaseAction.java 285 2009-04-26 22:42:14Z shred $
 */
public abstract class AsyncBaseAction extends BaseAction {
  private static final long serialVersionUID = 8756416480105882092L;

  /**
   * Create a new, asynchronous Action.
   *
   * @param   name        Action Name
   * @param   icon        Action Icon or null
   * @param   tip         Action Tooltip or null
   * @param   accel       Accelerator Key or null
   */
  public AsyncBaseAction( String name, Icon icon, String tip, KeyStroke accel ) {
    super( name, icon, tip, accel );
  }
  
  /**
   * The action has been invoked from a button, menu item etc.
   *
   * @param   e       ActionEvent
   */
  @Override
  public void actionPerformed( final ActionEvent e ) {
    //--- Create background thread ---
    Thread thread = new Thread( new Runnable() {
      public void run() {
        Cursor cursor = null;
        
        //--- Lock the frame ---
        final Frame frame = getFrame( e );
        if (frame != null) {
          frame.setEnabled( false );
          cursor = frame.getCursor();
          frame.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
        }

        //--- Invoke the action ---
        try {
          perform( e );
        }finally {
          //--- In any case, unlock the frame ---
          if (frame != null) {
            frame.setCursor( cursor );
            frame.setEnabled( true );
          }
        }
      }
    });
    
    //--- Run it ---
    // http://java.sun.com/developer/JDCTechTips/2005/tt0727.html#1
    thread.setPriority( Thread.NORM_PRIORITY );
    thread.start();
  }

}


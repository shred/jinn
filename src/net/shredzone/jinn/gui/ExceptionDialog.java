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

import net.shredzone.jinn.i18n.L;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;
import java.text.*;

/**
 * Informs the user about an Exception that has occured.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: ExceptionDialog.java,v 1.1 2005/11/01 23:19:32 shred Exp $
 */
public class ExceptionDialog {
  private static boolean debugMode = false;
  
  /**
   * Set the debug mode. If set, a stacktrace will be shown along with the
   * dialog.
   * 
   * @param mode    true: debug mode, false: standard mode
   */
  public static void setDebug( boolean mode ) {
    debugMode = mode;
  }
  
  /**
   * Check if the debug mode is enabled.
   * 
   * @return   true: debug mode, false: standard mode
   */
  public static boolean isDebug() {
    return debugMode;
  }
  
  /**
   * Show an exception dialog if an exception occured.
   *
   * @param   parent      Parent component to be locked during dialog
   * @param   op          Operation that was being performed when the
   *                      exception was raised.
   * @param   ex          Exception that was raised.
   */
  public static void show( Component parent, String op, Exception ex ) {
    //--- Write a stacktrace to stderr ---
    ex.printStackTrace();
    
    //--- Create the arguments array ---
    Object[] args = new Object[] {
      op,
      ex.getLocalizedMessage()
    };
    
    //--- Create a message string ---
    String message = MessageFormat.format( L.tr("exception.msg"), args );

    //--- Show the dialog ---
    Object msg = message;

    //--- Assemble the debug message ---
    if( isDebug() ) {
      StringWriter sw = new StringWriter();
      ex.printStackTrace( new PrintWriter( sw ) );
      
      JTextArea jtaTrace = new JTextArea();
      jtaTrace.setRows(3);
      jtaTrace.setColumns(40);
      jtaTrace.setEditable(false);
      jtaTrace.setText(sw.toString());
      jtaTrace.setCaretPosition(0);

      msg = new Object[] { message, new JScrollPane( jtaTrace ) };
    }
    
    //--- Debug: Show a stacktrace ---
    JOptionPane.showMessageDialog(
        parent,
        msg,
        L.tr("exception.title"),
        JOptionPane.ERROR_MESSAGE
    );
  }

}

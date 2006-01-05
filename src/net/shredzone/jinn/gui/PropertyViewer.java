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

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.shredzone.jinn.property.PropertyModel;


/**
 * A PropertyViewer is used to view a PropertyModel. It is shown in a
 * kind of Table view, with one column showing the line number, and another
 * column showing the line content. It is also possible to highlight a
 * line.
 * <p>
 * The PropertyViewer is merely a JList, but with a SINGLE_SELECTION model
 * turned on by default.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertyViewer.java,v 1.5 2005/10/30 21:48:50 shred Exp $
 */
public class PropertyViewer extends JList {
  private static final long serialVersionUID = -3305575282409649481L;

  /**
   * Create an empty PropertyViewer.
   */
  public PropertyViewer() {
    this( new PropertyModel() );
  }
  
  /**
   * Create a PropertyViewer for a given PropertyModel.
   * 
   * @param model   PropertyModel to show.
   */
  public PropertyViewer( PropertyModel model ) {
    super( model );
    setCellRenderer( new LineRenderer() );
    setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
  }

}

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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import net.shredzone.jinn.pool.ImgPool;


/**
 * A KeyRenderer renders a Key line shown in a JList.
 * 
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: KeyRenderer.java 68 2006-02-02 12:51:43Z shred $
 */
public class KeyRenderer extends DefaultListCellRenderer {
  private static final long serialVersionUID = -1559580545428262000L;
  
  private static final ImageIcon iconEmpty      = ImgPool.get("m-empty.png");
  private static final ImageIcon iconNew        = ImgPool.get("m-added.png");
  private static final ImageIcon iconTranslated = ImgPool.get("m-translated.png");

  public KeyRenderer() {
    setIcon( iconEmpty );
  }
  
  /**
   * Get a Component that draws the current line.
   * 
   * @param  list           Referred JList
   * @param  value          Value object to be shown
   * @param  index          Index number
   * @param  isSelected     true: line is selected
   * @param  cellHasFocus   true: line is focussed
   * @return  Component that draws this line
   */
  public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    JLabel comp = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
    
    final ListModel model = list.getModel();
    
    setIcon( iconEmpty );
    
    if (model != null && model instanceof PropertyKeyRefModel) {
      final PropertyKeyRefModel ref = (PropertyKeyRefModel) model;
      final String key = value.toString();
      
      if (ref.isChanged( key )) {
        setIcon( iconTranslated );
      }else if (ref.isNew( key )) {
        setIcon( iconNew );
      }else if (ref.isSurplus( key )) {
        setIcon( iconNew );
      }
    }
    
    return comp;
  }

}

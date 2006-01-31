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

import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.PropertyKeyModel;
import net.shredzone.jinn.property.PropertyLine;
import net.shredzone.jinn.property.PropertyModel;

/**
 * The common superclass for all search operation actions.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id:$
 */
public abstract class BaseSearchAction extends BaseAction {
  protected final Registry registry;
  
  /**
   * Create a new, synchronous Action. Event processing will be stopped
   * during execution, which also means that there is no GUI update.
   *
   * @param   registry    Registry to be used
   * @param   name        Action Name
   * @param   icon        Action Icon or null
   * @param   tip         Action Tooltip or null
   * @param   accel       Accelerator Key or null
   */
  public BaseSearchAction( Registry registry, String name, Icon icon, String tip, KeyStroke accel ) {
    super( name, icon, tip, accel );
    this.registry = registry;
  }
  
  protected String getSearchTerm() {
    return (String) registry.get( JinnRegistryKeys.SEARCH_TERM );
  }
  
  protected boolean isCaseSensitive() {
    Boolean cs = (Boolean) registry.get( JinnRegistryKeys.SEARCH_CASE_SENSITIVE );
    return cs.booleanValue();
  }
  
  protected void showNotFoundMessage( Frame frm ) {
    JOptionPane.showMessageDialog(
        frm,
        "No more matches",  //TODO: Translate
        "Nothing found",
        JOptionPane.INFORMATION_MESSAGE
    );
  }
  
  /**
   * Search the next match.
   * <p>
   * First, it will try to find a match within the current document, starting
   * from the caret position (or the end of a selected range, respectively).
   * If no match was found within the document, the next resource with the
   * search string is located. If there is no next resorce, false will be
   * returned.
   * <p>
   * After a match was found, the resource will be chosen and the word will
   * be selected.
   *
   * @return true: Match was found, false: No match
   */
  protected boolean searchNext() {
    boolean ok = searchDocument();
    if( ok ) return true;
    
    ok = searchResource();
    if( ok ) {
      searchDocument();
      return true;
    }
    
    return false;
  }
  
  private boolean searchResource() {
    final PropertyKeyModel keyModel = (PropertyKeyModel) registry.get( JinnRegistryKeys.MODEL_REFERENCE_KEY );
    final PropertyModel transModel = (PropertyModel) registry.get( JinnRegistryKeys.MODEL_TRANSLATION );
    
    if( keyModel==null ) return false;
    
    int current = 0;

    final String currentKey = registry.getString( JinnRegistryKeys.CURRENT_KEY );
    if( currentKey!=null ) {
      current = keyModel.findKey( currentKey ) + 1;
    }

    String term = getSearchTerm();
    if(! isCaseSensitive() ) {
      term = term.toLowerCase();
    }

    for( int ix=current; ix<keyModel.getSize(); ix++ ) {
      String key = (String) keyModel.getElementAt(ix);
      PropertyLine line = transModel.getPropertyLine( key );
      String content = line.getValue();
      if(! isCaseSensitive() ) {
        content = content.toLowerCase();
      }
      
      if( content.indexOf(term)>=0 ) {
        registry.put( JinnRegistryKeys.CURRENT_KEY, key );
        return true;        // Found something
      }
    }
    
    return false;       // End reached, nothing was found
  }
  
  private boolean searchDocument() {
    JTextComponent editor = (JTextComponent) registry.get( JinnRegistryKeys.TRANSLATION_TEXT );
    if( editor==null ) return false;
      
    int pos = Math.max( editor.getSelectionEnd(), editor.getSelectionStart() );
    if( pos==0 ) pos = editor.getCaretPosition();
      
    String term = getSearchTerm();
    String content = editor.getText();
    
    if( pos >= content.length() ) return false;
    
    if(! isCaseSensitive() ) {
      term = term.toLowerCase();
      content = content.toLowerCase();
    }

    int newpos = content.indexOf( term, pos+1 );
    if( newpos<0 ) return false;
    
    editor.setSelectionStart( newpos );
    editor.setSelectionEnd( newpos + term.length() );
    
    return true;
  }
  
}

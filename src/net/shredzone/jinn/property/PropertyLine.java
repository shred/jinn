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
 
package net.shredzone.jinn.property;

import java.io.IOException;


/**
 * This class represents a single translation line of a properties file.
 * It consists of a String type key and a String type translation.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertyLine.java 68 2006-02-02 12:51:43Z shred $
 */
public class PropertyLine extends AbstractLine {
  private String key;
  private String value;
  
  /**
   * Create a new PropertyLine with the given key and value.
   * 
   * @param key     Property key
   * @param value   Property value
   */
  public PropertyLine( String key, String value ) {
    this.key = key;
    this.value = value;
  }
  
  /**
   * Write the internal state to a content line.
   * 
   * @param     pw     PropertiesWriter to write to
   * @throws IOException    Could not write to stream
   */
  public void write( PropertiesWriter pw ) throws IOException {
    int mark = pw.writeKey( getKey() );
    pw.write( "= " );
    pw.writeWrapable( getValue(), mark + 2 );
    pw.newLine();
  }
  
  /**
   * Get the key of this entry.
   * 
   * @return  Current key.
   */
  public String getKey() {
    assert key!=null && key.length()>0 : "No valid key";
    return key;
  }
  
  /**
   * Set a new value for this Resource. Note that a key is not changeable.
   * 
   * @param value   New value to be set
   */
  public void setValue( String val ) {
    final String old = value;
    value = val;
    firePropertyChange( "value", old, val );
  }
  
  /**
   * Get the value that is currently set.
   * 
   * @return  Current value.
   */
  public String getValue() {
    return value;
  }
  
}

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

import java.util.Set;

import net.shredzone.jinn.property.PropertyLine;
import net.shredzone.jinn.property.PropertyModel;


/**
 * A PropertyKeyRefModel extends the PropertyModel by the ability to
 * compare the PropertyModel with a reference PropertyModel. There
 * are a few checks that can be made in order to compare this model
 * with the reference model.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertyKeyRefModel.java 106 2006-08-08 08:06:51Z shred $
 */
public class PropertyKeyRefModel extends PropertyKeyModel {
  protected final PropertyModel reference;
  private Set<String> sNewKeys = null;
  
  /**
   * Create a new PropertyKeyModel for a given PropertyModel and
   * a reference PropertyModel.
   * 
   * @param model   PropertyModel to create this model for
   * @param ref     Reference PropertyModel
   */
  public PropertyKeyRefModel( PropertyModel model, PropertyModel ref ) {
    super( model );
    this.reference = ref;
  }
  
  /**
   * Set a Set of keys that have been added by a merge process.
   * Actually you pass the result of the <code>PropertyModel.merge()</code>
   * method.
   * 
   * @param keys    Set of added keys
   */
  public void setAddedKeys( Set<String> keys ) {
    this.sNewKeys = keys;
  }

  /**
   * Find the next untranslated key. The search is started from the given
   * key (exclusive). If key is set to null, the search will start at the
   * top. If no next untranslated key was found, null will be returned.
   * 
   * @param   key   Starting key, null for first line
   * @return  Next untranslated key, null for no next key.
   */
  public String findNext( String key ) {
    // Compute the starting index
    int ix = 0;
    if (key != null) {
      ix = lKeys.indexOf( key ) + 1;
    }
    
    // Find the next untranslated key
    final int cnt = lKeys.size();
    while (ix < cnt) {
      final String check = lKeys.get( ix );
      if (isNew( check ) || !isChanged( check )) {
        return check;
      }
      
      ix++;
    }
    
    // Nothing was found
    return null;
  }
  
  /**
   * Check if a key's resource value is empty.
   * <p>
   * This check is true if the key exists, but contains an empty
   * string as value.
   * 
   * @param key     Key to check
   * @return  true: key exists, but value is an empty string
   */
  public boolean isEmpty( String key ) {
    final PropertyLine src = model.getPropertyLine( key );
    if (src == null) return false;
    return (src.getValue().length() == 0);
  }
  
  /**
   * Check if a key's resource has been changed compared to the reference
   * model.
   * <p>
   * This check is true if the key exists in both this model and the
   * reference model, but the values are not equal. This means that the
   * resource was already translated.
   * 
   * @param key     Key to check
   * @return  true: key exists, and values are not equal
   */
  public boolean isChanged( String key ) {
    final PropertyLine src = model.getPropertyLine( key );
    final PropertyLine ref = reference.getPropertyLine( key );
    
    if (src == null || ref == null) return false;
    return (! src.getValue().equals( ref.getValue() ));
  }
  
  /**
   * Check if a key's resource is new.
   * <p>
   * This check is true if a set of added keys was passed in (using
   * <code>setAddedKeys()</code>), and the set contains the key. This
   * means that the key was added since last time the reference model
   * was merged.
   * 
   * @param key     Key to check
   * @return  true: the key is new, according to the set of added keys.
   */
  public boolean isNew( String key ) {
    if (sNewKeys == null) return false;
    return (sNewKeys.contains( key ));
  }

  /**
   * Check if a key is surplus regarding the reference model.
   * <p>
   * This check is true if a key is available in this model, but not
   * in the reference model. This means that the key was probably
   * removed since last time the reference model was merged. Anyhow
   * the merging process should have removed those keys from this
   * model already, so usually this test should always result false.
   * 
   * @param key     Key to check
   * @return  true: the key is surplus
   */
  public boolean isSurplus( String key ) {
    final PropertyLine src = model.getPropertyLine( key );
    final PropertyLine ref = reference.getPropertyLine( key );
    return (src != null && ref == null);
  }
  
}

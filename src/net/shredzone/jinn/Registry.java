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
 
package net.shredzone.jinn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;


/**
 * Registry is a central registry for all kind of common variables.
 * This is the glue that keeps the other classes together. There must be
 * only one for each running Jinn application.
 * <p>
 * Basically it is just a Map with a possibility to listen to changes
 * of certain keys.
 * <p>
 * This class is threadsafe.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: Registry.java 69 2006-02-02 13:12:00Z shred $
 */
public class Registry {
  private final Map mData = new HashMap();
  private final PropertyChangeSupport support = new PropertyChangeSupport( this );
  
  /**
   * Set a registry key to a new value. The appropriate
   * PropertyChangeListeners will be notified. The new value may be null.
   * <p>
   * If val is equal to the current value, or if both are null, nothing will
   * happen.
   * 
   * @param key   Registry key to change
   * @param val   New value, may be null.
   */
  public void put( String key, Object val ) {
    if (key == null)
      throw new IllegalArgumentException( "key must not be null" );
    
    synchronized( this ) {
      final Object old = mData.get( key );
      
      if (old==null && val==null) return;         // Both are null
      if (old!=null && old.equals(val)) return;   // Unchanged
      
      mData.put( key, val );
      support.firePropertyChange( key, old, val );
    }
  }
  
  /**
   * Get the value for a registry key. If the value was not previously
   * set, null will be returned. Note that null will also be returned if
   * the value was set to null before. This means that there is no way
   * to find out if a key was previously set.
   * 
   * @param key   Registry key to read
   * @return  Current value, may be null.
   */
  public Object get( String key ) {
    if (key == null)
      throw new IllegalArgumentException( "key must not be null" );

    synchronized( this ) {
      return mData.get( key );
    }
  }
 
  /**
   * Get a registry value, as key. This is just a convenience method.
   * The value's <code>toString()</code> result will be returned.
   * 
   * @param key   Registry key to read
   * @return  Current value as String, may be null.
   */
  public String getString( String key ) {
    final Object val = get( key );
    return (val != null ? val.toString() : null);
  }

  /**
   * Add a PropertyChangeListener. It will be notified for all key
   * changes.
   * 
   * @param l   PropertyChangeListener to add
   */
  public void addPropertyChangeListener( PropertyChangeListener l ) {
    support.addPropertyChangeListener( l );
  }
  
  /**
   * Add a PropertyChangeListener. It will be notified for changes to
   * the given key only.
   * 
   * @param key   Key to listen for
   * @param l     PropertyChangeListener to add
   */
  public void addPropertyChangeListener( String key, PropertyChangeListener l ) {
    if (key == null)
      throw new IllegalArgumentException( "key must not be null" );

    support.addPropertyChangeListener( key, l );
  }
  
  /**
   * Remove a PropertyChangeListener for all keys. If it was not added,
   * nothing will happen.
   * 
   * @param l     PropertyChangeListener to be removed
   */
  public void removePropertyChangeListener( PropertyChangeListener l ) {
    support.removePropertyChangeListener( l );
  }
  
  /**
   * Remove a PropertyChangeListener for the given key. If it was not added,
   * nothing will happen.
   * 
   * @param key   Key the listener was bound to
   * @param l     PropertyChangeListener to be removed
   */
  public void removePropertyChangeListener( String key, PropertyChangeListener l ) {
    if (key == null)
      throw new IllegalArgumentException( "key must not be null" );

    support.removePropertyChangeListener( key, l );
  }
  
}

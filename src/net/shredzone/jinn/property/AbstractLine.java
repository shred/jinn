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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This is an abstract implementation of the Line interface.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: AbstractLine.java 68 2006-02-02 12:51:43Z shred $
 */
public abstract class AbstractLine implements Line {
  private PropertyChangeSupport support = new PropertyChangeSupport( this );
  
  /**
   * Clone a Line. The clone returned is independent from the
   * original. The default implementation will only create a shallow
   * copy, which is usually sufficient.
   * 
   * @return    a cloned Line
   */
  public Object clone() {
    try {
      final AbstractLine cl = (AbstractLine) super.clone();
      cl.support = new PropertyChangeSupport( cl );
      return cl;
    }catch (CloneNotSupportedException ex) {
      throw new InternalError( ex.toString() );
    }
  }
  
  /**
   * Add a PropertyChangeListener which is informed when the content of
   * this Line was changed.
   * 
   * @param l   PropertyChangeListener to add
   */
  public void addPropertyChangeListener( PropertyChangeListener l ) {
    support.addPropertyChangeListener( l );
  }

  /**
   * Remove a PropertyChangeListener. If it was not added, nothing will
   * happen.
   * 
   * @param l   PropertyChangeListener to remove
   */
  public void removePropertyChangeListener( PropertyChangeListener l ) {
    support.removePropertyChangeListener( l );
  }
  
  /**
   * Fire a PropertyChangeEvent.
   * 
   * @param key   Key that was changed
   * @param old   Old value
   * @param val   New value
   */
  protected void firePropertyChange( String key, String old, String val ) {
    if (old == null || val == null || !old.equals(val) ) {
      support.firePropertyChange( key, old, val );
    }
  }
  
}

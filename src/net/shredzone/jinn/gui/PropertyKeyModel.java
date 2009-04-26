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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.shredzone.jinn.property.Line;
import net.shredzone.jinn.property.PropertyLine;
import net.shredzone.jinn.property.PropertyModel;


/**
 * A PropertyKeyModel is a view for the keys of a PropertyModel.
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertyKeyModel.java 285 2009-04-26 22:42:14Z shred $
 */
public class PropertyKeyModel implements ListModel {
  protected final PropertyModel model;
  protected final List<String> lKeys = new ArrayList<String>();
  private final ListDataListener listener = new MyListDataListener();
  private final Set<WeakReference<ListDataListener>> sListener
      = new HashSet<WeakReference<ListDataListener>>();
  
  /**
   * Create a new PropertyKeyModel for a given PropertyModel.
   * 
   * @param model   PropertyModel to create this model for
   */
  public PropertyKeyModel( PropertyModel model ) {
    this.model = model;
    model.addListDataListener( listener );
    updateKeys( null );
  }

  /**
   * Get the size of this model, i.e. the number of elements.
   * 
   * @return    Size of this model
   */
  public int getSize() {
    return lKeys.size();
  }
  
  /**
   * Get an element of this model. This is only for the ListModel.
   * Use {@link #getKeyAt(int)} instead.
   * 
   * @param  index      Index to read from
   * @return  String containing the Key at this index
   */
  public Object getElementAt( int index ) {
    return getKeyAt( index );
  }
  
  /**
   * Get an element of this model.
   * 
   * @param  index      Index to read from
   * @return  String containing the Key at this index
   */
  public String getKeyAt( int index ) {
    return lKeys.get( index );
  }
  
  /**
   * Check if a key is known to this model.
   * 
   * @param  key    Key to be checked
   * @return true: key is known, false: key is unknown.
   */
  public boolean hasKey( String key ) {
    return lKeys.contains( key );
  }
  
  /**
   * Find a key and return its position.
   * 
   * @param key     Key to be found
   * @return  Index of this key, or -1 if the key was not found
   */
  public int findKey( String key ) {
    return lKeys.indexOf( key );
  }

  /**
   * Add a ListDataListener which is notified when this model changes.
   * 
   * @param  l    ListDataListener to be added.
   */
  public void addListDataListener( ListDataListener l ) {
    //--- Check if the Listener is already added ---
    for( WeakReference<ListDataListener> wr : sListener ) {
      if (wr.get() == l) {
        return;
      }
    }
    
    //--- Add the Listener ---
    sListener.add( new WeakReference<ListDataListener>( l ) );
  }

  /**
   * Remove a ListDataListener. If it was not added, nothing will happen.
   * 
   * @param  l    ListDataListener to be removed.
   */
  public void removeListDataListener( ListDataListener l ) {
    for (
        Iterator<WeakReference<ListDataListener>> it = sListener.iterator();
        it.hasNext();
        ) {
      WeakReference<ListDataListener> wr = it.next();
      if (wr.get() == l) {
        it.remove();
        break;
      }
    }
  }
  
  /**
   * Update all keys of the internal key list.
   * 
   * @param e     ListDataEvent of the PropertyModel event. null will
   *    always update the entire list.
   */
  protected void updateKeys( ListDataEvent e ) {
    /*TODO: evaluate the ListDataEvent and only replace the parts
     * of the lKeys list that have been changed. Also, notify the
     * ListDataListeners about the real changes only.
     */
    
    //--- Clear current key list ---
    lKeys.clear();
    
    //--- Fetch all PropertyLines ---
    for ( Line line : model.getLines() ) {
      if (line instanceof PropertyLine) {
        lKeys.add( ((PropertyLine) line).getKey() );
      }
    }
    
    //--- Notify that everything changed ---
    final ListDataEvent evt = new ListDataEvent(
        this,
        ListDataEvent.CONTENTS_CHANGED,
        0,
        lKeys.size()-1
    );
    for (
        Iterator<WeakReference<ListDataListener>> it = sListener.iterator();
        it.hasNext();
        ) {
      ListDataListener l = it.next().get();
      if (l != null) {
        l.contentsChanged( evt );
      }else {
        it.remove();
      }
    }
  }

  
/* ------------------------------------------------------------------------ */
  
  private class MyListDataListener implements ListDataListener {
    public void intervalAdded( ListDataEvent e ) {
      updateKeys( e );
    }
    
    public void intervalRemoved( ListDataEvent e ) {
      updateKeys( e );
    }
    
    public void contentsChanged( ListDataEvent e ) {
      // We assume that:
      //  1) a key name never changes once it is added
      //  2) the sequence of keys in lKeys is always identical to
      //     the sequence of keys in the model.
      
      int firstIx = e.getIndex0();
      int lastIx  = e.getIndex1();

      while (firstIx < lastIx) {
        Line l = model.getLineAt( firstIx );
        if (l != null && l instanceof PropertyLine)
          break;
        firstIx++;
      }
      
      while (firstIx < lastIx) {
        Line l = model.getLineAt( lastIx );
        if (l != null && l instanceof PropertyLine)
          break;
        lastIx--;
      }
      
      int start = lKeys.indexOf( model.getLineAt( firstIx ) );
      int end   = lKeys.indexOf( model.getLineAt( lastIx ) );
      if (start < 0) start = 0;
      if (end < 0)   end   = lKeys.size()-1;
      
      final ListDataEvent evt = new ListDataEvent(
          PropertyKeyModel.this,
          ListDataEvent.CONTENTS_CHANGED,
          start,
          end
      );
      
      for (
          Iterator<WeakReference<ListDataListener>> it = sListener.iterator();
          it.hasNext();
          ) {
        ListDataListener l = it.next().get();
        if (l != null) {
          l.contentsChanged( evt );
        }else {
          it.remove();
        }
      }
    }
    
  }
  
  
}

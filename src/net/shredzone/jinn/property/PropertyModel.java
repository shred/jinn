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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A PropertyModel contains a parsed properties file.
 * <p>
 * The file content itself is stored in a List, with one Line object entry
 * for each logical line of the file (i.e. continued lines are treatened as
 * a single line).
 * <p>
 * There is also a Map available which gives access to the PropertyLine
 * entries of the list, by the resource key as Map key.
 * <p>
 * A merge method is available which merges a reference PropertyModel into
 * this model.
 * <p>
 * This is a ListModel, so it can be immediately used in JList etc.
 * <p>
 * All methods are <em>not</em> synchronized!
 *
 * @author  Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertyModel.java,v 1.7 2005/12/21 14:05:43 shred Exp $
 */
public class PropertyModel implements ListModel {
  private final PropertyChangeListener listener = new MyPropertyChangeListener();
  private final List lContent   = new ArrayList();
  private final Map  mResources = new HashMap();
  private final Set  sListener  = new HashSet();
 
  /**
   * Create an empty Resource.
   */
  public PropertyModel() {
  }
  
  /**
   * Clear the current model. You will get a shiny new model object,
   * as if it was freshly constructed.
   */
  public void clear() {
    final int cnt = lContent.size();
    lContent.clear();
    mResources.clear();
    fireDataRemoved( 0, cnt-1 );
  }
 
  /**
   * Fill a model by reading a .properties file from an InputStream.
   * The model is cleared before.
   * 
   * @param  in     InputStream to read the .properties file from.
   * @throws IOException  if it could not read.
   */
  public void read( InputStream in ) throws IOException {
    read( new PropertiesReader( in ) );
  }
  
  /**
   * Fill a model by reading a .properties file from a PropertiesReader.
   * The model is cleared before.
   * 
   * @param  in     PropertiesReader to read the .properties file from.
   * @throws IOException  if it could not read.
   */
  public void read( PropertiesReader in ) throws IOException {
    clear();
    Line readLine;
    while ((readLine = in.readLine()) != null) {
      addLine( readLine );
    }
  }

  /**
   * Write the current content of this model to a valid .properties
   * file by using an OutputStream. The OutputStream will be flushed,
   * but not closed.
   * 
   * @param out         OutputStream to write the properties file to
   * @throws IOException  if it could not write.
   */
  public void write( OutputStream out ) throws IOException {
    write( new PropertiesWriter( out ) );
  }
  
  /**
   * Write the current content of this model to a valid .properties
   * file by using a PropertiesWriter. The PropertiesWriter will be flushed,
   * but not closed.
   * 
   * @param out         PropertiesWriter to write the properties file to
   * @throws IOException  if it could not write.
   */
  public void write( PropertiesWriter out ) throws IOException {
    final Iterator it = lContent.iterator();
    while (it.hasNext()) {
      final Line line = (Line) it.next();
      out.writeLine( line );
    }
  }

  /**
   * Get a List of all lines contained in this model. This list is
   * unmodifiable, but the Line objects contained in this list can be
   * modified.
   * 
   * @return  List containing all Line objects of this model, in the
   *    original sequence they were loaded from the .properties file.
   */
  public List getLines() {
    return Collections.unmodifiableList( lContent );
  }
  
  /**
   * Get a Map of all PropertyLine contained in this Resource. The Map's
   * key will be the name of the resource, the Map's value a reference to
   * the appropriate PropertyLine object. The Map is unmodifiable.
   * 
   * @return  Map containing all PropertyLine objects.
   */
  public Map getResourceMap() {
    return Collections.unmodifiableMap( mResources );
  }

  /**
   * Get the PropertyLine for the given resource key.
   * 
   * @param key     Resource key to fetch
   * @return PropertyLine or null if the key is not known.
   */
  public PropertyLine getPropertyLine( String key ) {
    return (PropertyLine) mResources.get( key );
  }
  
  /**
   * Add a Line to this model. The line is added to the end of the file.
   * 
   * @param line    Line to be added
   */
  protected void addLine( Line line ) {
    //--- Add to the Model ---
    final int index = lContent.size();
    lContent.add( line );
    
    //--- Remember Key ---
    if (line instanceof PropertyLine) {
      final String key = ((PropertyLine) line).getKey();
      mResources.put( key, line );
    }

    //--- Register a Listener ---
    line.addPropertyChangeListener( listener );
    
    //--- Notify about Change ---
    fireDataAdded( index, index );
  }

  /**
   * Remove a Line from this model. If the line was not added, nothing
   * will happen.
   * 
   * @param line    Line to be removed
   */
  protected void removeLine( Line line ) {
    //--- Remove our Listener ---
    line.removePropertyChangeListener( listener );
    
    //--- Remove the Key ---
    if (line instanceof PropertyLine) {
      final String key = ((PropertyLine) line).getKey();
      mResources.remove( key );
    }
    //--- Remove from Model ---
    final int index = lContent.indexOf( line );
    lContent.remove( index );
    
    //--- Notify about Change ---
    fireDataRemoved( index, index );
  }
  
  /**
   * Insert a Line into a certain index of this model.
   * 
   * @param index   Index position
   * @param line    Line to be added
   */
  protected void insertLine( int index, Line line ) {
    //--- Insert into Model ---
    lContent.add( index, line );
    
    //--- Remember Key ---
    if (line instanceof PropertyLine) {
      final String key = ((PropertyLine) line).getKey();
      mResources.put( key, line );
    }

    //--- Register a Listener ---
    line.addPropertyChangeListener( listener );
    
    //--- Notify about Change ---
    fireDataAdded( index, index );
  }
  
  /**
   * Merge a PropertyModel into this model.
   * <p>
   * Merging is a rather destructive process, since we're not dealing with
   * source code, but with translations. Basically, the current model will
   * be replaced with the reference model passed to this method. Anyhow
   * there are a few exceptions:
   * <ul>
   *   <li>The first block of comments will be kept unchanged, since it is
   *     assumed that this is an individual header (containing translation
   *     specific comments and revision tags). The first comment block ends
   *     when an empty line or property line is found.
   *   <li>If there is no initial comment in this model, it will be copied
   *     from the merged model, though.
   *   <li>For each property line of the merged model, if there is a property
   *     line with the same key in the current model, the value will be kept.
   *     This is because all translated properties shall be kept.
   *   <li>A property line of the current model that does not have a matching
   *     counterpart in the merged model, will be removed.
   * </ul>
   * <p>
   * Future implementations of this method may behave a little smarter and
   * more non-destructive than that.
   *  
   * @param r   PropertyModel to merge from.
   * @return  A Set of resource key names that have been added by this
   *   merge operation. The translator will have to translate these keys
   *   because they were freshly added.
   */
  public Set merge( PropertyModel r ) {
    // The following abbreviations mean:
    //   self  = this model, where data is merged into
    //   ref   = reference model, where data is merged from
    
    //--- Keep the old header ---
    List lHeader = new ArrayList();
    for (
        Iterator it = lContent.iterator();
        it.hasNext();
        ) {
      final Line line = (Line) it.next();
      if (line instanceof CommentLine) {
        lHeader.add( line );
      }else {
        break;
      }
    }
    
    //--- Keep the map of translated properties ---
    final Map mOldMap = new HashMap( mResources );
    
    //--- Clean up this model ---
    clear();
    
    //--- Fill with reference model ---
    final int refSize = r.lContent.size();
    int refIx = 0;
    
    //--- First add a header ---
    if (lHeader.size()>0) {
      // Add our own header
      for (
          Iterator it = lHeader.iterator();
          it.hasNext();
          ) {
        addLine( (Line) it.next() );
      }
      
      // Skip the reference's initial header
      while (refIx < refSize && r.lContent.get(refIx) instanceof CommentLine) {
        refIx++;
      }
      
    }else {
      // Add the reference header
      
      while (refIx < refSize && r.lContent.get(refIx) instanceof CommentLine) {
        final Line refLine = (Line) r.lContent.get(refIx);
        final Line refLineClone = (Line) refLine.clone();
        addLine( refLineClone );
        refIx++;
      }
    }
    
    lHeader = null;       // We don't need it any longer
    
    //--- Now add the rest ---
    final Set sNewKeys = new HashSet();
    while (refIx < refSize) {
      final Line refLine = (Line) r.lContent.get(refIx++);
      final Line refLineClone = (Line) refLine.clone();
      
      if (refLineClone instanceof PropertyLine) {
        // A property line: fill it with the translation we already have
        
        final PropertyLine refPropClone = (PropertyLine) refLineClone;
        final String refPropKey = refPropClone.getKey();
        
        if (mOldMap.containsKey( refPropKey )) {
          // Key is known, change the value.
          final PropertyLine selfProp = (PropertyLine) mOldMap.get( refPropKey );
          refPropClone.setValue( selfProp.getValue() );
        }else {
          // Key is not known! Remember that it was copied unchanged.
          sNewKeys.add( refPropClone.getKey() );
        }

      }

      addLine( refLineClone );
    }

    //--- Return the set of new Keys ---
    return Collections.unmodifiableSet( sNewKeys );
  }

  
  /**
   * Get the size of the list, which is equal
   * to the number of Line objects in this model.
   * 
   * @return   Number of Lines
   */
  public int getSize() {
    return lContent.size();
  }

  /**
   * Get the Line element of a certain index. The returned object is
   * guaranteed to be an instance of Line. The Object type is just required
   * to satisfy the ListModel interface.
   * 
   * @param    index        Line number
   * @return   Line object of that line
   */
  public Object getElementAt( int index ) {
    return lContent.get( index );
  }

  /**
   * Add a ListDataListener which is notified if the content of this
   * PropertyModel changed.
   * 
   * @param  l          ListDataListener to be added
   */
  public void addListDataListener( ListDataListener l ) {
    //--- Check if the Listener is already added ---
    for (
        Iterator it = sListener.iterator();
        it.hasNext();
        ) {
      final WeakReference wr = (WeakReference) it.next();
      if (wr.get() == l) {
        return;
      }
    }
    
    //--- Add the Listener ---
    sListener.add( new WeakReference( l ) );
  }

  /**
   * Remove a ListDataListener. If it was not added, nothing will happen.
   * 
   * @param  l          ListDataListener to be removed
   */
  public void removeListDataListener( ListDataListener l ) {
    for (
        Iterator it = sListener.iterator();
        it.hasNext();
        ) {
      final WeakReference wr = (WeakReference) it.next();
      if (wr.get() == l) {
        it.remove();
        break;
      }
    }
  }
  
  /**
   * Notify that Lines within a range were changed.
   * 
   * @param start   Start line number
   * @param end     End line number
   */
  protected void fireDataChanged( int start, int end ) {
    final ListDataEvent e = new ListDataEvent(
        this,
        ListDataEvent.CONTENTS_CHANGED,
        start, end
    );

    for (
        Iterator it = sListener.iterator();
        it.hasNext();
        ) {
      final WeakReference wr = (WeakReference) it.next();
      final ListDataListener l = (ListDataListener) wr.get();
      if (l != null) {
        l.contentsChanged( e );
      }else {
        it.remove();
      }
    }
  }

  /**
   * Notify that Lines within a range were added or inserted.
   * 
   * @param start   Start line number
   * @param end     End line number
   */
  protected void fireDataAdded( int start, int end ) {
    final ListDataEvent e = new ListDataEvent(
        this,
        ListDataEvent.INTERVAL_ADDED,
        start, end
    );

    for( Iterator it = sListener.iterator(); it.hasNext(); ) {
      final WeakReference wr = (WeakReference) it.next();
      final ListDataListener l = (ListDataListener) wr.get();
      if (l != null) {
        l.intervalAdded( e );
      }else {
        it.remove();
      }
    }
  }
  
  /**
   * Notify that Lines within a range were removed.
   * 
   * @param start   Start line number
   * @param end     End line number
   */
  protected void fireDataRemoved( int start, int end ) {
    final ListDataEvent e = new ListDataEvent(
        this,
        ListDataEvent.INTERVAL_REMOVED,
        start, end
    );

    for( Iterator it = sListener.iterator(); it.hasNext(); ) {
      final WeakReference wr = (WeakReference) it.next();
      final ListDataListener l = (ListDataListener) wr.get();
      if (l != null) {
        l.intervalRemoved( e );
      }else {
        it.remove();
      }
    }
  }
  
  
/* ------------------------------------------------------------------------ */  
  
  /**
   * This PropertyChangeListener is registered with every Line that is
   * added to this model, so this model is notified about every change
   * in the contents of the Lines.
   */
  private class MyPropertyChangeListener implements PropertyChangeListener {

    /**
     * A Line changed its content. Notify our Listeners about that
     * change.
     * 
     * @param  evt      PropertyChangeEvent
     */
    public void propertyChange( PropertyChangeEvent evt ) {
      //--- Find out the Line Index ---
      // The Source of the event is the Line object that was changed.
      // We need to find out its index.
      final int index = lContent.indexOf( evt.getSource() );
      if (index >= 0) {
        fireDataChanged( index, index );
      }else {
        // The Line was not added to this Model?! Do nothing...
      }
    }
    
  }

}

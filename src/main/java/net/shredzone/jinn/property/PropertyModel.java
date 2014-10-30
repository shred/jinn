/**
 * jinn - A property translation editor
 *
 * Copyright (C) 2009 Richard "Shred" Körber
 *   http://jinn.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * The file content itself is stored in a List, with one Line object entry for each
 * logical line of the file (i.e. continued lines are treatened as a single line).
 * <p>
 * There is also a Map available which gives access to the PropertyLine entries of the
 * list, by the resource key as Map key.
 * <p>
 * A merge method is available which merges a reference PropertyModel into this model.
 * <p>
 * This is a ListModel, so it can be immediately used in JList etc.
 * <p>
 * All methods are <em>not</em> synchronized!
 *
 * @author Richard "Shred" Körber
 */
public class PropertyModel implements ListModel {
    private final PropertyChangeListener listener = new MyPropertyChangeListener();
    private final List<Line> lContent = new ArrayList<Line>();
    private final Map<String, PropertyLine> mResources = new HashMap<String, PropertyLine>();
    private final Set<WeakReference<ListDataListener>> sListener
            = new HashSet<WeakReference<ListDataListener>>();

    /**
     * Create an empty Resource.
     */
    public PropertyModel() {}

    /**
     * Clear the current model. You will get a shiny new model object, as if it was
     * freshly constructed.
     */
    public void clear() {
        int cnt = lContent.size();
        lContent.clear();
        mResources.clear();
        fireDataRemoved(0, cnt - 1);
    }

    /**
     * Fill a model by reading a .properties file from an InputStream. The model is
     * cleared before.
     *
     * @param in
     *            InputStream to read the .properties file from.
     * @throws IOException
     *             if it could not read.
     */
    public void read(InputStream in) throws IOException {
        read(new PropertiesReader(in));
    }

    /**
     * Fill a model by reading a .properties file from a PropertiesReader. The model is
     * cleared before.
     *
     * @param in
     *            PropertiesReader to read the .properties file from.
     * @throws IOException
     *             if it could not read.
     */
    public void read(PropertiesReader in) throws IOException {
        clear();
        Line readLine;
        while ((readLine = in.readLine()) != null) {
            addLine(readLine);
        }
    }

    /**
     * Write the current content of this model to a valid .properties file by using an
     * OutputStream. The OutputStream will be flushed, but not closed.
     *
     * @param out
     *            OutputStream to write the properties file to
     * @throws IOException
     *             if it could not write.
     */
    public void write(OutputStream out) throws IOException {
        write(new PropertiesWriter(out));
    }

    /**
     * Write the current content of this model to a valid .properties file by using a
     * PropertiesWriter. The PropertiesWriter will be flushed, but not closed.
     *
     * @param out
     *            PropertiesWriter to write the properties file to
     * @throws IOException
     *             if it could not write.
     */
    public void write(PropertiesWriter out) throws IOException {
        for (Line line : lContent) {
            out.writeLine(line);
        }
    }

    /**
     * Get a List of all lines contained in this model. This list is unmodifiable, but the
     * Line objects contained in this list can be modified.
     *
     * @return List containing all Line objects of this model, in the original sequence
     *         they were loaded from the .properties file.
     */
    public List<Line> getLines() {
        return Collections.unmodifiableList(lContent);
    }

    /**
     * Get a Map of all PropertyLine contained in this Resource. The Map's key will be the
     * name of the resource, the Map's value a reference to the appropriate PropertyLine
     * object. The Map is unmodifiable.
     *
     * @return Map containing all PropertyLine objects.
     */
    public Map<String, PropertyLine> getResourceMap() {
        return Collections.unmodifiableMap(mResources);
    }

    /**
     * Get the PropertyLine for the given resource key.
     *
     * @param key
     *            Resource key to fetch
     * @return PropertyLine or null if the key is not known.
     */
    public PropertyLine getPropertyLine(String key) {
        return mResources.get(key);
    }

    /**
     * Add a Line to this model. The line is added to the end of the file.
     *
     * @param line
     *            Line to be added
     */
    protected void addLine(Line line) {
        // --- Add to the Model ---
        final int index = lContent.size();
        lContent.add(line);

        // --- Remember Key ---
        if (line instanceof PropertyLine) {
            final PropertyLine prop = (PropertyLine) line;
            mResources.put(prop.getKey(), prop);
        }

        // --- Register a Listener ---
        line.addPropertyChangeListener(listener);

        // --- Notify about Change ---
        fireDataAdded(index, index);
    }

    /**
     * Remove a Line from this model. If the line was not added, nothing will happen.
     *
     * @param line
     *            Line to be removed
     */
    protected void removeLine(Line line) {
        // --- Remove our Listener ---
        line.removePropertyChangeListener(listener);

        // --- Remove the Key ---
        if (line instanceof PropertyLine) {
            final String key = ((PropertyLine) line).getKey();
            mResources.remove(key);
        }
        // --- Remove from Model ---
        final int index = lContent.indexOf(line);
        lContent.remove(index);

        // --- Notify about Change ---
        fireDataRemoved(index, index);
    }

    /**
     * Insert a Line into a certain index of this model.
     *
     * @param index
     *            Index position
     * @param line
     *            Line to be added
     */
    protected void insertLine(int index, Line line) {
        // --- Insert into Model ---
        lContent.add(index, line);

        // --- Remember Key ---
        if (line instanceof PropertyLine) {
            final PropertyLine prop = (PropertyLine) line;
            mResources.put(prop.getKey(), prop);
        }

        // --- Register a Listener ---
        line.addPropertyChangeListener(listener);

        // --- Notify about Change ---
        fireDataAdded(index, index);
    }

    /**
     * Merge a PropertyModel into this model.
     * <p>
     * Merging is a rather destructive process, since we're not dealing with source code,
     * but with translations. Basically, the current model will be replaced with the
     * reference model passed to this method. Anyhow there are a few exceptions:
     * <ul>
     * <li>The first block of comments will be kept unchanged, since it is assumed that
     * this is an individual header (containing translation specific comments and revision
     * tags). The first comment block ends when an empty line or property line is found.
     * <li>If there is no initial comment in this model, it will be copied from the merged
     * model, though.
     * <li>For each property line of the merged model, if there is a property line with
     * the same key in the current model, the value will be kept. This is because all
     * translated properties shall be kept.
     * <li>A property line of the current model that does not have a matching counterpart
     * in the merged model, will be removed.
     * </ul>
     * <p>
     * Future implementations of this method may behave a little smarter and more
     * non-destructive than that.
     *
     * @param r
     *            PropertyModel to merge from.
     * @return A Set of resource key names that have been added by this merge operation.
     *         The translator will have to translate these keys because they were freshly
     *         added.
     */
    public Set<String> merge(PropertyModel r) {
        // The following abbreviations mean:
        // self = this model, where data is merged into
        // ref = reference model, where data is merged from

        // --- Keep the old header ---
        List<CommentLine> lHeader = new ArrayList<CommentLine>();
        for (Line line : lContent) {
            if (line instanceof CommentLine) {
                lHeader.add((CommentLine) line);
            } else {
                break;
            }
        }

        // --- Keep the map of translated properties ---
        final Map<String, PropertyLine> mOldMap = new HashMap<String, PropertyLine>(mResources);

        // --- Clean up this model ---
        clear();

        // --- Fill with reference model ---
        final int refSize = r.lContent.size();
        int refIx = 0;

        // --- First add a header ---
        if (lHeader.size() > 0) {
            // Add our own header
            for (Line line : lHeader) {
                addLine(line);
            }

            // Skip the reference's initial header
            while (refIx < refSize && r.lContent.get(refIx) instanceof CommentLine) {
                refIx++;
            }

        } else {
            // Add the reference header

            while (refIx < refSize && r.lContent.get(refIx) instanceof CommentLine) {
                final Line refLine = r.lContent.get(refIx);
                final Line refLineClone = (Line) refLine.clone();
                addLine(refLineClone);
                refIx++;
            }
        }

        lHeader = null; // We don't need it any longer

        // --- Now add the rest ---
        final Set<String> sNewKeys = new HashSet<String>();
        while (refIx < refSize) {
            final Line refLine = r.lContent.get(refIx++);
            final Line refLineClone = (Line) refLine.clone();

            if (refLineClone instanceof PropertyLine) {
                // A property line: fill it with the translation we already have

                final PropertyLine refPropClone = (PropertyLine) refLineClone;
                final String refPropKey = refPropClone.getKey();

                if (mOldMap.containsKey(refPropKey)) {
                    // Key is known, change the value.
                    final PropertyLine selfProp = mOldMap.get(refPropKey);
                    refPropClone.setValue(selfProp.getValue());
                } else {
                    // Key is not known! Remember that it was copied unchanged.
                    sNewKeys.add(refPropClone.getKey());
                }

            }

            addLine(refLineClone);
        }

        // --- Return the set of new Keys ---
        return Collections.unmodifiableSet(sNewKeys);
    }

    /**
     * Get the size of the list, which is equal to the number of Line objects in this
     * model.
     *
     * @return Number of Lines
     */
    @Override
    public int getSize() {
        return lContent.size();
    }

    /**
     * Get the Line element of a certain index. This method satisfies the ListModel
     * interface. Use {@link #getLineAt(int)} instead.
     *
     * @param index
     *            Line number
     * @return Line object of that line
     */
    @Override
    public Object getElementAt(int index) {
        return getLineAt(index);
    }

    /**
     * Get the Line element of a certain index. The returned object is guaranteed to be an
     * instance of Line. The Object type is just required to satisfy the ListModel
     * interface.
     *
     * @param index
     *            Line number
     * @return Line object of that line
     */
    public Line getLineAt(int index) {
        return lContent.get(index);
    }

    /**
     * Add a ListDataListener which is notified if the content of this PropertyModel
     * changed.
     *
     * @param l
     *            ListDataListener to be added
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        // --- Check if the Listener is already added ---
        for (WeakReference<ListDataListener> wr : sListener) {
            if (wr.get() == l) {
                return;
            }
        }

        // --- Add the Listener ---
        sListener.add(new WeakReference<ListDataListener>(l));
    }

    /**
     * Remove a ListDataListener. If it was not added, nothing will happen.
     *
     * @param l
     *            ListDataListener to be removed
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
            final WeakReference<ListDataListener> wr = it.next();
            if (wr.get() == l) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Notify that Lines within a range were changed.
     *
     * @param start
     *            Start line number
     * @param end
     *            End line number
     */
    protected void fireDataChanged(int start, int end) {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, start, end);

        for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
            final ListDataListener l = it.next().get();
            if (l != null) {
                l.contentsChanged(e);
            } else {
                it.remove();
            }
        }
    }

    /**
     * Notify that Lines within a range were added or inserted.
     *
     * @param start
     *            Start line number
     * @param end
     *            End line number
     */
    protected void fireDataAdded(int start, int end) {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, start, end);

        for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
            final ListDataListener l = it.next().get();
            if (l != null) {
                l.intervalAdded(e);
            } else {
                it.remove();
            }
        }
    }

    /**
     * Notify that Lines within a range were removed.
     *
     * @param start
     *            Start line number
     * @param end
     *            End line number
     */
    protected void fireDataRemoved(int start, int end) {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, start, end);

        for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
            final ListDataListener l = it.next().get();
            if (l != null) {
                l.intervalRemoved(e);
            } else {
                it.remove();
            }
        }
    }

    /* ------------------------------------------------------------------------ */

    /**
     * This PropertyChangeListener is registered with every Line that is added to this
     * model, so this model is notified about every change in the contents of the Lines.
     */
    private class MyPropertyChangeListener implements PropertyChangeListener {

        /**
         * A Line changed its content. Notify our Listeners about that change.
         *
         * @param evt
         *            PropertyChangeEvent
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // --- Find out the Line Index ---
            // The Source of the event is the Line object that was changed.
            // We need to find out its index.
            final int index = lContent.indexOf(evt.getSource());
            if (index >= 0) {
                fireDataChanged(index, index);
            } else {
                // The Line was not added to this Model?! Do nothing...
            }
        }

    }

}

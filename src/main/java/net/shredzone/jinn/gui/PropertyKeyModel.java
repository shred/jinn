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
 * @author Richard "Shred" Körber
 */
public class PropertyKeyModel implements ListModel {
    protected final PropertyModel model;
    protected final List<String> lKeys = new ArrayList<String>();
    private final ListDataListener listener = new MyListDataListener();
    private final Set<WeakReference<ListDataListener>> sListener = new HashSet<WeakReference<ListDataListener>>();

    /**
     * Create a new PropertyKeyModel for a given PropertyModel.
     *
     * @param model
     *            PropertyModel to create this model for
     */
    public PropertyKeyModel(PropertyModel model) {
        this.model = model;
        model.addListDataListener(listener);
        updateKeys(null);
    }

    /**
     * Get the size of this model, i.e. the number of elements.
     *
     * @return Size of this model
     */
    @Override
    public int getSize() {
        return lKeys.size();
    }

    /**
     * Get an element of this model. This is only for the ListModel. Use
     * {@link #getKeyAt(int)} instead.
     *
     * @param index
     *            Index to read from
     * @return String containing the Key at this index
     */
    @Override
    public Object getElementAt(int index) {
        return getKeyAt(index);
    }

    /**
     * Get an element of this model.
     *
     * @param index
     *            Index to read from
     * @return String containing the Key at this index
     */
    public String getKeyAt(int index) {
        return lKeys.get(index);
    }

    /**
     * Check if a key is known to this model.
     *
     * @param key
     *            Key to be checked
     * @return true: key is known, false: key is unknown.
     */
    public boolean hasKey(String key) {
        return lKeys.contains(key);
    }

    /**
     * Find a key and return its position.
     *
     * @param key
     *            Key to be found
     * @return Index of this key, or -1 if the key was not found
     */
    public int findKey(String key) {
        return lKeys.indexOf(key);
    }

    /**
     * Add a ListDataListener which is notified when this model changes.
     *
     * @param l
     *            ListDataListener to be added.
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
     *            ListDataListener to be removed.
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
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
     * @param e
     *            ListDataEvent of the PropertyModel event. null will always update the
     *            entire list.
     */
    protected void updateKeys(ListDataEvent e) {
        /*
         * TODO: evaluate the ListDataEvent and only replace the parts of the lKeys list
         * that have been changed. Also, notify the ListDataListeners about the real
         * changes only.
         */

        // --- Clear current key list ---
        lKeys.clear();

        // --- Fetch all PropertyLines ---
        for (Line line : model.getLines()) {
            if (line instanceof PropertyLine) {
                lKeys.add(((PropertyLine) line).getKey());
            }
        }

        // --- Notify that everything changed ---
        final ListDataEvent evt = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, lKeys.size() - 1);
        for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
            ListDataListener l = it.next().get();
            if (l != null) {
                l.contentsChanged(evt);
            } else {
                it.remove();
            }
        }
    }

    private class MyListDataListener implements ListDataListener {
        @Override
        public void intervalAdded(ListDataEvent e) {
            updateKeys(e);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            updateKeys(e);
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            // We assume that:
            // 1) a key name never changes once it is added
            // 2) the sequence of keys in lKeys is always identical to
            // the sequence of keys in the model.

            int firstIx = e.getIndex0();
            int lastIx = e.getIndex1();

            while (firstIx < lastIx) {
                Line l = model.getLineAt(firstIx);
                if (l != null && l instanceof PropertyLine) break;
                firstIx++;
            }

            while (firstIx < lastIx) {
                Line l = model.getLineAt(lastIx);
                if (l != null && l instanceof PropertyLine) break;
                lastIx--;
            }

            int start = lKeys.indexOf(model.getLineAt(firstIx));
            int end = lKeys.indexOf(model.getLineAt(lastIx));
            if (start < 0) start = 0;
            if (end < 0) end = lKeys.size() - 1;

            final ListDataEvent evt = new ListDataEvent(PropertyKeyModel.this, ListDataEvent.CONTENTS_CHANGED, start, end);

            for (Iterator<WeakReference<ListDataListener>> it = sListener.iterator(); it.hasNext();) {
                ListDataListener l = it.next().get();
                if (l != null) {
                    l.contentsChanged(evt);
                } else {
                    it.remove();
                }
            }
        }

    }

}

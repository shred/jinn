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
package net.shredzone.jinn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry is a central registry for all kind of common variables. This is the glue that
 * keeps the other classes together. There must be only one for each running Jinn
 * application.
 * <p>
 * Basically it is just a Map with a possibility to listen to changes of certain keys.
 * <p>
 * This class is threadsafe.
 *
 * @author Richard "Shred" Körber
 */
public class Registry {
    private final Map<String, Object> mData = new HashMap<String, Object>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Set a registry key to a new value. The appropriate PropertyChangeListeners will be
     * notified. The new value may be null.
     * <p>
     * If val is equal to the current value, or if both are null, nothing will happen.
     *
     * @param key
     *            Registry key to change
     * @param val
     *            New value, may be null.
     */
    public void put(String key, Object val) {
        if (key == null) throw new IllegalArgumentException("key must not be null");

        synchronized (this) {
            Object old = mData.get(key);

            if (old == null && val == null) return; // Both are null
            if (old != null && old.equals(val)) return; // Unchanged

            mData.put(key, val);
            support.firePropertyChange(key, old, val);
        }
    }

    /**
     * Get the value for a registry key. If the value was not previously set, null will be
     * returned. Note that null will also be returned if the value was set to null before.
     * This means that there is no way to find out if a key was previously set.
     *
     * @param key
     *            Registry key to read
     * @return Current value, may be null.
     */
    public Object get(String key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");

        synchronized (this) {
            return mData.get(key);
        }
    }

    /**
     * Get a registry value, as key. This is just a convenience method. The value's
     * <code>toString()</code> result will be returned.
     *
     * @param key
     *            Registry key to read
     * @return Current value as String, may be null.
     */
    public String getString(String key) {
        Object val = get(key);
        return (val != null ? val.toString() : null);
    }

    /**
     * Checks a boolean state of a registry key. The key must contain a Boolean object,
     * otherwise a ClassCastException is thrown. If the key is not set, false will be
     * returned.
     *
     * @param key
     *            Registry key to read
     * @return State of that key.
     * @throws ClassCastException
     *             If the key is not a boolean attribute.
     */
    public boolean is(String key) {
        Boolean bool = (Boolean) get(key);
        return (bool != null && bool);
    }

    /**
     * Add a PropertyChangeListener. It will be notified for all key changes.
     *
     * @param l
     *            PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    /**
     * Add a PropertyChangeListener. It will be notified for changes to the given key
     * only.
     *
     * @param key
     *            Key to listen for
     * @param l
     *            PropertyChangeListener to add
     */
    public void addPropertyChangeListener(String key, PropertyChangeListener l) {
        if (key == null) throw new IllegalArgumentException("key must not be null");

        support.addPropertyChangeListener(key, l);
    }

    /**
     * Remove a PropertyChangeListener for all keys. If it was not added, nothing will
     * happen.
     *
     * @param l
     *            PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    /**
     * Remove a PropertyChangeListener for the given key. If it was not added, nothing
     * will happen.
     *
     * @param key
     *            Key the listener was bound to
     * @param l
     *            PropertyChangeListener to be removed
     */
    public void removePropertyChangeListener(String key, PropertyChangeListener l) {
        if (key == null) throw new IllegalArgumentException("key must not be null");

        support.removePropertyChangeListener(key, l);
    }

}

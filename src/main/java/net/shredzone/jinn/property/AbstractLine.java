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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This is an abstract implementation of the Line interface.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractLine implements Line {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Clone a Line. The clone returned is independent from the original. The default
     * implementation will only create a shallow copy, which is usually sufficient.
     *
     * @return a cloned Line
     */
    @Override
    public Object clone() {
        try {
            final AbstractLine cl = (AbstractLine) super.clone();
            cl.support = new PropertyChangeSupport(cl);
            return cl;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.toString());
        }
    }

    /**
     * Add a PropertyChangeListener which is informed when the content of this Line was
     * changed.
     *
     * @param l
     *            PropertyChangeListener to add
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    /**
     * Remove a PropertyChangeListener. If it was not added, nothing will happen.
     *
     * @param l
     *            PropertyChangeListener to remove
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    /**
     * Fire a PropertyChangeEvent.
     *
     * @param key
     *            Key that was changed
     * @param old
     *            Old value
     * @param val
     *            New value
     */
    protected void firePropertyChange(String key, String old, String val) {
        if (old == null || val == null || !old.equals(val)) {
            support.firePropertyChange(key, old, val);
        }
    }

}

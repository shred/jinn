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
import java.io.IOException;

/**
 * This interface represents a single line of a properties file.
 *
 * @author Richard "Shred" Körber
 */
public interface Line extends Cloneable {

    /**
     * Write the internal state to a content line.
     *
     * @param pw
     *            PropertiesWriter to write to
     * @throws IOException
     *             Could not write to stream
     */
    public void write(PropertiesWriter pw) throws IOException;

    /**
     * A clone operation is a must-have for lines.
     *
     * @return A cloned Line object that can be modified independently of the original.
     */
    public Object clone();

    /**
     * Add a PropertyChangeListener which is informed when the content of this Line was
     * changed.
     *
     * @param l
     *            PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener l);

    /**
     * Remove a PropertyChangeListener. If it was not added, nothing will happen.
     *
     * @param l
     *            PropertyChangeListener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener l);

}

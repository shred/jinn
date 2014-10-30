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

import java.io.IOException;

/**
 * This class represents a single translation line of a properties file. It consists of a
 * String type key and a String type translation.
 *
 * @author Richard "Shred" Körber
 */
public class PropertyLine extends AbstractLine {
    private String key;
    private String value;

    /**
     * Create a new PropertyLine with the given key and value.
     *
     * @param key
     *            Property key
     * @param value
     *            Property value
     */
    public PropertyLine(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Write the internal state to a content line.
     *
     * @param pw
     *            PropertiesWriter to write to
     * @throws IOException
     *             Could not write to stream
     */
    @Override
    public void write(PropertiesWriter pw) throws IOException {
        int mark = pw.writeKey(getKey());
        pw.write("= ");
        pw.writeWrapable(getValue(), mark + 2);
        pw.newLine();
    }

    /**
     * Get the key of this entry.
     *
     * @return Current key.
     */
    public String getKey() {
        assert key != null && key.length() > 0 : "No valid key";
        return key;
    }

    /**
     * Set a new value for this Resource. Note that a key is not changeable.
     *
     * @param val
     *            New value to be set
     */
    public void setValue(String val) {
        final String old = value;
        value = val;
        firePropertyChange("value", old, val);
    }

    /**
     * Get the value that is currently set.
     *
     * @return Current value.
     */
    public String getValue() {
        return value;
    }

}

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

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.shredzone.jinn.property.PropertyModel;

/**
 * A KeyList is a JList that shows the keys of either a PropertyKeyModel or a
 * PropertyKeyRefModel.
 *
 * @author Richard "Shred" Körber
 */
public class KeyList extends JList<String> {
    private static final long serialVersionUID = -5451689667472060128L;

    /**
     * Create an empty KeyList
     */
    public KeyList() {
        this(new PropertyKeyModel(new PropertyModel()));
    }

    /**
     * Create a PropertyViewer for a given PropertyKeyModel.
     *
     * @param model
     *            PropertyKeyModel to show.
     */
    public KeyList(PropertyKeyModel model) {
        super(model);
        setCellRenderer(new KeyRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

}

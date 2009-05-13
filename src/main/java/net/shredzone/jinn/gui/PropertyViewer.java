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
 * A PropertyViewer is used to view a PropertyModel. It is shown in a kind of Table view,
 * with one column showing the line number, and another column showing the line content.
 * It is also possible to highlight a line.
 * <p>
 * The PropertyViewer is merely a JList, but with a SINGLE_SELECTION model turned on by
 * default.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertyViewer.java 315 2009-05-13 19:32:40Z shred $
 */
public class PropertyViewer extends JList {
    private static final long serialVersionUID = -3305575282409649481L;

    /**
     * Create an empty PropertyViewer.
     */
    public PropertyViewer() {
        this(new PropertyModel());
    }

    /**
     * Create a PropertyViewer for a given PropertyModel.
     * 
     * @param model
     *            PropertyModel to show.
     */
    public PropertyViewer(PropertyModel model) {
        super(model);
        setCellRenderer(new LineRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

}

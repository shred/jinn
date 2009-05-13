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

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import net.shredzone.jinn.pool.ImgPool;

/**
 * A KeyRenderer renders a Key line shown in a JList.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: KeyRenderer.java 315 2009-05-13 19:32:40Z shred $
 */
public class KeyRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = -1559580545428262000L;

    private static final ImageIcon iconEmpty = ImgPool.get("m-empty.png");
    private static final ImageIcon iconNew = ImgPool.get("m-added.png");
    private static final ImageIcon iconTranslated = ImgPool.get("m-translated.png");

    public KeyRenderer() {
        setIcon(iconEmpty);
    }

    /**
     * Get a Component that draws the current line.
     * 
     * @param list
     *            Referred JList
     * @param value
     *            Value object to be shown
     * @param index
     *            Index number
     * @param isSelected
     *            true: line is selected
     * @param cellHasFocus
     *            true: line is focussed
     * @return Component that draws this line
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel comp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final ListModel model = list.getModel();

        setIcon(iconEmpty);

        if (model != null && model instanceof PropertyKeyRefModel) {
            final PropertyKeyRefModel ref = (PropertyKeyRefModel) model;
            final String key = value.toString();

            if (ref.isChanged(key)) {
                comp.setIcon(iconTranslated);
                final Color bg = getBackground();
                setBackground(new Color((bg.getRed() * 95) / 100, bg.getGreen(), (bg.getBlue() * 95) / 100));
            } else if (ref.isNew(key) || ref.isSurplus(key)) {
                comp.setIcon(iconNew);
                final Color bg = getBackground();
                setBackground(new Color(bg.getRed(), (bg.getGreen() * 95) / 100, (bg.getBlue() * 95) / 100));
            }
        }

        return comp;
    }

}

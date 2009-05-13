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

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jshred.swing.JToolbarButton;

/**
 * This is Jinn's ToolBar.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: JinnToolBar.java 315 2009-05-13 19:32:40Z shred $
 */
public class JinnToolBar extends JToolBar {
    private static final long serialVersionUID = -5415692905814260570L;
    private final Registry registry;

    public JinnToolBar(Registry registry) {
        super(L.tr("generic.toolbar"));
        setRollover(true);

        this.registry = registry;

        add(createButton(JinnRegistryKeys.ACTION_OPEN));
        add(createButton(JinnRegistryKeys.ACTION_MERGE));
        add(createButton(JinnRegistryKeys.ACTION_SAVE));
        addSeparator();
        add(createButton(JinnRegistryKeys.ACTION_REVERT));
        add(createButton(JinnRegistryKeys.ACTION_CLEAN));
        add(createButton(JinnRegistryKeys.ACTION_NEXT));
        add(createButton(JinnRegistryKeys.ACTION_GOTO));
        addSeparator();
        add(createButton(JinnRegistryKeys.ACTION_CUT));
        add(createButton(JinnRegistryKeys.ACTION_COPY));
        add(createButton(JinnRegistryKeys.ACTION_PASTE));
        addSeparator();
        add(createButton(JinnRegistryKeys.ACTION_SEARCH));
        addSeparator();

        add(Box.createGlue());

        add(createButton(JinnRegistryKeys.ACTION_ABOUT));
    }

    /**
     * Create a JButton for a registry Action key, that is suitable for JToolBar usage.
     * 
     * @param key
     *            Action key to be used
     * @return Generated JButton
     */
    protected JButton createButton(String key) {
        return new JToolbarButton((Action) registry.get(key));
    }

}

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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.shredzone.jinn.i18n.L;

/**
 * A FileFilter that only shows ".properties" files.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: PropertiesFileFilter.java 315 2009-05-13 19:32:40Z shred $
 */
public class PropertiesFileFilter extends FileFilter {

    /**
     * A public default instance for this filter.
     */
    public static final PropertiesFileFilter DEFAULT = new PropertiesFileFilter();

    /**
     * Check if this filter accepts the given file. It will be accepted if it is either a
     * directory, or if it is a file that is visible and having a ".properties" suffix.
     * 
     * @param f
     *            File to check
     * @return true: Accept this file
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        if (f.isHidden()) return false;
        return f.getName().toLowerCase().endsWith(".properties");
    }

    /**
     * Get a human readable description for this filter.
     * 
     * @return Description
     */
    @Override
    public String getDescription() {
        return L.tr("pff.desc");
    }

}

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
package net.shredzone.jinn.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Offers internationalisation (i18n). It's a simple call of
 * <code>String text = L.tr("key");</code>.
 *
 * @author Richard "Shred" Körber
 */
public class L {
    private static final ResourceBundle resource;

    static {
        resource = ResourceBundle.getBundle("net.shredzone.jinn.i18n.GUI");
    }

    /**
     * Translates a key into the matching translation. If a translation string is missing,
     * the key is returned enclosed into double square brackets.
     *
     * @param key
     *            Key to be translated
     * @return Translated String
     */
    public static String tr(String key) {
        try {
            return resource.getString(key).intern();
        } catch (MissingResourceException e) {
            return "[[" + key + "]]";
        }
    }

}

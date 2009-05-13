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
package net.shredzone.jinn.pool;

import java.awt.Image;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * Serves images from a common image pool. You just need to pass the name of the picture,
 * and get the Image as result.
 * <p>
 * The ImgPool implements a weak caching mechanism. If you need several instances of the
 * same image, you can just call <code>get()</code> multiple times and always get the same
 * instance. The image is internally cached until the last reference has been discarded.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: ImgPool.java 315 2009-05-13 19:32:40Z shred $
 */
public final class ImgPool {

    private final static HashMap<String, SoftReference<ImageIcon>> cache
            = new HashMap<String, SoftReference<ImageIcon>>();

    /**
     * Get an ImageIcon by its name.
     * 
     * @param name
     *            Image name
     * @return ImageIcon or null if not found
     */
    public static ImageIcon get(String name) {
        // First check if there is a cache entry, because the GC could have
        // sweeped it already.

        SoftReference<ImageIcon> ref = cache.get(name);
        ImageIcon result = (ref != null ? ref.get() : null);
        if (result == null) {
            try {
                result = new ImageIcon(ImgPool.class.getResource(name));
                cache.put(name, new SoftReference<ImageIcon>(result));
            } catch (Exception ex) {}
        }
        return result;
    }

    /**
     * Get an ImageIcon by its name, and scales it to the given dimensions. Note that
     * scaled images will <em>not</em> be cached.
     * 
     * @param name
     *            Image name
     * @param width
     *            Image width
     * @param height
     *            Image height
     * @return ImageIcon or null if not found
     */
    public static ImageIcon getScaled(String name, int width, int height) {
        // --- Get original icon ---
        ImageIcon iconFull = get(name);
        if (iconFull == null) return null; // was not found

        // --- Scale ---
        Image img = iconFull.getImage();
        img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // --- Return new ImageIcon ---
        return new ImageIcon(img);
    }

}

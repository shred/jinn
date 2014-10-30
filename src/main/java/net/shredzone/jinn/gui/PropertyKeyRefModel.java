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

import java.util.Set;

import net.shredzone.jinn.property.PropertyLine;
import net.shredzone.jinn.property.PropertyModel;

/**
 * A PropertyKeyRefModel extends the PropertyModel by the ability to compare the
 * PropertyModel with a reference PropertyModel. There are a few checks that can be made
 * in order to compare this model with the reference model.
 *
 * @author Richard "Shred" Körber
 */
public class PropertyKeyRefModel extends PropertyKeyModel {
    protected final PropertyModel reference;
    private Set<String> sNewKeys = null;

    /**
     * Create a new PropertyKeyModel for a given PropertyModel and a reference
     * PropertyModel.
     *
     * @param model
     *            PropertyModel to create this model for
     * @param ref
     *            Reference PropertyModel
     */
    public PropertyKeyRefModel(PropertyModel model, PropertyModel ref) {
        super(model);
        this.reference = ref;
    }

    /**
     * Set a Set of keys that have been added by a merge process. Actually you pass the
     * result of the <code>PropertyModel.merge()</code> method.
     *
     * @param keys
     *            Set of added keys
     */
    public void setAddedKeys(Set<String> keys) {
        this.sNewKeys = keys;
    }

    /**
     * Find the next untranslated key. The search is started from the given key
     * (exclusive). If key is set to null, the search will start at the top. If no next
     * untranslated key was found, null will be returned.
     *
     * @param key
     *            Starting key, null for first line
     * @return Next untranslated key, null for no next key.
     */
    public String findNext(String key) {
        // Compute the starting index
        int ix = 0;
        if (key != null) {
            ix = lKeys.indexOf(key) + 1;
        }

        // Find the next untranslated key
        final int cnt = lKeys.size();
        while (ix < cnt) {
            final String check = lKeys.get(ix);
            if (isNew(check) || !isChanged(check)) {
                return check;
            }

            ix++;
        }

        // Nothing was found
        return null;
    }

    /**
     * Check if a key's resource value is empty.
     * <p>
     * This check is true if the key exists, but contains an empty string as value.
     *
     * @param key
     *            Key to check
     * @return true: key exists, but value is an empty string
     */
    public boolean isEmpty(String key) {
        final PropertyLine src = model.getPropertyLine(key);
        if (src == null) return false;
        return (src.getValue().length() == 0);
    }

    /**
     * Check if a key's resource has been changed compared to the reference model.
     * <p>
     * This check is true if the key exists in both this model and the reference model,
     * but the values are not equal. This means that the resource was already translated.
     *
     * @param key
     *            Key to check
     * @return true: key exists, and values are not equal
     */
    public boolean isChanged(String key) {
        final PropertyLine src = model.getPropertyLine(key);
        final PropertyLine ref = reference.getPropertyLine(key);

        if (src == null || ref == null) return false;
        return (!src.getValue().equals(ref.getValue()));
    }

    /**
     * Check if a key's resource is new.
     * <p>
     * This check is true if a set of added keys was passed in (using
     * <code>setAddedKeys()</code>), and the set contains the key. This means that the key
     * was added since last time the reference model was merged.
     *
     * @param key
     *            Key to check
     * @return true: the key is new, according to the set of added keys.
     */
    public boolean isNew(String key) {
        if (sNewKeys == null) return false;
        return (sNewKeys.contains(key));
    }

    /**
     * Check if a key is surplus regarding the reference model.
     * <p>
     * This check is true if a key is available in this model, but not in the reference
     * model. This means that the key was probably removed since last time the reference
     * model was merged. Anyhow the merging process should have removed those keys from
     * this model already, so usually this test should always result false.
     *
     * @param key
     *            Key to check
     * @return true: the key is surplus
     */
    public boolean isSurplus(String key) {
        final PropertyLine src = model.getPropertyLine(key);
        final PropertyLine ref = reference.getPropertyLine(key);
        return (src != null && ref == null);
    }

}

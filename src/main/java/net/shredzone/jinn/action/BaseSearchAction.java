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
package net.shredzone.jinn.action;

import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.PropertyKeyModel;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.property.PropertyLine;
import net.shredzone.jinn.property.PropertyModel;

/**
 * The common superclass for all search operation actions.
 *
 * @author Richard "Shred" Körber
 */
public abstract class BaseSearchAction extends BaseAction {
    private static final long serialVersionUID = -1766114067062536829L;
    protected final Registry registry;

    /**
     * Create a new BaseSearchAction.
     *
     * @param registry
     *            Registry to be used
     * @param name
     *            Action Name
     * @param icon
     *            Action Icon or null
     * @param tip
     *            Action Tooltip or null
     * @param accel
     *            Accelerator Key or null
     */
    public BaseSearchAction(Registry registry, String name, Icon icon, String tip, KeyStroke accel) {
        super(name, icon, tip, accel);
        this.registry = registry;
    }

    /**
     * Get the search term to look for.
     *
     * @return Search term
     */
    protected String getSearchTerm() {
        return (String) registry.get(JinnRegistryKeys.SEARCH_TERM);
    }

    /**
     * Check if the search shall be case sensitive.
     *
     * @return true: case sensitive, false: ignore case
     */
    protected boolean isCaseSensitive() {
        Boolean cs = (Boolean) registry.get(JinnRegistryKeys.SEARCH_CASE_SENSITIVE);
        return cs.booleanValue();
    }

    /**
     * Show a message that there is no search result.
     *
     * @param frm
     *            Parent frame that is to be blocked.
     */
    protected void showNotFoundMessage(Frame frm) {
        JOptionPane.showMessageDialog(frm, L.tr("search.nothing"), L.tr("search.nothing.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Search the next match.
     * <p>
     * First, it will try to find a match within the current document, starting from the
     * caret position (or the end of a selected range, respectively). If no match was
     * found within the document, the next resource with the search string is located. If
     * there is no next resorce, false will be returned.
     * <p>
     * After a match was found, the resource will be chosen and the word will be selected.
     *
     * @return true: Match was found, false: No match
     */
    protected boolean searchNext() {
        boolean ok = searchDocument();
        if (ok) return true;

        ok = searchResource();
        if (ok) {
            searchDocument();
            return true;
        }

        return false;
    }

    /**
     * Search for the next resource that contains the search term. The search is started
     * from the currently selected resource, which will be completely ignored. If no
     * resource is selected, it will start at the first resource.
     *
     * @return true: found a resource, false: found nothing.
     */
    private boolean searchResource() {
        final PropertyKeyModel keyModel = (PropertyKeyModel) registry.get(JinnRegistryKeys.MODEL_REFERENCE_KEY);
        final PropertyModel transModel = (PropertyModel) registry.get(JinnRegistryKeys.MODEL_TRANSLATION);

        if (keyModel == null) return false;

        int current = 0;

        final String currentKey = registry.getString(JinnRegistryKeys.CURRENT_KEY);
        if (currentKey != null) {
            current = keyModel.findKey(currentKey) + 1;
        }

        String term = getSearchTerm();
        if (!isCaseSensitive()) {
            term = term.toLowerCase();
        }

        for (int ix = current; ix < keyModel.getSize(); ix++) {
            String key = (String) keyModel.getElementAt(ix);
            PropertyLine line = transModel.getPropertyLine(key);
            String content = line.getValue();
            if (!isCaseSensitive()) {
                content = content.toLowerCase();
            }

            if (content.indexOf(term) >= 0) {
                registry.put(JinnRegistryKeys.CURRENT_KEY, key);
                return true; // Found something
            }
        }

        return false; // End reached, nothing was found
    }

    /**
     * Search for the next search term match in the current document. The search is
     * started from the caret position.
     *
     * @return true: found a match, false: found nothing.
     */
    private boolean searchDocument() {
        JTextComponent editor = (JTextComponent) registry.get(JinnRegistryKeys.TRANSLATION_TEXT);
        if (editor == null) return false;

        int pos = Math.max(editor.getSelectionEnd(), editor.getSelectionStart());
        if (pos == 0) pos = editor.getCaretPosition();

        String term = getSearchTerm();
        String content = editor.getText();

        if (pos >= content.length()) return false;

        if (!isCaseSensitive()) {
            term = term.toLowerCase();
            content = content.toLowerCase();
        }

        int newpos = content.indexOf(term, pos + 1);
        if (newpos < 0) return false;

        editor.setSelectionStart(newpos);
        editor.setSelectionEnd(newpos + term.length());

        return true;
    }

}

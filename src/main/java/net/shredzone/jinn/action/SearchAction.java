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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.shredzone.jinn.Registry;
import net.shredzone.jinn.gui.SearchPane;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * Show the search window.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: SearchAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class SearchAction extends BaseSearchAction {
    private static final long serialVersionUID = 3478879399066426698L;

    /**
     * Create a new SearchAction.
     */
    public SearchAction(Registry registry) {
        super(registry, L.tr("action.search"), ImgPool.get("search.png"), L.tr("action.search.tt"), KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));

        setEnabled(false); /* DEBUG: Search is deactivated for now */
    }

    /**
     * The action implementation itself.
     * 
     * @param e
     *            ActionEvent, may be null if directly invoked
     */
    @Override
    public void perform(ActionEvent e) {
        int rc = SearchPane.showSearchDialog(registry, getFrame(e), L.tr("search.title"));

        if (rc == SearchPane.SEARCH_OPTION) {
            boolean found = searchNext();
            if (!found) showNotFoundMessage(getFrame(e));
        }
    }

}

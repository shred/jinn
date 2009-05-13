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
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * Find the next or previous occurance of a search string.
 * 
 * @author Richard Körber &lt;dev@shredzone.de&gt;
 * @version $Id: SearchNextAction.java 315 2009-05-13 19:32:40Z shred $
 */
public class SearchNextAction extends BaseSearchAction {
    private static final long serialVersionUID = -3246883749259834931L;
    public static boolean NEXT = false;
    public static boolean PREVIOUS = true;

    @SuppressWarnings("unused")
    private final boolean direction;

    /**
     * Create a new SearchAction.
     */
    public SearchNextAction(Registry registry, boolean prev) {
        super(registry, L.tr(prev ? "action.search.prev" : "action.search.next"), ImgPool.get("search.png"), L.tr(prev ? "action.search.prev.tt"
                                                                                                                      : "action.search.next.tt"), (prev ? KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK)
                                                                                                                                                       : KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.CTRL_MASK)));
        this.direction = prev;

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

    }

}

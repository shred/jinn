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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import net.shredzone.jinn.JinnRegistryKeys;
import net.shredzone.jinn.Registry;
import net.shredzone.jinn.Style;
import net.shredzone.jinn.i18n.L;
import net.shredzone.jinn.pool.ImgPool;

/**
 * Show an about window.
 *
 * @author Richard "Shred" Körber
 */
public class AboutAction extends BaseAction {
    private static final long serialVersionUID = 48992099038287792L;
    private final Registry registry;

    /**
     * Create a new AboutAction.
     */
    public AboutAction(Registry registry) {
        super(L.tr("action.about"), ImgPool.get("about.png"), L.tr("action.about.tt"), null);
        this.registry = registry;
    }

    /**
     * The action implementation itself.
     *
     * @param e
     *            ActionEvent, may be null if directly invoked
     */
    @Override
    public void perform(ActionEvent e) {

        final String msg = MessageFormat.format(
            L.tr("about.msg"), new Object[] { Style.VERSION, new Date(), });

        JOptionPane.showMessageDialog((Component)
            registry.get(JinnRegistryKeys.FRAME_MAIN), msg, L.tr("about.title"),
                         JOptionPane.INFORMATION_MESSAGE);
    }

}
